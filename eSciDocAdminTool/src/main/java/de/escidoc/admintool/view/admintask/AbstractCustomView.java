package de.escidoc.admintool.view.admintask;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractCustomView extends CustomComponent {
    private static final long serialVersionUID = -1351048887772117350L;

    private final VerticalLayout viewLayout = new VerticalLayout();

    AbstractCustomView() {
        setCompositionRoot(viewLayout);
    }

    public VerticalLayout getViewLayout() {
        return viewLayout;
    }
}
