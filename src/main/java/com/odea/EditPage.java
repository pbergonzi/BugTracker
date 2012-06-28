package com.odea;

import com.odea.dao.TicketDAO;
import com.odea.domain.Ticket;
import com.odea.domain.Ticket.TicketStatus;
import com.odea.domain.Ticket.TicketType;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.Arrays;
import java.util.Date;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 10:59
 */
public class EditPage extends BasePage {
    private transient TicketDAO ticketDAO = TicketDAO.getInstance();
    private IModel<Ticket> ticketModel;
    
    public EditPage(){
        this.ticketModel = new CompoundPropertyModel<Ticket>(new LoadableDetachableModel<Ticket>() {
            @Override
            protected Ticket load() {
                return new Ticket(new Date().getTime(),null,null,TicketType.BUG,TicketStatus.OPEN);
            }
        });
        this.preparePage();    
    }
    
    public EditPage(final PageParameters parameters) {
        this.ticketModel = new CompoundPropertyModel<Ticket>(new LoadableDetachableModel<Ticket>() {
            @Override
            protected Ticket load() {
                return ticketDAO.getTicket(parameters.get("idTicket").toLong());
            }
        });
        this.preparePage();
    }
    
    private void preparePage(){
        add(new BookmarkablePageLink<ListPage>("link",ListPage.class));
        add(new FeedbackPanel("feedback"));
        
        Form<Ticket> form = new Form<Ticket>("form",ticketModel){
            @Override
            protected void onSubmit() {
                Ticket t = getModelObject();
                ticketDAO.insertOrUpdate(t);
                setResponsePage(ListPage.class);
            }
        };

        RequiredTextField<String> title = new RequiredTextField<String>("title");
        TextArea<String> description = new TextArea<String>("description");
        DropDownChoice<TicketType> ticketType = new DropDownChoice<TicketType>("type", Arrays.asList(TicketType.values()));
        RadioChoice<TicketStatus> ticketStatus = new RadioChoice<TicketStatus>("status", Arrays.asList(TicketStatus.values()));
        Button submit = new Button("submit");

        form.add(title);
        form.add(description);
        form.add(ticketType);
        form.add(ticketStatus);
        form.add(submit);

        add(form);
    }

}
