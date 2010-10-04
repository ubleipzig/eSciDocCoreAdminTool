package de.escidoc.admintool.view.context;

import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.AbstractResourceSelectListener;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.ResourceView;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class ContextSelectListener extends AbstractResourceSelectListener {

    private static final long serialVersionUID = 6656459571802886855L;

    public ContextSelectListener(final AdminToolApplication app) {
        this.app = app;
    }

    private final AdminToolApplication app;

    @Override
    public ResourceView getView() {
        try {
            return app.getContextView();
        }
        catch (final EscidocException e) {
            ErrorMessage.show(app.getMainWindow(), e);
        }
        catch (final InternalClientException e) {
            ErrorMessage.show(app.getMainWindow(), e);
        }
        catch (final TransportException e) {
            ErrorMessage.show(app.getMainWindow(), e);
        }
        return (ResourceView) new VerticalLayout();
    }
}
