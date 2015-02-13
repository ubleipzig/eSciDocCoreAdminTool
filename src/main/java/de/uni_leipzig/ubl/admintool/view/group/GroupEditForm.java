/**
 * CDDL HEADER START
 * 
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 * 
 * You can obtain a copy of the license at license/ESCIDOC.LICENSE
 * or https://www.escidoc.org/license/ESCIDOC.LICENSE .
 * See the License for the specific language governing permissions
 * and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at license/ESCIDOC.LICENSE.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 * 
 * CDDL HEADER END
 * 
 * 
 * 
 * Copyright 2015 Leipzig University Library
 * 
 * This code is the result of the project
 * "Die Bibliothek der Milliarden Woerter". This project is funded by
 * the European Social Fund. "Die Bibliothek der Milliarden Woerter" is
 * a cooperation project between the Leipzig University Library, the
 * Natural Language Processing Group at the Institute of Computer
 * Science at Leipzig University, and the Image and Signal Processing
 * Group at the Institute of Computer Science at Leipzig University.
 * 
 * All rights reserved.  Use is subject to license terms.
 * 
 * @author Uwe Kretschmer <u.kretschmer@denk-selbst.de>
 * 
 */
package de.uni_leipzig.ubl.admintool.view.group;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.terminal.SystemError;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.CellStyleGenerator;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.Notification;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.service.internal.UserService;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.LinkClickListener;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.util.Constants;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.useraccount.UserAccount;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.aa.usergroup.SelectorType;
import de.escidoc.core.resources.aa.usergroup.UserGroup;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;
import de.escidoc.core.resources.common.reference.UserAccountRef;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;
import de.uni_leipzig.ubl.admintool.view.group.selector.AddAttributeSelector;
import de.uni_leipzig.ubl.admintool.view.group.selector.AddInternalSelector;
import de.uni_leipzig.ubl.admintool.view.group.selector.InternalSelectorName;
import de.uni_leipzig.ubl.admintool.view.group.selector.RemoveSelectorButtonListener;

@SuppressWarnings("serial")
public class GroupEditForm extends CustomComponent implements ClickListener {

	private static final Logger LOG = LoggerFactory.getLogger(GroupEditForm.class);
	
    private static final int LABEL_WIDTH = 100;

    private static final int LABEL_HEIGHT = 15;
    
    private static final int ROLE_LIST_HEIGHT = 100;

    private static final int SELECTOR_LIST_HEIGHT = 600;

    private static final String RESOURCE_TYPE_GRANT = "grant";
    
    private static final String RESOURCE_TYPE_SELECTOR = "selector";
    
    final AdminToolApplication app;
	
	final GroupService groupService;
	
	final UserService userService;
	
	private final PdpRequest pdpRequest;
	
	private Item item;
	
	String groupObjectId;
	
	POJOContainer<Grant> grantContainer;
	
	POJOContainer<Selector> selectorInternalContainer;

	POJOContainer<Selector> selectorAttributeContainer;
	
	Map<String, String> selectorsInternalTitles = new HashMap<String, String>();
	
	// fields
    private final Label objId = new Label();
    private TextField name; 		// required
    private TextField label;		// required
    private TextArea description;
    private TextField email;
    private CheckBox activeStatus;
    final Table roles = new Table();
    final Table selectorsInternal = new Table();
    final Table selectorsAttribute = new Table();
    private final Label modifiedOn = new Label();
    private Button modifiedOnLink;
    private LinkClickListener modifiedOnLinkListener;
    private final Label createdOn = new Label(); 
    private Button createdByLink;
    private LinkClickListener createdByLinkListener;
    
	
	// components
	private final Window mainWindow;
	
	private final Panel panel = new Panel();
	
	private final VerticalLayout base = new VerticalLayout();
	
	private final Accordion accordion = new Accordion();
	
	private final FormLayout tab1 = new FormLayout();
	
	private final VerticalLayout tab2 = new VerticalLayout();

	private final VerticalLayout tab3 = new VerticalLayout();
	
	private final HorizontalLayout header = new HorizontalLayout();
	
