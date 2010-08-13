package de.escidoc.admintool.view.orgunit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public OrgUnitView(final AdminToolApplication app,
        final OrgUnitList orgUnitTable, final OrgUnitEditForm orgUnitEditForm,
        OrgUnitAddForm orgUnitAddForm) {
        this.app = app;
        this.addStyleName("view");
        this.setFirstComponent(orgUnitTable);
        this.setSecondComponent(orgUnitEditForm);
        this.setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
        this.setOrientation(ORIENTATION_HORIZONTAL);
    }

    public OrgUnitView showOrganizationalUnitAddForm() {
        try {
            setSecondComponent(app.newOrgUnitAddView());
        }
        catch (EscidocException e) {
            // TODO Auto-generated catch block
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (InternalClientException e) {
            // TODO Auto-generated catch block
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (TransportException e) {
            // TODO Auto-generated catch block
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        return this;
    }
}