package de.uni_leipzig.ubl.admintool.view.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.PdpRequest;
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
		if (groupListView == null) {
			return;
		}
		groupListView.select(groupListView.firstItemId());
		groupView.showEditView(getFirstItem());
	}

	private Item getFirstItem() {
		return groupListView.getContainerDataSource().getItem(groupListView.firstItemId());
	}
}
