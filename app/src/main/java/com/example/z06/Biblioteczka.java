package com.example.z06;

import static com.example.z06.DatabaseHelper.TABLE_KSIAZKI;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class Biblioteczka {
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public Biblioteczka(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public void dodajKsiazke(Ksiazka ksiazka){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_KATALOGOWA, ksiazka.getKatalogowa());
        values.put(DatabaseHelper.COL_TYTUL, ksiazka.getTytul() );
        values.put(DatabaseHelper.COL_AUTOR, ksiazka.getAutor() );
        values.put(DatabaseHelper.COL_ROK, ksiazka.getRokWydania());
        values.put(DatabaseHelper.COL_OPIS, ksiazka.getOpis());
        values.put(DatabaseHelper.COL_STRONA, ksiazka.getStronaWWW());
        db.insert(TABLE_KSIAZKI, null, values);
    }

    public void dodajOsobe(Osoba osoba){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COL_NAZWISKO, osoba.getNazwisko() );
        values.put(DatabaseHelper.COL_IMIE, osoba.getImie() );
        values.put(DatabaseHelper.COL_TELEFON, osoba.getTelefon());
        values.put(DatabaseHelper.COL_EMAIL, osoba.getEmail());
        db.insert(DatabaseHelper.TABLE_OSOBY, null, values);
    }


    public boolean czyOsobaIstnieje(Osoba osoba) {
        Cursor cursor = db.query(DatabaseHelper.TABLE_OSOBY, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int telefonIndex = cursor.getColumnIndex(DatabaseHelper.COL_TELEFON);
            int emailIndex = cursor.getColumnIndex(DatabaseHelper.COL_EMAIL);
            if (cursor.getString(telefonIndex).equals(osoba.getTelefon()) || cursor.getString(emailIndex).equals(osoba.getEmail())) {
                return true;
            }
        }
        return false;
    }


}
