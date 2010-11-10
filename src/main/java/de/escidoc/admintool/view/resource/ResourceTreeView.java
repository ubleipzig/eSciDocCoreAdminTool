package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.data.Container;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.lab.orgunit.AddChildrenCommand;

public class ResourceTreeView extends CustomComponent
    implements ResourceFolderView {

    private static final long serialVersionUID = 6912762184225745680L;

    private final VerticalLayout treeLayout = new VerticalLayout();

    final Tree tree = new Tree();

    private final Container container;

    private final FolderHeader header;

    public AddChildrenCommand addChildrenCommand;

    private final ShowEditResourceView showEditResourceView;

    public ResourceTreeView(final FolderHeader header,
        final Container container,
        final ShowEditResourceView showEditResourceView) {

        Preconditions.checkNotNull(container, " containeris null: %s",
            container);

        this.header = header;
        this.container = container;
        this.showEditResourceView = showEditResourceView;

        init();
    }

    private void init() {
        setCompositionRoot(treeLayout);

        treeLayout.addComponent(header);
        treeLayout.addComponent(tree);

        // setFullSize();
        addListeners();
        setDataSource();
    }

    private void addListeners() {
        tree.addListener(new ResourceNodeExpandListener(this));
        tree.addListener(new ResourceNodeClickedListener(showEditResourceView));
    }

    private void setFullSize() {
        setSizeFull();
        treeLayout.setSizeFull();
        tree.setSizeFull();
    }

    private void setDataSource() {
        tree.setContainerDataSource(container);
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);
    }

}
