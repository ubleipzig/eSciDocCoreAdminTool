package de.escidoc.admintool.view.orgunit;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.AbstractResourceSelectListener;
import de.escidoc.admintool.view.ResourceView;

public class OrgUnitSelectListener extends AbstractResourceSelectListener {
    private static final long serialVersionUID = 771573406912843328L;

    private final AdminToolApplication app;

    public OrgUnitSelectListener(final AdminToolApplication app) {
        this.app = app;
    }

    @Override
    public ResourceView getView() {
        return app.getOrgUnitView();
    }
}