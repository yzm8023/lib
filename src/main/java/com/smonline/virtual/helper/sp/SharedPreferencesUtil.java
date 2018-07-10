package com.smonline.virtual.helper.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.smonline.virtual.BuildConfig;
import com.smonline.virtual.client.core.VirtualCore;
import com.smonline.virtual.helper.utils.VLog;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 对SharedPreference进行操纵的封装类
 * <p>
 * Created by yzm on 18-7-6.
 */

public class SharedPreferencesUtil {
    private static String TAG = "SharedPreferencesUtil";

    public static boolean removeValue(String xml, String key) {
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }

        sharedPreferences.edit().remove(key).apply();
        return true;
    }


    public static boolean setValue(String xml, String key, Object value) {
        if (value == null) {
            return false;
        }
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }
        if (value instanceof Integer) {
            sharedPreferences.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof String) {
            sharedPreferences.edit().putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            sharedPreferences.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Set) {
            sharedPreferences.edit().putStringSet(key, (Set<String>) value).apply();
        } else if (value instanceof Long) {
            sharedPreferences.edit().putLong(key, (Long) value).apply();
        } else if (value instanceof Float) {
            sharedPreferences.edit().putFloat(key, (Float) value).apply();
        } else {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the class is not supported %s", value);
            }
            return false;
        }
        return true;
    }

    public static boolean setValue(String xml, String key, Object value, boolean multi_process) {
        if (!multi_process) {
            return setValue(xml, key, value);
        }
        if (value == null) {
            return false;
        }
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_MULTI_PROCESS);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }
        if (value instanceof Integer) {
            sharedPreferences.edit().putInt(key, (Integer) value).apply();
        } else if (value instanceof String) {
            sharedPreferences.edit().putString(key, (String) value).apply();
        } else if (value instanceof Boolean) {
            sharedPreferences.edit().putBoolean(key, (Boolean) value).apply();
        } else if (value instanceof Set) {
            sharedPreferences.edit().putStringSet(key, (Set<String>) value).apply();
        } else if (value instanceof Long) {
            sharedPreferences.edit().putLong(key, (Long) value).apply();
        } else {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the class is not supported %s", value);
            }
            return false;
        }
        return true;
    }

    public static boolean setValues(String xml, HashMap<String, Object> map) {
        if (xml == null || map == null) {
            return false;
        }
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_PRIVATE);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof Integer) {
                editor.putInt(key, (Integer) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (Boolean) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (Long) value);
            } else {
                if (BuildConfig.DEBUG) {
                    VLog.e(TAG, "the class is not supported %s", value);
                }
            }
        }
        editor.apply();
        return true;
    }

    public static int getIntValue(String xml, String key, int defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return -100;
        }
        return sharedPreferences.getInt(key, defValue);
    }

    public static String getStringValue(String xml, String key, String defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return null;
        }
        return sharedPreferences.getString(key, defValue);
    }

    public static boolean getBooleanValue(String xml, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static boolean getBooleanValue(String xml, String key, boolean defValue, boolean multi_process) {
        if (!multi_process) {
            return getBooleanValue(xml, key, defValue);
        }
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_MULTI_PROCESS);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return false;
        }
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static Set<String> getStringSetValue(String xml, String key, Set<String> defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return null;
        }
        return sharedPreferences.getStringSet(key, defValue);
    }

    public static long getLongValue(String xml, String key, long defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return defValue;
        }
        return sharedPreferences.getLong(key, defValue);
    }

    public static Map<String, ?> getAll(String xml) {
        SharedPreferences sharedPreferences = getSharedPreferences(xml);
        if (sharedPreferences == null) {
            if (BuildConfig.DEBUG) {
                VLog.e(TAG, "the sharedPreferences is null");
            }
            return null;
        }
        return sharedPreferences.getAll();
    }

    private static SharedPreferences getSharedPreferences(String xml) {
        if (xml == null) {
            return null;
        }
        SharedPreferences sharedPreferences = VirtualCore.get().getContext().getSharedPreferences(xml, Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
