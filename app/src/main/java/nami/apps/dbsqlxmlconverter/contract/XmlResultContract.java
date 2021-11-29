package nami.apps.dbsqlxmlconverter.contract;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import nami.apps.dbsqlxmlconverter.model.fileManager.FileProperties;

public interface XmlResultContract {
    interface View{
        void onXMLResultSuccess(String result);
        void onFailure(String error);
        void onOpenFileSuccess(File file, String dataType);
        void onSaveFileSuccess(String result);
    }
    interface Model{

        interface OnListerner{
            void onXMLResultSuccess(String result);
            void onFailure(String error);
            void onOpenFileSuccess(File file, String dataType);
            void onSaveFileSuccess(String result);
        }
        void getXMLResult(OnListerner listerner, Context context, FileProperties fileProperties);
        void openFile(OnListerner listerner);
        void saveFile(OnListerner listerner);
    }

    interface Presenter{
        void getXMLResult(Context context, FileProperties fileProperties);
        void openFile();
        void saveFile();
    }
}
