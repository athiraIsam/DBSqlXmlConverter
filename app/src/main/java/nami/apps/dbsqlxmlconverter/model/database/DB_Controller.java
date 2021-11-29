package nami.apps.dbsqlxmlconverter.model.database;


import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public abstract class DB_Controller {

    protected SQLiteOpenHelper db_helper;

    public synchronized void close() {db_helper.close();}

    public boolean isOpen()
    {
        if(db_helper != null && db_helper.getWritableDatabase().isOpen())
            return true;
        else return false;
    }

    protected long insertInDB(String tableName, ContentValues values)
    {
        long insertId = db_helper.getWritableDatabase().insert(tableName,null,values);
        Log.d(TAG.getClass().getName(),"Insert id: " +insertId + "\ttable: " +tableName + "\tvalues: " + values);
        return insertId;
    }

}
