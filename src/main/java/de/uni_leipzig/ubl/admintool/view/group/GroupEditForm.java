package de.uni_leipzig.ubl.admintool.view.group;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.ModalDialog;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.context.LinkClickListener;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.util.Constants;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.aa.usergroup.Selector;
import de.escidoc.core.resources.common.reference.Reference;
import de.escidoc.core.resources.common.reference.RoleRef;
import de.escidoc.core.resources.common.reference.UserAccountRef;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

@SuppressWarnings("serial")
public class GroupEditForm extends CustomComponent implements ClickListener {

	private static final Logger LOG = LoggerFactory.getLogger(GroupEditForm.class);
	
    private static final int LABEL_WIDTH = 100;

    private static final int LABEL_HEIGHT = 15;
    
    private static final int ROLE_LIST_HEIGHT = 50;

    private static final int SELECTOR_LIST_HEIGHT = 200;

    private static final String RESOURCE_TYPE_GRANT = "grant";
    
    private static final String RESOURCE_TYPE_SELECTOR = "selector";
    
    final AdminToolApplication app;
	
	final GroupService groupService;
	
	private final PdpRequest pdpRequest;
	
	private Item item;
	
	String groupObjectId;
	
	POJOContainer<Grant> grantContainer;
	
	POJOContainer<Selector> selectorContainer;
	
	// fields
    private final Label objId = new Label();
    private TextField name; 		// required
    private TextField label;		// required
    private TextField description;
    private TextField email;
    private CheckBox activeStatus;
    final Table roles = new Table();
    final Table selectors = new Table();
    private final Label modifiedOn = new Label();
    private Button modifiedOnLink;
    private LinkClickListener modifiedOnLinkListener;
    private final Label createdOn = new Label(); 
    private Button createdByLink;
    private LinkClickListener createdByLinkListener;
    
	
	// components
	private final Window mainWindow;
	
	private final Panel panel = new Panel();
	
	private final HorizontalLayout header = new HorizontalLayout();
	
	private final HorizontalLayout footer = new HorizontalLayout();
	
	private final FormLayout form = new FormLayout();
	
	private final Button newGroupBtn = new Button(ViewConstants.NEW, new NewGroupListener());
	
	private final Button deleteGroupBtn = new Button(ViewConstants.DELETE, new DeleteGroupListener());
	
	private final Button saveBtn = new Button(ViewConstants.SAVE_LABEL, this);
	
	private final Button cancelBtn = new Button(ViewConstants.CANCEL, this);
	
    private final Button addRoleBtn = new Button();

    private final Button removeRoleBtn = new Button();
    
    private final Button addSelectorBtn = new Button();
    
    private final Button editSelectorBtn = new Button();
    
    private final Button removeSelectorBtn = new Button(); 

    public GroupEditForm(final AdminToolApplication app, final GroupService groupService, final PdpRequest pdpRequest) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
		
		this.app = app;
		this.groupService = groupService;
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
        
        addVerticalSpace();
        addRoles();
        addVerticalSpace();
        addSelectors();
        
