package de.uni_leipzig.ubl.admintool.view.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.EscidocPagedTable;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.SelectorType;
import de.escidoc.core.resources.aa.usergroup.Selectors;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;
import de.uni_leipzig.ubl.admintool.view.group.selector.InternalSelectorName;

public class GroupSummaryView extends CustomComponent {

	private static final long serialVersionUID = 2107531184136070196L;
	
	private static final int WINDOW_HEIGHT_INT = 90;

	private static final int WINDOW_WIDTH_INT = 60;
	
	private static final String WINDOW_HEIGHT = WINDOW_HEIGHT_INT + "%";
	
	private static final String WINDOW_WIDTH = WINDOW_WIDTH_INT + "%";
	
	// app and services
	private final AdminToolApplication app;
	
	private final GroupService groupService;
	
	private final UserService userService;
	
	// data
	private UserGroup userGroup;
	
	private GroupEditForm groupEditForm;
	
	private POJOContainer<UserAccount> allUserAccountsContainer;
	
	private HierarchicalContainer allUserGroupContainer;
	
	private POJOContainer<Grant> allGrantsContainer;
	
	private String groupTitle;
	
	private int numDirectUAs = 0;
	
	private int numGroupUAs = 0;
	
	private int numAttributeUAs = 0;
	
	private int numTotalUAs = 0;
	
	// components
	private final Window modalWindow = new Window();
	
	private final VerticalLayout root = new VerticalLayout();
	
	private final HorizontalLayout header = new HorizontalLayout();
	
	private final HorizontalLayout footer = new HorizontalLayout();
	
	private final HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
	
	private final VerticalSplitPanel rightSplitPanel = new VerticalSplitPanel();
	
	private final VerticalLayout topRightPanel = new VerticalLayout();
	
	private final VerticalLayout bottomRightPanel = new VerticalLayout();
	
	private final EscidocPagedTable allUserAccounts = new EscidocPagedTable();
	
	private final Tree allUserGroups = new Tree();
	
	private final Table allGrants = new Table();
	
