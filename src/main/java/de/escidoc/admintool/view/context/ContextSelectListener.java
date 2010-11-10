package de.escidoc.admintool.view.context;

import org.apache.commons.lang.NotImplementedException;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ErrorMessage;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
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
        return new EmptyView();
    }

    private class EmptyView extends VerticalLayout implements ResourceView {

        private static final long serialVersionUID = -2661185418534655185L;

        @Override
        public void showAddView() {
            throw new NotImplementedException(EmptyView.class);
        }

        @Override
        public void showEditView(final Item item) {
            throw new NotImplementedException(EmptyView.class);
        }
    }
}
