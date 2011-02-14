package de.escidoc.admintool.view.role;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;

import de.escidoc.core.resources.Resource;

final class ResourceSelectionListener implements ValueChangeListener {

    private final RoleView roleView;

    ResourceSelectionListener(final RoleView roleView) {
        this.roleView = roleView;
    }

    private static final String EMPTY_STRING = "";

    private static final long serialVersionUID = -3079481037459553076L;

    @Override
    public void valueChange(final ValueChangeEvent event) {
        if (event.getProperty() != null
            && event.getProperty().getValue() != null
            && event.getProperty().getValue() instanceof Resource) {
            final Resource selected = (Resource) event.getProperty().getValue();
            roleView.searchBox.setValue(selected.getXLinkTitle());
        }
        else {
            roleView.searchBox.setValue(EMPTY_STRING);
        }
    }
}