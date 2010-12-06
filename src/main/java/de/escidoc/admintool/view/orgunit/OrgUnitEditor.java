package de.escidoc.admintool.view.orgunit;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Window;

import java.util.Set;

import de.escidoc.admintool.service.OrgUnitService;

public class OrgUnitEditor extends CustomComponent {

  private final ListSelect orgUnitList;

  private final Button addOrgUnitButton;

  private final Button removeOrgUnitButton;

  private final String caption;

  private final OrgUnitService service;

  private final Button editParentButton;

  private final ClickListener editParentListener;

  // What is the responsibility of this class?
  // Show the user OrgUnitTree with button add, remove, edit parent
  public OrgUnitEditor(final String caption, final ListSelect orgUnitList,
      final Button addOrgUnitButton, final Button removeOrgUnitButton,
      final Button editParentButton, final OrgUnitService service,
      final ClickListener editParentListener) {

    this.caption = caption;
    this.orgUnitList = orgUnitList;

    this.addOrgUnitButton = addOrgUnitButton;
    this.removeOrgUnitButton = removeOrgUnitButton;
    this.editParentButton = editParentButton;

    this.service = service;
    this.editParentListener = editParentListener;

    init();
  }

  private void init() {
    setCompositionRoot(orgUnitList);
    addListeners();
  }

  private void addListeners() {

    addOrgUnitButton.addListener(new AddOrgUnitButtonListener(this));

    removeOrgUnitButton.addListener(new Button.ClickListener() {
      private static final long serialVersionUID = -8884073931795851352L;

      public void buttonClick(final ClickEvent event) {
        removeButtonClicked();
      }
    });

    editParentButton.addListener(editParentListener);
  }

  public void addButtonClicked() {
    getApplication().getMainWindow().addWindow(new Window());
  }

  public void removeButtonClicked() {
    final Object o = orgUnitList.getValue();
    if (o instanceof Set) {
      final Set set = (Set) o;
      for (final Object ob : set) {
        orgUnitList.removeItem(ob);
      }
    } else if (o != null) {
      orgUnitList.removeItem(o);
    }
  }
}