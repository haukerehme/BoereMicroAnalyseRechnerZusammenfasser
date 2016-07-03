package de.hrs.Rechner;

import de.hrs.model.Tradevorhersage;

import java.util.List;

/**
 * Created by hrs on 21.06.16.
 */
public class RechnerZusammenfasser {
    List<Integer> closewerte;
    List<List<Integer>> aktuellerAbschnittUnterteilt;
    int ausgangspkt,vergleichsLaenge,auswertungslaenge;
    boolean longPosition, mehrereVergleichsstrecken, SimulatorModus;
    int zusammenfasserInterval;
    int spread;
    String instrument;

    RechnerZusammenfasser(){}

    public RechnerZusammenfasser(List<Integer> intArray,int ausgangspkt,int vergleichsLaenge,int auswertungslaenge, int zusammenfasserInterval, int spread, String instrument, boolean mehrereVergleichsstrecken, boolean Simulatormodus){
        closewerte = intArray;
        this.ausgangspkt =ausgangspkt;
        this.vergleichsLaenge = vergleichsLaenge;
        this.auswertungslaenge = auswertungslaenge;
        this.zusammenfasserInterval = zusammenfasserInterval;
        this.spread = spread;
        this.instrument = instrument;
        this.mehrereVergleichsstrecken = mehrereVergleichsstrecken;
        this.SimulatorModus = Simulatormodus;
    }

