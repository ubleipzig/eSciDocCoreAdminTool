package de.escidoc.admintool.view.user.password;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.view.ViewConstants;

public class UpdatePasswordViewImpl extends CustomComponent implements
    UpdatePasswordView {

  private static final long serialVersionUID = 4601029565488311333L;

  private static final boolean IS_NULL_ALLOWED = false;

  private static final int MAX_PASSWORD_LENGTH = 30;

  private static final int MIN_PASSWORD_LENGTH = 6;

  private final FormLayout layout = new FormLayout();

  private final TextField passwordField = new TextField(
      ViewConstants.PASSWORD_CAPTION);

  private final TextField retypePasswordField = new TextField(
      ViewConstants.RETYPE_PASSWORD_CAPTION);

  private HorizontalLayout footers;

  public UpdatePasswordViewImpl() {
    setCompositionRoot(layout);
  }

  public static UpdatePasswordView createView() {
    return new UpdatePasswordViewImpl();
  }

  @Override
  public void addPasswordField() {
    configure(passwordField);
    layout.addComponent(passwordField);
  }

  private void configure(final TextField textField) {
    textField.setSecret(true);
    textField.setWidth(ViewConstants.PASSWORD_FIELD_WIDTH, UNITS_PIXELS);
    textField.setImmediate(true);
  }

  @Override
  public void addRetypePasswordField() {
    configure(retypePasswordField);
    layout.addComponent(retypePasswordField);
  }

  @Override
  public void setPassword(final String password) {
    passwordField.setValue(password);
  }

  @Override
  public void addMinCharValidator() {
    final Validator minCharValidator = new StringLengthValidator(
        ViewConstants.TOO_SHORT_PASSWORD_MESSAGE, MIN_PASSWORD_LENGTH,
        MAX_PASSWORD_LENGTH, IS_NULL_ALLOWED);
    passwordField.addValidator(minCharValidator);
  }

  @Override
  public void addOkButton(final ClickListener updatePasswordOkListener) {
    final Button saveBtn = new Button("Change Password",
        updatePasswordOkListener);
    saveBtn.setStyleName(Reindeer.BUTTON_SMALL);
    // layout.addComponent(saveBtn);
    footers = new HorizontalLayout();
    footers.addComponent(saveBtn);
    layout.addComponent(footers);
  }

  @Override
  public TextField getPasswordField() {
    return passwordField;
  }

  @Override
  public TextField getRetypePasswordField() {
    return retypePasswordField;
  }

  @Override
  public void addCancelButton(final ClickListener cancelListener) {
    final Button cancelBtn = new Button(ViewConstants.CANCEL_LABEL,
        cancelListener);
    cancelBtn.setStyleName(Reindeer.BUTTON_SMALL);
    // layout.addComponent(cancelBtn);
    footers.addComponent(cancelBtn);
  }

  @Override
  public void resetFields() {
    passwordField.setValue(ViewConstants.EMPTY_STRING);
    retypePasswordField.setValue(ViewConstants.EMPTY_STRING);
  }

  @Override
  public void removeErrorMessages() {
    retypePasswordField.setComponentError(null);
    passwordField.setComponentError(null);
  }
}
