package de.escidoc.admintool.view.resource;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.EscidocService;
import de.escidoc.core.resources.Resource;

public class AbstractTreeView extends CustomComponent
    implements ResourceFolderView {
    private static final long serialVersionUID = -4105655991582180997L;

    private AdminToolApplication app;

    private EscidocService service;

    private final Tree tree = new Tree();

    private HierarchicalContainer container;

    private final Panel treePanel = new Panel();

    public AbstractTreeView() {
        buildUI();
    }

    public AbstractTreeView(final AdminToolApplication app,
        final EscidocService service) {
        this.app = app;
        this.service = service;

        buildUI();
    }

    private void buildUI() {
        setCompositionRoot(treePanel);
        setSizeFull();
        treePanel.setSizeFull();
        treePanel.setStyleName(Reindeer.PANEL_LIGHT);
        treePanel.addComponent(tree);
        tree.setSizeFull();
        tree.setContainerDataSource(container);
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);

    }

    @Override
    public void select(final Resource resource) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Not yet implemented");

    }
}
