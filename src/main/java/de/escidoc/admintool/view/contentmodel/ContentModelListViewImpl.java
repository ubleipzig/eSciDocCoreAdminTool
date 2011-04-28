package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
public class ContentModelListViewImpl extends CustomComponent implements ContentModelListView {

    private final Table table = new Table();

    private final ContentModelSelectListener listener;

    private final ContentModelContainerImpl contentModelContainerImpl;

    public ContentModelListViewImpl(final ContentModelContainerImpl contentModelContainerImpl,
        final ResourceService contentModelService) {
        Preconditions.checkNotNull(contentModelContainerImpl, "contentModelContainer is null: %s",
            contentModelContainerImpl);
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        this.contentModelContainerImpl = contentModelContainerImpl;
        listener = new ContentModelSelectListener(contentModelService);
    }

    public void init() throws EscidocClientException {
        setCompositionRoot(table);
        table.setSizeFull();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        contentModelContainerImpl.reload();
        table.setContainerDataSource(contentModelContainerImpl.getDataSource());
        table.setVisibleColumns(new Object[] { PropertyId.X_LINK_TITLE });
        table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        table.addListener(listener);
    }

    @Override
    public void setContentModelView(final ContentModelView view) {
        Preconditions.checkNotNull(view, "view is null: %s", view);
        listener.setContentModelView(view);
    }

    @Override
    public void setContentModel(final Resource contentModel) {
        Preconditions.checkNotNull(contentModel, "contentModel is null: %s", contentModel);
        table.select(contentModel);
    }

    @Override
    public void selectFirstItem() {
        final Object firstItemId = table.firstItemId();
        table.select(firstItemId);
    }

    @Override
    public Resource firstItemId() {
        return (Resource) table.firstItemId();
    }

    @Override
    public Item firstItem() {
        return table.getItem(table.firstItemId());
    }

}
