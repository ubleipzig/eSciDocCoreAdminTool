package de.escidoc.admintool.view.lab.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Tree.ExpandEvent;

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

    public OrgUnitTreeViewLab(final HierarchicalContainer container,
        final Command addChildrenCommand) {
        this.container = container;
        this.addChildrenCommand = addChildrenCommand;
        setCompositionRoot(tree);
        init();
    }

    public void select(final OrganizationalUnit orgUnit) {
        tree.select(orgUnit);
    }

    private void init() {
        setSizeFull();
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
            final Object selectedOrgUnit = event.getItemId();
            if (selectedOrgUnit instanceof OrganizationalUnit) {
                orgUnitViewLab.select((OrganizationalUnit) selectedOrgUnit);
            }
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
}