package de.escidoc.admintool.app;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.contentmodel.ContentModelAddView;

@SuppressWarnings("serial")
public class ContentModelViewImpl extends CustomComponent
    implements ContentModelView {

    private final HorizontalSplitPanel hSplitPanel = new HorizontalSplitPanel();

    private final ContentModelListView listView;

    private final Component addView;

    public ContentModelViewImpl(final ContentModelListView listView,
        final ContentModelAddView addView) {

        Preconditions.checkNotNull(listView, "listView is null: %s", listView);
        Preconditions.checkNotNull(addView, "addView is null: %s", addView);

        setCompositionRoot(hSplitPanel);

        this.listView = listView;
        this.addView = addView;
    }

    public void init() {
        configureLayout();
        addComponents();
    }

    private void configureLayout() {
        hSplitPanel.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        hSplitPanel.setHeight(100, UNITS_PERCENTAGE);
    }

    private void addComponents() {
        hSplitPanel.addComponent(listView);
        hSplitPanel.addComponent(addView);
    }

}
