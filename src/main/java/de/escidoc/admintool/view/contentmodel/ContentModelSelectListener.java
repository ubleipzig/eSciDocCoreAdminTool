package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.ResourceType;
import de.escidoc.core.resources.cmm.ContentModel;

@SuppressWarnings("serial")
public final class ContentModelSelectListener implements ItemClickListener {

    private final ResourceService contentModelService;

    private ContentModelView contentModelView;

    private Window mainWindow;

    public ContentModelSelectListener(final ResourceService contentModelService) {
        Preconditions.checkNotNull(contentModelService, "contentModelService is null: %s", contentModelService);
        this.contentModelService = contentModelService;
    }

    @Override
    public void itemClick(final ItemClickEvent event) {
        final Object itemId = event.getItemId();

        if (!(itemId instanceof ContentModel)) {
            throw new UnsupportedOperationException("Not yet implemented");
        }

        contentModelView.showEditView(tryFindContentModelById(itemId));
    }

    private Resource tryFindContentModelById(final Object object) {
        try {
            return contentModelService.findById(((ContentModel) object).getObjid());
        }
        catch (final EscidocClientException e) {
            mainWindow.showNotification(e.getMessage());
        }
        return new Resource() {

            @Override
            public ResourceType getResourceType() {
                throw new UnsupportedOperationException("Not yet implemented");
            }
        };
    }

    public void setContentModelView(final ContentModelView view) {
        Preconditions.checkNotNull(view, "editView is null: %s", view);
        contentModelView = view;
    }

}