package de.escidoc.admintool.app;

import com.vaadin.data.Container;
import com.vaadin.data.util.POJOContainer;

import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.cmm.ContentModel;

public class ContentModelContainer {

    private POJOContainer container;

    private Container createHierarchicalContainer() {
        container =
            new POJOContainer<ContentModel>(ContentModel.class, new String[] {});
        return container;
    }

    private void addContainerProperties() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void add(final Resource created) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
