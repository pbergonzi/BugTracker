package com.odea.dao.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * User: pbergonzi
 * Date: 28/06/12
 * Time: 17:24
 */
public class HSQLDBInitializer {
    Connection c;
    public HSQLDBInitializer() {
        try{
        Class.forName("org.hsqldb.jdbcDriver");
        this.c = DriverManager.getConnection("jdbc:hsqldb:mem:user", "sa", "");
        String createTable = "CREATE memory TABLE Tickets (id int not null primary key,title varchar(255),description varchar(4000),type varchar(255) , status varchar(255))";
        Statement stmt = c.createStatement();
        stmt.execute(createTable);
        stmt.close();

        String insert = "INSERT INTO Tickets VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = c.prepareStatement(insert);
        for(int i=0;i<100;i++){
            pstmt.setLong(1, i);
            pstmt.setString(2,"ticket " + i);
            pstmt.setString(3,"descripcion " + i);
            pstmt.setString(4,"BUG");
            pstmt.setString(5,"OPEN");
            pstmt.execute();
        }

        pstmt.close();
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }

    }
    
    public Connection getConnection(){
        return c;
    }

}
