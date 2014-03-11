package de.uni_leipzig.ubl.admintool.view.group;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.data.util.POJOItem;
import com.vaadin.ui.Table;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.view.EscidocPagedTable;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.dialog.ErrorDialog;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

@SuppressWarnings("serial")
public class GroupListView extends EscidocPagedTable {

	private static final Logger LOG = LoggerFactory.getLogger(GroupListView.class);
	
	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	private final Collection<UserGroup> allUserGroups = new ArrayList<UserGroup>();
	
	private POJOContainer<UserGroup> groupContainer;
	
	public GroupListView(final AdminToolApplication app, final GroupService groupService) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		this.app = app;
		this.groupService = groupService;
		buildView();
		findAllGroups();
		bindDataSource();
		setPageLength(50);
	}

	private void buildView() {
		setSizeFull();
		setSelectable(true);
		setImmediate(true);
		addListener(new GroupSelectListener(app, groupService));
        setNullSelectionAllowed(false);
        setColumnHeaderMode(1);
        setColumnCollapsingAllowed(true);
	}

	private void findAllGroups() {
		try {
			allUserGroups.addAll(groupService.findAll());
		} catch (final EscidocClientException e) {
			app.getMainWindow().addWindow(
                new ErrorDialog(app.getMainWindow(), "Error", "An unexpected error occured! See LOG for details."));
            LOG.error("An unexpected error occured! See LOG for details.", e);
		}
	}

	private void bindDataSource() {
		if(isGroupExist()) {
			initGroupContainer();
		}
	}

	private boolean isGroupExist() {
		return allUserGroups != null && !allUserGroups.isEmpty();
	}
	
	private void initGroupContainer() {
		groupContainer =
				new POJOContainer<UserGroup>(allUserGroups, PropertyId.OBJECT_ID, PropertyId.NAME,
                PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
                PropertyId.ACTIVE);
		setContainerDataSource(groupContainer);
		
		setSortContainerPropertyId(PropertyId.NAME);
		setSortAscending(true);

		setColumnHeader(PropertyId.NAME, ViewConstants.NAME_LABEL);
		setColumnExpandRatio(PropertyId.NAME, 0.75f);
		setColumnHeader(PropertyId.ACTIVE, ViewConstants.ACTIVE_STATUS);
		setColumnExpandRatio(PropertyId.ACTIVE, 0.25f);
		setColumnHeader(PropertyId.LAST_MODIFICATION_DATE, ViewConstants.MODIFIED_ON_LABEL);
		setColumnCollapsed(PropertyId.LAST_MODIFICATION_DATE, true);
		
		setVisibleColumns(new Object[] { PropertyId.NAME, PropertyId.LAST_MODIFICATION_DATE, PropertyId.ACTIVE });
	}
	
	public void remove(final UserGroup deletedUserGroup) {
		final boolean removeItem = groupContainer.removeItem(deletedUserGroup);
		assert removeItem == true : "Failed to remove user group from the container.";
	}
	
	public POJOItem<UserGroup> addGroup(final UserGroup createdUserGroup) {
		final POJOItem<UserGroup> item = groupContainer.addItem(createdUserGroup);
		sort(new Object[] { getSortContainerPropertyId() }, new boolean[] { true });
        return item;
	}

	public void updateGroup(UserGroup updatedUserGroup) {
		// container has to be updated by removing old item and adding updated item
		remove(updatedUserGroup);
		addGroup(updatedUserGroup);		
	}

}
