package de.hrs.Rechner;

import de.hrs.model.TradeMessage;
import de.hrs.model.Tradevorhersage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hrs on 21.06.16.
 */
public class AnalyseMehereVergleichsstrecken implements Runnable {

    public static Logger log = LoggerFactory.getLogger(AnalyseMehereVergleichsstrecken.class);
    RechnerZusammenfasser rechner;
    List<Integer> closewerte;
    Timestamp now;
    int ausgangspkt,vergleichsLaenge,auswertungslaenge;
    boolean longPosition, mehrereVergleichsstrecken, SimulatorModus;
    int zusammenfasserInterval;
    int spread;
    String instrument;
    List<Integer> listVergleichsLaenge;

    int GewinnzaehlerLong = 0;
    int VerlustzaehlerLong = 0;

    int GewinnzaehlerShort = 0;
    int VerlustzaehlerShort = 0;

    int GenerellPlus = 0;
    int GenerellMinus = 0;
    int hohesMinus = 0;
    int hohesPlus  = 0;

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

    public AnalyseMehereVergleichsstrecken(Timestamp now, List<Integer> intArray,int ausgangspkt,List<Integer> listVergleichsLaenge,int auswertungslaenge, int spread, String instrument){
        closewerte = intArray;
        this.now = now;
        this.ausgangspkt =ausgangspkt;
        this.listVergleichsLaenge = listVergleichsLaenge;
        this.auswertungslaenge = auswertungslaenge;
        this.spread = spread;
        this.instrument = instrument;
    }

    void addiere(Tradevorhersage tmp){
        GewinnzaehlerLong += tmp.getGewinnzaehlerLong();
        VerlustzaehlerLong += tmp.getVerlustzaehlerLong();

        GewinnzaehlerShort += tmp.getGewinnzaehlerShort();
        VerlustzaehlerShort += tmp.getVerlustzaehlerShort();

        GenerellPlus += tmp.getGenerellPlus();
        GenerellMinus += tmp.getGenerellMinus();
        hohesMinus += tmp.getHohesMinus();
        hohesPlus  += tmp.getHohesPlus();

        hoherLongVerlust += tmp.getHoherLongVerlust();
        geringerLongGewinn += tmp.getGeringerLongGewinn();
        mittlererLongGewinn += tmp.getMittlererLongGewinn();
        hoherLongGewinn += tmp.getHoherLongGewinn();
        sehrHoherLongGewinn += tmp.getSehrHoherLongGewinn();

        geringerShortGewinn += tmp.getGeringerShortGewinn();
        mittlererShortGewinn += tmp.getMittlererShortGewinn();
        hoherShortGewinn += tmp.getHoherShortGewinn();
        sehrHoherShortGewinn += tmp.getSehrHoherShortGewinn();
        hoherShortVerlust += tmp.getHoherShortVerlust();
        anzFormFound += tmp.getAnzFormFound();
    }

