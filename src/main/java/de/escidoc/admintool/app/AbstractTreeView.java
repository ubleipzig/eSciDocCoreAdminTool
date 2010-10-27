package de.escidoc.admintool.app;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.service.EscidocService;

public class AbstractTreeView extends CustomComponent
    implements ResourceListView {
    private static final long serialVersionUID = -4105655991582180997L;

    private final AdminToolApplication app;

    private final EscidocService service;

    private final Tree tree = new Tree();

    private HierarchicalContainer container;

    private final Panel treePanel = new Panel();

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
        // tree.addListener(new NodeExpandListener());
        // tree.addListener(new NodeClickedListener());
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);

    }

    private Window getMainWindow() {
        return app.getMainWindow();
    }
}
