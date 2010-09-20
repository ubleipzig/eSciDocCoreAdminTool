package de.escidoc.admintool.view.context;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.orgunit.OrgUnitTreeFactory;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitTree extends CustomComponent {
    private static final long serialVersionUID = 671280566874568107L;

    private final Logger log = LoggerFactory.getLogger(OrgUnitTree.class);

    private Tree tree = new Tree();

    private final OrgUnitService service;

    private OrgUnitTreeFactory orgUnitTreeFactory;

    public OrgUnitTree(final OrgUnitService service) {
        this.service = service;
        final Panel panel = new Panel();
        panel.setStyleName(Reindeer.PANEL_LIGHT);
        tree.setHeight(null);
        tree.setWidth("100%");
        tree.setMultiSelect(true);
        tree.setImmediate(true);
        panel.addComponent(tree);
        setCompositionRoot(panel);
        loadOrgUnits();
    }

    public void setMultiSelect(final boolean multiSelect) {
        tree.setMultiSelect(multiSelect);
    }

    /**
     * Replace me by a call of the service.
     */
    private void loadOrgUnits() {
        try {
            tree.removeAllItems();

            orgUnitTreeFactory =
                new OrgUnitTreeFactory(tree, new ArrayList<OrganizationalUnit>(
                    service.getOrganizationalUnits()), service.getOrgUnitById());
            tree = orgUnitTreeFactory.create();
        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);

        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
        }
    }

    public Object getSelectedItems() {
        return tree.getValue();
    }
}