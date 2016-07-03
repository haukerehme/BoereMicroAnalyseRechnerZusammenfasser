package de.hrs.model;

/**
 * Created by hrs on 21.06.16.
 */
public class Tradevorhersage {
    double wahrscheinlichkeitShort;
    double wahrscheinlichkeitLong;
    double wahrscheinlichkeitLongHoherGewinn;
    double wahrscheinlichkeitShortHoherGewinn;
    int anzForm;


    int GewinnzaehlerLong;
    int VerlustzaehlerLong;

    int GewinnzaehlerShort;
    int VerlustzaehlerShort;

    int GenerellPlus;
    int GenerellMinus;
    int hohesMinus;
    int hohesPlus ;

    int hoherLongVerlust;
    int geringerLongGewinn;
    int mittlererLongGewinn;
    int hoherLongGewinn;
    int sehrHoherLongGewinn;

    int geringerShortGewinn;
    int mittlererShortGewinn;
    int hoherShortGewinn;
    int sehrHoherShortGewinn;
    int hoherShortVerlust;
    int anzFormFound;

    public Tradevorhersage(){}

    public Tradevorhersage(int anzForm, double wahrscheinlichkeitLong, double wahrscheinlichkeitShort, double wahrscheinlichkeitLongHoherGewinn, double wahrscheinlichkeitShortHoherGewinn){
        this.anzForm = anzForm;
        this.wahrscheinlichkeitLong = wahrscheinlichkeitLong;
        this.wahrscheinlichkeitShort = wahrscheinlichkeitShort;

        this.wahrscheinlichkeitLongHoherGewinn = wahrscheinlichkeitLongHoherGewinn;
        this.wahrscheinlichkeitShortHoherGewinn = wahrscheinlichkeitShortHoherGewinn;
    }

    double Auswertung(double startWert, int differenzEndpunkt, double spread, boolean longTrade) {
        double schlusswert = (startWert + (differenzEndpunkt/10000));
        startWert -= spread;
        double guv = (schlusswert - startWert) / startWert;
        if(!longTrade){
            guv *= -1;
        }
        return guv;
    }

    public double getWahrscheinlichkeitShort() {
        return wahrscheinlichkeitShort;
    }

    public void setWahrscheinlichkeitShort(double wahrscheinlichkeitShort) {
        this.wahrscheinlichkeitShort = wahrscheinlichkeitShort;
    }

    public double getWahrscheinlichkeitLong() {
        return wahrscheinlichkeitLong;
    }

    public void setWahrscheinlichkeitLong(double wahrscheinlichkeitLong) {
        this.wahrscheinlichkeitLong = wahrscheinlichkeitLong;
    }

    public double getWahrscheinlichkeitLongHoherGewinn() {
        return wahrscheinlichkeitLongHoherGewinn;
    }

    public void setWahrscheinlichkeitLongHoherGewinn(double wahrscheinlichkeitLongHoherGewinn) {
        this.wahrscheinlichkeitLongHoherGewinn = wahrscheinlichkeitLongHoherGewinn;
    }

    public double getWahrscheinlichkeitShortHoherGewinn() {
        return wahrscheinlichkeitShortHoherGewinn;
    }

    public void setWahrscheinlichkeitShortHoherGewinn(double wahrscheinlichkeitShortHoherGewinn) {
        this.wahrscheinlichkeitShortHoherGewinn = wahrscheinlichkeitShortHoherGewinn;
    }

    public int getAnzForm() {
        return anzForm;
    }

    public void setAnzForm(int anzForm) {
        this.anzForm = anzForm;
    }

    public int getGewinnzaehlerLong() {
        return GewinnzaehlerLong;
    }

    public void setGewinnzaehlerLong(int gewinnzaehlerLong) {
        GewinnzaehlerLong = gewinnzaehlerLong;
    }

    public int getVerlustzaehlerLong() {
        return VerlustzaehlerLong;
    }

    public void setVerlustzaehlerLong(int verlustzaehlerLong) {
        VerlustzaehlerLong = verlustzaehlerLong;
    }

    public int getGewinnzaehlerShort() {
        return GewinnzaehlerShort;
    }

    public void setGewinnzaehlerShort(int gewinnzaehlerShort) {
        GewinnzaehlerShort = gewinnzaehlerShort;
    }

    public int getVerlustzaehlerShort() {
        return VerlustzaehlerShort;
    }

    public void setVerlustzaehlerShort(int verlustzaehlerShort) {
        VerlustzaehlerShort = verlustzaehlerShort;
    }

    public int getGenerellPlus() {
        return GenerellPlus;
    }

    public void setGenerellPlus(int generellPlus) {
        GenerellPlus = generellPlus;
    }

    public int getGenerellMinus() {
        return GenerellMinus;
    }

    public void setGenerellMinus(int generellMinus) {
        GenerellMinus = generellMinus;
    }

    public int getHohesMinus() {
        return hohesMinus;
    }

    public void setHohesMinus(int hohesMinus) {
        this.hohesMinus = hohesMinus;
    }

    public int getHohesPlus() {
        return hohesPlus;
    }

    public void setHohesPlus(int hohesPlus) {
        this.hohesPlus = hohesPlus;
    }

    public int getHoherLongVerlust() {
        return hoherLongVerlust;
    }

    public void setHoherLongVerlust(int hoherLongVerlust) {
        this.hoherLongVerlust = hoherLongVerlust;
    }

    public int getGeringerLongGewinn() {
        return geringerLongGewinn;
    }

    public void setGeringerLongGewinn(int geringerLongGewinn) {
        this.geringerLongGewinn = geringerLongGewinn;
    }

    public int getMittlererLongGewinn() {
        return mittlererLongGewinn;
    }

    public void setMittlererLongGewinn(int mittlererLongGewinn) {
        this.mittlererLongGewinn = mittlererLongGewinn;
    }

    public int getHoherLongGewinn() {
        return hoherLongGewinn;
    }

    public void setHoherLongGewinn(int hoherLongGewinn) {
        this.hoherLongGewinn = hoherLongGewinn;
    }

    public int getSehrHoherLongGewinn() {
        return sehrHoherLongGewinn;
    }

    public void setSehrHoherLongGewinn(int sehrHoherLongGewinn) {
        this.sehrHoherLongGewinn = sehrHoherLongGewinn;
    }

    public int getGeringerShortGewinn() {
        return geringerShortGewinn;
    }

    public void setGeringerShortGewinn(int geringerShortGewinn) {
        this.geringerShortGewinn = geringerShortGewinn;
    }

    public int getMittlererShortGewinn() {
        return mittlererShortGewinn;
    }

    public void setMittlererShortGewinn(int mittlererShortGewinn) {
        this.mittlererShortGewinn = mittlererShortGewinn;
    }

    public int getHoherShortGewinn() {
        return hoherShortGewinn;
    }

    public void setHoherShortGewinn(int hoherShortGewinn) {
        this.hoherShortGewinn = hoherShortGewinn;
    }

    public int getSehrHoherShortGewinn() {
        return sehrHoherShortGewinn;
    }

    public void setSehrHoherShortGewinn(int sehrHoherShortGewinn) {
        this.sehrHoherShortGewinn = sehrHoherShortGewinn;
    }

    public int getHoherShortVerlust() {
        return hoherShortVerlust;
    }

    public void setHoherShortVerlust(int hoherShortVerlust) {
        this.hoherShortVerlust = hoherShortVerlust;
    }

    public int getAnzFormFound() {
        return anzFormFound;
    }

    public void setAnzFormFound(int anzFormFound) {
        this.anzFormFound = anzFormFound;
    }
}
