package com.example.z06;




public class Wypozyczenie {

    private Osoba osoba;
    private Ksiazka ksiazka;
    private String dataWypozyczenia;
    private String dataOddania;
    int id;


    public Wypozyczenie(Osoba osoba, Ksiazka ksiazka, String dataWypozyczenia, String dataOddania, int id) {
        this.osoba = osoba;
        this.ksiazka = ksiazka;
        this.dataWypozyczenia = dataWypozyczenia;
        this.dataOddania = dataOddania;
        this.id = id;
    }

    public Osoba getOsoba() {
        return osoba;
    }

    public Ksiazka getKsiazka() {
        return ksiazka;
    }

    public String getData(){
        return dataWypozyczenia;
    }

    public String getData_zwrotu(){
        return dataOddania;
    }


    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Osoba: ").append(osoba).append(" |").toString()
                + new StringBuilder()
                .append(" Ksiazka: ").append(ksiazka).append(" |").toString()
                + new StringBuilder()
                .append(" Data wypożyczenia: ").append(dataWypozyczenia).append(" |").toString()
                + new StringBuilder()
                .append(" Data oddania: ").append(dataOddania).append(" |").toString()
                + new StringBuilder()
                .append(" Id: ").append(id).append(" |").toString();
    }
}
