package com.example.z06;

public class Utwor {
    protected String tytuł;
    protected String autor;
    protected int rok;
    protected String opis;
    protected String stronaWWW;
    protected int id;

    public Utwor(String tytuł, String autor, int rok, String opis, String stronaWWW, int id) {
        this.tytuł = tytuł;
        this.autor = autor;
        this.rok = rok;
        this.opis = opis;
        this.stronaWWW = stronaWWW;
        this.id = id;
    }

    public String getTytul() {
        return tytuł;
    }





    public String getAutor() {
        return autor;
    }

    public int getRokWydania() {
        return rok;
    }

    public String getOpis() {
        return opis;
    }

    public String getStronaWWW() {
        return stronaWWW;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return new StringBuilder()
                .append("Tytuł: ").append(tytuł).append(" |").toString()
                + new StringBuilder()
                .append(" Autor: ").append(autor).append(" |").toString()
                + new StringBuilder()
                .append(" Rok: ").append(rok).append(" |").toString()
                + new StringBuilder()
                .append(" Opis: ").append(opis).append(" |").toString()
                + new StringBuilder()
                .append(" Strona WWW: ").append(stronaWWW).append(" |").toString()
                + new StringBuilder()
                .append(" Id: ").append(id).append(" |").toString();
    }

}
