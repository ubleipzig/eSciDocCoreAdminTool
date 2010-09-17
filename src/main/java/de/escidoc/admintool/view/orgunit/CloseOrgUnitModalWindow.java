package de.escidoc.admintool.view.orgunit;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class CloseOrgUnitModalWindow extends AbstractStatusDialog {

    private static final String CLOSE = "Close";

    public CloseOrgUnitModalWindow(final OrgUnitEditView orgUnitEditView) {
        super(orgUnitEditView);
    }

    @Override
    protected String getSubmitBtnText() {
        return CLOSE;
    }

    @Override
    protected void doAction(final String enteredComment)
        throws EscidocException, InternalClientException, TransportException {
        orgUnitEditView.close(enteredComment);
    }
}
