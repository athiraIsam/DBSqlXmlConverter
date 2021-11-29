package nami.apps.dbsqlxmlconverter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import nami.apps.dbsqlxmlconverter.BuildConfig;
import nami.apps.dbsqlxmlconverter.R;
import nami.apps.dbsqlxmlconverter.contract.MainContract;
import nami.apps.dbsqlxmlconverter.model.MainModel;
import nami.apps.dbsqlxmlconverter.presenter.MainPresenter;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    MainPresenter presenter;
    ImageButton internalStorageButton,externalStorageButton;
    String internalStorageDir,externalStorageDir;
    TextView appVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internalStorageButton = findViewById(R.id.internal_storage_button);
        externalStorageButton = findViewById(R.id.externalStorageButton);
        appVersion = findViewById(R.id.appVersion);
        appVersion.setText(appVersion.getText()+ " " + BuildConfig.VERSION_NAME);
        presenter = new MainPresenter(this,new MainModel());
        presenter.getListOfStorage(this);

        internalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseFile(internalStorageDir);
            }
        });

        externalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browseFile(externalStorageDir);
            }
        });
    }

    @Override
    public void onResultListOfStorage(List<String> storages) {
        Log.d(getClass().getName(),""+ storages.size());
        for(String storage:storages)
        {
            if (storage.contains(Environment.getExternalStorageDirectory().getPath()))
                internalStorageDir = storage;
            else
                externalStorageDir = storage;
            }
    }

    @Override
    public void onFailure(String error) {
        Log.e(getClass().getName(),error);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(error)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
        }).create().show();

    }

    private void browseFile(String storageDirectory)
    {
        if(storageDirectory == null) {
            this.onFailure("There is no external storage in this device");
            return;
        }
        Intent intent = new Intent(this,BrowserFileActivity.class);
        intent.putExtra("directory",storageDirectory);
        startActivity(intent);
    }
}
