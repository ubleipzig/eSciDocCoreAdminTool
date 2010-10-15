package de.escidoc.admintool.view.user;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class Helper {

    public static void addTable(
        final Panel panel, final String label, final Table table,
        final int width, final int height, final boolean required,
        final Button[] buttons) {

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setHeight(height + "px");
        hLayout.addComponent(new Label(" "));

        final String text = "<p align=\"right\">" + label + "</p>";
        final Label l = new Label(text, Label.CONTENT_XHTML);
        hLayout.addComponent(l);
        l.setSizeUndefined();
        l.setWidth(width + "px");
        hLayout.setComponentAlignment(l, Alignment.MIDDLE_RIGHT);
        if (required) {
            hLayout
                .addComponent(new Label(
                    "&nbsp;<span style=\"color:red; position:relative; top:"
                        + (height / 2 - 13) + "px;\">*</span>",
                    Label.CONTENT_XHTML));
        }
        else {
            hLayout
                .addComponent(new Label("&nbsp;&nbsp;", Label.CONTENT_XHTML));
        }

        hLayout.addComponent(table);
        hLayout.setComponentAlignment(table, Alignment.MIDDLE_RIGHT);
        hLayout.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));

        final VerticalLayout vl = new VerticalLayout();
        vl.addComponent(hLayout);
        final HorizontalLayout hl = new HorizontalLayout();
        final Label la = new Label("&nbsp;", Label.CONTENT_XHTML);
        la.setSizeUndefined();
        la.setWidth(width + "px");
        hl.addComponent(la);
        for (final Button b : buttons) {
            hl.addComponent(b);
        }
        vl.addComponent(hl);
        hLayout.setSpacing(false);
        panel.addComponent(vl);
    }
}