package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;

public class ResourceViewImpl extends AbstractResourceView {

    private static final long serialVersionUID = -5537726031167765555L;

    private final ResourceEditView resourceEditView = new ResourceEditViewImpl(
        this);

    public ResourceViewImpl(final ResourceFolderView resourceListView) {
        super(resourceListView);
    }

    @Override
    public void showAddView() {
        getSplitPanel().setSecondComponent(new Label("Add"));
    }

    @Override
    public void showEditView(final Item item) {
        resourceEditView.bind(item);
        getSplitPanel().setSecondComponent(resourceEditView);
    }
}