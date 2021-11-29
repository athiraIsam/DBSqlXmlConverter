package nami.apps.dbsqlxmlconverter.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;

public abstract class DB_Helper  extends SQLiteOpenHelper {

    protected boolean isUpgraded = false;
    protected boolean isDowngraded = false;

    protected static class SQLiteCursorFactory implements SQLiteDatabase.CursorFactory
    {

        @Override
        public Cursor newCursor(SQLiteDatabase sqLiteDatabase, SQLiteCursorDriver sqLiteCursorDriver, String editTable, SQLiteQuery sqLiteQuery) {
            Log.d(TAG.getClass().getName(), "Query: " + sqLiteQuery.toString());
            return new SQLiteCursor(sqLiteCursorDriver,editTable,sqLiteQuery);
        }
    }

    public DB_Helper (Context context, File file, final int DATABASE_VERSION)
    {
        super(context,file.getAbsolutePath(), new SQLiteCursorFactory(),DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG.getClass().getName(),"Upgrading the database from version " + oldVersion + " to " + newVersion);

        //clear all data
        this.deleteTables(db);
        //recreate the table
        onCreate(db);

        isUpgraded = true;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG.getClass().getName(), "Downgrading the database from version " + oldVersion + " to " + newVersion);

        //clear all data
        this.deleteTables(db);
        //recreate the table
        onCreate(db);
        isDowngraded = true;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly())
            db.execSQL("PRAGMA foreign_keys=ON");
    }

    protected abstract void deleteTables (SQLiteDatabase db);

    protected void execSQL(SQLiteDatabase db, String sql)
    {
        try {
            Log.d(TAG.getClass().getName(), "SQL: " + sql);
            db.execSQL(sql);
        }catch (SQLException e) {
            Log.e(TAG.getClass().getName(), e.getMessage());
        }

    }
    public boolean isUpgraded() {
        return isUpgraded;
    }

    public boolean isDowngraded() {
        return isDowngraded;
    }
}
