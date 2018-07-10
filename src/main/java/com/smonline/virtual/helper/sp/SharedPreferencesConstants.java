package com.smonline.virtual.helper.sp;

/**
 * 对SharedPreferences访问文件名和属性名的封装
 *
 * Created by yzm on 18-7-6.
 */

public class SharedPreferencesConstants {

    /**
     * 安装后的一些初始化信息
     */
    public static class InitInfo{
        public static String name = "initInfo";
        public static String versionCode = "versionCode";//当前程序版本号
        public static String lastUpdateTime = "lastUpdateTime";//最后一次检查插件更新的的时间
    }
}
