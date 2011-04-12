package de.escidoc.admintool.view.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.AbstractField;

import de.escidoc.admintool.app.AdminToolApplication;

public class StartButtonListenerImpl extends AbstractStartButtonListener {

    private static final Logger LOG = LoggerFactory
        .getLogger(StartButtonListenerImpl.class);

    private static final long serialVersionUID = 2949659635673188343L;

    public StartButtonListenerImpl(final AbstractField escidocComboBox,
        final AdminToolApplication app) {
        super(escidocComboBox, app);
    }

    @Override
    protected void redirectToMainView() {
        final String redirectUri =
            super.getApplication().getURL() + "?escidocurl=" + getEscidocUri();
        LOG.info("redirect to: " + redirectUri);
        redirectTo(redirectUri);
    }

    private void redirectTo(final String url) {
        super.getMainWindow().open(new ExternalResource(url));
    }
}