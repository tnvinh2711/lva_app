package com.lva.shop.db;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lva.shop.ui.location.model.Address;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "dvhc.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Activity activity) {
        super(activity, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Address> getTinh() {
        List<Address> listAddress = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Tinh", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Address address = new Address();
                address.setId(cursor.getString(0));
                address.setName(cursor.getString(1));
                listAddress.add(address);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAddress;
    }

    public List<Address> getHuyen(String tinh) {
        List<Address> listHuyen = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Huyen WHERE tinh = ?", new String[]{tinh});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Address huyen = new Address();
                huyen.setId(cursor.getString(0));
                huyen.setName(cursor.getString(1));
                huyen.setFilter(cursor.getString(2));
                listHuyen.add(huyen);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listHuyen;
    }

    public List<Address> getXa(String huyen) {
        List<Address> listXa = new ArrayList<>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM Xa WHERE huyen = ?", new String[]{huyen});
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Address xa = new Address();
                xa.setId(cursor.getString(0));
                xa.setName(cursor.getString(1));
                xa.setFilter(cursor.getString(2));
                listXa.add(xa);
                cursor.moveToNext();
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listXa;
    }
}