	private final HorizontalLayout saveCancelButtons = new HorizontalLayout();
	
	private final Button newGroupBtn = new Button(ViewConstants.NEW, new NewGroupListener());
	
	private final Button deleteGroupBtn = new Button(ViewConstants.DELETE, new DeleteGroupListener());
	
	private final Button finalBtn = new Button("Show Group Summary", new GroupSummaryViewListener());
	
	private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL, this);
	
	private final Button cancelBtn = new Button(ViewConstants.CANCEL, this);
	
    private final Button addRoleBtn = new Button(ViewConstants.ADD_LABEL, new AddRoleListener());

    private final Button removeRoleBtn = new Button(ViewConstants.REMOVE, new RemoveRoleButtonListener(this));
    
    private final Button addInternalSelectorBtn = new Button();
    
    private final Button removeInternalSelectorBtn = new Button(ViewConstants.REMOVE, new RemoveSelectorButtonListener(this, selectorsInternal)); 

    private final Button addAttributeSelectorBtn = new Button();
    
    
    private final Button removeAttributeSelectorBtn = new Button(ViewConstants.REMOVE, new RemoveSelectorButtonListener(this, selectorsAttribute)); 
    
    public GroupEditForm(final AdminToolApplication app, final GroupService groupService, final PdpRequest pdpRequest) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
		
		this.app = app;
		this.groupService = groupService;
		this.userService = app.getUserService();
		this.pdpRequest = pdpRequest;
		mainWindow = app.getMainWindow();
	}
	
	public final void init() {
		configureLayout();
		
		addName();
		addLabel();
		addDescription();
		addEmail();
		
		addObjectId();

        addCreated();
        addModified();
        addActiveStatus();
        addSaveCancelButtons();
        
        addVerticalSpace();
        addRoles();
        addVerticalSpace();
        addSelectors();
}
	
    private void addVerticalSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

	private void addName() {
		name = new TextField();
		name.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		name.setWidth(ViewConstants.FIELD_WIDTH);
		name.setWriteThrough(false);
		tab1.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, name, 100, true));
	}
	
	private void addLabel() {
		label = new TextField();
		label.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		label.setWidth(ViewConstants.FIELD_WIDTH);
		label.setWriteThrough(false);
		label.setReadOnly(true);
		tab1.addComponent(LayoutHelper.create(ViewConstants.LABEL, label, 100, true));
	}
	
	private void addDescription() {
		description = new TextArea();
		description.setWidth(ViewConstants.FIELD_WIDTH);
		description.setRows(ViewConstants.DESCRIPTION_ROWS);
		description.setMaxLength(ViewConstants.MAX_DESC_LENGTH);
		description.setWriteThrough(false);
		tab1.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL, description, LABEL_WIDTH, 80, false));
	}
	
	private void addEmail() {
		email = new TextField();
		email.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		email.setWidth(ViewConstants.FIELD_WIDTH);
		email.setWriteThrough(false);
		email.setRequired(false);
		tab1.addComponent(LayoutHelper.create(ViewConstants.EMAIL_LABEL, email, 100, false));
	}
	
	private void addObjectId() {
		tab1.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL, objId, 100, false));
	}
	
	private void addCreated() {
		createdByLink = new Button();
		createdByLink.setStyleName(BaseTheme.BUTTON_LINK);
		createdByLinkListener = new LinkClickListener(app);
		createdByLink.addListener(createdByLinkListener);
		tab1.addComponent(LayoutHelper.create("Created", "by", createdOn, createdByLink, LABEL_WIDTH, LABEL_HEIGHT, false));
	}
	
	private void addModified() {
		modifiedOnLink = new Button();
		modifiedOnLink.setStyleName(BaseTheme.BUTTON_LINK);
		modifiedOnLinkListener = new LinkClickListener(app);
		modifiedOnLink.addListener(modifiedOnLinkListener);
		tab1.addComponent(LayoutHelper.create("Modified", "by", modifiedOn, modifiedOnLink, LABEL_WIDTH, LABEL_HEIGHT, false));
	}
	
	private void addActiveStatus() {
		activeStatus = new CheckBox();
		activeStatus.setWriteThrough(false);
		tab1.addComponent(LayoutHelper.create(ViewConstants.ACTIVE_STATUS, activeStatus, LABEL_WIDTH, false));
	}
	
	private void addSaveCancelButtons() {
		final Label space  = new Label();
		space.setWidth(ViewConstants.DEFAULT_LABEL_WIDTH, UNITS_PIXELS);
		
		saveCancelButtons.addComponent(space);
		saveCancelButtons.addComponent(saveBtn);
		saveCancelButtons.addComponent(cancelBtn);
		tab1.addComponent(saveCancelButtons);
	}
	
	private void addRoles() {
        // set table params
		roles.setHeight(ROLE_LIST_HEIGHT, UNITS_PIXELS);
        roles.setWidth(ViewConstants.FIELD_WIDTH);
        roles.setSelectable(true);
        roles.setNullSelectionAllowed(true);
        roles.setMultiSelect(true);
        roles.setImmediate(true);
        roles.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        // set button parameters
        addRoleBtn.setCaption(ViewConstants.ADD);
        addRoleBtn.setStyleName(Reindeer.BUTTON_SMALL);
        removeRoleBtn.setCaption(ViewConstants.REMOVE);
        removeRoleBtn.setStyleName(Reindeer.BUTTON_SMALL);
        // create role component
        final VerticalLayout rolesComponent = 
        		createLayout(ViewConstants.ROLES_LABEL, roles, ViewConstants.DEFAULT_LABEL_WIDTH, ROLE_LIST_HEIGHT, false, new Button[] {
        				addRoleBtn, removeRoleBtn
        		});
        tab1.addComponent(rolesComponent);
	}
	
	private void addSelectors() {
		addInternalSelectors();
		addAttributeSelectors();
		
		Button[] selectorButtons = {addInternalSelectorBtn, addAttributeSelectorBtn, removeInternalSelectorBtn, 
				removeAttributeSelectorBtn};
		for (Button button : selectorButtons) {
			button.setStyleName(Reindeer.BUTTON_SMALL);
		}
        addInternalSelectorBtn.setCaption(ViewConstants.ADD);
        addInternalSelectorBtn.addListener(new AddInternalSelector(app, groupService, selectorsInternal));
        addAttributeSelectorBtn.setCaption(ViewConstants.ADD);
        addAttributeSelectorBtn.addListener(new AddAttributeSelector(app, groupService, selectorsAttribute));

        HorizontalLayout hlInternal = new HorizontalLayout();
        hlInternal.addComponent(addInternalSelectorBtn);
        hlInternal.addComponent(removeInternalSelectorBtn);

        HorizontalLayout hlAttribute = new HorizontalLayout();
        hlAttribute.addComponent(addAttributeSelectorBtn);
        hlAttribute.addComponent(removeAttributeSelectorBtn);
        
        tab2.addComponent(selectorsInternal);
        tab2.addComponent(hlInternal);

        tab3.addComponent(selectorsAttribute);
        tab3.addComponent(hlAttribute);
	}
	
	private void addInternalSelectors() {
		setupSelectors(selectorsInternal);
		selectorsInternal.setMultiSelect(true);
		selectorsInternal.addContainerProperty(PropertyId.NAME, String.class, "", null, null, null);
		selectorsInternal.addGeneratedColumn(PropertyId.NAME, new InternalSelectorReadableNameColumnGenerator());
		selectorsInternal.setCellStyleGenerator(new InternalSelectorHighlightStyleGenerator());
		selectorsInternal.setColumnExpandRatio(PropertyId.NAME, 1.0f);
	}
	
	private void addAttributeSelectors() {
		setupSelectors(selectorsAttribute);
		selectorsAttribute.setMultiSelect(true);
	}
	
	private void setupSelectors(final Table selectors) {
		selectors.setHeight(SELECTOR_LIST_HEIGHT, UNITS_PIXELS);
        selectors.setWidth(100, UNITS_PERCENTAGE);
        selectors.setSelectable(true);
        selectors.setNullSelectionAllowed(true);
        selectors.setImmediate(true);
        selectors.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	}
	
	private void configureLayout() {
		setCompositionRoot(panel);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setCaption(ViewConstants.EDIT_GROUP_VIEW_CAPTION);
		panel.setContent(base);
		
		accordion.addTab(tab1, ViewConstants.GROUP_ATTRIBUTES_LABEL, null);
		accordion.addTab(tab2, ViewConstants.SELECTORS_INTERNAL_LABEL, null);
		accordion.addTab(tab3, ViewConstants.SELECTORS_ATTRIBUTE_LABEL, null);
		accordion.setSizeFull();
		
        base.setSpacing(false);
        base.setSizeFull();
        base.addComponent(createHeader());
        base.addComponent(accordion);
	}

    private VerticalLayout createLayout(
            final String layoutLabel, final Table table, final int labelWidth, final int layoutListHeight, final boolean b,
            final Button[] buttons) {

            final HorizontalLayout hLayout = new HorizontalLayout();
            hLayout.setHeight(layoutListHeight + Constants.PX);
            hLayout.addComponent(new Label(" "));

            final Label textLabel =
                new Label(Constants.P_ALIGN_RIGHT + layoutLabel + "   " + Constants.P, Label.CONTENT_XHTML);
            textLabel.setSizeUndefined();
            textLabel.setWidth(labelWidth + Constants.PX);
            hLayout.addComponent(textLabel);
            hLayout.setComponentAlignment(textLabel, Alignment.MIDDLE_RIGHT);
            hLayout.addComponent(new Label("&nbsp;", Label.CONTENT_XHTML));
            hLayout.addComponent(table);
            hLayout.setComponentAlignment(table, Alignment.MIDDLE_RIGHT);
            hLayout.addComponent(new Label(" &nbsp; ", Label.CONTENT_XHTML));

            final VerticalLayout vLayout = new VerticalLayout();
            vLayout.addComponent(hLayout);

            final HorizontalLayout hl = new HorizontalLayout();
            final Label la = new Label("&nbsp;", Label.CONTENT_XHTML);
            la.setSizeUndefined();
            la.setWidth(labelWidth + Constants.PX);
            hl.addComponent(la);

            for (final Button button : buttons) {
                hl.addComponent(button);
            }
            vLayout.addComponent(hl);
            hLayout.setSpacing(false);

            return vLayout;
        }

    private HorizontalLayout createHeader() {
        header.setMargin(true);
        header.setSpacing(true);
        header.setWidth(100, UNITS_PERCENTAGE);
        header.addComponent(finalBtn);
       	header.addComponent(newGroupBtn);
       	header.addComponent(deleteGroupBtn);
       	header.setExpandRatio(finalBtn, 1.0f);
       	header.setComponentAlignment(newGroupBtn, Alignment.TOP_RIGHT);
       	header.setComponentAlignment(deleteGroupBtn, Alignment.TOP_RIGHT);
        header.setVisible(true);
        return header;
	}

	public void setSelected(final Item item) {
		if(item == null){
			return;
		}
		this.item = item;
		bindData();
	}
		
	
	
	private void bindData() {
		setObjectId();
		bindObjectId();
		bindName();
		bindLabel();
		bindDescription();
		bindEmail();
		bindActiveStatus();
        bindCreatedOn();
        bindCreatedBy();
        bindModifiedOn();
        bindModifiedBy();
        bindRolesWithView();
        bindSelectors();
        bindUserRightsWithView();
	}

	private void setObjectId() {
		groupObjectId = (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
	}

	private void bindObjectId() {
		objId.setPropertyDataSource(item.getItemProperty(PropertyId.OBJECT_ID));
	}

	private void bindName() {
		name.setPropertyDataSource(item.getItemProperty(PropertyId.NAME));
	}
	
	private void bindLabel() {
		label.setPropertyDataSource(item.getItemProperty(PropertyId.PROP_LABEL));
	}
	
	private void bindDescription() {
		description.setPropertyDataSource(item.getItemProperty(PropertyId.DESCRIPTION));
	}

	private void bindEmail() {
		email.setPropertyDataSource(item.getItemProperty(PropertyId.EMAIL));
	}
	
	private void bindActiveStatus() {
		activeStatus.setPropertyDataSource(item.getItemProperty(PropertyId.ACTIVE));
	}

	private void bindCreatedOn() {
		createdOn.setCaption(Converter.dateTimeToString((org.joda.time.DateTime) item.getItemProperty(PropertyId.CREATED_ON).getValue()));
	}

	private void bindCreatedBy() {
		createdByLink.setCaption(getCreatorName());
		if(isRetrieveGroupAllowed(getCreatorId())) {
			createdByLinkListener.setUser(getCreatorId());
		}
		else {
			createdByLink.setEnabled(false);
		}
	}

	private void bindModifiedOn() {
		modifiedOn.setCaption(Converter.dateTimeToString((org.joda.time.DateTime) item.getItemProperty(PropertyId.LAST_MODIFICATION_DATE).getValue()));
	}

	private void bindModifiedBy() {
		modifiedOnLink.setCaption(getModifierName());
		if(isRetrieveGroupAllowed(getModifierId())) {
			createdByLinkListener.setUser(getModifierId());
		}
		else {
			createdByLink.setEnabled(false);
		}
	}
	
	private void bindRolesWithView() {
		final List<Grant> groupGrants = (List<Grant>) getGrants();
		// don't show any grants if group has no
		if (groupGrants.isEmpty()) {
			if (grantContainer != null) {
				grantContainer.removeAllItems();
			}
		}
		else {
            grantContainer =
                    new POJOContainer<Grant>(Grant.class, PropertyId.XLINK_TITLE, PropertyId.OBJECT_ID,
                        PropertyId.GRANT_ROLE_OBJECT_ID, PropertyId.ASSIGN_ON);
            roles.setContainerDataSource(grantContainer);
            roles.setVisibleColumns(new String[] { PropertyId.XLINK_TITLE });
            roles.setColumnHeaders(ViewConstants.ROLE_COLUMN_HEADERS);
            
            for (final Grant grant : groupGrants) {
				final Reference assignedOn = grant.getProperties().getAssignedOn();
				if (assignedOn == null) {
					grant.getProperties().setAssignedOn(new RoleRef("", ""));
				}
				grantContainer.addPOJO(grant);
			}
		}
	}
	
	private void bindSelectors() {
		final List<Selector> groupSelectors = (List<Selector>) getSelectors();
		int selectorInternalSize = 0;
		int selectorAttributeSize = 0;

		if (groupSelectors.isEmpty()) {
			if (selectorInternalContainer != null) {
				selectorInternalContainer.removeAllItems();
			}
			if (selectorAttributeContainer != null) {
				selectorAttributeContainer.removeAllItems();
			}
		}
		else {
			selectorInternalContainer = 
					new POJOContainer<Selector>(Selector.class, "content", "name", "type");
			selectorsInternal.setContainerDataSource(selectorInternalContainer);
			selectorsInternal.setVisibleColumns(new String[] { "content", "name", PropertyId.NAME });

			selectorAttributeContainer = 
					new POJOContainer<Selector>(Selector.class, "content", "name", "type");
			selectorsAttribute.setContainerDataSource(selectorAttributeContainer);
			selectorsAttribute.setVisibleColumns(new String[] { "content", "name" });
			
			for (Selector selector : groupSelectors) {
				// split selectors by type
				if (selector.getType() == SelectorType.INTERNAL) {
					selectorInternalContainer.addPOJO(selector);
					// get title of internal selector and store that information
					selectorsInternalTitles.put(selector.getContent(), getInternalSelectorTitle(selector));
				}
				else {
					selectorAttributeContainer.addPOJO(selector);
				}
			}
			selectorInternalContainer.sort(new Object[] { "content" }, new boolean[] { true }); // FIXME - sort didn't work
			selectorInternalSize = selectorInternalContainer.size();
			selectorAttributeSize = selectorAttributeContainer.size();
		}
		
		// update label and show number of contained items
		accordion.getTab(tab2).setCaption(ViewConstants.SELECTORS_INTERNAL_LABEL + " (" + selectorInternalSize + ")");
		accordion.getTab(tab3).setCaption(ViewConstants.SELECTORS_ATTRIBUTE_LABEL + " (" + selectorAttributeSize + ")");
	}

	private void bindUserRightsWithView() {
		newGroupBtn.setVisible(isCreateNewGroupAllowed());
		deleteGroupBtn.setVisible(isDeleteGroupAllowed());
		name.setReadOnly(isUpdateGroupNotAllowed());
//		description.setReadOnly(isUpdateGroupNotAllowed());
//		email.setReadOnly(isUpdateGroupNotAllowed());
		// FIXME As long as description and email aren't updatable caused by a escidoc core bug,
		//		 they are set read only.
		description.setReadOnly(true);
		email.setReadOnly(true);
		activeStatus.setReadOnly(isDeactivateGroupNotAllowed());
		if (isUpdateGroupNotAllowed()) {
			tab1.removeComponent(saveCancelButtons);
		}
		roles.setReadOnly(!isCreateUserGroupGrantAllowed() && !isRevokeUserGroupGrantAllowed());
		addRoleBtn.setVisible(isCreateUserGroupGrantAllowed());
		removeRoleBtn.setVisible(isRevokeUserGroupGrantAllowed());
		selectorsInternal.setReadOnly(!isAddUserGroupSelectorsAllowed() && !isRemoveUserGroupSelectorsAllowed());
		addInternalSelectorBtn.setVisible(isAddUserGroupSelectorsAllowed());
		removeInternalSelectorBtn.setVisible(isRemoveUserGroupSelectorsAllowed());
		addAttributeSelectorBtn.setVisible(isAddUserGroupSelectorsAllowed());
		removeInternalSelectorBtn.setVisible(isRemoveUserGroupSelectorsAllowed());
	}
	
    private String getCreatorId() {
        return getCreator().getObjid();
    }

    private String getCreatorName() {
        return getCreator().getXLinkTitle();
    }

    private UserAccountRef getCreator() {
        return (UserAccountRef) item.getItemProperty(PropertyId.CREATED_BY).getValue();
    }

    private String getModifierId() {
        return getModifier().getObjid();
    }

    private String getModifierName() {
        return getModifier().getXLinkTitle();
    }

    private UserAccountRef getModifier() {
        return (UserAccountRef) item.getItemProperty(PropertyId.MODIFIED_BY).getValue();
    }
    
    private String getSelectedItemId() {
    	if (item == null) {
    		return "";
    	}
    	return (String) item.getItemProperty(PropertyId.OBJECT_ID).getValue();
    }
    
    @SuppressWarnings("unchecked")
	private Collection<Grant> getGrants() {
    	return (Collection<Grant>) retrieveResourcesForGroupByType(groupObjectId, RESOURCE_TYPE_GRANT);
    }
    
    @SuppressWarnings("unchecked")
	private Collection<Selector> getSelectors() {
    	return (Collection<Selector>) retrieveResourcesForGroupByType(groupObjectId, RESOURCE_TYPE_SELECTOR);
    }
    
    private String getInternalSelectorTitle(final Selector selector) {
		if (selector != null) {
			String name = selector.getName();
			String content = selector.getContent();
			
			if (name.equals(InternalSelectorName.USER_ACCOUNT.getXmlValue())) {
				try {
					UserAccount ua = userService.getUserById(content);
					if (ua != null) {
						return ua.getXLinkTitle();
					}
				} catch (EscidocClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (name.equals(InternalSelectorName.USER_GROUP.getXmlValue())) {
				try {
					UserGroup ug = groupService.getGroupById(content);
					if (ug != null) {
						return ug.getXLinkTitle();
					}
				} catch (EscidocClientException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return "";
    }
    
    private Collection<?> retrieveResourcesForGroupByType(String groupObjectId, String type) {
		try {
			if (type == RESOURCE_TYPE_GRANT) {
				return groupService.retrieveCurrentGrants(groupObjectId);
			}
			else if (type == RESOURCE_TYPE_SELECTOR) {
				return groupService.getGroupById(groupObjectId).getSelectors();
			}
			
		}
        catch (final InternalClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final TransportException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
        catch (final EscidocClientException e) {
            ModalDialog.show(app.getMainWindow(), e);
            LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
        }
		return Collections.emptyList();
	}
    
    
    // some public getters
    public AdminToolApplication getApp() {
    	return app;
    }
    
    public GroupService getGroupService() {
    	return groupService;
    }
    
    public String getGroupId() {
    	return groupObjectId;
    }
    
    
    // permission checks

    private boolean isRetrieveGroupAllowed(final String userId) {
    	return pdpRequest.isPermitted(ActionIdConstants.RETRIEVE_USER_GROUP, userId);
    }
    
    private boolean isCreateNewGroupAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.CREATE_USER_GROUP);
    }
    
    private boolean isDeactivateGroupNotAllowed() {
        return !pdpRequest.isPermitted(ActionIdConstants.DEACTIVATE_USER_GROUP, getSelectedItemId());
    }

    private boolean isDeleteGroupAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.DELETE_USER_GROUP, getSelectedItemId());
    }
    
    private boolean isUpdateGroupNotAllowed() {
    	return pdpRequest.isDenied(ActionIdConstants.UPDATE_USER_GROUP, getSelectedItemId());
    }
    
    private boolean isRetrieveUserGroupGrantAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.RETRIEVE_USER_GROUP_GRANT, getSelectedItemId());
    }
    
    private boolean isCreateUserGroupGrantAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.CREATE_USER_GROUP_GRANT, getSelectedItemId());
    }
    
    private boolean isRevokeUserGroupGrantAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.REVOKE_USER_GROUP_GRANT, getSelectedItemId());
    }
    
    private boolean isAddUserGroupSelectorsAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.ADD_USER_GROUP_SELECTORS, getSelectedItemId());
    }
    
    private boolean isRemoveUserGroupSelectorsAllowed() {
    	return pdpRequest.isPermitted(ActionIdConstants.REMOVE_USER_GROUP_SELECTORS, getSelectedItemId());
    }
    
	@Override
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();
		if (source.equals(cancelBtn)) {
            discardFields();
            removeAllError();
		}
		else if (source.equals(saveBtn) && isValid()) {
            updateUserGroup();
            commitFields();
            removeAllError();
		}
	}
	
	private void discardFields() {
		name.discard();
		description.discard();
		email.discard();
	}
	
	private void commitFields() {
		name.commit();
		label.commit();
		description.commit();
		email.commit();
	}
	
	private void removeAllError() {
		name.setComponentError(null);
		label.setComponentError(null);
		email.setComponentError(null);
	}
	
	private void updateUserGroup() {
		try {
			final UserGroup updatedUserGroup =
					groupService.update(getSelectedItemId(), (String) name.getValue(), (String) description.getValue(), (String) email.getValue());
			if ( activeStatus.isModified() ) {
				changeState(updatedUserGroup);
			}
			// TODO seperate methods for updating selectors analogue to OUs for user accounts?
//			updateSelectors();
			final UserGroup reloadedUserGroup = groupService.retrieve(getSelectedItemId());
			updateView(reloadedUserGroup);
		}
		catch (final EscidocException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		catch (final InternalClientException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		catch (final TransportException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		catch (final EscidocClientException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
	}
	
	private UserGroup deleteGroup() throws EscidocClientException {
		return groupService.delete(getSelectedItemId());
	}
	
	private void changeState(final UserGroup updatedUserGroup) throws EscidocException, InternalClientException, TransportException {
        final Object value = activeStatus.getPropertyDataSource().getValue();
        if (!(value instanceof Boolean)) {
            return;
        }

        if ((!(Boolean) activeStatus.getPropertyDataSource().getValue())) {
            groupService.activate(updatedUserGroup);
        }
        else {
            groupService.deactivate(updatedUserGroup);
        }
	}
	
	private void updateView(UserGroup updatedUserGroup) {
		try {
			// group map must be updated, because app.showGroup() gets cached group from map
			groupService.findAll();
		} catch (EscidocClientException e) {
			ModalDialog.show(mainWindow, e);
			LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
		}
		// update group container of ListView
		app.getGroupView().getGroupList().updateGroup(updatedUserGroup);
		// update all view components
		app.showGroup(updatedUserGroup);
		showMessage("User Group »" + updatedUserGroup.getProperties().getName() + "« is updated.");
	}
	
	private void showMessage(String message) {
		app.getMainWindow().showNotification(
	            new Notification("Info", message, Notification.TYPE_TRAY_NOTIFICATION));
	}
	
	
	// click listener
	
	private class NewGroupListener implements Button.ClickListener {
		private static final long serialVersionUID = -7825175731887685295L;

		@Override
		public void buttonClick(ClickEvent event) {
			((GroupView) getParent().getParent()).showAddView();
		}
	}

	private class DeleteGroupListener implements Button.ClickListener {
		

		@Override
		public void buttonClick(ClickEvent event) {
        	// open confirmation dialog
			ConfirmDialog.show(mainWindow, "Delete User Group?", new ConfirmDialog.Listener() {
				
				@Override
				public void onClose(ConfirmDialog dialog) {
					if (dialog.isConfirmed()) {
						try {
	                		final UserGroup deletedUserGroup = deleteGroup();
	                		((GroupView) getParent().getParent()).remove(deletedUserGroup);
	                		final UserAccount currentUser = userService.getCurrentUser();
	                		LOG.info("User Group »" + deletedUserGroup.getProperties().getName() + "« deleted by " + currentUser.getProperties().getName());
	                		showMessage("User Group »" + deletedUserGroup.getProperties().getName() + "« is deleted.");
						}
						catch (final InternalClientException e) {
							setComponentError(new SystemError(e.getMessage()));
							LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
						}
						catch (final TransportException e) {
							setComponentError(new SystemError(e.getMessage()));
							LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
						}
						catch (final EscidocException e) {
							LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
							setComponentError(new SystemError(e.getMessage()));
						}
						catch (final EscidocClientException e) {
							setComponentError(new SystemError(e.getMessage()));
							LOG.error(ViewConstants.AN_UNEXPECTED_ERROR_OCCURED_SEE_LOG_FOR_DETAILS, e);
							ModalDialog.show(mainWindow, e);
						}
					}
				}
			});
        }
	}
	
	private class AddRoleListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			((GroupView) getParent().getParent()).showAddRoleToGroupView();
		}
	}
	
	private class GroupSummaryViewListener implements Button.ClickListener {

		@Override
		public void buttonClick(ClickEvent event) {
			try {
				((GroupView) getParent().getParent()).showSummaryView(groupService.getGroupById(groupObjectId));
			} catch (EscidocClientException e) {
				LOG.error("An unexpected error occured! See LOG for details.", e);
				ModalDialog.show(mainWindow, e);
			}
		}
		
	}
	
	// column generators
	
	private class InternalSelectorReadableNameColumnGenerator implements ColumnGenerator {
		
		@Override
		public Component generateCell(Table source, Object itemId, Object columnId) {
			// get selector and its title from already prepared map selectorsInternalTitles
			Selector selector = (Selector) itemId;
			if (selector != null) {
				return new Label(selectorsInternalTitles.get(selector.getContent()));
			}
					
			return null;
		}
		
	}
	
	private class InternalSelectorHighlightStyleGenerator implements CellStyleGenerator {

		@Override
		public String getStyle(Object itemId, Object propertyId) {
			// if title is empty, add style v-table-row-deadReference
			if (selectorsInternalTitles.get(((Selector) itemId).getContent()).equals("")) {
				return "deadReference";
			}
			return null;
		}
		
	}

	// control form
	
	private boolean isValid() {
		boolean isValid = true;
		isValid = EmptyFieldValidator.isValid(name, "Please enter a " + ViewConstants.NAME_ID);
		isValid &= EmptyFieldValidator.isValid(label, "Please enter a " + ViewConstants.LABEL);
		return isValid;
	}

}
