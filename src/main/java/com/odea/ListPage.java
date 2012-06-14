package com.odea;

import com.odea.dao.TicketDAO;
import com.odea.domain.Ticket;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 10:59
 */
public class ListPage extends BasePage {
    private TicketDAO ticketDAO = TicketDAO.getInstance();

    IModel<List<Ticket>> ticketsModel = new LoadableDetachableModel<List<Ticket>>() {
        @Override
        protected List<Ticket> load() {
            return ticketDAO.getTickets();
        }
    };

    public ListPage() {
        add(new FeedbackPanel("feedback"));
        add(new BookmarkablePageLink<EditPage>("link",EditPage.class));
        WebMarkupContainer ticketContainer = new WebMarkupContainer("ticketContainer");

        ListView <Ticket>listView = new ListView<Ticket>("ticket",ticketsModel) {
            @Override
            protected void populateItem(final ListItem<Ticket> components) {
                Ticket ticket = components.getModelObject();
                
                if(ticket.getStatus() == Ticket.TicketStatus.CLOSED){
                    components.add(new AttributeModifier("class","article-closed"));        
                }else if(ticket.getId() % 2 == 0){
                    components.add(new AttributeModifier("class","article"));
                }else {
                    components.add(new AttributeModifier("class","article-zebra"));
                }
                
                components.add(new Label("title",ticket.getTitle()));
                components.add(new Label("description",ticket.getDescription()));

                components.add(new AjaxLink<Long>("deleteLink",new Model<Long>(ticket.getId())) {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        deleteTicket(getModelObject());
                        ajaxRequestTarget.add(getPage().get("ticketContainer"));
                    }

                });
                
                components.add(new BookmarkablePageLink<EditPage>("editLink",EditPage.class,new PageParameters().add("idTicket",ticket.getId())));

            }
        };
        listView.setOutputMarkupId(true);
        ticketContainer.add(listView);
        ticketContainer.setOutputMarkupId(true);
        add(ticketContainer);
    }
    
    private void deleteTicket(long id){
        ticketDAO.deleteTicket(id);
    }
}
