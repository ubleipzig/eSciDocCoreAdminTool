package de.escidoc.admintool.view;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class StartPage extends CustomComponent {

    private final VerticalLayout layout = new VerticalLayout();

    private final Label label = new Label("Welcome to eSciDoc Admin Tool");

    public StartPage() {
        setCompositionRoot(layout);
        layout.addComponent(label);
    }
}