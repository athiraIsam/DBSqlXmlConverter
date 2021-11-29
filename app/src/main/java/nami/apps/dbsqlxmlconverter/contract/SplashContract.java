package nami.apps.dbsqlxmlconverter.contract;

import android.app.Activity;
import android.content.Context;

public interface SplashContract {

     interface View
    {
        void onPermissionIsGranted();
        void onPermissionIsNotGranted();
    }

    interface Model
    {
        interface OnListerner{
            void onPermissionIsGranted();
            void onPermissionIsNotGranted();
        }

        void checkPermissionGranted(Context context, Activity activity,OnListerner listerner);
    }

    interface Presenter
    {
        void checkPermissionGranted(Context context,Activity activity);
    }
}
