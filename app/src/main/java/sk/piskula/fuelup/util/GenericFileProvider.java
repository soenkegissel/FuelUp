package sk.piskula.fuelup.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.util.List;

/**
 * Generic file provider which allows app to acces all files in external storage
 * <p>
 * Created by mstyk on 9/20/17.
 */
public class GenericFileProvider extends FileProvider {

    public static final String AUTHORITY = "sk.piskula.fuelup.file.provider";

    /**
     * Grants URI permissions to all applications which can respond to given intent
     *
     * @param ctx
     * @param intent
     * @param uri
     * @param permissionFlags
     */
    public static void grantUriPermission(Activity ctx, Intent intent, Uri uri, int permissionFlags) {
        List<ResolveInfo> resolvedIntentActivities = ctx.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);

        for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
            String packageName = resolvedIntentInfo.activityInfo.packageName;

            ctx.grantUriPermission(packageName, uri, permissionFlags);
        }
    }
}