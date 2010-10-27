package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.Command;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

@SuppressWarnings("serial")
public class OrgUnitTreeViewLab extends CustomComponent {

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitTreeViewLab.class);

    private final Tree tree = new Tree();

    private final HierarchicalContainer container;

    private final Command addChildrenCommand;

    private OrgUnitViewLab orgUnitViewLab;

    private final Panel treePanel = new Panel();

    public OrgUnitTreeViewLab(final HierarchicalContainer container,
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
        tree.addListener(new NodeExpandListener());
        tree.addListener(new NodeClickedListener());
        tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
        tree.setItemCaptionPropertyId(PropertyId.NAME);
    }

    private class NodeClickedListener implements ItemClickListener {

        @Override
        public void itemClick(final ItemClickEvent event) {
            final Item item = event.getItem();
            if (item == null) {
                return;
            }
            assert orgUnitViewLab != null : "orgUnitViewLab can not be null";
            orgUnitViewLab.select(item);
        }
    }

    private class NodeExpandListener implements Tree.ExpandListener {

        @Override
        public void nodeExpand(final ExpandEvent event) {
            final Object selectedOrgUnit = event.getItemId();
            if (selectedOrgUnit == null) {
                return;
            }

            if (selectedOrgUnit instanceof OrganizationalUnit
                && isAddChildrenNeeded(selectedOrgUnit)) {
                addChildren((OrganizationalUnit) selectedOrgUnit);
            }
        }

        private boolean isAddChildrenNeeded(final Object selectedOrgUnit) {
            return !tree.hasChildren(selectedOrgUnit);
        }

        private void addChildren(final OrganizationalUnit selectedOrgUnit) {
            ((AddChildrenCommand) addChildrenCommand)
                .setParent(selectedOrgUnit);
            try {
                addChildrenCommand.execute();
            }
            catch (final EscidocClientException e) {
                // ErrorMessage.show(e);
                e.printStackTrace();
            }
        }
    }

    public void setOrgUnitView(final OrgUnitViewLab orgUnitViewLab) {
        this.orgUnitViewLab = orgUnitViewLab;
    }

    public void delete(final OrganizationalUnit orgUnit) {
        final boolean removeItem = container.removeItem(orgUnit);
        assert removeItem == true : "removing item failed";
    }
}