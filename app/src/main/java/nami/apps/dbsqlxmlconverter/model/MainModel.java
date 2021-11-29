package nami.apps.dbsqlxmlconverter.model;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import hendrawd.storageutil.library.StorageUtil;
import nami.apps.dbsqlxmlconverter.contract.MainContract;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileManager;

public class MainModel implements MainContract.Model {
    @Override
    public void getListOfStorage(OnListerner listerner,Context context) {
       String[] storages = StorageUtil.getStorageDirectories(context.getApplicationContext());
        List<String> listOfStorage = new ArrayList<>();

       if(storages==null) {
           listerner.onFailure("error to get storage");
           return;
       }
       if(storages.length==0) {
           listerner.onFailure("There is no storage available");
           return;
       }

       //check if storage is not empty
        for(String storage: storages)
        {
            Log.d(getClass().getName(),"List of Storage available: " + storage);

            if(FileManager.checkPathIsNotEmpty(storage))
                listOfStorage.add(storage);
        }

        listerner.onResultListOfStorage(listOfStorage);
    }
}
