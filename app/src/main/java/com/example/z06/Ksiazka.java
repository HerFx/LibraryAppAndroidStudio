package com.example.z06;

public class Ksiazka extends Utwor {
    private int katalogowa;


    public Ksiazka(int katalogowa, String tytul, String autor, int rokWydania, String opis, String stronaWWW, int id) {
        super(tytul, autor, rokWydania, opis, stronaWWW, id);
        this.katalogowa = katalogowa;
    }

    public int getKatalogowa() {
        return katalogowa;
    }

    @Override
    public String toString(){
        return  super.toString() + " Książka";
    }
}
