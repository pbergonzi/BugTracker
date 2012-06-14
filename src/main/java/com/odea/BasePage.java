package com.odea;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

/**
 * User: pbergonzi
 * Date: 14/06/12
 * Time: 10:54
 */
public class BasePage extends WebPage {
    public BasePage() {
        add(new Label("title","Odea-BugTracker"));
    }
}
