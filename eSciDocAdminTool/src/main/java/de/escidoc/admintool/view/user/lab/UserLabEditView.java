package de.escidoc.admintool.view.user.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

@SuppressWarnings("serial")
public class UserLabEditView extends VerticalLayout {
	private static final Logger log =
        LoggerFactory.getLogger(UserLabEditView.class);

    private final UserLabEditForm userLabEditForm;

    public UserLabEditView(final UserLabEditForm userLabForm) {
        userLabEditForm = userLabForm;
        buildUI();
    }

    private void buildUI() {
        addStyleName("view");
        addComponent(createHeader());
        addComponent(userLabEditForm);
    }

    private final Button newUserBtn =
        new Button("New", new NewUserListener());

    private final Button deleteUserBtn =
        new Button("Delete", new DeleteUserListener());

    private Item item;

    private HorizontalLayout createHeader() {
        final HorizontalLayout header = new HorizontalLayout();
        header.setMargin(true);
        header.setSpacing(true);
        header.addComponent(newUserBtn);
        header.addComponent(deleteUserBtn);
        header.setVisible(true);
        return header;
    }

    public void setSelected(final Item item) {
        this.item = item;
        userLabEditForm.setSelected(item);
    }

    private class NewUserListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            ((UserLabView) getParent()).showAddView();
        }
    }

    private class DeleteUserListener implements Button.ClickListener {
        public void buttonClick(final ClickEvent event) {
            try {
                final UserAccount deletedUser = userLabEditForm.deleteUser();
                ((UserLabView) getParent()).remove(deletedUser);

            }
            catch (final InternalClientException e) {
                setComponentError(new SystemError(e.getMessage()));
                log.error("An unexpected error occured! See log for details.", e);
                e.printStackTrace();
            }
            catch (final TransportException e) {
                setComponentError(new SystemError(e.getMessage()));
                log.error("An unexpected error occured! See log for details.", e);
                e.printStackTrace();
            }
            catch (final EscidocException e) {
            	log.error("An unexpected error occured! See log for details.", e);
                e.printStackTrace();
                setComponentError(new SystemError(e.getMessage()));
            }
        }
    }
}
