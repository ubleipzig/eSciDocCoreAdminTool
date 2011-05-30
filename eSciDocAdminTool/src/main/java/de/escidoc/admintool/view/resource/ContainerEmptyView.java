package de.escidoc.admintool.view.resource;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.resources.Resource;

public class ContainerEmptyView extends CustomComponent implements ResourceFolderView {

    private static final Label EMPTY = new Label(ViewConstants.EMPTY);

    private static final long serialVersionUID = -7036333192749220272L;

    private final VerticalLayout vLayout = new VerticalLayout();

    public ContainerEmptyView(final FolderHeader toolbar) {
        setCompositionRoot(vLayout);
        vLayout.addComponent(toolbar);
        vLayout.addComponent(EMPTY);
        EMPTY.setHeight(30, UNITS_PIXELS);
    }

    @Override
    public void select(final Resource resource) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}