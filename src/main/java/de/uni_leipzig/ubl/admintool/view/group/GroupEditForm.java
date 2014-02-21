package de.uni_leipzig.ubl.admintool.view.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.POJOContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.PdpRequest;
import de.escidoc.admintool.view.context.LinkClickListener;
import de.escidoc.admintool.view.navigation.ActionIdConstants;
import de.escidoc.admintool.view.util.Converter;
import de.escidoc.core.resources.aa.useraccount.Grant;
import de.escidoc.core.resources.common.reference.UserAccountRef;
import de.uni_leipzig.ubl.admintool.service.internal.GroupService;

public class GroupEditForm extends CustomComponent implements ClickListener {

	private static final Logger LOG = LoggerFactory.getLogger(GroupEditForm.class);
	
	final AdminToolApplication app;
	
	final GroupService groupService;
	
	private final PdpRequest pdpRequest;
	
	private Item item;
	
	String groupObjectId;
	
	POJOContainer<Grant> grantContainer;
	
	// fields
    private final Label objId = new Label();
    private TextField name;
    private CheckBox activeStatus;
    private final Label modifiedOn = new Label();
    private Button modifiedOnLink;
    private LinkClickListener modifiedOnLinkListener;
    private final Label createdOn = new Label(); 
    private Button createdByLink;
    private LinkClickListener createdByLinkListener;
    
	
	// components
	private final Window mainWindow;
	
	private final Panel panel = new Panel();
	
	private final Label header = new Label();
	
	public GroupEditForm(final AdminToolApplication app, final GroupService groupService, final PdpRequest pdpRequest) {
		Preconditions.checkNotNull(app, "app is null: %s", app);
		Preconditions.checkNotNull(groupService, "groupService is null: %s", groupService);
		Preconditions.checkNotNull(pdpRequest, "pdpRequest is null: %s", pdpRequest);
		
		this.app = app;
		this.groupService = groupService;
		this.pdpRequest = pdpRequest;
		mainWindow = app.getMainWindow();
	}
	
	private final void init() {
		
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
		bindActiveStatus();
        bindCreatedOn();
        bindCreatedBy();
        bindModifiedOn();
        bindModifiedBy();
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

	private void bindActiveStatus() {
		activeStatus.setPropertyDataSource(item.getItemProperty(PropertyId.ACTIVE));
	}

	private void bindCreatedOn() {
		createdOn.setCaption(Converter.dateTimeToString((org.joda.time.DateTime) item.getItemProperty(PropertyId.CREATED_ON).getValue()));
	}

	private void bindCreatedBy() {
		createdByLink.setCaption(getCreatorName());
		if(isRetrieveUserPermitted(getCreatorId())) {
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
		if(isRetrieveUserPermitted(getModifierId())) {
			createdByLinkListener.setUser(getModifierId());
		}
		else {
			createdByLink.setEnabled(false);
		}
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

    // permission checks
    private boolean isRetrieveUserPermitted(final String userId) {
    	return pdpRequest.isPermitted(ActionIdConstants.RETRIEVE_USER_ACCOUNT, userId);
    }
    
    
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub

	}

}
