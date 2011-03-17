package de.escidoc.admintool.app;

import de.escidoc.core.resources.cmm.ContentModel;
import de.escidoc.core.resources.cmm.ContentModelProperties;

public class ContentModelBuilder {

    private final String title;

    private String description;

    public ContentModelBuilder(final String title) {
        this.title = title;
    }

    public ContentModelBuilder description(final String description) {
        this.description = description;
        return this;
    }

    public ContentModel build() {
        final ContentModel contentModel = new ContentModel();
        final ContentModelProperties contentModelProperties =
            new ContentModelProperties();
        contentModelProperties.setName(title);
        contentModelProperties.setDescription(description);
        contentModel.setProperties(contentModelProperties);
        return contentModel;
    }

}
