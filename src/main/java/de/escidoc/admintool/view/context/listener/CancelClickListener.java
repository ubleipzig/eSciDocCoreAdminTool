package de.escidoc.admintool.view.context.listener;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Window;

public class CancelClickListener implements Button.ClickListener {

    private static final long serialVersionUID = 5254384219030633295L;

    private final Window subwindow;

    public CancelClickListener(final Window subwindow) {
        super();
        this.subwindow = subwindow;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        subwindow.getParent().removeWindow(subwindow);
    }
}
