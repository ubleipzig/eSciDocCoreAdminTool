package de.escidoc.admintool.view.lab.orgunit;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.themes.Reindeer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitTreeView extends CustomComponent {

    private static final long serialVersionUID = -9114535418069718149L;

    private static final Logger LOG = LoggerFactory
        .getLogger(OrgUnitTreeView.class);

    final Tree tree = new Tree();

    private final HierarchicalContainer container;

    final Command addChildrenCommand;

    OrgUnitViewLab orgUnitViewLab;

    private final Panel treePanel = new Panel();

    public OrgUnitTreeView(final HierarchicalContainer container,
        final Command addChildrenCommand) {
        this.container = container;
        this.addChildrenCommand = addChildrenCommand;

        setCompositionRoot(treePanel);
        init();
    }

    public void select(final Item item) {
        tree.select(item);
    }

    private void init() {
        setSizeFull();
        treePanel.setSizeFull();
        treePanel.setStyleName(Reindeer.PANEL_LIGHT);
        treePanel.addComponent(tree);
        tree.setSizeFull();
        tree.setContainerDataSource(container);
        tree.addListener(new NodeExpandListener(this));
        tree.addListener(new NodeClickedListener(this));
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);
    }

    public void setOrgUnitView(final OrgUnitViewLab orgUnitViewLab) {
        this.orgUnitViewLab = orgUnitViewLab;
    }

    public void delete(final OrganizationalUnit orgUnit) {
        final boolean removeItem = container.removeItem(orgUnit);
        assert removeItem == true : "removing item failed";
    }
}