    public void run(){

        //Hier ArrayList<RechnerZusammenFasser> deklarieren
        //Jeder Thread bekommt eine Sublist von closewerte und
        //Die ArrayList<RechnerZusammenFasser> wird in einer for-each Schleife durchlaufen.
        //Wenn bei allen Threads thread.join erfolgreich, dann Auswertung

        ArrayList<RechnerZusammenfasser> listRechner = new ArrayList<>();
        ArrayList<Thread> listThread = new ArrayList<>();

        int[] vergleichslaengen = {240,210,180,150,120};
        int[] zusammenfasserInterval = {30,30,20,10,10};
        int threadPaare = 1;

        // - auswertungslaenge, weil die auswertungslaenge eh übergeben wird
        // - 240 da das längste Muster abgezogen werden muss
        int blockgroesse = (this.closewerte.size() - (auswertungslaenge + 240)) / threadPaare;
        for (int i = 0; i < threadPaare ; i++){
            List<Integer> historie = new ArrayList<>(this.closewerte.subList(i * blockgroesse,((i + 1) * blockgroesse) - 1 + auswertungslaenge));
            List<Integer> muster = new ArrayList<>(this.closewerte.subList(this.closewerte.size()-(vergleichslaengen.length+1),this.closewerte.size()-1));

            for(int j = 0; j < vergleichslaengen.length;j++){
                listRechner.add(new RechnerZusammenfasser(historie, muster, vergleichslaengen[j],
                        auswertungslaenge, zusammenfasserInterval[j], spread, "EUR/USD", true, false));
                listThread.add(new Thread(listRechner.get((i*vergleichslaengen.length)+j)));
                listThread.get((i*vergleichslaengen.length)+j).start();
            }
        }

        for (int i = 0; i < threadPaare * vergleichslaengen.length; i++){
            try {
                listThread.get(i).join();
                System.out.println("Thread "+ i +": Formation "+listRechner.get(i).getTradeTmp().getAnzFormFound()+" mal gefunden");
                addiere(listRechner.get(i).getTradeTmp());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        TradeMessage tradeMessage = new TradeMessage(now, "EUR/USD", auswertungslaenge, anzFormFound, GewinnzaehlerLong, mittlererLongGewinn, hoherLongGewinn, sehrHoherLongGewinn, VerlustzaehlerLong, hoherLongVerlust, geringerShortGewinn, mittlererShortGewinn, hoherShortGewinn, sehrHoherShortGewinn, VerlustzaehlerShort, hoherShortVerlust);
        /*try {
            tradeMessage.persistTradeMessage();
            //    public TradeMessage(Timestamp id, String instrument, int timeperiod, int anzFound, int longWin, int longWinMiddle, int longWinHigh, int longWinVeryHigh, int longLose,
            //int longLoseHigh, int shortWin, int shortWinMiddle, int shortWinHigh, int shortWinVeryHigh, int shortLose, int shortLoseHigh) {
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(AnalyseMehererVergleichsstrecken.class.getName()).log(Level.SEVERE, null, ex);
        }*/


        //if(anzFormFound>19 && (GewinnzaehlerLong > VerlustzaehlerLong*2 || GewinnzaehlerShort > VerlustzaehlerShort*2 || (hoherLongGewinn > hoherLongVerlust*2 && hoherLongGewinn > 4 )||(hoherShortGewinn > hoherShortVerlust*2 && hoherShortGewinn > 4))){
            String ausgabe = "";
            if(this.spread == 1){
                ausgabe += "\033[34mTRADEN: Mehrere Vergleichslaengen ;) Instrument: "+this.instrument+" "+this.auswertungslaenge+"min\033[0m";
            }else{
                ausgabe += "\033[32mTRADEN: Mehrere Vergleichslaengen ;) Instrument: "+this.instrument+ " "+this.auswertungslaenge+"min\033[0m";
            }
            ausgabe += "\nLong:   GEWINN: "+GewinnzaehlerLong+"/"+anzFormFound+" , "+sehrHoherLongGewinn+"/"+GewinnzaehlerLong+" , "+hoherLongGewinn+"/"+GewinnzaehlerLong+" , "+mittlererLongGewinn+"/"+GewinnzaehlerLong+" , "+geringerLongGewinn+"/"+GewinnzaehlerLong;
            ausgabe += "\nLong:   VERLUST: "+VerlustzaehlerLong+"/"+anzFormFound+" , "+hoherLongVerlust+"/"+VerlustzaehlerLong;

            ausgabe += "\nShort:   GEWINN: "+GewinnzaehlerShort+"/"+anzFormFound+" , "+sehrHoherShortGewinn+"/"+GewinnzaehlerShort+" , "+hoherShortGewinn+"/"+GewinnzaehlerShort+" , "+mittlererShortGewinn+"/"+GewinnzaehlerShort+" , "+geringerShortGewinn+"/"+GewinnzaehlerShort;
            ausgabe += "\nShort:   VERLUST: "+VerlustzaehlerShort+"/"+anzFormFound+" , "+hoherShortVerlust+"/"+VerlustzaehlerShort+"\n";
            log.info(ausgabe);
//            try {
//                MailClass.sendMail("haukekatha","43mitmilch","hrs@logentis.de","lemur.katha@googlemail.com","Handeln",ausgabe);
//            } catch (MessagingException ex) {
//                log.error("Fehler beim Mail senden. Error: {}", ex.toString());
//            }
        //}
    }

}
