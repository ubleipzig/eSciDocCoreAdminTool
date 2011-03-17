package de.escidoc.admintool.view;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;

public class EscidocServiceLocation {

    private final String eSciDocUri;

    public EscidocServiceLocation(final String eSciDocUri) {
        Preconditions.checkNotNull(eSciDocUri, "eSciDocUri is null");
        this.eSciDocUri = eSciDocUri;
    }

    public String getUri() {
        return eSciDocUri;
    }

    public String getLoginUri() {
        return eSciDocUri + AppConstants.LOGIN_TARGET;
    }

    public String getLogoutUri() {
        return eSciDocUri + AppConstants.LOGOUT_TARGET;
    }
}