package de.uni_leipzig.ubl.admintool.view.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOItem;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class GroupViewComponent {

	private static final Logger LOG = LoggerFactory.getLogger(GroupViewComponent.class);
	
	private final AdminToolApplication app;
	
	private final PdpRequest pdpRequest;
	
	private final GroupService groupService;
	
	private GroupView groupView;
	
	private GroupEditView groupEditView;
	
	private GroupEditForm groupEditForm;
	
	private GroupEditForm editForm;
	
	private GroupListView groupListView;
	
	public GroupViewComponent(final AdminToolApplication app, final GroupService groupService, final PdpRequest pdpRequest) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
		
		this.app = app;
		this.groupService = groupService;
		this.pdpRequest = pdpRequest;
	}
	
	public void init() {
		createListView();
		createEditForm();
		createGroupView();
		setGroupView(groupView);
	}

	private void createListView() {
		this.groupListView = new GroupListView(app, groupService);
	}

	private void createEditForm() {
		editForm = new GroupEditForm(app, groupService, pdpRequest);
		editForm.init();
		setGroupEditForm(editForm);
		setGroupEditView(new GroupEditView(getGroupEditForm()));
	}

	private void createGroupView() {
		groupView = new GroupView(app, groupListView, getGroupEditView());
		groupView.init();
	}

	public GroupView getGroupView() {
		return groupView;
	}
	
	public void setGroupView(final GroupView groupView) {
		this.groupView = groupView;
	}
	
	public GroupEditForm getGroupEditForm() {
		return groupEditForm;
	}
	
	public void setGroupEditForm(final GroupEditForm groupEditForm) {
		this.groupEditForm = groupEditForm;
	}
	
	public GroupEditView getGroupEditView() {
		return groupEditView;
	}
	
	public void setGroupEditView(final GroupEditView groupEditView) {
		this.groupEditView = groupEditView;
	}

	public void showFirstItemInEditView() {
		if (groupListView.firstItemId() == null) {
			return;
		}
		groupListView.select(groupListView.firstItemId());
		try {
			groupView.showEditView(getFirstItem());
		} catch (EscidocClientException e) {
			app.getMainWindow().showNotification(e.getMessage());
		}
	}

	public void showGroupInEditView(final UserGroup group) {
		// FIXME may not work 
		groupView.showEditView(userGroupToItem(group));
	}
	
	public void showAddView() {
		groupView.showAddView();
	}

	private Item getFirstItem() throws EscidocClientException {
		// load item from service not from table data source because not all data is given e.g. label, descr., etc...
		return userGroupToItem(groupService.retrieve((String) groupListView.getContainerProperty(groupListView.firstItemId(), PropertyId.OBJECT_ID).getValue()));

	}

	private POJOItem<UserGroup> userGroupToItem(final UserGroup userGroup) {
        return new POJOItem<UserGroup>(userGroup, new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ACTIVE, PropertyId.DESCRIPTION, PropertyId.PROP_LABEL, PropertyId.EMAIL });
	}

}
