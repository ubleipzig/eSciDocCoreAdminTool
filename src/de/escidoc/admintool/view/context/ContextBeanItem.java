package de.escidoc.admintool.view.context;

import com.vaadin.data.util.PropertysetItem;

import de.escidoc.core.resources.om.context.Context;

public class ContextBeanItem extends PropertysetItem {

    Context context;

    public ContextBeanItem(final Context context) {
        this.context = context;
    }

}
