package com.example.pedro.llistadc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    private static final String COL4 = "relacao";
    private static final String COL5 = "nivel";
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
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, " + COL3 + " INTEGER, " + COL4 + " TEXT, " + COL5 + " INTEGER)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Produto> getAllProdutosFromDBRelatedTo( String relacao, int nivel ){
        ArrayList<Produto> list = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL4 + " = '" + relacao + "' " + "AND " + COL5 + " = " + nivel;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor c = sqLiteDatabase.rawQuery(selectQuery, null);

        if(c.moveToFirst()){
            do {
                if( c.getInt( c.getColumnIndex(COL3)) == 0 ){
                    Item p = new Item( c.getString(c.getColumnIndex(COL2)) );
                    p.type = 0;

                    list.add(p);
                }
                else {
                    Pasta p = new Pasta( c.getString(c.getColumnIndex(COL2)) );
                    p.type = 1;

                    list.add(p);
                }

            } while (c.moveToNext());
        }
        c.close();

        return list;
    }



    public boolean addDataToDB(String item, int type, String relacao, int nivel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);
        contentValues.put(COL3, type);
        contentValues.put(COL4, relacao);
        contentValues.put(COL5, nivel);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else
            return true;

    }

    public void editDataFromDB(String oldValue, String newValue, String relacao, int nivel){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL2, newValue);
        ContentValues valuesRelated = new ContentValues();
        valuesRelated.put(COL4, newValue);

        db.update(TABLE_NAME, values, COL2 + " = '" + oldValue + "' AND " + COL4 + " = '"+ relacao +"' " + "AND " + COL5 + " = " + nivel, null);
        nivel++;
        db.update(TABLE_NAME, valuesRelated, COL4 + " = '" + oldValue + "' " + "AND " + COL5 + " = " + nivel, null);
    }

    public void deleteDataFromDB(String s, String relacao, int nivel){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL2 + " = '" + s + "' AND " + COL4 + " = '"+ relacao +"' " + "AND " + COL5 + " = " + nivel, null);

    }
}
