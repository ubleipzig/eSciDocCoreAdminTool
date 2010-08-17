package de.escidoc.admintool.view.orgunit.predecessor;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

import de.escidoc.admintool.view.ViewConstants;

public class AbstractPredecessorView extends CustomComponent {
    private static final long serialVersionUID = 1L;

    protected final Panel mainLayout = new Panel();

    protected final GridLayout hl = new GridLayout(1, 3);

    public AbstractPredecessorView(String predecessor, String newOrgUnitName) {
        mainLayout.setWidth("400px");
        Label left =
            new Label("<span align=\"center\">" + predecessor + "</span>",
                Label.CONTENT_XHTML);
        hl.addComponent(left, 0, 0);
        hl.setComponentAlignment(left, Alignment.TOP_CENTER);
        Label middle =
            new Label("<span align=\"center\">" + ViewConstants.DOWN_ARROW
                + "</span>", Label.CONTENT_XHTML);
        hl.addComponent(middle, 0, 1);
        hl.setComponentAlignment(middle, Alignment.MIDDLE_CENTER);
        Label right = new Label(newOrgUnitName);
        hl.addComponent(right, 0, 2);
        hl.setComponentAlignment(right, Alignment.BOTTOM_CENTER);
        mainLayout.addComponent(hl);
        setCompositionRoot(mainLayout);
    }
}