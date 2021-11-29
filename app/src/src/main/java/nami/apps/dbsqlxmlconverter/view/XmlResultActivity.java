package nami.apps.dbsqlxmlconverter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Xml;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import nami.apps.dbsqlxmlconverter.R;
import nami.apps.dbsqlxmlconverter.contract.XmlResultContract;
import nami.apps.dbsqlxmlconverter.model.XmlResultModel;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileProperties;
import nami.apps.dbsqlxmlconverter.presenter.XMLResultPresenter;

public class XmlResultActivity extends AppCompatActivity implements XmlResultContract.View {
    ProgressDialog progressDialog;
    XMLResultPresenter presenter;
    ImageButton back,saveFile,openFile;
    TextView xmlResult,fileLocation;
    FileProperties fileProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_result);

        fileProperties = new FileProperties();
        fileProperties.setName(getIntent().getStringExtra("FILE_NAME"));
        fileProperties.setPath(getIntent().getStringExtra("FILE_PATH"));

        back = findViewById(R.id.back);
        saveFile = findViewById(R.id.saveFile);
        openFile = findViewById(R.id.openFile);
        xmlResult = findViewById(R.id.result);
        fileLocation = findViewById(R.id.fileLocation);

        initProgressDialog();

        presenter = new XMLResultPresenter(this,new XmlResultModel());
        presenter.getXMLResult(this,fileProperties);

        saveFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });

        openFile.setEnabled(false);
        openFile.setAlpha(.5f);
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initProgressDialog()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait..."); // Setting Message
        progressDialog.setTitle("Converting file"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);

    }
    @Override
    public void onXMLResultSuccess(final String result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fileLocation.setText(fileProperties.getPath());
                xmlResult.setText(result);
                xmlResult.setMovementMethod(new ScrollingMovementMethod());
                progressDialog.cancel();
            }
        });

    }

    @Override
    public void onFailure(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.cancel();
                xmlResult.setText(error);
            }
        });

    }

    @Override
    public void onOpenFileSuccess(File file, String dataType) {
        Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                "com.example.android.fileprovider",file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, dataType);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        XmlResultActivity.this.startActivity(intent);
    }

    @Override
    public void onSaveFileSuccess(String result) {
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();
        openFile.setEnabled(true);
        openFile.setAlpha(1f);
    }

    private void openFile(){
        this.presenter.openFile();
    }

    private void saveFile()
    {
        this.presenter.saveFile();
    }
}
