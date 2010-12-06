package de.escidoc.admintool.view.resource;

import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import java.util.Collection;
import java.util.Map;

import de.escidoc.admintool.service.ResourceService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceBtnListenerData {
  public Collection<Field> allFields;
  public Window mainWindow;
  public ResourceService resourceService;
  public Item item;
  public Resource oldOrgUnit;
  public OrganizationalUnit toBeUpdated;
  public Map<String, Field> fieldByName;

  public ResourceBtnListenerData() {
  }
}