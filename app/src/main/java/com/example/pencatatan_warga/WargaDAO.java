package com.example.pencatatan_warga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WargaDAO {

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME,
            DatabaseHelper.COLUMN_JOB
    };

    public WargaDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Warga createWarga(String name, String job) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_JOB, job);
        long insertId = database.insert(DatabaseHelper.TABLE_WARGA, null, values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_WARGA, allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        Warga newWarga = cursorToWarga(cursor);
        cursor.close();
        return newWarga;
    }

    public void deleteWarga(Warga warga) {
        long id = warga.getId();
        database.delete(DatabaseHelper.TABLE_WARGA, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }
    public void updateWarga(Warga warga) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, warga.getNama());
        values.put(DatabaseHelper.COLUMN_JOB, warga.getPekerjaan());

        database.update(DatabaseHelper.TABLE_WARGA, values, DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(warga.getId())});
    }

    public Warga getWarga(long id) {
        Cursor cursor = database.query(DatabaseHelper.TABLE_WARGA, allColumns, DatabaseHelper.COLUMN_ID + " = " + id, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            Warga warga = cursorToWarga(cursor);
            cursor.close();
            return warga;
        } else {
            return null;
        }
    }

    public List<Warga> getAllWarga() {
        List<Warga> wargaList = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_WARGA, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Warga warga = cursorToWarga(cursor);
            wargaList.add(warga);
            cursor.moveToNext();
        }
        cursor.close();
        return wargaList;
    }

    private Warga cursorToWarga(Cursor cursor) {
        Warga warga = new Warga();
        warga.setId(cursor.getLong(0));
        warga.setNama(cursor.getString(1));
        warga.setPekerjaan(cursor.getString(2));
        return warga;
    }
}

