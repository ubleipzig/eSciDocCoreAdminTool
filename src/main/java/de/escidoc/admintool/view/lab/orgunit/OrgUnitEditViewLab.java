package de.escidoc.admintool.view.lab.orgunit;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class OrgUnitEditViewLab extends AbstractOrgUnitViewLab {

    private static final long serialVersionUID = -584963284131407616L;

    private OrganizationalUnit orgUnit;

    private OrgUnitToolbarLab toolbar;

    public OrgUnitEditViewLab(final OrgUnitService service,
        final Window mainWindow) {
        super(service, mainWindow);
    }

    public void setOrgUnit(final OrganizationalUnit orgUnit) {
        this.orgUnit = orgUnit;
    }

    @Override
    protected String getViewCaption() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Component addToolbar() {
        toolbar = new OrgUnitToolbarLab();
        return toolbar;
    }

    @Override
    protected void onAddPredecessorClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void saveClicked(final ClickEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void cancelClicked(final ClickEvent event) {
        // TODO Auto-generated method stub

    }

}
