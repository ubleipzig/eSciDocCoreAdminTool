package de.escidoc.admintool.view.orgunit.predecessor;

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;

public class AbstractPredecessorView extends CustomComponent {
    private static final long serialVersionUID = 1L;

    protected Panel mainLayout = new Panel();

    protected final GridLayout hl = new GridLayout(1, 3);

    private ResourceRefDisplay resourceRefDisplay;

    private List<ResourceRefDisplay> resourceRefList;

    public AbstractPredecessorView(final String predecessor,
        final String newOrgUnitName) {
        mainLayout.setWidth("400px");
        final Label left =
            new Label("<span align=\"center\">" + predecessor + "</span>",
                Label.CONTENT_XHTML);
        hl.addComponent(left, 0, 0);
        hl.setComponentAlignment(left, Alignment.TOP_CENTER);
        final Label middle =
            new Label("<span align=\"center\">" + ViewConstants.DOWN_ARROW
                + "</span>", Label.CONTENT_XHTML);
        hl.addComponent(middle, 0, 1);
        hl.setComponentAlignment(middle, Alignment.MIDDLE_CENTER);
        final Label right = new Label(newOrgUnitName);
        hl.addComponent(right, 0, 2);
        hl.setComponentAlignment(right, Alignment.BOTTOM_CENTER);
        mainLayout.addComponent(hl);
        setCompositionRoot(mainLayout);
    }

    public AbstractPredecessorView() {
        buildMainLayout();
        setCompositionRoot(mainLayout);
        mainLayout.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
    }

    private void buildMainLayout() {
        mainLayout = new Panel();
        mainLayout.setWidth("400px");
    }

    public void setResourceRefDisplay(
        final ResourceRefDisplay resourceRefDisplay) {
        this.resourceRefDisplay = resourceRefDisplay;
    }

    public ResourceRefDisplay getResourceRefDisplay() {
        return resourceRefDisplay;
    }

    public boolean isSelected() {
        return resourceRefDisplay != null || resourceRefList.size() > 0;
    }

    public void setResourceRefDisplay(
        final List<ResourceRefDisplay> selectedPredecessors) {
        resourceRefList = selectedPredecessors;
    }

    public List<ResourceRefDisplay> getResourceRefList() {
        return resourceRefList;
    }

}