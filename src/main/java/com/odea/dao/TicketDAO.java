package com.odea.dao;

import com.odea.domain.Ticket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 16:23
 */
public class TicketDAO implements Serializable {

    private static final TicketDAO instance = new TicketDAO();  
    public List<Ticket> tickets;
    
    private TicketDAO(){
        tickets = loadTickets();
    }
    
    public static TicketDAO getInstance(){
        return instance;
    }

    private List<Ticket> loadTickets(){
        List<Ticket> tickets = new Vector<Ticket>();
        for(long i=0;i<5;i++){
            tickets.add(new Ticket(i,"titulo " + i,"descripcion " + i, Ticket.TicketType.BUG, Ticket.TicketStatus.OPEN));
        }
        return tickets;
    }
    
    public List<Ticket> getTickets(){
        Collections.sort(tickets);
        return tickets;
    }

    public void deleteTicket(long id){
        for(Ticket t : tickets){
            if(t.getId().equals(id)){
                tickets.remove(t);
                break;
            }
        }
    }

    public Ticket getTicket(long id){
        for(Ticket t : tickets){
            if(t.getId().equals(id)){
                return t;
            }
        }
        return null;
    }

    public void insertOrUpdate(Ticket t){
        deleteTicket(t.getId());
        tickets.add(t);
    }
}
