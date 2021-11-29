package nami.apps.dbsqlxmlconverter.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import nami.apps.dbsqlxmlconverter.contract.XmlResultContract;
import nami.apps.dbsqlxmlconverter.model.fileManager.FileProperties;

public class XMLResultPresenter implements XmlResultContract.Presenter,XmlResultContract.Model.OnListerner {

    XmlResultContract.View mainView;
    XmlResultContract.Model mainModel;

    public XMLResultPresenter(XmlResultContract.View mainView, XmlResultContract.Model mainModel) {
        this.mainView = mainView;
        this.mainModel = mainModel;
    }

    @Override
    public void onXMLResultSuccess(String result) {
        if(mainView!=null)
            mainView.onXMLResultSuccess(result);
    }

    @Override
    public void onFailure(String error) {
        if(mainView!=null)
            mainView.onFailure(error);
    }

    @Override
    public void onOpenFileSuccess(File file, String dataType) {
        if(mainView!=null)
            mainView.onOpenFileSuccess(file,dataType);
    }

    @Override
    public void onSaveFileSuccess(String resultDir) {
        if(mainView!=null)
            mainView.onSaveFileSuccess(resultDir);
    }


    @Override
    public void getXMLResult(Context context, FileProperties fileProperties) {
        if(mainModel!=null)
            mainModel.getXMLResult(this,context,fileProperties);
    }

    @Override
    public void openFile() {
        if(mainModel!=null)
            mainModel.openFile(this);
    }


    @Override
    public void saveFile() {
        if(mainModel!=null)
            mainModel.saveFile(this);
    }
}
