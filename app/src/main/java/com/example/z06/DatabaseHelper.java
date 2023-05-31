package com.example.z06;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biblioteczka.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela ksiazki
    public static final String TABLE_KSIAZKI = "ksiazki";
    public static final String COL_ID = "_id";
    public static final String COL_KATALOGOWA = "katalogowa";
    public static final String COL_TYTUL = "tytul";
    public static final String COL_AUTOR = "autor";
    public static final String COL_ROK = "rok";
    public static final String COL_OPIS = "opis";
    public static final String COL_STRONA = "strona";

    // Tabela osoba
    public static final String TABLE_OSOBY = "osoby";
    public static final String COL_NAZWISKO = "nazwisko";
    public static final String COL_IMIE = "imie";
    public static final String COL_TELEFON = "telefon";
    public static final String COL_EMAIL = "email";

    //Tabela wypozyczenia książki
    public static final String TABLE_WYPOZYCZENIA = "wypozyczenia";
    public static final String COL_OSOBA = "osoba";
    public static final String COL_KSIAZKA = "ksiazka";
    public static final String COL_DATA = "data";
    public static final String COL_DATA_ZWROTU = "data_zwrotu";








    private static final String CREATE_TABLE_KSIAZKI = "CREATE TABLE " + TABLE_KSIAZKI + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_KATALOGOWA + " INTEGER, " +
            COL_TYTUL + " TEXT, " +
            COL_AUTOR + " TEXT, " +
            COL_ROK + " INTEGER, " +
            COL_OPIS + " TEXT, " +
            COL_STRONA + " TEXT);"
            ;

    private static final String CREATE_TABLE_OSOBY = "CREATE TABLE " + TABLE_OSOBY + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NAZWISKO + " TEXT, " +
            COL_IMIE + " TEXT, " +
            COL_TELEFON + " TEXT, " +
            COL_EMAIL + " TEXT);";

    private static final String CREATE_TABLE_WYPOZYCZENIA = "CREATE TABLE " + TABLE_WYPOZYCZENIA + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_OSOBA + " TEXT, " +
            COL_KSIAZKA + " TEXT, " +
            COL_DATA + " DATA, " +
            COL_DATA_ZWROTU + " DATA);";





    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KSIAZKI);
        db.execSQL(CREATE_TABLE_OSOBY);
        db.execSQL(CREATE_TABLE_WYPOZYCZENIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KSIAZKI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OSOBY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WYPOZYCZENIA);
        onCreate(db);
    }




    public List<Ksiazka> getAllBooks(){
        List<Ksiazka> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_KSIAZKI, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                int katalogowa = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KATALOGOWA));
                String tytul = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYTUL));
                String autor = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTOR));
                int rok = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROK));
                String opis = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPIS));
                String strona = cursor.getString(cursor.getColumnIndexOrThrow(COL_STRONA));

                Ksiazka book = new Ksiazka(katalogowa, tytul, autor, rok, opis, strona, id);
                books.add(book);
            } while (cursor.moveToNext());
        }

        return books;
    }

    public List<Osoba> getAllPeople(){
        List<Osoba> persons = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_OSOBY, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String nazwisko = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAZWISKO));
                String imie = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMIE));
                String telefon = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFON));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));

                Osoba person = new Osoba(nazwisko, imie, telefon, email, id);
                persons.add(person);
            } while (cursor.moveToNext());
        }

        return persons;
    }





    public void addBorrow(Wypozyczenie wypozyczenie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_OSOBA, wypozyczenie.getOsoba().getId());
        values.put(COL_KSIAZKA, wypozyczenie.getKsiazka().getId());
        values.put(COL_DATA, wypozyczenie.getData());
        values.put(COL_DATA_ZWROTU, wypozyczenie.getData_zwrotu());
        db.insert(TABLE_WYPOZYCZENIA, null, values);
        db.close();
    }

    public List<Wypozyczenie> getAllBorrows() {

        List<Wypozyczenie> borrows = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_WYPOZYCZENIA + " WHERE " +  COL_DATA_ZWROTU + " IS NULL";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                int osoba = cursor.getInt(cursor.getColumnIndexOrThrow(COL_OSOBA));
                int ksiazka = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KSIAZKA));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA));
                String data_zwrotu = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_ZWROTU));
                Osoba person = getPersonById(osoba);
                Ksiazka book = getBookById(ksiazka);

                Wypozyczenie borrow = new Wypozyczenie(person, book, data, data_zwrotu, id);
                borrows.add(borrow);
            } while (cursor.moveToNext());
        }

        return borrows;
    }

    private Osoba getPersonById(int osoba) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_OSOBY + " where " + COL_ID + " = " + osoba, null);
        Osoba person = null;
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String nazwisko = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAZWISKO));
                String imie = cursor.getString(cursor.getColumnIndexOrThrow(COL_IMIE));
                String telefon = cursor.getString(cursor.getColumnIndexOrThrow(COL_TELEFON));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL));

                person = new Osoba(nazwisko, imie, telefon, email, id);
            } while (cursor.moveToNext());
        }

        return person;
    }

    private Ksiazka getBookById(int ksiazka) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_KSIAZKI + " where " + COL_ID + " = " + ksiazka, null);
        Ksiazka book = null;
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                int katalogowa = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KATALOGOWA));
                String tytul = cursor.getString(cursor.getColumnIndexOrThrow(COL_TYTUL));
                String autor = cursor.getString(cursor.getColumnIndexOrThrow(COL_AUTOR));
                int rok = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROK));
                String opis = cursor.getString(cursor.getColumnIndexOrThrow(COL_OPIS));
                String strona = cursor.getString(cursor.getColumnIndexOrThrow(COL_STRONA));

                book = new Ksiazka(katalogowa, tytul, autor, rok, opis, strona, id);
            } while (cursor.moveToNext());
        }
        return book;
    }

    public List<Wypozyczenie> getAllReturnedRentals(){
        List<Wypozyczenie> rentalList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_WYPOZYCZENIA + " WHERE " +  COL_DATA_ZWROTU + " IS NOT NULL";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                int osoba = cursor.getInt(cursor.getColumnIndexOrThrow(COL_OSOBA));
                int ksiazka = cursor.getInt(cursor.getColumnIndexOrThrow(COL_KSIAZKA));
                String data = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA));
                String data_zwrotu = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_ZWROTU));
                Osoba person = getPersonById(osoba);
                Ksiazka book = getBookById(ksiazka);

                Wypozyczenie rental = new Wypozyczenie(person, book, data, data_zwrotu, id);
                rentalList.add(rental);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return rentalList;
    }



    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void returnBook(Wypozyczenie wypozyczenie) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DATA_ZWROTU, getCurrentDateTime());
        db.update(TABLE_WYPOZYCZENIA, values, COL_ID + " = ?",
                new String[]{String.valueOf(wypozyczenie.getId())});
        db.close();

    }

}


