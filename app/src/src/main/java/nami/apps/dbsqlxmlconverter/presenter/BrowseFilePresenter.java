package nami.apps.dbsqlxmlconverter.presenter;

import java.util.ArrayList;
import java.util.HashMap;

import nami.apps.dbsqlxmlconverter.contract.BrowserFileContract;

public class BrowseFilePresenter implements BrowserFileContract.Presenter,BrowserFileContract.Model.OnListerner {

    BrowserFileContract.View mainView;
    BrowserFileContract.Model mainModel;

    public BrowseFilePresenter(BrowserFileContract.View mainView, BrowserFileContract.Model mainModel) {
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    @Override
    public void getItems(String directory) {
        if(this.mainModel!=null)
            this.mainModel.getItems(this,directory);
    }

    @Override
    public void onGetItemsSuccess(HashMap<ArrayList<String>, ArrayList<String>> items) {
        if(this.mainView!=null)
            this.mainView.onGetItemsSuccess(items);
    }

    @Override
    public void onFailure(String error) {
        if(this.mainView!=null)
            this.mainView.onFailure(error);
    }
}