        addFooter();
}
	
    private void addVerticalSpace() {
        panel.addComponent(new Label("<br/>", Label.CONTENT_XHTML));
    }

	private void addName() {
		name = new TextField();
		name.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		name.setWidth(ViewConstants.FIELD_WIDTH);
		name.setWriteThrough(false);
		form.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, name, 100, true));
	}
	
	private void addLabel() {
		label = new TextField();
		label.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		label.setWidth(ViewConstants.FIELD_WIDTH);
		label.setWriteThrough(false);
		label.setReadOnly(true);
		form.addComponent(LayoutHelper.create(ViewConstants.LABEL, label, 100, true));
	}
	
	private void addDescription() {
		description = new TextField();
		description.setWidth(ViewConstants.FIELD_WIDTH);
		description.setRows(ViewConstants.DESCRIPTION_ROWS);
		description.setMaxLength(ViewConstants.MAX_DESC_LENGTH);
		description.setWriteThrough(false);
		form.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL, description, LABEL_WIDTH, 80, false));
	}
	
	private void addEmail() {
		email = new TextField();
		email.setMaxLength(ViewConstants.MAX_TITLE_LENGTH);
		email.setWidth(ViewConstants.FIELD_WIDTH);
		email.setWriteThrough(false);
		form.addComponent(LayoutHelper.create(ViewConstants.EMAIL_LABEL, email, 100, true));
	}
	
	private void addObjectId() {
		form.addComponent(LayoutHelper.create(ViewConstants.OBJECT_ID_LABEL, objId, 100, false));
	}
	
	private void addCreated() {
		createdByLink = new Button();
		createdByLink.setStyleName(BaseTheme.BUTTON_LINK);
		createdByLinkListener = new LinkClickListener(app);
		createdByLink.addListener(createdByLinkListener);
		form.addComponent(LayoutHelper.create("Created", "by", createdOn, createdByLink, LABEL_WIDTH, LABEL_HEIGHT, false));
	}
	
	private void addModified() {
		modifiedOnLink = new Button();
		modifiedOnLink.setStyleName(BaseTheme.BUTTON_LINK);
		modifiedOnLinkListener = new LinkClickListener(app);
		modifiedOnLink.addListener(modifiedOnLinkListener);
		form.addComponent(LayoutHelper.create("Modified", "by", modifiedOn, modifiedOnLink, LABEL_WIDTH, LABEL_HEIGHT, false));
	}
	
	private void addActiveStatus() {
		activeStatus = new CheckBox();
		activeStatus.setWriteThrough(false);
		form.addComponent(LayoutHelper.create(ViewConstants.ACTIVE_STATUS, activeStatus, LABEL_WIDTH, false));
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
        form.addComponent(rolesComponent);
	}
	
	private void addSelectors() {
		selectors.setHeight(SELECTOR_LIST_HEIGHT, UNITS_PIXELS);
        selectors.setWidth(ViewConstants.FIELD_WIDTH);
        selectors.setSelectable(true);
        selectors.setNullSelectionAllowed(true);
        selectors.setMultiSelect(true);
        selectors.setImmediate(true);
        selectors.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
        addSelectorBtn.setCaption(ViewConstants.ADD);
        addSelectorBtn.setStyleName(Reindeer.BUTTON_SMALL);
        editSelectorBtn.setCaption(ViewConstants.EDIT);
        editSelectorBtn.setStyleName(Reindeer.BUTTON_SMALL);
        removeSelectorBtn.setCaption(ViewConstants.REMOVE);
        removeSelectorBtn.setStyleName(Reindeer.BUTTON_SMALL);
        // create selector component
        final VerticalLayout selectorsComponent = 
        		createLayout(ViewConstants.SELECTORS_LABEL, selectors, ViewConstants.DEFAULT_LABEL_WIDTH, SELECTOR_LIST_HEIGHT, false, new Button[] {
        				addSelectorBtn, editSelectorBtn, removeSelectorBtn
        		});
        form.addComponent(selectorsComponent);
	}
	
	private void addFooter() {
		footer.setWidth(100, UNITS_PERCENTAGE);
		final HorizontalLayout hl = new HorizontalLayout();
		hl.addComponent(saveBtn);
		hl.addComponent(cancelBtn);
		footer.addComponent(hl);
		footer.setComponentAlignment(hl, Alignment.MIDDLE_RIGHT);
		form.addComponent(footer);
	}

	private void configureLayout() {
		setCompositionRoot(panel);
		panel.setStyleName(Reindeer.PANEL_LIGHT);
		panel.setCaption(ViewConstants.EDIT_GROUP_VIEW_CAPTION);
		panel.setContent(form);
		
        form.setSpacing(false);
        form.setWidth(530, UNITS_PIXELS);
        form.addComponent(createHeader());
	}

    private VerticalLayout createLayout(
            final String rolesLabel, final Table table, final int labelWidth, final int roleListHeight, final boolean b,
            final Button[] buttons) {

            final HorizontalLayout hLayout = new HorizontalLayout();
            hLayout.setHeight(roleListHeight + Constants.PX);
            hLayout.addComponent(new Label(" "));

            final Label textLabel =
                new Label(Constants.P_ALIGN_RIGHT + rolesLabel + "   " + Constants.P, Label.CONTENT_XHTML);
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
       	header.addComponent(newGroupBtn);
       	header.addComponent(deleteGroupBtn);        	
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
		// TODO
		final List<Selector> groupSelectors = (List<Selector>) getSelectors();
		if (groupSelectors.isEmpty()) {
			if (selectorContainer != null) {
				selectorContainer.removeAllItems();
			}
		}
		else {
			selectorContainer = 
					new POJOContainer<Selector>(Selector.class, "content", "name", "type");
			selectors.setContainerDataSource(selectorContainer);
			selectors.setVisibleColumns(new String[] { "content", "name" });
			
			for (Selector selector : groupSelectors) {
				selectorContainer.addPOJO(selector);
			} 
		}
	}

	private void bindUserRightsWithView() {
		newGroupBtn.setVisible(isCreateNewGroupAllowed());
		deleteGroupBtn.setVisible(isDeleteGroupAllowed());
		name.setReadOnly(isUpdateGroupNotAllowed());
		description.setReadOnly(isUpdateGroupNotAllowed());
		email.setReadOnly(isUpdateGroupNotAllowed());
		activeStatus.setReadOnly(isDeactivateGroupNotAllowed());
		if (isUpdateGroupNotAllowed()) {
			form.removeComponent(footer);
		}
		// TODO grants
		// TODO selectors
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
    
    private Collection<?> retrieveResourcesForGroupByType(String groupObjectId, String type) {
		try {
			if (type == RESOURCE_TYPE_GRANT) {
				return groupService.retrieveCurrentGrants(groupObjectId);
			}
			else if (type == RESOURCE_TYPE_SELECTOR) {
//				return groupService.retrieveCurrentSelectors(groupObjectId);
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
    
	@Override
	public void buttonClick(ClickEvent event) {
		final Button source = event.getButton();
		if (source.equals(cancelBtn)) {
            discardFields();
            removeAllError();
		}
		else if (source.equals(saveBtn) && isValid()) {
			// TODO do something with sense here
			mainWindow.showNotification("SAVE CLICKED");
//            updateUserGroup();
//            commitFields();
//            removeAllError();
//            showMessage();
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
	
	// click listener
	
	private class NewGroupListener implements Button.ClickListener {
		private static final long serialVersionUID = -7825175731887685295L;

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO do something with sense here
			mainWindow.showNotification("New Group ... will be implemented");
		}
	}

	private class DeleteGroupListener implements Button.ClickListener {
		

		@Override
		public void buttonClick(ClickEvent event) {
			// TODO do something with sense here
			mainWindow.showNotification("Delete Group ... will be implemented");
		}
	}
	
	// control form
	
	private boolean isValid() {
		boolean isValid = true;
		isValid = EmptyFieldValidator.isValid(name, "Please enter a " + ViewConstants.NAME_ID);
		isValid &= EmptyFieldValidator.isValid(label, "Please enter a " + ViewConstants.LABEL);
		isValid &= EmptyFieldValidator.isValid(email, "Please enter a " + ViewConstants.EMAIL_LABEL);
		return isValid;
	}

}
