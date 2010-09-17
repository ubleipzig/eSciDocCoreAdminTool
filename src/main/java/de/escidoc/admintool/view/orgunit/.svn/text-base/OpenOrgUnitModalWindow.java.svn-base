package de.escidoc.admintool.view.orgunit;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class OpenOrgUnitModalWindow extends AbstractStatusDialog {

    private static final String OPEN = "Open";

    public OpenOrgUnitModalWindow(final OrgUnitEditView orgUnitEditView) {
        super(orgUnitEditView);
    }

    @Override
    protected String getSubmitBtnText() {
        return OPEN;
    }

    @Override
    protected void doAction(final String enteredComment)
        throws EscidocException, InternalClientException, TransportException {
        orgUnitEditView.open(enteredComment);
    }
}
