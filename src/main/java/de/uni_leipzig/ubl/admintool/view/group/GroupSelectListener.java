package de.uni_leipzig.ubl.admintool.view.group;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOItem;
import com.vaadin.event.ItemClickEvent;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.resource.AbstractResourceSelectListener;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class GroupSelectListener extends AbstractResourceSelectListener {

	private static final long serialVersionUID = -1451681532928396045L;

	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	public GroupSelectListener(final AdminToolApplication app, final GroupService groupService) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		this.app = app;
		this.groupService = groupService;
	}
	
	@Override
	public void itemClick(final ItemClickEvent event) {
		try {
			getView().showEditView(userGroupToItem(groupService.retrieve(getSelectedObjectId(event))));
		} catch (EscidocClientException e) {
			app.getMainWindow().showNotification(e.getMessage());
		}
	}
	
	private String getSelectedObjectId(final ItemClickEvent event) {
		return (String) event.getItem().getItemProperty(PropertyId.OBJECT_ID).getValue();
	}

	private POJOItem<UserGroup> userGroupToItem(final UserGroup userGroup) {
        return new POJOItem<UserGroup>(userGroup, new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ACTIVE });
	}

	@Override
	public ResourceView getView() {
		return app.getUserView();
	}

}
