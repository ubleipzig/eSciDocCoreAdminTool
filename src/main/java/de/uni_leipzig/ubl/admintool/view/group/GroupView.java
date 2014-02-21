package de.uni_leipzig.ubl.admintool.view.group;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import com.vaadin.ui.SplitPanel;
import com.vaadin.ui.VerticalLayout;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.resource.ResourceView;
import de.escidoc.core.resources.Resource;

@SuppressWarnings("serial")
public class GroupView extends SplitPanel implements ResourceView {

	private final AdminToolApplication app;
	
	private final GroupListView groupList;
	
	private final GroupEditView groupEditView;
	
	private final VerticalLayout vLayout = new VerticalLayout();
	
	public GroupView(final AdminToolApplication app, final GroupListView groupListView, final GroupEditView groupEditView) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupListView, "groupListView is null: %s", groupListView);
		Preconditions.checkNotNull(groupEditView, "groupEditView is null: %s", groupEditView);
		this.app = app;
		groupList = groupListView;
		this.groupEditView = groupEditView;
	}
	
	public void init() {
		setSplitPosition(ViewConstants.SPLIT_POSITION_IN_PERCENT);
		setOrientation(ORIENTATION_HORIZONTAL);
		
		vLayout.setHeight(100, UNITS_PERCENTAGE);
		addHeader(vLayout);
		addListView(vLayout);
		setFirstComponent(vLayout);
	}
	
	private void addHeader(final VerticalLayout vLayout) {
		vLayout.addComponent(new Label("<b>UserGroups</b>", Label.CONTENT_XHTML));
	}

	private void addListView(final VerticalLayout vLayout2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showAddView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void showEditView(Item item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectInFolderView(Resource resource) {
		// TODO Auto-generated method stub

	}

}
