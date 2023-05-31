package com.example.z06;

public class Osoba {
    private String imie;
    private String nazwisko;
    private String email;
    private String telefon;
    private int id;

    public Osoba(String imie, String nazwisko, String email, String telefon, int id) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.email = email;
        this.telefon = telefon;
        this.id = id;
    }

    public String getImie() {
        return imie;
    }


    public String getNazwisko() {
        return nazwisko;
    }


    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }


    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Imię: ").append(imie).append(" |").toString()
                + new StringBuilder()
                .append(" Nazwisko: ").append(nazwisko).append(" |").toString()
                + new StringBuilder()
                .append(" Email: ").append(email).append(" |").toString()
                + new StringBuilder()
                .append(" Telefon: ").append(telefon).append(" |").toString()
                + new StringBuilder()
                .append(" Id: ").append(id).append(" |").toString();
    }

}
