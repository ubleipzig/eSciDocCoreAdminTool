package de.escidoc.admintool.view.user.password;

import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TextField;

public interface UpdatePasswordView extends ComponentContainer {

  void addPasswordField();

  void addRetypePasswordField();

  void setPassword(String emptyPassword);

  void addMinCharValidator();

  void addOkButton(ClickListener updatePasswordOkListener);

  TextField getPasswordField();

  TextField getRetypePasswordField();

  void addCancelButton(ClickListener cancelListener);

  void resetFields();

  void removeErrorMessages();

}