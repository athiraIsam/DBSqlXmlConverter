package nami.apps.dbsqlxmlconverter.presenter;

import android.content.Context;

import java.util.List;

import nami.apps.dbsqlxmlconverter.contract.MainContract;

public class MainPresenter implements MainContract.Presenter,MainContract.Model.OnListerner {

    MainContract.View mainView;
    MainContract.Model mainModel;

    public MainPresenter(MainContract.View mainView, MainContract.Model mainModel) {
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    @Override
    public void onResultListOfStorage(List<String> storages) {
        if(this.mainView!=null)
            this.mainView.onResultListOfStorage(storages);
    }

    @Override
    public void onFailure(String error) {
        if(this.mainView!=null)
            this.mainView.onFailure(error);
    }

    @Override
    public void getListOfStorage(Context context) {
        if(this.mainModel!=null)
            this.mainModel.getListOfStorage(this,context);
    }
}
