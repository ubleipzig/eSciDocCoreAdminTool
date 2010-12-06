package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OrgUnitView extends SplitPanel implements ResourceView {
    private static final long serialVersionUID = -2179646235525092542L;

    private final Logger LOG = LoggerFactory.getLogger(OrgUnitView.class);

    private final AdminToolApplication app;

    private final OrgUnitEditView orgUnitEditView;

    public OrgUnitView(final AdminToolApplication app,
        final OrgUnitListView orgUnitTable,
        final OrgUnitEditView orgUnitEditView,
        final OrgUnitAddView orgUnitAddForm) {
        this.app = app;
        this.orgUnitEditView = orgUnitEditView;

        addStyleName("view");
        setFirstComponent(orgUnitTable);
        setSecondComponent(orgUnitEditView);
        setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
    }

    public void showAddView() {
        try {
            setSecondComponent(app.newOrgUnitAddView());
        }
        catch (final EscidocException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final InternalClientException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
        catch (final TransportException e) {
            LOG.error("An unexpected error occured! See LOG for details.", e);
        }
    }

    public void showEditView(final Item item) {
        setSecondComponent(orgUnitEditView);
        orgUnitEditView.setSelected(item);
    }
}