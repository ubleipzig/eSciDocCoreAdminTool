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

@SuppressWarnings("serial")
public class OrgUnitView extends SplitPanel {
    private final Logger log = LoggerFactory.getLogger(OrgUnitView.class);

    private final AdminToolApplication app;

    private final OrgUnitEditForm orgUnitEditForm;

    public OrgUnitView(final AdminToolApplication app,
        final OrgUnitList orgUnitTable, final OrgUnitEditForm orgUnitEditForm,
        final OrgUnitAddView orgUnitAddForm) {
        this.app = app;
        this.orgUnitEditForm = orgUnitEditForm;
        addStyleName("view");
        setFirstComponent(orgUnitTable);
        setSecondComponent(orgUnitEditForm);
        this.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        setOrientation(ORIENTATION_HORIZONTAL);
    }

    public OrgUnitView showOrganizationalUnitAddForm() {
        try {
            setSecondComponent(app.newOrgUnitAddView());
        }
        catch (final EscidocException e) {
            // TODO Auto-generated catch block
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final InternalClientException e) {
            // TODO Auto-generated catch block
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final TransportException e) {
            // TODO Auto-generated catch block
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