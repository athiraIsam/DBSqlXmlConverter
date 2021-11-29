package nami.apps.dbsqlxmlconverter.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import nami.apps.dbsqlxmlconverter.R;
import nami.apps.dbsqlxmlconverter.contract.SplashContract;
import nami.apps.dbsqlxmlconverter.model.SplashModel;
import nami.apps.dbsqlxmlconverter.presenter.SplashPresenter;

public class SplashActivity extends AppCompatActivity implements SplashContract.View{

    private Handler handler;
    SplashContract.Presenter presenter;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        presenter = new SplashPresenter(this,new SplashModel());

        presenter.checkPermissionGranted(this,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1 || requestCode == 2) {
                handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                },3000);

            }
        }
    }

    @Override
    public void onPermissionIsGranted() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                  Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                 startActivity(intent);
                finish();
            }
        },3000);
    }

    @Override
    public void onPermissionIsNotGranted() {

      builder = new AlertDialog.Builder(this);
      builder.setMessage("No permission is granted. Please allow permission to continue")
              .setCancelable(false)
              .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int i) {
                    requestPermission();
                  }
              }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
              finish();
          }
      }).create().show();
    

    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
    }
}
