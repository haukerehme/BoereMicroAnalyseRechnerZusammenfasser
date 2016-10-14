package de.hrs.Rechner;

import de.hrs.model.Tradevorhersage;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by hrs on 21.06.16.
 */
public class RechnerZusammenfasser implements Runnable {

    private volatile Tradevorhersage tradevorhersage;

    public Tradevorhersage getTradeTmp() {
        return tradevorhersage;
    }

    List<Integer> historie;
    List<Integer> gesuchtesMuster;
    List<List<Integer>> aktuellerAbschnittUnterteilt;
    int vergleichsLaenge, auswertungslaenge;
    boolean longPosition, mehrereVergleichsstrecken, SimulatorModus;
    int zusammenfasserInterval;
    int spread;
    String instrument;

    RechnerZusammenfasser() {
    }

    public RechnerZusammenfasser(List<Integer> intArray, List<Integer> muster, int vergleichsLaenge, int auswertungslaenge, int zusammenfasserInterval, int spread, String instrument, boolean mehrereVergleichsstrecken, boolean Simulatormodus) {
        this.historie = intArray;
        this.vergleichsLaenge = vergleichsLaenge;
        this.auswertungslaenge = auswertungslaenge;
        this.zusammenfasserInterval = zusammenfasserInterval;
        this.spread = spread;
        this.instrument = instrument;
        this.mehrereVergleichsstrecken = mehrereVergleichsstrecken;
        this.SimulatorModus = Simulatormodus;
        this.tradevorhersage = new Tradevorhersage();
        this.gesuchtesMuster = muster;
    }

    @Override
    public void run(/*int ausgangspkt,int vergleichsLaenge,int auswertungslaenge*/) {

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
        int anzErsterRight = 0;
        boolean formFound;

        for (int i = 0; i < historie.size() - (gesuchtesMuster.size() + this.auswertungslaenge + 1); i++) {
            formFound = true;

            int diffSummeMusterOther = addierer(gesuchtesMuster, 0, zusammenfasserInterval - 1) - addierer(historie, i, i + (zusammenfasserInterval - 1));

            if (diffSummeMusterOther < 4 && diffSummeMusterOther > -4) {

                for (int z = zusammenfasserInterval; z < gesuchtesMuster.size(); z = z + zusammenfasserInterval) {
                    diffSummeMusterOther = addierer(gesuchtesMuster, z, z + zusammenfasserInterval - 1) - addierer(historie, i + z, i + z + zusammenfasserInterval - 1);

                    if (diffSummeMusterOther >= 4 || diffSummeMusterOther <= -4) {
                        formFound = false;
                        break;
                    }
                }
                if (formFound) {
                    diffSummeMusterOther = addierer(gesuchtesMuster, 0, gesuchtesMuster.size() - 1) - addierer(historie, i, i + (gesuchtesMuster.size() - 1));
                    if ((diffSummeMusterOther >= (this.vergleichsLaenge / 10)) || (diffSummeMusterOther <= -(this.vergleichsLaenge / 10))) {
                        //System.out.println("Vom Endgegner abgelehnt!!!");
                        formFound = false;
                    }
                }
                if (formFound) {
                    anzFormFound++;
                    int entwicklung = 0;
                    for (int z = i + this.vergleichsLaenge; z < i + this.vergleichsLaenge + this.auswertungslaenge; z++) {
                        entwicklung += historie.get(z);
                    }
                    if (entwicklung > 0) {
                        GenerellPlus++;
                        if (entwicklung > 10) {
                            hohesPlus++;
                        }
                    }
                    if (entwicklung < 0) {
                        GenerellMinus++;
                        if (entwicklung < -10) {
                            hohesMinus++;
                        }
                    }

                    //bei Gewinn wird 1 zurückgegeben, bei Verlust 2 und wenn es gleich geblieben ist 0.
                    if (entwicklung > this.spread) {
                        GewinnzaehlerLong++;
                        if (entwicklung > this.spread) {
                            geringerLongGewinn++;
                        }
                        if (entwicklung > this.spread + 3) {
                            mittlererLongGewinn++;
                        }
                        if (entwicklung > this.spread + 9) {
                            hoherLongGewinn++;
                        }
                        if (entwicklung > this.spread + 15) {
                            sehrHoherLongGewinn++;
                        }

                        if (entwicklung > this.spread + 6) {
                            hoherShortVerlust++;
                        }
                    }

                    if (entwicklung < this.spread) {
                        VerlustzaehlerLong++;
                    }


                    //bei Gewinn wird 1 zurückgegeben, bei Verlust 2 und wenn es gleich geblieben ist 0.
                    if (entwicklung < -this.spread) {
                        GewinnzaehlerShort++;

                        if (entwicklung < -this.spread) {
                            geringerShortGewinn++;
                        }
                        if (entwicklung < -this.spread - 3) {
                            mittlererShortGewinn++;
                        }
                        if (entwicklung < -this.spread - 10) {
                            hoherShortGewinn++;
                        }
                        if (entwicklung < -this.spread - 15) {
                            sehrHoherShortGewinn++;
                        }
                        if (entwicklung > -this.spread - 6) {
                            hoherLongVerlust++;
                        }
                    }

                    if (entwicklung > -this.spread) {
                        VerlustzaehlerShort++;
                    }

                }
            }
        }

        if (mehrereVergleichsstrecken) {
            tradevorhersage.setGenerellMinus(GenerellMinus);
            tradevorhersage.setGenerellPlus(GenerellPlus);
            tradevorhersage.setGewinnzaehlerLong(GewinnzaehlerLong);
            tradevorhersage.setGewinnzaehlerShort(GewinnzaehlerShort);
            tradevorhersage.setVerlustzaehlerLong(VerlustzaehlerLong);
            tradevorhersage.setVerlustzaehlerShort(VerlustzaehlerShort);
            tradevorhersage.setAnzFormFound(anzFormFound);
            tradevorhersage.setGeringerLongGewinn(geringerLongGewinn);
            tradevorhersage.setGeringerShortGewinn(geringerShortGewinn);
            tradevorhersage.setHoherLongGewinn(hoherLongGewinn);
            tradevorhersage.setHoherLongVerlust(hoherLongVerlust);
            tradevorhersage.setHoherShortGewinn(hoherShortGewinn);
            tradevorhersage.setHoherShortVerlust(hoherShortVerlust);
            tradevorhersage.setHohesMinus(hohesMinus);
            tradevorhersage.setHohesPlus(hohesPlus);
            tradevorhersage.setMittlererLongGewinn(mittlererLongGewinn);
            tradevorhersage.setMittlererShortGewinn(mittlererShortGewinn);
            tradevorhersage.setSehrHoherLongGewinn(sehrHoherLongGewinn);
            tradevorhersage.setSehrHoherShortGewinn(sehrHoherShortGewinn);
        }
    }

    List<Integer> getAnalyseArray(int vergleichsLaenge) {
        return historie.subList(historie.size() - (vergleichsLaenge+1), historie.size() - 1);
    }

    int sublistAddierer(List<Integer> liste) {
        int result = 0;
        for (Integer differenz : liste) {
            result += differenz;
        }
        return result;
    }

    int addierer(List<Integer> liste, int startIndex, int endIndex) {
        int result = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            if (liste.size() > i) {
                result += liste.get(i);
            }else{
                Logger.getGlobal().warning("Addierer will be out of range addieren. Liste.size() : "+liste.size()+ "Index: "+i);
            }
        }
        return result;
    }
}
