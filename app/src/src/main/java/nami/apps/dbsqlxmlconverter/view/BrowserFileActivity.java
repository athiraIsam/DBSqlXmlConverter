package nami.apps.dbsqlxmlconverter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import nami.apps.dbsqlxmlconverter.R;
import nami.apps.dbsqlxmlconverter.contract.BrowserFileContract;
import nami.apps.dbsqlxmlconverter.model.BrowseFileModel;
import nami.apps.dbsqlxmlconverter.model.adapter.FileBrowserAdapter;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileManager;
import nami.apps.dbsqlxmlconverter.presenter.BrowseFilePresenter;

public class BrowserFileActivity extends AppCompatActivity implements FileBrowserAdapter.onFileBrowserListener, BrowserFileContract.View {

    //component attribute
    private RecyclerView rv_fileBrowser;
    private SearchView editsearch;
    String storageDirectory;

    //general attribute
    private ArrayList<String> item;
    private ArrayList<String> path;
    private ArrayList<String> allFiles;
    private ArrayList<String> allFilesDir;

    private boolean run = true;

    private HashMap<ArrayList<String>, ArrayList<String>> fileMap = null;

    private BrowseFilePresenter presenter;
    private FileBrowserAdapter fileBrowserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_file);
        rv_fileBrowser = (RecyclerView) findViewById(R.id.rv_file_browser);
        editsearch = (SearchView) findViewById(R.id.searchView);

        storageDirectory = getIntent().getStringExtra("directory");
        Log.d(getClass().getName(),"Storage Directory: " + storageDirectory);

        FileManager.setRootDir(storageDirectory);
        FileManager.getAllListOfFile(storageDirectory);
        presenter = new BrowseFilePresenter(this,new BrowseFileModel());
        presenter.getItems(storageDirectory);

        editsearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                OnQueryTextSubmit(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                OnQueryTextChange(s);
                return true;
            }
        });
    }

    private void OnQueryTextSubmit(String s) {
        if(run){

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please Wait..."); // Setting Message
            progressDialog.setTitle("Searching File"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);

            for (int i = 0; i < allFiles.size(); i++) {
                if (allFiles.get(i).contains(s)) {
                    Log.d(getClass().getName(),"Pos: " + i);
                    Log.d(getClass().getName(),"Files: " + allFiles.get(i));
                    Log.d(getClass().getName(),"Dir: " + allFilesDir.get(i));
                    path.add(allFilesDir.get(i));
                    item.add(allFiles.get(i));
                }
                if (i == (allFiles.size() - 1))
                    progressDialog.cancel();
            }

            setFileBrowserAdapter();

            fileBrowserAdapter.getFilter().filter(s);
            run = false;

            if (path.size() == 0) {
                progressDialog.cancel();
                Toast.makeText(this, "No Match found", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void setFileItemandPath () {
        if (item != null) {
            item.clear();
            path.clear();

            item = new ArrayList<>();
            path = new ArrayList<>();
        }

        for (ArrayList key : fileMap.keySet())
            item = key;
        for (ArrayList value : fileMap.values())
            path = value;

       setFileBrowserAdapter();

    }

    private void setFileBrowserAdapter()
    {
        fileBrowserAdapter = new FileBrowserAdapter(this, this);
        rv_fileBrowser.setAdapter(fileBrowserAdapter);
        rv_fileBrowser.setLayoutManager(new LinearLayoutManager(this));
        fileBrowserAdapter.setItem(item);

    }

    private void CheckFileType (File file)
    {
        if(FileManager.checkIsDbExtension(file.getName())) {
            Intent intent = new Intent(this,XmlResultActivity.class);
            intent.putExtra("FILE_PATH",file.getAbsolutePath());
            intent.putExtra("FILE_NAME",file.getName());
            startActivity(intent);

        }
        else
        {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_warning)
                    .setTitle("[" + file.getName() + "]")
                    .setNegativeButton("Cannot Open this file", null).show();
        }
    }

    @Override
    public void onFileBrowserClick(int position) {
        File file = new File(path.get(position));
        if (file.isDirectory())
        {
            if(file.canRead()){
                fileMap = FileManager.getItem(path.get(position));
                setFileItemandPath();
            }else{
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher_background)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", null).show();
            }
        }else {
            CheckFileType(file);
        }
    }

    @Override
    public void onGetItemsSuccess(HashMap<ArrayList<String>, ArrayList<String>> items) {

        allFiles = FileManager.getAllFilesFound();
        allFilesDir = FileManager.getAllFilesFoundDir();
        this.fileMap = items;

        setFileItemandPath();
    }

    @Override
    public void onFailure(String error) {
        new AlertDialog.Builder(this)
                .setTitle(error)
                .setPositiveButton("OK", null).show();
    }

    private void OnQueryTextChange(String s)
    {
        run = true;
        if(s.equals(""))
        {
            if(this.fileMap != null)
                this.fileMap = null;
            this.presenter.getItems(storageDirectory);
        }
        else {

            if(path!=null)
                path.clear();
            if(item!=null)
                item.clear();
        }
    }
}
