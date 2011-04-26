package de.escidoc.admintool.app;

import com.google.common.base.Preconditions;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Table;

import de.escidoc.core.client.exceptions.EscidocClientException;

@SuppressWarnings("serial")
public class ContentModelListViewImpl extends CustomComponent implements ContentModelListView {

    private final Table table = new Table();

    private final ContentModelContainerImpl contentModelContainerImpl;

    public ContentModelListViewImpl(final ContentModelContainerImpl contentModelContainerImpl) {
        Preconditions.checkNotNull(contentModelContainerImpl, "contentModelContainer is null: %s",
            contentModelContainerImpl);
        setCompositionRoot(table);
        this.contentModelContainerImpl = contentModelContainerImpl;
    }

    public void init() throws EscidocClientException {
        table.setSizeFull();
        table.setSelectable(true);
        table.setImmediate(true);
        table.setNullSelectionAllowed(false);
        contentModelContainerImpl.reload();
        table.setContainerDataSource(contentModelContainerImpl.getDataSource());
        table.setVisibleColumns(new Object[] { PropertyId.X_LINK_TITLE });
        table.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
    }
}
