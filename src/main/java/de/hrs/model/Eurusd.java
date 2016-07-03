package de.hrs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by hrs on 18.06.16.
 */
@Entity
public class Eurusd {

    @Id
    @Column(nullable = false)
    Timestamp zeit;

    @Column(nullable = false)
    double value;

    public Eurusd() {
    }

    public Eurusd(Timestamp zeit, double value) {
        this.zeit = zeit;
        this.value = value;
    }

    public Timestamp getZeit() {
        return zeit;
    }

    public void setZeit(Timestamp zeit) {
        this.zeit = zeit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Eurusd{" +
                "zeit=" + zeit +
                ", value=" + value +
                '}';
    }
}
