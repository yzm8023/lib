package com.smonline.virtual.client.stub;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.job.IJobCallback;
import android.app.job.IJobService;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.smonline.virtual.client.core.InvocationStubManager;
import com.smonline.virtual.client.hook.proxies.am.ActivityManagerStub;
import com.smonline.virtual.helper.utils.VLog;
import com.smonline.virtual.helper.collection.SparseArray;
import com.smonline.virtual.os.VUserHandle;

import java.util.Map;

import static com.smonline.virtual.server.job.VJobSchedulerService.JobConfig;
import static com.smonline.virtual.server.job.VJobSchedulerService.JobId;
import static com.smonline.virtual.server.job.VJobSchedulerService.get;

/**
 * @author Lody
 *         <p>
 *         This service running on the Server process.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class StubJob extends Service {

    private static final String TAG = StubJob.class.getSimpleName();
    private final SparseArray<JobSession> mJobSessions = new SparseArray<>();
    private JobScheduler mScheduler;
    private final IJobService mService = new IJobService.Stub() {

        @Override
        public void startJob(JobParameters jobParams) throws RemoteException {
            int jobId = jobParams.getJobId();
            IBinder binder = mirror.android.app.job.JobParameters.callback.get(jobParams);
            IJobCallback callback = IJobCallback.Stub.asInterface(binder);
            Map.Entry<JobId, JobConfig> entry = get().findJobByVirtualJobId(jobId);
            if (entry == null) {
                emptyCallback(callback, jobId);
                mScheduler.cancel(jobId);
            } else {
                JobId key = entry.getKey();
                JobConfig config = entry.getValue();
                synchronized (mJobSessions) {
                    JobSession session = mJobSessions.get(jobId);
                    if (session != null) {
                        // Job Session has exist.
                        emptyCallback(callback, jobId);
                    } else {
                        session = new JobSession(jobId, callback, jobParams);
                        mirror.android.app.job.JobParameters.callback.set(jobParams, session.asBinder());
                        mirror.android.app.job.JobParameters.jobId.set(jobParams, key.clientJobId);
                        Intent service = new Intent();
                        service.setComponent(new ComponentName(key.packageName, config.serviceName));
                        service.putExtra("_VA_|_user_id_", VUserHandle.getUserId(key.vuid));
                        boolean bound = false;
                        try {
                            bound = bindService(service, session, Context.BIND_AUTO_CREATE);
                        } catch (Throwable e) {
                            VLog.e(TAG, e);
                        }
                        if (bound) {
                            mJobSessions.put(jobId, session);
                        } else {
                            emptyCallback(callback, jobId);
                            mScheduler.cancel(jobId);
                            get().cancel(jobId);
                        }
                    }
                }
            }
        }

        @Override
        public void stopJob(JobParameters jobParams) throws RemoteException {
            int jobId = jobParams.getJobId();
            synchronized (mJobSessions) {
                JobSession session = mJobSessions.get(jobId);
                if (session != null) {
                    session.stopSession();
                }
            }
        }
    };

    /**
     * Make JobScheduler happy.
     */
    private void emptyCallback(IJobCallback callback, int jobId) {
        try {
            callback.acknowledgeStartMessage(jobId, false);
            callback.jobFinished(jobId, false);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        InvocationStubManager.getInstance().checkEnv(ActivityManagerStub.class);
        mScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        Log.d("Q_M", "StubJob-->onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mService.asBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        for(int i = 0; i < mJobSessions.size(); i++){
            int key = mJobSessions.keyAt(i);
            JobSession session = mJobSessions.get(key);
            if(session != null){
                unbindService(session);
            }
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private final class JobSession extends IJobCallback.Stub implements ServiceConnection {

        private int jobId;
        private IJobCallback clientCallback;
        private JobParameters jobParams;
        private IJobService clientJobService;

        JobSession(int jobId, IJobCallback clientCallback, JobParameters jobParams) {
            this.jobId = jobId;
            this.clientCallback = clientCallback;
            this.jobParams = jobParams;
        }

        @Override
        public void acknowledgeStartMessage(int jobId, boolean ongoing) throws RemoteException {
            clientCallback.acknowledgeStartMessage(jobId, ongoing);
        }

        @Override
        public void acknowledgeStopMessage(int jobId, boolean reschedule) throws RemoteException {
            clientCallback.acknowledgeStopMessage(jobId, reschedule);
        }

        @Override
        public void jobFinished(int jobId, boolean reschedule) throws RemoteException {
            clientCallback.jobFinished(jobId, reschedule);
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            clientJobService = IJobService.Stub.asInterface(service);
            if (clientJobService == null) {
                emptyCallback(clientCallback, jobId);
                stopSession();
                return;
            }
            try {
                clientJobService.startJob(jobParams);
            } catch (RemoteException e) {
                forceFinishJob();
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        void forceFinishJob() {
            try {
                clientCallback.jobFinished(jobId, false);
            } catch (RemoteException e) {
                e.printStackTrace();
            } finally {
                stopSession();
            }
        }

        void stopSession() {
            if (clientJobService != null) {
                try {
                    clientJobService.stopJob(jobParams);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            mJobSessions.remove(jobId);
            unbindService(this);
        }
    }

}
