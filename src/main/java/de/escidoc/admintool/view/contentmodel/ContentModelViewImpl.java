package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
public class ContentModelViewImpl extends CustomComponent implements ContentModelView {

    private static final String CONTENT_MODELS_LABEL = "Content Models";

    private final HorizontalSplitPanel hSplitPanel = new HorizontalSplitPanel();

    private final VerticalLayout vLayout = new VerticalLayout();

    private final ContentModelListView listView;

    private final ContentModelAddView addView;

    private final ContentModelEditView editView;

    public ContentModelViewImpl(final ContentModelListView listView, final ContentModelAddView addView,
        final ContentModelEditView editView) {

        Preconditions.checkNotNull(listView, "listView is null: %s", listView);
        Preconditions.checkNotNull(addView, "addView is null: %s", addView);
        Preconditions.checkNotNull(editView, "editView is null: %s", editView);

        setCompositionRoot(hSplitPanel);

        this.listView = listView;
        this.addView = addView;
        this.editView = editView;
    }

    @Override
    public void init() {
        configureLayout();
        addComponents();
    }

    private void configureLayout() {
        hSplitPanel.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        hSplitPanel.setHeight(100, UNITS_PERCENTAGE);
        hSplitPanel.setSizeFull();
        listView.setSizeFull();
        vLayout.setHeight(100, UNITS_PERCENTAGE);
    }

    private void addComponents() {

        addHeader();
        addListView();

        hSplitPanel.addComponent(vLayout);
        hSplitPanel.addComponent(editView);
    }

    private void addListView() {
        vLayout.addComponent(listView);
        vLayout.setExpandRatio(listView, 1.0f);
    }

    private void addHeader() {
        vLayout.addComponent(new Label("<b>" + CONTENT_MODELS_LABEL + "</b>", Label.CONTENT_XHTML));
    }

    @Override
    public void showAddView() {
        hSplitPanel.replaceComponent(editView, createAddView());
    }

    private Component createAddView() {
        // addView = new ContentModelAddView(this, mainWindow, contentModelService, container, pdpRequest);
        // addView.init();
        return addView;
    }

    @Override
    public void showEditView(final Resource contentModel) {
        Preconditions.checkNotNull(contentModel, "contentModel is null: %s", contentModel);
        listView.setContentModel(contentModel);
        editView.setContentModel(contentModel);
        hSplitPanel.setSecondComponent(editView);
    }

    @Override
    public void showEditView(final Item item) {
        Preconditions.checkNotNull(item, "item is null: %s", item);
        editView.bind(item);
    }
}
