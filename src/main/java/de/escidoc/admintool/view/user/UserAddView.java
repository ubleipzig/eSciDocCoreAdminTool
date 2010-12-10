package de.escidoc.admintool.view.user;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.POJOItem;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserAddView extends CustomComponent implements ClickListener {

  private static final long serialVersionUID = 3007285643463919742L;

  private static final Logger LOG = LoggerFactory.getLogger(UserAddView.class);

  private static final int LABEL_WIDTH = 100;

  private final Panel panel = new Panel();

  private final FormLayout form = new FormLayout();

  private final HorizontalLayout footer = new HorizontalLayout();

  private final Button save = new Button("Save", this);

  private final Button cancel = new Button("Cancel", this);

  private final UserListView userListView;

  private final UserService userService;

  private final OrgUnitService orgUnitService;

  private TextField nameField;

  private TextField loginNameField;

  private ObjectProperty nameProperty;

  private ObjectProperty loginNameProperty;

  private final AdminToolApplication app;

  public UserAddView(final AdminToolApplication app,
      final UserListView userListView, final UserService userService,
      final OrgUnitService orgUnitService) {
    this.app = app;
    this.userListView = userListView;
    this.userService = userService;
    this.orgUnitService = orgUnitService;
    init();
  }

  public void init() {
    setCompositionRoot(panel);

    panel.setStyleName(Reindeer.PANEL_LIGHT);
    panel.setCaption(ViewConstants.USER_ADD_VIEW_CAPTION);

    panel.setContent(form);

    form.setSpacing(false);
    form.setWidth(75, UNITS_PERCENTAGE);

    addNameField();
    addLoginField();
    createPasswordField();
    addPasswordField();
    addFooter();
  }

  private void addPasswordField() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  private void createPasswordField() {
    throw new UnsupportedOperationException("not-yet-implemented.");
  }

  private void addNameField() {
    panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL,
        nameField = new TextField(), LABEL_WIDTH, true));
    nameProperty = new ObjectProperty("", String.class);
    nameField.setPropertyDataSource(nameProperty);
    nameField.setWidth(ViewConstants.FIELD_WIDTH);
  }

  private void addLoginField() {
    panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL,
        loginNameField = new TextField(), LABEL_WIDTH, true));
    loginNameProperty = new ObjectProperty("", String.class);
    loginNameField.setPropertyDataSource(loginNameProperty);
    loginNameField.setWidth(ViewConstants.FIELD_WIDTH);
  }

  private void addFooter() {
    footer.setSpacing(true);
    footer.setMargin(true);
    footer.setVisible(true);

    footer.addComponent(save);
    footer.addComponent(cancel);

    final VerticalLayout verticalLayout = new VerticalLayout();
    verticalLayout.addComponent(footer);
    verticalLayout.setComponentAlignment(footer, Alignment.MIDDLE_RIGHT);

    panel.addComponent(verticalLayout);
  }

  @Override
  public void buttonClick(final ClickEvent event) {
    final Button source = event.getButton();
    if (source == cancel) {
      resetFields();
    } else if (source == save) {
      boolean valid = true;
      valid = EmptyFieldValidator.isValid(nameField, "Please enter a "
          + ViewConstants.NAME_ID);
      valid &= (EmptyFieldValidator.isValid(loginNameField, "Please enter a "
          + ViewConstants.LOGIN_NAME_ID));

      if (valid) {
        try {
          final UserAccount createdUserAccount = userService.create(
              (String) nameProperty.getValue(),
              (String) loginNameProperty.getValue());

          final POJOItem<UserAccount> item = userListView.addUser(createdUserAccount);
          resetFields();
          userListView.select(createdUserAccount);
          showInEditView(item);
        } catch (final EscidocException e) {
          final String error = "A user with login name "
              + (String) nameProperty.getValue() + " already exist.";
          loginNameField.setComponentError(new UserError(error));
          LOG.error(error, e);
        } catch (final InternalClientException e) {
          LOG.error("An unexpected error occured! See LOG for details.", e);
        } catch (final TransportException e) {
          LOG.error("An unexpected error occured! See LOG for details.", e);
        }
      }
    }
  }

  private void showInEditView(final POJOItem<UserAccount> item) {
    app.getUserView().showEditView(item);
  }

  private void resetFields() {
    nameField.setComponentError(null);
    loginNameField.setComponentError(null);
    nameField.setValue("");
    loginNameField.setValue("");
  }
}