package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Panel;

import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceEditView;

@SuppressWarnings("serial")
public class ContentModelEditView extends CustomComponent implements ResourceEditView {

    private final Panel panel = new Panel(ViewConstants.EDIT_CONTENT_MODEL);

    private ContentModelToolbar toolbar;

    public ContentModelEditView() {
        setCompositionRoot(panel);
        init();
    }

    public void init() {
        createToolbar();
        addToolbar(toolbar);
    }

    private void addToolbar(final ContentModelToolbar toolbar) {
        panel.addComponent(toolbar);
    }

    private ContentModelToolbar createToolbar() {
        toolbar = new ContentModelToolbar();
        toolbar.init();
        return toolbar;
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        toolbar.setContentModelView(contentModelView);
    }

    @Override
    public void bind(final Item item) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setFormReadOnly(final boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setFooterVisible(final boolean b) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
