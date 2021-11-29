package nami.apps.dbsqlxmlconverter.contract;

import android.content.Context;

import java.util.List;

public interface MainContract {

    interface View{
        void onResultListOfStorage(List<String> storages);
        void onFailure(String error);
    }
    interface Model{

        interface OnListerner{
            void onResultListOfStorage(List<String> storages);
            void onFailure(String error);
        }
        void getListOfStorage(OnListerner listerner, Context context);
    }
    interface Presenter{
        void getListOfStorage(Context context);
    }
}
