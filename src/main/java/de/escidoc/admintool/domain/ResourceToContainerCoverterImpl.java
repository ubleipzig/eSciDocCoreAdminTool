package de.escidoc.admintool.domain;

import java.util.Set;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.data.util.POJOContainer;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.core.resources.Resource;

public class ResourceToContainerCoverterImpl implements ResourcesToContainer {

    public static Container toContainer(final Set<Resource> resources) {
        Preconditions.checkNotNull(resources, "resources is null: %s",
            resources);

        if (resources.isEmpty()) {
            return new POJOContainer<Resource>(Resource.class,
                PropertyId.GENERIC_PROPERTIES);
        }
        else {
            return new POJOContainer<Resource>(resources,
                PropertyId.GENERIC_PROPERTIES);
        }

    }
}
