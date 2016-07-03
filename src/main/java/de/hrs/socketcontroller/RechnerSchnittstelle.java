package de.hrs.socketcontroller;

import de.hrs.Rechner.AnalyseMehereVergleichsstrecken;
import de.hrs.dao.EurUsdDao;
import de.hrs.model.Eurusd;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hrs on 19.06.16.
 */
@Component
public class RechnerSchnittstelle extends Thread{
    public static Logger log = LoggerFactory.getLogger(RechnerSchnittstelle.class);
    ServerSocket socket = null;
    DatagramPacket inPacket = null;
    byte[] inBuf = new byte[256];

    public RechnerSchnittstelle(){}

    @Resource
    EurUsdDao eurUsdDao;

    @Override
    public void run() {
        try {
            socket = new ServerSocket(19999);
            inPacket = new DatagramPacket(inBuf, inBuf.length);
            log.info("Get Eur/Usd lastvalue");
            double eurusdLetzterWert = eurUsdDao.findLastEntry().getValue();
            log.info("Lastvalue is {}", eurusdLetzterWert);
            double eurusdWert = 0;
            log.info("Get Eur/Usd history-values");
            ArrayList<Integer> diffClosewerte = dbEurUsdColumInArrayList();


            while(true) {
                log.info("Listen for Socket-Connection");
                Socket msgSocket = socket.accept();
                String msg = new BufferedReader(new InputStreamReader(msgSocket.getInputStream())).readLine();
                log.info(msg);
                eurusdWert = Double.parseDouble(msg);
                int diff = (int) (10000*eurusdWert - 10000*eurusdLetzterWert);
                diffClosewerte.add(diff);
                eurusdLetzterWert = eurusdWert;
                new AnalyseMehereVergleichsstrecken(
                        new Timestamp(new Date().getTime()),
                        diffClosewerte, diffClosewerte.size()-1, null, 20 , 1, "EUR/USD").run();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    ArrayList<Integer> dbEurUsdColumInArrayList(){
        ArrayList<Integer> closewerte = new ArrayList();
        List<Eurusd> eurUsdDaoAll = eurUsdDao.findAll();

        double wertAlt = eurUsdDaoAll.get(0).getValue();
        double wertNeu;
        int diff;

        for (Eurusd eurusd : eurUsdDaoAll){
            wertNeu = eurusd.getValue();
            diff = (int) (10000 * wertNeu - 10000 * wertAlt);
            closewerte.add(diff);
            wertAlt = wertNeu;
        }
        log.info("Arraylänge EURUSD: " + closewerte.size());
        return closewerte;
    }
}
