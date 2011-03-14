package de.escidoc.admintool.app;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ParameterHandler;

public class ParamaterHandlerImpl implements ParameterHandler {

    private static final long serialVersionUID = 6392830954652643671L;

    private static final Logger LOG = LoggerFactory
        .getLogger(ParamaterHandlerImpl.class);

    private final AdminToolApplication app;

    private final ParamaterDecoder paramDecoder;

    public ParamaterHandlerImpl(final AdminToolApplication app) {
        Preconditions.checkNotNull(app, "app is null: %s", app);
        this.app = app;
        paramDecoder = new ParamaterDecoder(app);
    }

    @Override
    public void handleParameters(final Map<String, String[]> parameters) {
        if (isEscidocUrlExists(parameters) && isTokenExist(parameters)) {
            LOG.debug("both escidocurl and token exists");
            app.setEscidocUri(parseEscidocUriFrom(parameters));
            showMainView(parameters);
        }
        if (isTokenExist(parameters)) {
            LOG.debug("only token exists");
            showMainView(parameters);
        }
        else if (isEscidocUrlExists(parameters) && !isTokenExist(parameters)) {
            LOG.debug("escidocurl exists but no token");
            app.setEscidocUri(parseEscidocUriFrom(parameters));
            showLoginView();
        }
        else if (!isEscidocUrlExists(parameters) && !isTokenExist(parameters)) {
            LOG.debug("nothing");
            app.showLandingView();
        }
    }

    // if (isTokenExist(parameters)) {
    // LOG.debug("the user has a token.");
    // app.loadProtectedResources(paramDecoder
    // .parseAndDecodeToken(parameters));
    // }
    // else {
    // LOG.debug("the user does not provide any token.");
    // loginMe();
    // }

    private void showMainView(final Map<String, String[]> parameters) {
        app
            .loadProtectedResources(paramDecoder
                .parseAndDecodeToken(parameters));
    }

    protected void showLoginView() {
        redirectTo(app.escidocLoginUrl + app.getURL());
    }

    private void redirectTo(final String url) {
        app.getMainWindow().open(new ExternalResource(url));
    }

    private String parseEscidocUriFrom(final Map<String, String[]> parameters) {
        return parameters.get(AppConstants.ESCIDOC_URL)[0];
    }

    private boolean isEscidocUrlExists(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_URL);
    }

    private boolean isTokenExist(final Map<String, String[]> parameters) {
        return parameters.containsKey(AppConstants.ESCIDOC_USER_HANDLE);
    }
}