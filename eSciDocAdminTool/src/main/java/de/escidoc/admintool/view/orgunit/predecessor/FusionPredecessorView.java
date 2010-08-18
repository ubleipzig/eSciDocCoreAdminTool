package de.escidoc.admintool.view.orgunit.predecessor;

import java.util.List;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;

public class FusionPredecessorView extends CustomComponent {

    private static final long serialVersionUID = -1293310177987070701L;

    protected final Panel mainLayout = new Panel();

    protected final HorizontalLayout horizontalLayout = new HorizontalLayout();

    protected final VerticalLayout verticalLayout = new VerticalLayout();

    public FusionPredecessorView(List<Object> list, String newOrgUnitName) {
        setCompositionRoot(mainLayout);
        mainLayout.setSizeUndefined();
        mainLayout.addComponent(horizontalLayout);
        horizontalLayout.addComponent(verticalLayout);
        for (Object predecessor : list) {
            verticalLayout.addComponent(new Label((String) predecessor));
        }
        verticalLayout.addComponent(new Label(ViewConstants.DOWN_ARROW,
            Label.CONTENT_XHTML));
        verticalLayout.addComponent(new Label(newOrgUnitName));
    }
}