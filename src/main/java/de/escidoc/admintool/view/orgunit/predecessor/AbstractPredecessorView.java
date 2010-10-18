package de.escidoc.admintool.view.orgunit.predecessor;

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.view.ResourceRefDisplay;
import de.escidoc.admintool.view.ViewConstants;

public class AbstractPredecessorView extends CustomComponent {
    private static final long serialVersionUID = 1279873768615689377L;

    protected Panel mainLayout = new Panel();

    protected final GridLayout gridLayout = new GridLayout(1, 3);

    private ResourceRefDisplay resourceRefDisplay;

    private List<ResourceRefDisplay> resourceRefList;

    public AbstractPredecessorView(final String predecessor,
        final String newOrgUnitName) {
        mainLayout.setStyleName(Reindeer.PANEL_LIGHT);
        setCompositionRoot(mainLayout);
        mainLayout.setWidth(ViewConstants.FIELD_WIDTH);

        final Label left =
            new Label("<span align=\"center\">" + predecessor + "</span>",
                Label.CONTENT_XHTML);
        gridLayout.addComponent(left, 0, 0);
        gridLayout.setComponentAlignment(left, Alignment.TOP_CENTER);

        final Label middle =
            new Label("<span align=\"center\">" + ViewConstants.DOWN_ARROW
                + "</span>", Label.CONTENT_XHTML);
        gridLayout.addComponent(middle, 0, 1);
        gridLayout.setComponentAlignment(middle, Alignment.MIDDLE_CENTER);

        final Label right = new Label(newOrgUnitName);
        gridLayout.addComponent(right, 0, 2);
        gridLayout.setComponentAlignment(right, Alignment.BOTTOM_CENTER);
        mainLayout.addComponent(gridLayout);
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
        return resourceRefDisplay != null || resourceRefList != null
            && resourceRefList.size() > 0;
    }

    public void setResourceRefDisplay(
        final List<ResourceRefDisplay> selectedPredecessors) {
        resourceRefList = selectedPredecessors;
    }

    public List<ResourceRefDisplay> getResourceRefList() {
        return resourceRefList;
    }
}