    Tradevorhersage analyse(/*int ausgangspkt,int vergleichsLaenge,int auswertungslaenge*/){

        int GewinnzaehlerLong = 0;
        int VerlustzaehlerLong = 0;

        int GewinnzaehlerShort = 0;
        int VerlustzaehlerShort = 0;

        int GenerellPlus = 0;
        int GenerellMinus = 0;
        int hohesMinus = 0;
        int hohesPlus = 0;

        int hoherLongVerlust = 0;
        int geringerLongGewinn = 0;
        int mittlererLongGewinn = 0;
        int hoherLongGewinn = 0;
        int sehrHoherLongGewinn = 0;

        int geringerShortGewinn = 0;
        int mittlererShortGewinn = 0;
        int hoherShortGewinn = 0;
        int sehrHoherShortGewinn = 0;
        int hoherShortVerlust = 0;
        int anzFormFound = 0;
        int anzErsterRight =0;
        boolean formFound;

        List<Integer> akt = getAnalyseArray(vergleichsLaenge);
        //System.out.println("Size akt-Array: " + akt.size());
        //System.out.println("Zusammenfasser: "+ this.zusammenfasserInterval);
        //aktuellerAbschnittUnterteilt = new ArrayList<List<Integer>>();

        //aktueller Array wird zuvor in sublists unterteilt
        //  -> Performance verbessern ;)
        /*for(int u = 0; u < akt.size(); u=u+zusammenfasserInterval){
            aktuellerAbschnittUnterteilt.add(akt.subList(u, u+zusammenfasserInterval-1));
        }*/
        //System.out.println("Arraygröße: "+aktuellerAbschnittUnterteilt.size());
        for(int i = 0; i < closewerte.size()-(vergleichsLaenge+this.auswertungslaenge);i++){
            formFound = true;

            if(
                //sublistAddierer(aktuellerAbschnittUnterteilt.get(0)) - addierer(closewerte,i, i+zusammenfasserInterval-1) < 4 &&
                //sublistAddierer(aktuellerAbschnittUnterteilt.get(0)) - addierer(closewerte,i, i+zusammenfasserInterval-1) > -4

                    addierer(akt,0,zusammenfasserInterval-1) - addierer(closewerte,i, i+zusammenfasserInterval-1) < 4 &&
                            addierer(akt,0,zusammenfasserInterval-1) - addierer(closewerte,i, i+zusammenfasserInterval-1) > -4
                    ){
                for(int z = zusammenfasserInterval; z < akt.size();z=z+zusammenfasserInterval){
                    if(
                        //sublistAddierer(aktuellerAbschnittUnterteilt.get((z+1)/zusammenfasserInterval)) - addierer(closewerte,i+z+1, i+z+zusammenfasserInterval) >= 4 ||
                        //sublistAddierer(aktuellerAbschnittUnterteilt.get((z+1)/zusammenfasserInterval)) - addierer(closewerte,i+z+1, i+z+zusammenfasserInterval) <= -4

                            addierer(akt,z,z+zusammenfasserInterval-1) - addierer(closewerte,i+z, i+z+zusammenfasserInterval-1) >= 4 ||
                                    addierer(akt,z,z+zusammenfasserInterval-1) - addierer(closewerte,i+z, i+z+zusammenfasserInterval-1) <= -4
                            )
                    {
                        formFound = false;
                        break;
                    }
                }
                if(formFound){
                    if(
                            (addierer(akt,0,akt.size()-1) - addierer(closewerte,i, akt.size()-1) >= (this.vergleichsLaenge / 10)) ||
                                    (addierer(akt,0,akt.size()-1) - addierer(closewerte,i, akt.size()-1) <= -(this.vergleichsLaenge /10 ))
                            ){
                        //System.out.println("Vom Endgegner abgelehnt!!!");
                        formFound = false;
                    }
                }
                if(formFound){
                    anzFormFound++;
                    int entwicklung = 0;
                    for(int z =i+this.vergleichsLaenge;z < i+this.vergleichsLaenge+this.auswertungslaenge;z++){
                        entwicklung += closewerte.get(z);
                    }
                    if(entwicklung > 0)
                    {
                        //System.out.println("Gewinn " + alledaten.get(ausgangswert).zeitdatum + " " + alledaten.get(ausgangswert).Closewert);
                        GenerellPlus++;
                        if(entwicklung > 10){
                            hohesPlus++;
                        }
                    }
                    if(entwicklung < 0){
                        GenerellMinus++;
                        if(entwicklung < -10){
                            hohesMinus++;
                        }
                    }

                    //bei Gewinn wird 1 zurückgegeben, bei Verlust 2 und wenn es gleich geblieben ist 0.
                    if(entwicklung > this.spread)
                    {
                        //System.out.println("Gewinn " + alledaten.get(ausgangswert).zeitdatum + " " + alledaten.get(ausgangswert).Closewert);
                        GewinnzaehlerLong++;
                        if(entwicklung > this.spread){
                            geringerLongGewinn++;
                        }
                        if(entwicklung > this.spread+3){
                            mittlererLongGewinn++;
                        }
                        if(entwicklung > this.spread+9){
                            hoherLongGewinn++;
                        }
                        if(entwicklung > this.spread+15){
                            sehrHoherLongGewinn++;
                        }

                        if(entwicklung > this.spread+6){
                            hoherShortVerlust++;
                        }
                    }

                    if(entwicklung < this.spread){
                        //System.out.println("Verlust " + alledaten.get(ausgangswert).zeitdatum + " " + alledaten.get(ausgangswert).Closewert);
                        VerlustzaehlerLong++;

                    }


                    //bei Gewinn wird 1 zurückgegeben, bei Verlust 2 und wenn es gleich geblieben ist 0.
                    if(entwicklung < -this.spread)
                    {
                        //System.out.println("Gewinn " + alledaten.get(ausgangswert).zeitdatum + " " + alledaten.get(ausgangswert).Closewert);
                        GewinnzaehlerShort++;

                        if(entwicklung < -this.spread){
                            geringerShortGewinn++;
                        }
                        if(entwicklung < -this.spread-3){
                            mittlererShortGewinn++;
                        }
                        if(entwicklung < -this.spread-10){
                            hoherShortGewinn++;
                        }
                        if(entwicklung < -this.spread-15){
                            sehrHoherShortGewinn++;
                        }
                        if(entwicklung > -this.spread-6){
                            hoherLongVerlust++;
                        }
                    }

                    if(entwicklung > -this.spread){
                        //System.out.println("Verlust " + alledaten.get(ausgangswert).zeitdatum + " " + alledaten.get(ausgangswert).Closewert);
                        VerlustzaehlerShort++;
                    }

                }
            }
        }
        //if(anzFormFound>3){
            /*System.out.print("\033[32mGENERELL:  \033[37m");
            System.out.println(this.vergleichsLaenge+" Minuten:");
            System.out.print("STEIGT: "+GenerellPlus+"/"+anzFormFound+" , "+hohesPlus+"/"+GenerellPlus+"  ");
            System.out.println("FÄLLT: "+GenerellMinus+"/"+anzFormFound+" , "+hohesMinus+"/"+GenerellMinus+"     "+this.auswertungslaenge+" MINUTEN\n");*/

        //System.out.println("Für "+this.vergleichsLaenge+" Minuten Formation "+anzFormFound+" mal gefunden! Generell "+GenerellPlus+" mal Plus davon "+hohesPlus+" mal hohes Plus und "+GenerellMinus+" mal Minus davon "+hohesMinus+" mal hohes Minus nach "+this.auswertungslaenge+" Minuten!");
        //}

        //System.out.println("Ersten "+anzErsterRight+" mal gefunden!");
        if(anzFormFound>9 && (GewinnzaehlerLong > VerlustzaehlerLong*2 || GewinnzaehlerShort > VerlustzaehlerShort*2)){
            //System.out.println("Long: Letzten "+this.vergleichsLaenge+" Minuten. Gewinn "+GewinnzaehlerLong+" mal davon hoher Gewinn "+hoherLongGewinn+" mal und Verlust "+VerlustzaehlerLong+" mal davon"+hoherLongVerlust+" mal hoher Verlust gefunden nach "+this.auswertungslaenge+" Minuten!");
            //System.out.println("Short: Letzten "+this.vergleichsLaenge+" Minuten. Gewinn "+GewinnzaehlerShort+" mal davon hoher Gewinn "+hoherShortGewinn+" mal und Verlust "+VerlustzaehlerShort+" mal davon"+hoherShortVerlust+" mal hoher Verlust gefunden nach "+this.auswertungslaenge+" Minuten!");
            String plattform;
            //if(this.spread == 1 || this.spread == 3){
            plattform = "IG";
            /*}else{
                plattform = "Plus500";
            }*/

            String ausgabe = "";
            if(this.spread == 1){
                ausgabe += "\033[31mTRADEN: Plattform: "+ plattform +" Instrument: "+this.instrument+" "+this.vergleichsLaenge +"min "+this.auswertungslaenge+"min\033[0m";
            }else{
                ausgabe += "\033[33mTRADEN: Plattform: "+ plattform +" Instrument: "+this.instrument+" "+this.vergleichsLaenge +"min "+this.auswertungslaenge+"min\033[0m";

            }
            ausgabe += "\nLong:   GEWINN: "+GewinnzaehlerLong+"/"+anzFormFound+" , "+sehrHoherLongGewinn+"/"+GewinnzaehlerLong+" , "+hoherLongGewinn+"/"+GewinnzaehlerLong+" , "+mittlererLongGewinn+"/"+GewinnzaehlerLong+" , "+geringerLongGewinn+"/"+GewinnzaehlerLong;
            ausgabe += "\nLong:   VERLUST: "+VerlustzaehlerLong+"/"+anzFormFound+" , "+hoherLongVerlust+"/"+VerlustzaehlerLong;

            ausgabe += "\nShort:   GEWINN: "+GewinnzaehlerShort+"/"+anzFormFound+" , "+sehrHoherShortGewinn+"/"+GewinnzaehlerShort+" , "+hoherShortGewinn+"/"+GewinnzaehlerShort+" , "+mittlererShortGewinn+"/"+GewinnzaehlerShort+" , "+geringerShortGewinn+"/"+GewinnzaehlerShort;
            ausgabe += "\nShort:   VERLUST: "+VerlustzaehlerShort+"/"+anzFormFound+" , "+hoherShortVerlust+"/"+VerlustzaehlerShort+"\n";
            //System.out.print(ausgabe);
        }

        double wahrscheinlichkeitLong = 100* (double) GewinnzaehlerLong / ((double) GewinnzaehlerLong+(double) VerlustzaehlerLong);
        double wahrscheinlichkeitShort = 100* (double) GewinnzaehlerShort / ((double) GewinnzaehlerShort+(double) VerlustzaehlerShort);
        double wahrscheinlichkeitLongHoherGewinn = 100* (double) hoherLongGewinn / ((double) GewinnzaehlerLong);
        double wahrscheinlichkeitShortHoherGewinn = 100* (double) hoherShortGewinn / ((double) GewinnzaehlerShort);

        if(mehrereVergleichsstrecken){
            Tradevorhersage trade = new Tradevorhersage();

            trade.setGenerellMinus(GenerellMinus);
            trade.setGenerellPlus(GenerellPlus);
            trade.setGewinnzaehlerLong(GewinnzaehlerLong);
            trade.setGewinnzaehlerShort(GewinnzaehlerShort);
            trade.setVerlustzaehlerLong(VerlustzaehlerLong);
            trade.setVerlustzaehlerShort(VerlustzaehlerShort);
            trade.setAnzFormFound(anzFormFound);
            trade.setGeringerLongGewinn(geringerLongGewinn);
            trade.setGeringerShortGewinn(geringerShortGewinn);
            trade.setHoherLongGewinn(hoherLongGewinn);
            trade.setHoherLongVerlust(hoherLongVerlust);
            trade.setHoherShortGewinn(hoherShortGewinn);
            trade.setHoherShortVerlust(hoherShortVerlust);
            trade.setHohesMinus(hohesMinus);
            trade.setHohesPlus(hohesPlus);
            trade.setMittlererLongGewinn(mittlererLongGewinn);
            trade.setMittlererShortGewinn(mittlererShortGewinn);
            trade.setSehrHoherLongGewinn(sehrHoherLongGewinn);
            trade.setSehrHoherShortGewinn(sehrHoherShortGewinn);
            return trade;
        }
        if( SimulatorModus ){
            return new Tradevorhersage(anzFormFound, wahrscheinlichkeitLong, wahrscheinlichkeitShort,wahrscheinlichkeitLongHoherGewinn,wahrscheinlichkeitShortHoherGewinn);
        }
        return null;
    }

    List<Integer> getAnalyseArray(int vergleichsLaenge){
        List<Integer> tmp = closewerte.subList(closewerte.size()-(vergleichsLaenge), closewerte.size());

        return tmp;
    }

    int sublistAddierer(List<Integer> liste){
        int result = 0;
        for (Integer differenz : liste) {
            result += differenz;
        }
        return result;
    }

    int addierer(List<Integer> liste, int startIndex, int endIndex){
        int result = 0;
        for(int i = startIndex; i <= endIndex;i++){
            result += liste.get(i);
        }
        return result;
    }

}
