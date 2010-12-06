package de.escidoc.admintool.view.orgunit;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public final class AddOrgUnitButtonListener implements Button.ClickListener {
  private static final long serialVersionUID = -266152738843712262L;

  private final OrgUnitEditor orgUnitEditor;

  public AddOrgUnitButtonListener(OrgUnitEditor orgUnitEditor) {
    this.orgUnitEditor = orgUnitEditor;
  }

  public void buttonClick(final ClickEvent event) {
    this.orgUnitEditor.addButtonClicked();
  }
}