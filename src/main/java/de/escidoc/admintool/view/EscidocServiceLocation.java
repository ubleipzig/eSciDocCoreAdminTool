package de.escidoc.admintool.view;

import com.google.common.base.Preconditions;

public class EscidocServiceLocation {

    private static final String LOGOUT_TARGET = "/aa/logout?target=";

    private static final String LOGIN_TARGET = "/aa/login?target=";

    private final String eSciDocUri;

    public EscidocServiceLocation(final String eSciDocUri) {
        Preconditions.checkNotNull(eSciDocUri, "eSciDocUri is null");
        this.eSciDocUri = eSciDocUri;
    }

    public String getUri() {
        return eSciDocUri;
    }

    public String getLoginUri() {
        return eSciDocUri + LOGIN_TARGET;
    }

    public String getLogoutUri() {
        return eSciDocUri + LOGOUT_TARGET;
    }
}
