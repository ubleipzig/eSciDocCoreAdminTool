package de.escidoc.admintool.view.context;

import org.apache.commons.lang.NotImplementedException;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.client.exceptions.EscidocClientException;

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
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
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
