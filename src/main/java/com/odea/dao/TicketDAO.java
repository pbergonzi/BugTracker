package com.odea.dao;

import com.odea.dao.mock.HSQLDBInitializer;
import com.odea.domain.Ticket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 16:23
 */
public class TicketDAO {

    private static final TicketDAO instance = new TicketDAO();
    private static final HSQLDBInitializer hsqldb = new HSQLDBInitializer();

    private TicketDAO() {
    }

    public static TicketDAO getInstance() {
        return instance;
    }

    public List<Ticket> getTickets() {
        List<Ticket> tickets = new ArrayList<Ticket>();
        try {
            Statement stmt = hsqldb.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from Tickets order by id");

            while (rs.next()) {
                tickets.add(this.mapTicket(rs));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return tickets;
    }

    private Ticket mapTicket(ResultSet rs) throws Exception {
        String type = rs.getString(4);
        String status = rs.getString(5);
        return new Ticket(rs.getLong(1), rs.getString(2), rs.getString(3), Ticket.TicketType.valueOf(type), Ticket.TicketStatus.valueOf(status));
    }


    public List<Ticket> getTickets(String text) {
        List<Ticket> tickets = new ArrayList<Ticket>();
        try {
            Statement stmt = hsqldb.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from Tickets where title like '%" + text + "%' order by id");

            while (rs.next()) {
                tickets.add(this.mapTicket(rs));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return tickets;
    }

    public void deleteTicket(long id) {
        try {
            Statement stmt = hsqldb.getConnection().createStatement();
            stmt.execute("delete from Tickets where id = " + id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Ticket getTicket(long id) {
        try {
            Statement stmt = hsqldb.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from Tickets where id = " + id);
            rs.next();
            return this.mapTicket(rs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void insertOrUpdate(Ticket t) {
        deleteTicket(t.getId());
        try {
            String insert = "INSERT INTO Tickets VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = hsqldb.getConnection().prepareStatement(insert);
            pstmt.setLong(1, t.getId());
            pstmt.setString(2, t.getTitle());
            pstmt.setString(3, t.getDescription());
            pstmt.setString(4, t.getType().name());
            pstmt.setString(5, t.getStatus().name());
            pstmt.execute();
            pstmt.close();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
