package de.escidoc.admintool.app;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.orgunit.OrgUnitEditView;
import de.escidoc.admintool.view.orgunit.OrgUnitListView;
import de.escidoc.admintool.view.orgunit.OrgUnitView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OrgUnitViewFactory {

    private OrgUnitListView orgUnitList;

    private OrgUnitEditView orgUnitEditForm;

    private OrgUnitView orgUnitView;

    private final OrgUnitAddView orgUnitAddForm;

    private final AdminToolApplication app;

    private final OrgUnitService orgUnitService;

    public OrgUnitViewFactory(final AdminToolApplication app,
        final OrgUnitService orgUnitService, final OrgUnitAddView orgUnitAddForm) {
        this.app = app;
        this.orgUnitService = orgUnitService;
        this.orgUnitAddForm = orgUnitAddForm;
    }

    public OrgUnitView getOrgUnitView() throws EscidocException,
        InternalClientException, TransportException {
        if (orgUnitView == null) {
            orgUnitView = create();
        }
        return orgUnitView;
    }

    private OrgUnitView create() throws EscidocException,
        InternalClientException, TransportException {
        orgUnitList = new OrgUnitListView(app, orgUnitService);
        orgUnitEditForm = new OrgUnitEditView(app, orgUnitService);
        orgUnitEditForm.setOrgUnitList(orgUnitList);
        return new OrgUnitView(app, orgUnitList, orgUnitEditForm,
            orgUnitAddForm);
    }

}