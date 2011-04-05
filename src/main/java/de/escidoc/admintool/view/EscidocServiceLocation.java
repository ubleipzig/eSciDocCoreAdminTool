package de.escidoc.admintool.view;

import java.net.URL;

import com.google.common.base.Preconditions;

import de.escidoc.admintool.app.AppConstants;

public class EscidocServiceLocation {

    private final String escidocUri;

    private URL appUri;

    public EscidocServiceLocation(final String eSciDocUri) {
        Preconditions.checkNotNull(eSciDocUri, "eSciDocUri is null");
        escidocUri = eSciDocUri;
    }

    public EscidocServiceLocation(final String escidocUri, final URL appUri) {
        Preconditions.checkNotNull(escidocUri, "eSciDocUri is null: %s",
            escidocUri);
        Preconditions.checkNotNull(appUri, "appUri is null: %s", appUri);
        this.escidocUri = escidocUri;
        this.appUri = appUri;
    }

    public String getUri() {
        return escidocUri;
    }

    public String getLoginUri() {
        return escidocUri + AppConstants.LOGIN_TARGET + appUri;
    }

    public String getLogoutUri() {
        return escidocUri + AppConstants.LOGOUT_TARGET;
    }
}