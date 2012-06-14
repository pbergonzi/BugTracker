package com.odea.domain;

import java.io.Serializable;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 13:04
 */
public class Ticket implements Serializable,Comparable{
    public int compareTo(Object o) {
        Ticket t = (Ticket)o;
        if(this.getId() < t.getId()){
            return 1;
        }
        if(this.getId() > t.getId()){
            return -1;
        }

        return 0;
    }

    public static enum TicketType {
        BUG, MEJORA, NUEVA_FUNCIONALIDAD;
    }

    public static enum TicketStatus {
        OPEN, CLOSED;
    }

    private Long id;
    private String title;
    private String description;
    private TicketType type;
    private TicketStatus status;

    public Ticket() {
    }

    public Ticket(Long id, String title, String description, TicketType type, TicketStatus status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }
}
