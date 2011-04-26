package de.escidoc.admintool.app;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;

import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class ContentModelViewImpl extends CustomComponent
    implements ContentModelView {

    private final HorizontalSplitPanel hSplitPanel = new HorizontalSplitPanel();

    private final ContentModelListView listView;

    public ContentModelViewImpl(final ContentModelListView listView) {
        Preconditions.checkNotNull(listView, "listView is null: %s", listView);
        setCompositionRoot(hSplitPanel);
        this.listView = listView;
    }

    public void init() {
        hSplitPanel.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        hSplitPanel.addComponent(listView);
        hSplitPanel.addComponent(new Label("right"));
    }

}
