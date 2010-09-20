package de.escidoc.admintool.view.orgunit;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.PublicStatus;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

@SuppressWarnings("serial")
public class CloseOrgUnitModalWindow extends AbstractStatusDialog {

    public CloseOrgUnitModalWindow(final OrgUnitToolbar toolbar,
        final OrgUnitEditView orgUnitEditView) {
        super(toolbar, orgUnitEditView);
    }

    @Override
    protected String getSubmitBtnText() {
        return ViewConstants.CLOSE;
    }

    @Override
    protected void doAction(final String enteredComment)
        throws EscidocException, InternalClientException, TransportException {
        orgUnitEditView.close(enteredComment);
        toolbar.changeState(PublicStatus.CLOSED);
    }
}
