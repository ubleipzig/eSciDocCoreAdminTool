package de.escidoc.admintool.app;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.util.POJOContainer;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class ContentModelContainerImpl {

    private final ResourceService contentModelService;

    private POJOContainer<Resource> itemContainer;

    public ContentModelContainerImpl(final ResourceService contentModelService) {
        Preconditions.checkNotNull(contentModelService,
            "contentModelService is null: %s", contentModelService);
        this.contentModelService = contentModelService;

    }

    public void reload() throws EscidocClientException {
        final Set<Resource> allContentModels = contentModelService.findAll();
        itemContainer =
            new POJOContainer<Resource>(Resource.class, PropertyId.X_LINK_TITLE);
        for (final Resource resource : allContentModels) {
            itemContainer.addItem(resource);
        }
    }

    public Container getDataSource() {
        return itemContainer;
    }

    public void add(final Resource created) {
        Preconditions.checkNotNull(created, "created is null: %s", created);
        itemContainer.addItem(created);
    }
}
