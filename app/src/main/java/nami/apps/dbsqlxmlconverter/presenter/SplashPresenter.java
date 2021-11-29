package nami.apps.dbsqlxmlconverter.presenter;

import android.app.Activity;
import android.content.Context;

import nami.apps.dbsqlxmlconverter.contract.SplashContract;

public class SplashPresenter implements SplashContract.Presenter,SplashContract.Model.OnListerner {

    SplashContract.View mainView;
    SplashContract.Model mainModel;

    public SplashPresenter(SplashContract.View mainView, SplashContract.Model mainModel) {
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    @Override
    public void onPermissionIsGranted() {
        if(this.mainView!=null)
            this.mainView.onPermissionIsGranted();
    }

    @Override
    public void onPermissionIsNotGranted() {
        if(this.mainView!=null)
            this.mainView.onPermissionIsNotGranted();
    }

    @Override
    public void checkPermissionGranted(Context context, Activity activity) {
        if(this.mainModel!=null)
            this.mainModel.checkPermissionGranted(context,activity,this);

    }
}
