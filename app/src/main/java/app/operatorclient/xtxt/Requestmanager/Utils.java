package app.operatorclient.xtxt.Requestmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by mac on 04/07/15.
 */
public class Utils {

    public static String getAndroidVer() {
        String myVersion = "";
        try {
            myVersion = android.os.Build.VERSION.RELEASE;
        } catch (Exception e) {

        }
        return myVersion;
    }

    public static String getAppVer(Context ctx) {
        String version = "";
        try {
            PackageManager manager = ctx.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    ctx.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {

        }
        return version;
    }

    public static void clearPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(RequestManger.PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();
    }
}
