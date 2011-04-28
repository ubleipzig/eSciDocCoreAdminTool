package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.navigation.ActionIdConstants;

@SuppressWarnings("serial")
public class ContentModelToolbar extends CustomComponent {

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final ShowNewContentModelViewListener listener = new ShowNewContentModelViewListener();

    private final Button newBtn = new Button(ViewConstants.NEW, listener);

    private final PdpRequest pdpRequest;

    public ContentModelToolbar(final PdpRequest pdpRequest) {
        Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
        this.pdpRequest = pdpRequest;
        setCompositionRoot(hLayout);
    }

    public void init() {
        configureLayout();
        addNewButton();
    }

    private void addNewButton() {
        hLayout.addComponent(newBtn);
    }

    private void configureLayout() {
        setSizeFull();
        hLayout.setHeight(100, UNITS_PERCENTAGE);
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        listener.setContentModelView(contentModelView);
    }

    public void bind(final String contentModelId) {
        newBtn.setVisible(isCreateAllowed());
    }

    private boolean isCreateAllowed() {
        return pdpRequest.isPermitted(ActionIdConstants.CREATE_CONTENT_MODEL);
    }
}
