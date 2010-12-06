package de.escidoc.admintool.view.resource;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;

public class ResourceTreeView extends CustomComponent
    implements ResourceFolderView {

    private static final long serialVersionUID = 6912762184225745680L;

    private final VerticalLayout treeLayout = new VerticalLayout();

    final Tree tree = new Tree();

    private final ResourceContainer resourceContainer;

    private final FolderHeader header;

    private ShowEditResourceView showEditResourceView;

    private AddChildrenCommand addChildrenCommand;

    private final Window mainWindow;

    public ResourceTreeView(final Window mainWindow, final FolderHeader header,
        final ResourceContainer resourceContainer) {
        preconditions(mainWindow, header, resourceContainer);

        this.mainWindow = mainWindow;
        this.header = header;
        this.resourceContainer = resourceContainer;
        init();
    }

    private void preconditions(
        final Window mainWindow, final FolderHeader header,
        final ResourceContainer resourceContainer) {
        Preconditions.checkNotNull(resourceContainer, " containeris null: %s",
            resourceContainer);
    }

    private void init() {
        setCompositionRoot(treeLayout);

        treeLayout.addComponent(header);
        treeLayout.addComponent(tree);

        setDataSource();

    }

    interface AddChildrenCommand {
        void addChildrenFor(Resource resource) throws EscidocClientException;
    }

    public void addResourceNodeExpandListener() {
        final ResourceNodeExpandListener resourceNodeExpandListener =
            new ResourceNodeExpandListener(tree, mainWindow, addChildrenCommand);

        tree.addListener(resourceNodeExpandListener);
    }

    public void addResourceNodeClickedListener() {
        final ResourceNodeClickedListener resourceNodeClickedListener =
            new ResourceNodeClickedListener(showEditResourceView);

        tree.addListener(resourceNodeClickedListener);
    }

    public void addListener(final ItemClickListener itemClickListener) {
        tree.addListener(itemClickListener);
    }

    private void setDataSource() {
        tree.setContainerDataSource(resourceContainer.getContainer());
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);
    }

    public void setEditView(final ShowEditResourceView showEditResourceView) {
        this.showEditResourceView = showEditResourceView;
    }

    public void setCommand(final AddChildrenCommand addChildrenCommand) {
        this.addChildrenCommand = addChildrenCommand;
    }

    public Object getSelected() {
        return tree.getValue();
    }

    public void multiSelect() {
        tree.setMultiSelect(true);
    }
}