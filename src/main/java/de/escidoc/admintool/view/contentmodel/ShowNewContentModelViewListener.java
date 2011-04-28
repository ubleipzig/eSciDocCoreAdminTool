package de.escidoc.admintool.view.contentmodel;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class ShowNewContentModelViewListener implements ClickListener {

    private ContentModelView contentModelView;

    public void setContentModelView(final ContentModelView contentModelView) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        this.contentModelView = contentModelView;
    }

    @Override
    public void buttonClick(final ClickEvent event) {
        Preconditions.checkNotNull(contentModelView, "contentModelView is null: %s", contentModelView);
        contentModelView.showAddView();
    }

}