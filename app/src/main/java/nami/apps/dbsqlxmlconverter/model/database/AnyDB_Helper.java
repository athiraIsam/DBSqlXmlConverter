package nami.apps.dbsqlxmlconverter.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class AnyDB_Helper extends DB_Helper {


    public AnyDB_Helper(Context context, File file, int DATABASE_VERSION) {
        super(context, file, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    protected void deleteTables(SQLiteDatabase db) {

    }
}
