package com.odea;

import com.odea.dao.TicketDAO;
import com.odea.domain.Ticket;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 10:59
 */
public class ListPage extends BasePage {
    private static final Logger logger = LoggerFactory.getLogger(ListPage.class);
    private transient TicketDAO ticketDAO = TicketDAO.getInstance();
    private String searchText;

    IModel<List<Ticket>> ticketsModel = new LoadableDetachableModel<List<Ticket>>() {
        @Override
        protected List<Ticket> load() {
            if(searchText == null){
                return ticketDAO.getTickets();
            }else{
                return ticketDAO.getTickets(searchText);
            }
        }
    };

    public ListPage() {
        Form<?> form = new Form<Void>("search");

        TextField<String> searchTextField = new TextField<String>("searchText",new PropertyModel<String>(this,"searchText"));
        AjaxButton submitSearch = new AjaxButton("submitSearch") {
            @Override
            protected void onSubmit(AjaxRequestTarget ajaxRequestTarget, Form<?> components) {
                ajaxRequestTarget.add(getPage().get("ticketContainer"));
            }

            @Override
            protected void onError(AjaxRequestTarget ajaxRequestTarget, Form<?> components) {
                logger.info("Error mientras se ejecuta la Busqueda de tickets");
            }
        };

        form.add(submitSearch);
        form.add(searchTextField);
        add(form);
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
