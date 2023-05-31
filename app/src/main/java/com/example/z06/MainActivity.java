package com.example.z06;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText bookTitle, bookAuthor, bookWWWpage, bookYear, bookDesc, bookKatalogowa, et_borrow_date;
    private Button addBook, viewAllBooks, viewAllPeople, viewAllLoans, addLoan, save_borrow_button, booksBtn, button_oddaj, save_return_button, loansHistory, action_back;
    private ListView listView;
    private Spinner spinnerOsoba, spinnerKsiazka, spinnerWypo;
    private Biblioteczka biblioteczka;
    private DatabaseHelper databaseHelper;

    private List<Ksiazka> books = new ArrayList<>();
    private List<Osoba> people = new ArrayList<>();
    private List<Wypozyczenie> loans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        booksBtn = findViewById(R.id.books);
        viewAllPeople = findViewById(R.id.viewAllPeople);
        viewAllLoans = findViewById(R.id.loans);
        loansHistory = findViewById(R.id.loansHistory);
        addLoan = findViewById(R.id.addLoan);
        biblioteczka = new Biblioteczka(this);
        databaseHelper = new DatabaseHelper(this);


        loansHistory.setOnClickListener(this::onClick8);

        viewAllPeople.setOnClickListener(this::onClick2);
        booksBtn.setOnClickListener(this::onClick5);

        addLoan.setOnClickListener(this::onClick6);
        viewAllLoans.setOnClickListener(this::onClick7);
    }

    private void onClick8(View view) {
        setContentView(R.layout.loans_history_activity);
        listView = findViewById(R.id.wypohist_list_view);
        action_back = findViewById(R.id.action_back);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        loans = databaseHelper.getAllReturnedRentals();
        ListAdapter listAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.loans_history_activity, R.id.loans_history_title_text_view, loans);
        listView.setAdapter(listAdapter);
        action_back.setOnClickListener(this::onBack);


    }

    private void onClick7(View view) {
        setContentView(R.layout.wypozyczenia_activity);
        listView = findViewById(R.id.wypo_list_view);
        action_back = findViewById(R.id.action_back);
        databaseHelper = new DatabaseHelper(MainActivity.this);
        loans = databaseHelper.getAllBorrows();
        ListAdapter listAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.wypozyczenia_activity, R.id.wypo_title_text_view, loans);
        listView.setAdapter(listAdapter);

        button_oddaj = findViewById(R.id.button_oddaj);
        button_oddaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              setContentView(R.layout.oddaj_activity);
              spinnerWypo = findViewById(R.id.spinner_wypo);
                spinnerWypo.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, databaseHelper.getAllBorrows()));
                save_return_button = findViewById(R.id.save_borrow_button);
                save_return_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Wypozyczenie wypozyczenie = (Wypozyczenie) spinnerWypo.getSelectedItem();
                        databaseHelper.returnBook(wypozyczenie);
                        Toast.makeText(MainActivity.this, "Oddano książkę", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        action_back.setOnClickListener(this::onBack);
    }

    private void onClick6(View view) {
        setContentView(R.layout.dodajwypo_activity);
        spinnerOsoba = findViewById(R.id.spinner_osoba);
        spinnerKsiazka = findViewById(R.id.spinner_ksiazka);
        save_borrow_button = findViewById(R.id.save_borrow_button);
        et_borrow_date = findViewById(R.id.et_borrow_date);
        action_back = findViewById(R.id.action_back);

        databaseHelper = new DatabaseHelper(this);

        spinnerKsiazka.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, databaseHelper.getAllBooks()));
        spinnerOsoba.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, databaseHelper.getAllPeople()));

        action_back.setOnClickListener(this::onBack);

        save_borrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Osoba osoba = (Osoba) spinnerOsoba.getSelectedItem();
                Ksiazka ksiazka = (Ksiazka) spinnerKsiazka.getSelectedItem();
                String borrowDate = et_borrow_date.getText().toString();
                Wypozyczenie wypozyczenie = new Wypozyczenie(osoba, ksiazka, borrowDate, null, 0);
                databaseHelper.addBorrow(wypozyczenie);
                Toast.makeText(MainActivity.this, "Dodano wypożyczenie", Toast.LENGTH_SHORT).show();

                spinnerOsoba.setSelection(0);
                spinnerKsiazka.setSelection(0);
                et_borrow_date.setText("");

            }
        });
    }


    private void getContactsAndsaveToDb() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Pobranie imienia i nazwiska z kontaktu
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
               String surname = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_ALTERNATIVE));

                // Pobranie adresów e-mail i numerów telefonu z kontaktu
                Cursor emailCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                       ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))}, null);
                String email = "";
                if (emailCursor != null && emailCursor.moveToFirst()) {
                    email = emailCursor.getString(emailCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA));
                    emailCursor.close();
                }

              Cursor phoneCursor = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))}, null);

                String phoneNumber = "";
               if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phoneCursor.close();
                }

              // Sprawdzenie czy osoba już istnieje w bazie danych i dodanie jej do bazy danych

                Osoba osoba = new Osoba(name, surname, email, phoneNumber, 0);
               biblioteczka.open();
                if (!biblioteczka.czyOsobaIstnieje(osoba)) {
                   biblioteczka.dodajOsobe(osoba);
                }
               biblioteczka.close();
           }
        }
    }

    private void onClick2(View view) {
        getContactsAndsaveToDb();
        setContentView(R.layout.osoby_activity);
        listView = findViewById(R.id.osoby_list_view);
        action_back = findViewById(R.id.action_back);
        databaseHelper = new DatabaseHelper(this);
        people = databaseHelper.getAllPeople();
        ListAdapter listAdapter = new ArrayAdapter<>(this, R.layout.osoby_activity, R.id.titleTextViewOsoba, people);
        listView.setAdapter(listAdapter);
        action_back.setOnClickListener(this::onBack);


    }



    private void onClick5(View view) {
        setContentView(R.layout.ksiaka_activity);
        bookTitle = findViewById(R.id.bookTitle);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookWWWpage = findViewById(R.id.bookPages);
        bookYear = findViewById(R.id.bookYear);
        bookDesc = findViewById(R.id.bookDesc);
        addBook = findViewById(R.id.addBook);
        bookKatalogowa = findViewById(R.id.bookKatalogowa);
        viewAllBooks = findViewById(R.id.viewAllBooks);
        action_back = findViewById(R.id.action_back);

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = bookTitle.getText().toString().trim();
                String author = bookAuthor.getText().toString().trim();
                String year = bookYear.getText().toString().trim();
                String description = bookDesc.getText().toString().trim();
                String website = bookWWWpage.getText().toString().trim();
                String katalogowa = bookKatalogowa.getText().toString().trim();

                Ksiazka ksiazka = new Ksiazka(Integer.parseInt(katalogowa), title, author, Integer.parseInt(year), description, website, 0);

                biblioteczka.open();
                biblioteczka.dodajKsiazke(ksiazka);
                biblioteczka.close();

                Toast.makeText(MainActivity.this, "Dodano książkę", Toast.LENGTH_SHORT).show();

                bookTitle.setText("");
                bookAuthor.setText("");
                bookYear.setText("");
                bookDesc.setText("");
                bookWWWpage.setText("");
                bookKatalogowa.setText("");


            }
        });

        viewAllBooks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.ksiazki_activity);
                listView = findViewById(R.id.ksiazki_list_view);
                databaseHelper = new DatabaseHelper(MainActivity.this);
                books = databaseHelper.getAllBooks();
                ListAdapter listAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, books);
                listView.setAdapter(listAdapter);
            }
        });

        action_back.setOnClickListener(this::onBack);
    }

    private void onBack(View view) {
        onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