	private final Button closeButton = new Button(ViewConstants.CLOSE, new closeButtonListener());
	
	
	public GroupSummaryView(final AdminToolApplication app, final GroupService groupService, final UserService userService,
			final UserGroup userGroup, final GroupEditForm groupEditForm) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(userService, "userService is null: %s", userService);
		Preconditions.checkNotNull(userGroup, "userGroup is null: %s", userGroup);
		Preconditions.checkNotNull(groupEditForm, "groupeditForm is null: %s", groupEditForm);
		this.app = app;
		this.groupService = groupService;
		this.userService = userService;
		this.userGroup = userGroup;
		this.groupEditForm = groupEditForm;
		bindData();
	}
	
	
	private void init() {
		configure();
		addHeader();
		addSplitPanel();
		addUserAccounts();
		addUserSummary();
		addGroupSummary();
		addGrants();
		addFooter();
	}
	
	
	private void configure() {
		modalWindow.setModal(true);
		modalWindow.setWidth(WINDOW_WIDTH);
//		modalWindow.setHeight(WINDOW_HEIGHT);
		modalWindow.setCaption("Summary Group View");
		modalWindow.setContent(root);
		
		root.setMargin(true);
	}
	
	
	private void addSpace() {
		final Label space = new Label("<br /><br />", Label.CONTENT_XHTML);
		root.addComponent(space);
	}
	
	private void addHeader() {
		Label description = new Label("This is the summary of the group <b>" + groupTitle + "</b>. All values are inherited and represent the final group settings for user accounts and assigned roles.", Label.CONTENT_XHTML);
		
		header.addComponent(description);
		header.setComponentAlignment(description, Alignment.BOTTOM_LEFT);
		
		root.addComponent(header);
	}
	
	
	private void addSplitPanel() {
		rightSplitPanel.setLocked(true);
		rightSplitPanel.setSplitPosition(70);
		rightSplitPanel.setFirstComponent(topRightPanel);
		rightSplitPanel.setSecondComponent(bottomRightPanel);
		
		splitPanel.setLocked(true);
		splitPanel.setHeight("600px");
		splitPanel.setSplitPosition(40);
		splitPanel.setFirstComponent(allUserAccounts);
		splitPanel.setSecondComponent(rightSplitPanel);
		
		addSpace();
		root.addComponent(splitPanel);
	}
	
	
	private void addUserAccounts() {
		allUserAccounts.setContainerDataSource(allUserAccountsContainer);
		allUserAccounts.setReadOnly(true);
		allUserAccounts.setSelectable(true);
		allUserAccounts.setSizeFull();
		allUserAccounts.setVisibleColumns(new String [] { PropertyId.NAME });
		allUserAccounts.setColumnHeader(PropertyId.NAME, "Assigned and inherited user accounts …");
	}
	
	
	private void addUserSummary() {
		final Label totalUserAccountLabel = new Label("Total User Accounts: " + numTotalUAs);
		final Label directUserAccountLabel = new Label("User Accounts from user account selectors: " + numDirectUAs);
		final Label groupUserAccountLabel = new Label("User Accounts from user group selectors: " + numGroupUAs);
		final Label attributeAccountLabel = new Label("User Accounts from user attribute selectors: " + numAttributeUAs + "<br /><br />", Label.CONTENT_XHTML);
		
		topRightPanel.setMargin(false, false, true, true);
		topRightPanel.addComponent(totalUserAccountLabel);
		topRightPanel.addComponent(directUserAccountLabel);
		topRightPanel.addComponent(groupUserAccountLabel);
		topRightPanel.addComponent(attributeAccountLabel);
	}
	
	
	private void addGroupSummary() {
		allUserGroups.setCaption("Group Structure:");
		allUserGroups.setContainerDataSource(allUserGroupContainer);
		allUserGroups.expandItemsRecursively(userGroup.getXLinkTitle());
		
		topRightPanel.addComponent(allUserGroups);
	}
	
	
	private void addGrants() {
		allGrants.setContainerDataSource(allGrantsContainer);
		allGrants.setReadOnly(true);
		allGrants.setSelectable(false);
		allGrants.setSizeFull();
		allGrants.setColumnHeader(PropertyId.X_LINK_TITLE, "Assigned and inherited roles …");
		
		bottomRightPanel.addComponent(allGrants);
	}
	
	
	private void addFooter() {
		footer.addComponent(closeButton);
		footer.setComponentAlignment(closeButton, Alignment.BOTTOM_CENTER);
		
		addSpace();
		root.addComponent(footer);
	}

	
	public void setGroup(final UserGroup userGroup) {
		if (userGroup == null) {
			throw new IllegalArgumentException("userGroup must not be null.");
		}
		this.userGroup = userGroup;
		bindData();
	}
	
	
	public void show() {
		init();
		app.getMainWindow().addWindow(modalWindow);
	}
	
	
	public void close() {
		app.getMainWindow().removeWindow(modalWindow);
	}
	
	
	@SuppressWarnings("unchecked")
	private List<UserAccount> getUserAccounts() {
		List<UserAccount> rawUsers = new ArrayList<UserAccount>();
		List<UserAccount> finalUsers = new ArrayList<UserAccount>();
		
		// add direct user account selectors
		List<UserAccount> directUAs = getDirectUserAccountsFromGroup(userGroup);
		rawUsers.addAll(directUAs);
		numDirectUAs = directUAs.size();
		
		// add user accounts from group selectors
		List<UserAccount> groupUAs = (List<UserAccount>) removeDuplicates(getGroupUserAccountsFromGroup(userGroup));
		rawUsers.addAll(groupUAs);
		numGroupUAs = groupUAs.size();
		
		// TODO add user accounts from attribute selectors
		
		
		finalUsers = (List<UserAccount>) removeDuplicates(rawUsers);
		numTotalUAs = finalUsers.size();
		return finalUsers;
	}
	
	
	private List<? extends Resource> removeDuplicates(List<? extends Resource> list) {
		List<String> ids = new ArrayList<String>();
		List<Resource> listWithoutDuplicates = new ArrayList<Resource>();
		for (final Resource resource : list) {
			if (!ids.contains(resource.getObjid())) {
				listWithoutDuplicates.add(resource);
				ids.add(resource.getObjid());
			}
		}
		return listWithoutDuplicates;
	}
	

	private List<UserAccount> getDirectUserAccountsFromGroup(final UserGroup group) {
		List<UserAccount> userAccounts = new ArrayList<UserAccount>();
		
		userAccounts.addAll(getUserAccountsFromSelectors(group.getSelectors()));
		
		
		return userAccounts;
	}
	
	
	private List<UserAccount> getGroupUserAccountsFromGroup(final UserGroup group) {
		List<UserAccount> userAccounts = new ArrayList<UserAccount>();
		List<Selector> userGroupSelectors = getUserGroupSelectors(group);
		
		for (final Selector userGroupSelector : userGroupSelectors) {
			UserGroup selectorGroup;
			try {
				// get group
				selectorGroup = groupService.getGroupById(userGroupSelector.getContent());
				
				// get direct UAs from group
				userAccounts.addAll(getDirectUserAccountsFromGroup(selectorGroup));
				
				// get group UAs from group
				userAccounts.addAll(getGroupUserAccountsFromGroup(selectorGroup));
			} catch (EscidocClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return userAccounts;
	}

	
	private Collection<UserAccount> getUserAccountsFromSelectors(final Selectors selectors) {
		List<UserAccount> users = new ArrayList<UserAccount>();
		
		if (selectors instanceof Collection<?>) {
			for (final Selector selector : selectors) {
				if (selector.getType().equals(SelectorType.INTERNAL) && selector.getName().equals(InternalSelectorName.USER_ACCOUNT.getXmlValue())) {
					try {
						users.add(userService.getUserById(selector.getContent()));
					} catch (EscidocClientException e) {
						// TODO handle exception!
					}
				}
			}
		} 
		
		return users;
	}
	
	
	private List<Selector> getUserGroupSelectors(final UserGroup group) {
		List<Selector> groupSelectors = new ArrayList<Selector>();
		final List<Selector> selectors = group.getSelectors();
		
		if (selectors instanceof Collection<?>) {
			for (final Selector selector : selectors) {
				if (selector.getType().equals(SelectorType.INTERNAL) && selector.getName().equals(InternalSelectorName.USER_GROUP.getXmlValue())) {
						groupSelectors.add(selector);
				}
			}
		} 
		
		return groupSelectors;
	}
	
	
	private List<UserGroup> getParentUserGroups(final UserGroup group) {
		List<UserGroup> parentGroups = new ArrayList<UserGroup>();
		try {
			// iterate over all user groups and check if group exists as group selector
			final Collection<UserGroup> allGroups = groupService.findAll();
			for (final UserGroup parent : allGroups) {
				final List<Selector> selectors = getUserGroupSelectors(parent);
				for (final Selector selector : selectors) {
					if (selector.getContent().equals(group.getObjid())) {
						// parent found, add group to list
						parentGroups.add(parent);
						// check next parental level
						final List<UserGroup> nextParentalGroups = getParentUserGroups(parent);
						if (!nextParentalGroups.isEmpty()) {
							parentGroups.addAll(nextParentalGroups);
						}
						break;
					}
				}
			}
			
		} catch (EscidocClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return parentGroups;
	}
	
	
	private List<Grant> getAllGrants(final UserGroup group) throws EscidocException, InternalClientException, TransportException {
		final List<Grant> grants = new ArrayList<Grant>();
		// add direct assigned Grants to the list
		grants.addAll(groupService.retrieveCurrentGrants(group.getObjid()));
		// add grants from parental groups to the list
		for (final UserGroup parent : getParentUserGroups(group)) {
			grants.addAll(groupService.retrieveCurrentGrants(parent.getObjid()));
		}
		
		final List<Grant> filteredGrants = new ArrayList<Grant>();
		// check all grants back for duplicates
		for (final Grant uncheckedGrant : grants) {
			boolean addThis = true;
			for (final Grant filteredGrant : filteredGrants) {
				// grant is equal if object equals, role equals and assigned to equals
				if (uncheckedGrant.getProperties().equals(filteredGrant.getProperties())) {
					// duplicate detected, switch value of addThis and leave inner loop
					addThis = false;
					break;
				}
			}
			if (addThis) {
				// add grant to filtered list
				filteredGrants.add(uncheckedGrant);
			}
		}
		
		return filteredGrants;
	}
	
	
	private void bindData() {
		groupTitle = userGroup.getXLinkTitle();
		bindUserAccounts();
		bindGroupTree();
		bindGrants();
	}
	
	
	private void bindUserAccounts() {
		List<UserAccount> users = getUserAccounts();
		allUserAccountsContainer = new POJOContainer<UserAccount>(UserAccount.class, PropertyId.NAME);
		for (final UserAccount user : users) {
			allUserAccountsContainer.addPOJO(user);
		}
		allUserAccountsContainer.sort(new String[] {PropertyId.NAME}, new boolean[] {true});
	}
	
	
	private void bindGroupTree() {
		allUserGroupContainer = new HierarchicalContainer();
		allUserGroupContainer.addContainerProperty(PropertyId.OBJECT_ID, String.class, "");
		
		allUserGroupContainer.addItem(userGroup.getXLinkTitle());
		createGroupTree(userGroup);
	}
	
	
	private void createGroupTree(final UserGroup parent) {
		List<Selector> selectors = getUserGroupSelectors(parent);
		if (selectors.isEmpty()) {
			allUserGroupContainer.setChildrenAllowed(parent.getXLinkTitle(), false);
		}
		else {
			for (final Selector selector : selectors) {
				UserGroup group;
				try {
					group = groupService.getGroupById(selector.getContent());
					allUserGroupContainer.addItem(group.getXLinkTitle());
					allUserGroupContainer.getItem(group.getXLinkTitle()).getItemProperty(PropertyId.OBJECT_ID).setValue(group.getObjid());
					allUserGroupContainer.setParent(group.getXLinkTitle(), parent.getXLinkTitle());
					createGroupTree(group);
				} catch (EscidocClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void bindGrants() {
			List<Grant> grants;
			allGrantsContainer = new POJOContainer<Grant>(Grant.class, PropertyId.X_LINK_TITLE);
			try {
				grants = (List<Grant>) getAllGrants(userGroup);
				for (final Grant grant : grants) {
					allGrantsContainer.addPOJO(grant);
				}
			} catch (EscidocException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InternalClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	private class closeButtonListener implements Button.ClickListener {

		private static final long serialVersionUID = 218805084731560485L;

		@Override
		public void buttonClick(ClickEvent event) {
			app.getMainWindow().removeWindow(modalWindow);
		}
		
	}

}
