package de.hrs;

import de.hrs.model.Eurusd;
import de.hrs.socketcontroller.RechnerSchnittstelle;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by hrs on 18.06.16.
 */
@SpringBootApplication
public class Main {
    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("jdbc-config.xml");
        context.getBean(RechnerSchnittstelle.class).start();
        //Hallo
    }
}