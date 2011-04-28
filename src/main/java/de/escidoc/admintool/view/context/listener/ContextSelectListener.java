package de.escidoc.admintool.view.context.listener;

import org.apache.commons.lang.NotImplementedException;

import com.vaadin.data.Item;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;

public class ContextSelectListener extends AbstractResourceSelectListener {

    private static final long serialVersionUID = 6656459571802886855L;

    public ContextSelectListener(final AdminToolApplication app) {
        this.app = app;
    }

    private final AdminToolApplication app;

    @Override
    public ResourceView getView() {
        return app.getContextView();
    }

    @SuppressWarnings("unused")
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

        @Override
        public void selectInFolderView(final Resource resource) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}
