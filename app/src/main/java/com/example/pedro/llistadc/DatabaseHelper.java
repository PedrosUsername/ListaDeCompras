package com.example.pedro.llistadc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.inputmethod.InputConnectionWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 11/05/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    //ProdutosContract
    private static final String TABLE_NAME = "produtos";
    private static final String COL1 = "ID";
    private static final String COL2 = "titulo";
    private static final String COL3 = "tipo";
    private static final String COL6 = "produtoId";
    private static final String COL7 = "path";
    //*****************


    private static final String DATABASE_NAME = "llistas.db";
    static final int DATABASE_VERSION = 1;



    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " INTEGER, " + COL6 + " INTEGER, " + COL7 + " TEXT)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Produto> getAllProdutosFromDBRelatedTo(String path){
        ArrayList<Produto> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL7 + " GLOB '" + path + ",[0-9]' OR "+ COL7 + " GLOB '" + path + ",[0-9][0-9]' OR "+ COL7 + " GLOB '" + path + ",[0-9][0-9][0-9]'";
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do {
                if( c.getInt( c.getColumnIndex(COL3)) == 0 ){
                    Item p = new Item( c.getString(c.getColumnIndex(COL2)) );
                    p.type = 0;
                    p.setId( c.getInt(c.getColumnIndex(COL6)) );
                    p.setPath( c.getString(c.getColumnIndex(COL7)) );

                    list.add(p);
                }
                else {
                    Pasta p = new Pasta( c.getString(c.getColumnIndex(COL2)) );
                    p.type = 1;
                    p.setId( c.getInt(c.getColumnIndex(COL6)) );
                    p.setPath( c.getString(c.getColumnIndex(COL7)) );

                    list.add(p);
                }

            } while (c.moveToNext());
        }
        c.close();

        return list;
    }



    public boolean addDataToDB(String item, int type, int produtoId, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, type);
        contentValues.put(COL6, produtoId);
        contentValues.put(COL7, path);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else
            return true;

    }

    public void editDataFromDBTitle(String newValue, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, newValue);

        db.update(TABLE_NAME, values, COL7 + " = '" + path + "'", null);
    }

    public void editDataFromDBID(int newValue, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL6, newValue);

        db.update(TABLE_NAME, values, COL7 + " = '" + path + "'", null);
    }

    public void editDataFromDBType(int newValue, String path){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL3, newValue);

        db.update(TABLE_NAME, values, COL7 + " = '" + path + "'", null);
    }

    public void editPathFromDB(String newValue, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_NAME + " SET " +COL7+ " = REPLACE(" +COL7+ ", '"+path+"', '" +newValue+ "') WHERE " +COL7+ " LIKE ('"+ path +",%') OR "+COL7+" = '"+path+"'";

        ContentValues values = new ContentValues();
        values.put(COL7, newValue);

        db.execSQL(selectQuery);
    }
    public void editPathFromDBSimple(String newValue, String path){
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "UPDATE " + TABLE_NAME + " SET " +COL7+ " = REPLACE(" +COL7+ ", '"+path+"', '" +newValue+ "') WHERE " +COL7+" = '"+path+"'";

        ContentValues values = new ContentValues();
        values.put(COL7, newValue);

        db.execSQL(selectQuery);
    }



    public void deleteDataFromDBWithPath(String path){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL7 + " LIKE ('"+ path +",%') OR "+COL7+" = '"+path+"'", null);
    }
}
