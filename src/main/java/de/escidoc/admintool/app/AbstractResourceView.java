package de.escidoc.admintool.app;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ResourceView;
import de.escidoc.admintool.view.ViewConstants;

public abstract class AbstractResourceView extends CustomComponent
    implements ResourceView {
    private static final long serialVersionUID = 2631752920109093495L;

    private final SplitPanel splitPanel = new SplitPanel();

    private final AdminToolApplication app;

    private final ResourceListView resourceListView;

    public AbstractResourceView(final AdminToolApplication app,
        final ResourceListView resourceListView) {
        this.app = app;
        this.resourceListView = resourceListView;
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        splitPanel.setSizeFull();
        setCompositionRoot(splitPanel);
        splitPanel.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        splitPanel.setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        splitPanel.setFirstComponent(resourceListView);
    }
}
