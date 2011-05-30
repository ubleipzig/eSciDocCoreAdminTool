package de.escidoc.admintool.domain;

import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.service.PdpService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public final class PdpRequestImpl implements PdpRequest {

    private static final Logger LOG = LoggerFactory.getLogger(PdpRequestImpl.class);

    private final UserAccount currentUser;

    private final PdpService service;

    public PdpRequestImpl(final PdpService service, final UserAccount currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.admintool.services.PdpRequest#isAllowed()
     */
    @Override
    public boolean isPermitted(final String actionId) {
        return evaluatePdpRequest(actionId, ViewConstants.EMPTY_STRING);
    }

    @Override
    public boolean isPermitted(final String actionId, final String resourceId) {
        return evaluatePdpRequest(actionId, resourceId);
    }

    private boolean evaluatePdpRequest(final String actionId, final String resourceId) {
        try {
            return service.isAction(actionId).forUser(currentUser.getObjid()).forResource(resourceId).permitted();
        }
        catch (final URISyntaxException e) {
            LOG.error(e.getMessage());
        }
        catch (final EscidocClientException e) {
            LOG.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isDenied(final String actionId, final String selectedItemId) {
        return !isPermitted(actionId, selectedItemId);
    }

    @Override
    public boolean isDenied(final String actionId) {
        return !isPermitted(actionId);
    }
}
