package de.hrs.dao;

import de.hrs.model.Eurusd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by hrs on 18.06.16.
 */
@Repository
public class EurUsdDao {
    private JdbcTemplate template;

    private static final String insertSql =
            "INSERT INTO eurusd (" +  " zeit, " +
                    " wert) " + "VALUES (?, ?) ";

    @Autowired
    private void setDataSource(DataSource dataSource){
        DataSource dataSource1 = dataSource;
        this.template = new JdbcTemplate(dataSource);
    }

    public Eurusd findLastEntry(){
        String sql ="Select * from eurusd where zeit = (Select max(zeit) from eurusd)";

        List<Eurusd> eurusdList = new ArrayList<Eurusd>();

        List<Map<String,Object>> rows = template.queryForList(sql);

        for (Map row : template.queryForList(sql)) {
            eurusdList.add(new Eurusd((Timestamp) row.get("zeit"), (Double) row.get("wert")));
        }
        return eurusdList.get(0);
    }

    public List<Eurusd> findAll(){
        String sql = "SELECT * FROM eurusd order by zeit ASC";

        List<Eurusd> eurusdList = new ArrayList<Eurusd>();

        //List<Map<String,Object>> rows = template.queryForList(sql);
        for (Map row : template.queryForList(sql)) {
            eurusdList.add(new Eurusd((Timestamp) row.get("zeit"), (Double) row.get("wert")));
        }
        return eurusdList;
    }

    public void saveWert(Eurusd eurusd){
        //template.execute("Insert into eurusd (zeit,wert) values ('"+eurusd.getZeit()+"',"+eurusd.getValue()+")");
        Object[] params = new Object[] { eurusd.getZeit(), eurusd.getValue()};
        int[] types = new int[] { Types.TIMESTAMP, Types.DOUBLE};
        int row = template.update(insertSql, params, types);
        System.out.println(row + " row(s) inserted.");
    }
}
