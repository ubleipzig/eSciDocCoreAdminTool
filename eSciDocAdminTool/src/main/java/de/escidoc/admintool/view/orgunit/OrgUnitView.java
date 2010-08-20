package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.ui.SplitPanel;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OrgUnitView extends SplitPanel {
    private static final long serialVersionUID = -2179646235525092542L;

    private final Logger log = LoggerFactory.getLogger(OrgUnitView.class);

    private final AdminToolApplication app;

    private final OrgUnitEditView orgUnitEditForm;

    public OrgUnitView(final AdminToolApplication app,
        final OrgUnitList orgUnitTable, final OrgUnitEditView orgUnitEditView,
        final OrgUnitAddView orgUnitAddForm) {
        this.app = app;
        this.orgUnitEditForm = orgUnitEditView;
        addStyleName("view");
        setFirstComponent(orgUnitTable);
        setSecondComponent(orgUnitEditView);
        this.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
    }

    public OrgUnitView showOrganizationalUnitAddForm() {
        try {
            setSecondComponent(app.newOrgUnitAddView());
        }
        catch (final EscidocException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final InternalClientException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final TransportException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        return this;
    }

    public void showEditView(final Item item) {
        setSecondComponent(orgUnitEditForm);
        orgUnitEditForm.setSelected(item);
    }
}