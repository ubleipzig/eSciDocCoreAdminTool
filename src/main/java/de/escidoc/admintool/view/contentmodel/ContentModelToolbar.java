package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;

import de.escidoc.admintool.view.ViewConstants;

@SuppressWarnings("serial")
public class ContentModelToolbar extends CustomComponent {

    private final HorizontalLayout hLayout = new HorizontalLayout();

    private final ShowNewContentModelViewListener listener = new ShowNewContentModelViewListener();

    private Button newBtn;

    public ContentModelToolbar() {
        setCompositionRoot(hLayout);
    }

    public void init() {
        configureLayout();
        createNewButton();
        addNewButton();
    }

    private void addNewButton() {
        hLayout.addComponent(newBtn);
    }

    private void createNewButton() {
        newBtn = new Button(ViewConstants.NEW, listener);
    }

    private void configureLayout() {
        setSizeFull();
        hLayout.setHeight(100, UNITS_PERCENTAGE);
    }

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        listener.setContentModelView(contentModelView);
    }
}
