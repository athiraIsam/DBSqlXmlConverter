package nami.apps.dbsqlxmlconverter.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import nami.apps.dbsqlxmlconverter.contract.SplashContract;

public class SplashModel implements SplashContract.Model {
    @Override
    public void checkPermissionGranted(Context context, Activity activity,OnListerner listerner) {

        if ((context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)&&
                (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)) {
          listerner.onPermissionIsGranted();
        }
        else {
           listerner.onPermissionIsNotGranted();
        }
    }


}
