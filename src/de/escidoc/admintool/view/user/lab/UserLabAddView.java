package de.escidoc.admintool.view.user.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.terminal.UserError;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.UserService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.aa.useraccount.UserAccount;

public class UserLabAddView extends CustomComponent implements ClickListener {
	private static final long serialVersionUID = 3007285643463919742L;
	 private static final Logger log =
	        LoggerFactory.getLogger(UserLabAddView.class);
    private final UserLabListView userLabList;
    private final UserService userService;
    private HorizontalLayout footer;
    private final Button save = new Button("Save", (ClickListener) this);
    private final Button cancel = new Button("Cancel", (ClickListener) this);
    private TextField nameField; 
    private TextField loginNameField;
    private ObjectProperty nameProperty;
    private ObjectProperty loginNameProperty;
    
    public UserLabAddView(final AdminToolApplication app,
        final UserLabListView userLabList, final UserService userService) {
        this.userLabList = userLabList;
        this.userService = userService;
        init();
    }

    public void init(){
    	Panel panel = new Panel();
    	FormLayout form = new FormLayout();
    	panel.setContent(form);
    	form.setSpacing(false);
    	panel.setCaption("Add a new User Account");
    	panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, nameField = new TextField(), "100px", true));
        nameProperty = new ObjectProperty("", String.class);
        nameField.setPropertyDataSource(nameProperty);
       
        panel.addComponent(LayoutHelper.create(ViewConstants.LOGIN_NAME_LABEL, loginNameField = new TextField(), "100px", true));
        
        loginNameProperty = new ObjectProperty("", String.class);
        loginNameField.setPropertyDataSource(loginNameProperty);
        panel.addComponent(addFooter());
    	setCompositionRoot(panel);
    }

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);
        footer.addComponent(LayoutHelper.create(save));
        footer.addComponent(LayoutHelper.create(cancel));
        footer.setVisible(true);
        return footer;
    }
    
    public void buttonClick(final ClickEvent event) {
        final Button source = event.getButton();

        if (source == cancel) {
        	nameField.setValue("");
        	loginNameField.setValue("");
        }
        else if (source == save) {
        	boolean valid = true;
        	valid = EmptyFieldValidator.isValid(nameField, "Please enter a " + ViewConstants.NAME_ID);
        	valid &= (EmptyFieldValidator.isValid(loginNameField, "Please enter a " + ViewConstants.LOGIN_NAME_ID));
        	
        	if(valid){
        		nameField.setComponentError(null);
        		loginNameField.setComponentError(null);
        		
                try {
                    final UserAccount createdUserAccount =
                        userService.create((String)nameProperty.getValue(), (String)loginNameProperty.getValue());
                    userLabList.addUser(createdUserAccount);
                    userLabList.select(createdUserAccount);
                	nameField.setValue("");
                	loginNameField.setValue("");
                } catch (final EscidocException e) {
                	String error = "A user with login name "
                        + (String)nameProperty.getValue()+ " already exist.";
                	
                    loginNameField.setComponentError(new UserError(error));
                    e.printStackTrace();
                	log.error(error, e);
                }
                catch (final InternalClientException e) {
                	log.error("An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
                catch (final TransportException e) {
                	log.error("An unexpected error occured! See log for details.", e);
                    e.printStackTrace();
                }
            }
        }
    }
}