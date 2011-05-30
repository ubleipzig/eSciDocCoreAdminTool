package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.view.ViewConstants;

public abstract class AbstractResourceView extends CustomComponent implements ResourceView {
    private static final long serialVersionUID = 2631752920109093495L;

    private final SplitPanel splitPanel = new SplitPanel();

    private final ResourceFolderView resourceListView;

    public AbstractResourceView(final ResourceFolderView resourceListView) {
        Preconditions.checkNotNull(resourceListView, "resourceListView is null: %s", resourceListView);
        this.resourceListView = resourceListView;
        buildUI();
    }

    private void buildUI() {
        setSizeFull();
        getSplitPanel().setSizeFull();
        setCompositionRoot(getSplitPanel());

        getSplitPanel().setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        getSplitPanel().setOrientation(SplitPanel.ORIENTATION_HORIZONTAL);
        getSplitPanel().setFirstComponent(resourceListView);
    }

    public SplitPanel getSplitPanel() {
        return splitPanel;
    }
}
