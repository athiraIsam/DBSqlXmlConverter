package nami.apps.dbsqlxmlconverter.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;


public class AnyDB_Controller extends DB_Controller {

    private File dbFile;

    public AnyDB_Controller(Context context, File fileName) {
        dbFile = fileName;
        open(context);

    }

    public SQLiteDatabase open(Context c) {
        db_helper = new AnyDB_Helper(c, dbFile, 1);
        //open the database
        try {
            return db_helper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(getClass().getName()," Unable to open database " + e.getMessage());
        }
        return null;
    }

    public int getVersion()
    {
        int result = 0;
        {
            try
            {
                String select = "SELECT VERSION_ID FROM PKG_VERSION";
                Cursor cursor = db_helper.getWritableDatabase().rawQuery(select,null);

                if(cursor != null && cursor.moveToFirst()) {
                    result = cursor.getInt(0);
                    cursor.close();
                }
                Log.d(getClass().getName(),"Select: " +select);
            } catch ( Exception e)
            {
                Log.e(getClass().getName(),("unable to get Version"));
            }
            finally {

                return result;
            }
        }
    }

    public ArrayList<String> getAllTableName ()
    {
        ArrayList<String> tableName = new ArrayList<>();

        try{
            String select = "SELECT * FROM sqlite_master WHERE type='table'";
            Cursor cursor = db_helper.getWritableDatabase()
                    .rawQuery(select, null);
            if(cursor!=null)
            {
                while (cursor.moveToNext()) {
                    tableName.add(cursor.getString(1));
                }
                cursor.close();
            }
            Log.d(getClass().getName(),"Select: " +select);
        }catch (Exception e)
        {
            Log.e(getClass().getName(),"Unable to getAllTable: " + e);
        } finally {
            return tableName;
        }
    }

    public ArrayList<String> getAllColumnNamesbyTableName (String tableName)
    {
        ArrayList<String> columnNames = new ArrayList<>();
        int countColumn = 0;
        try
        {

            String select = "SELECT*FROM " + tableName;
            Cursor cursor = db_helper.getWritableDatabase().rawQuery(select,null);
            Log.d(getClass().getName(),"Select: " +select);
            if(cursor != null)
            {
                String[] allColumnNames = cursor.getColumnNames();
                for(String column : allColumnNames)
                    columnNames.add(column);

                cursor.close();

                return columnNames;
            }
        }catch (Exception e)
        {
            Log.e(getClass().getName(),"Unable to getAllColumnNamesbyTableName: " + e);
        }
        finally {
            return columnNames;
        }
    }

    public ArrayList<String> getAllDataFromTableByColumnName (String tableName,String columnName) {
        ArrayList<String> getAllData = new ArrayList<>();

        try {

            String select = "SELECT " + columnName + " FROM " + tableName;
            Cursor cursor = db_helper.getWritableDatabase().rawQuery(select,null);
            if(cursor != null)
            {
                while (cursor.moveToNext())
                    getAllData.add(cursor.getString(cursor.getColumnIndex(columnName)));
            }
        } catch (Exception e) {
            Log.e(getClass().getName(),"Unable to getAllDataFromTableByColumnName " + e);
        } finally {
                return getAllData;
        }
    }

    public int getDataCount (String tableName,String columnName) {
        int count = 0;
        try {
            String select = "SELECT " + columnName + " FROM " + tableName;
            Cursor cursor = db_helper.getWritableDatabase().rawQuery(select,null);
            cursor.getCount();
        } catch (Exception e) {
            Log.e(getClass().getName(),"Unable to getAllDataFromTableByColumnName" + e);
        } finally {
            return count;
        }
    }

    public int getRowCount (String tableName) {
        int count = 0;
        try {
            String select = "SELECT COUNT(*) FROM "+ tableName;
            count = (int)DatabaseUtils.queryNumEntries(db_helper.getWritableDatabase(),tableName
            );
            Log.d(getClass().getName(),"Select: " +select);
        } catch (Exception e) {
            Log.e(getClass().getName(),"Unable to getAllDataFromTableByColumnName" + e);
        } finally {
            return count;
        }
    }

    public String getDataFromTableByColumnAndRow (String tableName,String columnName,int row) {
        String getData ="";
        try {
            String select = "SELECT " + columnName + " FROM " + tableName + " LIMIT " + String.valueOf(row-1) + ", 1";
            Cursor cursor = db_helper.getWritableDatabase().rawQuery(select,null);
            if(cursor != null )
            {
                while (cursor.moveToNext()) {
                    getData = cursor.getString(cursor.getColumnIndex(columnName));
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(getClass().getName(),"Unable to getAllDataFromTableByColumnName" + e);
        } finally {
            return getData;
        }
    }
}
