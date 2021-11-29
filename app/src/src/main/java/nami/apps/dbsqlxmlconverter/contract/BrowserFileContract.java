package nami.apps.dbsqlxmlconverter.contract;

import java.util.ArrayList;
import java.util.HashMap;

public interface BrowserFileContract {

    interface View{
        void onGetItemsSuccess(HashMap<ArrayList<String>,ArrayList<String>> items);
        void onFailure(String error);
    }
    interface Model{

        interface OnListerner{

            void onGetItemsSuccess(HashMap<ArrayList<String>,ArrayList<String>> items);
            void onFailure(String error);
        }
        void getItems(OnListerner listerner,String directory);
    }
    interface Presenter{

        void getItems(String directory);
    }
}
