package nami.apps.dbsqlxmlconverter.model;

import java.util.ArrayList;
import java.util.HashMap;

import nami.apps.dbsqlxmlconverter.contract.BrowserFileContract;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileManager;

public class BrowseFileModel implements BrowserFileContract.Model {
    @Override
    public void getItems(OnListerner listerner,String directory) {

        HashMap<ArrayList<String>,ArrayList<String>> items = FileManager.getItem(directory);

        if(items==null) {
            listerner.onFailure("Error to continue process");
            return;
        }

        listerner.onGetItemsSuccess(items);
    }
}
