package de.uni_leipzig.ubl.admintool.view.group;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.usergroup.UserGroup;

@SuppressWarnings("serial")
public class GroupView extends HorizontalSplitPanel implements ResourceView {

	private final AdminToolApplication app;
	
	private final GroupListView groupList;
	
	private final GroupEditView groupEditView;
	
	private final VerticalLayout vLayout = new VerticalLayout();
	
	public GroupView(final AdminToolApplication app, final GroupListView groupListView, final GroupEditView groupEditView) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupListView, "groupListView is null: %s", groupListView);
		Preconditions.checkNotNull(groupEditView, "groupEditView is null: %s", groupEditView);
		this.app = app;
		this.groupList = groupListView;
		this.groupEditView = groupEditView;
	}
	
	public void init() {
		setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
		
		vLayout.setHeight(100, UNITS_PERCENTAGE);
		addHeader(vLayout);
		addListView(vLayout);
		setFirstComponent(vLayout);
	}
	
	private void addHeader(final VerticalLayout vLayout) {
		vLayout.addComponent(new Label("<b>User Groups</b>", Label.CONTENT_XHTML));
	}

	private void addListView(final VerticalLayout vLayout) {
		groupList.setSizeFull();
		vLayout.addComponent(groupList);
		vLayout.addComponent(groupList.createControls());
		vLayout.setExpandRatio(groupList, 1.0f);
	}

	@Override
	public void showAddView() {
		setSecondComponent(app.newGroupAddView());
	}

	@Override
	public void showEditView(Item item) {
		setSecondComponent(groupEditView);
		groupEditView.setSelected(item);
	}

	@Override
	public void selectInFolderView(Resource resource) {
		// TODO Auto-generated method stub

	}

	public GroupListView getGroupList() {
		return groupList;
	}

	public Item getSelectedItem() {
		return groupList.getItem(groupList.getValue());
	}
	
	public void remove(final UserGroup deletedUserGroup) {
		app.showGroupView();
	}
	
}
