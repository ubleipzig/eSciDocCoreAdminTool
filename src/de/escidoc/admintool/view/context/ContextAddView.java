package de.escidoc.admintool.view.context;

import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.service.ContextService;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.OrgUnitAddView;
import de.escidoc.admintool.view.util.LayoutHelper;
import de.escidoc.core.resources.om.context.AdminDescriptors;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

@SuppressWarnings("serial")
public class ContextAddView extends CustomComponent implements ClickListener {
	 private final Logger log =
	        LoggerFactory.getLogger(ContextAddView.class);
    private final AdminToolApplication app;
    private final ContextListView contextListView;
    private final ContextService contextService;
    private AdminDescriptorsAddView adminDescriptorsAddView;
    private OrgUnitAddView orgUnitAddView;
    private final OrgUnitService orgUnitService;
    private TextField nameField;
    private TextField descriptionField;
    private TextField typeField;
    
    private ObjectProperty nameProperty;
    private ObjectProperty descriptionProperty;
	private ObjectProperty typeProperty;
	private Object[][] orgUnits;
    
    public ContextAddView(final AdminToolApplication app,
        final ContextListView contextListView,
        final ContextService contextService, final OrgUnitService orgUnitService) {
        this.app = app;
        this.contextListView = contextListView;
        this.contextService = contextService;
        this.orgUnitService = orgUnitService;
        init();
    }

    private ObjectProperty mapBinding(String initText, TextField tf){
    	ObjectProperty op = new ObjectProperty(initText, String.class);
        tf.setPropertyDataSource(op);
    	return op; 
    }

    private void init(){
    	Panel panel = new Panel();
    	FormLayout form = new FormLayout();
    	panel.setContent(form);
    	form.setSpacing(false);
    	panel.setCaption("Add a new Context");
    	nameField = new TextField();
        nameField.setWidth("400px");
    	panel.addComponent(LayoutHelper.create(ViewConstants.NAME_LABEL, nameField, "150px", true));
        nameProperty = mapBinding("", nameField);

        descriptionField = new TextField();
        descriptionField.setWidth("400px");
        descriptionField.setRows(3);
    	panel.addComponent(LayoutHelper.create(ViewConstants.DESCRIPTION_LABEL, descriptionField, "150px", 80, true));
    	descriptionProperty = mapBinding("", descriptionField);
        
        typeField = new TextField();
        typeField.setWidth("400px");
    	panel.addComponent(LayoutHelper.create(ViewConstants.TYPE_LABEL, typeField, "150px", true));
    	typeProperty = mapBinding("", typeField);

        final ListSelect select = new ListSelect();
    	panel.addComponent(LayoutHelper.create(ViewConstants.ORGANIZATION_UNITS_LABEL, select, "150px", 100, true));
        select.setRows(5);
        select.setWidth("400px");
        select.setNullSelectionAllowed(true);
        select.setMultiSelect(true);
        select.setImmediate(true);
        select.getValue();
        
        Button addOrgUnitButton = new Button("Add");

        addOrgUnitButton.addListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				final Window openTreeButtonWindow = new Window(ViewConstants.ORGANIZATION_UNITS_LABEL); //$NON-NLS-1$
				openTreeButtonWindow.setModal(true);
		    	final OrgUnitTree tree = new OrgUnitTree(openTreeButtonWindow);
				/* Set window size. */
				openTreeButtonWindow.setHeight("650px"); //$NON-NLS-1$
				openTreeButtonWindow.setWidth("550px"); //$NON-NLS-1$
				openTreeButtonWindow.addComponent(tree);
		    	Button okButton = new Button("OK");
		    	okButton.addListener(new Button.ClickListener() {
					
					public void buttonClick(ClickEvent event) {
						Object o = tree.getSelectedItems();
						o.getClass().toString();
						if (o instanceof HashSet){
							HashSet<String> set = (HashSet<String>)o;
							for (String str : set){
								select.addItem(str);
							}
						} else if (o instanceof Set){
							Set<String> set = (Set<String>)o;
							for (String str : set){
								select.addItem(str);
							}
						} else if (o instanceof Object){
							select.addItem(o);
						}
						((Window) openTreeButtonWindow.getParent()).removeWindow(openTreeButtonWindow);
					}
				});
		    	Button cancelButton = new Button("Cancel");
		    	cancelButton.addListener(new Button.ClickListener() {
					
					public void buttonClick(ClickEvent event) {
						((Window) openTreeButtonWindow.getParent()).removeWindow(openTreeButtonWindow);
					}
				});
		    	
		    	HorizontalLayout hor = LayoutHelper.create("", "", okButton, cancelButton, "10px", false);
		    	openTreeButtonWindow.addComponent(hor);
				getApplication().getMainWindow().addWindow(openTreeButtonWindow);
			}
		});
        Button removeOrgUnitButton = new Button("Remove");
        panel.addComponent(LayoutHelper.create("", "", addOrgUnitButton, removeOrgUnitButton, "150px", "0px", false));

 		Accordion accordion = new Accordion();
		// Have it take all space available in the layout.
		accordion.setSizeFull();
		accordion.setWidth("800px");
		panel.addComponent(LayoutHelper.create("Admin Descriptors", accordion, "150px", 800, true));		
        panel.addComponent(addFooter());
    	setCompositionRoot(panel);
    }
    
    
//nameField.addValidator(new EmptyStringValidator("Name can not be empty."));
//            nameField.setRequiredError("Please enter a " + ViewConstants.NAME_ID);
//  descriptionField.addValidator(new EmptyStringValidator("Description can not be empty."));
//  descriptionField.setRequiredError("Please enter a "+ ViewConstants.DESCRIPTION_ID);
//    typeField.addValidator(new EmptyStringValidator(ViewConstants.TYPE_LABEL
//            + " can not be empty."));
//        typeField.setRequiredError("Please enter a " + ViewConstants.TYPE_ID);
    

//    private ContextAddView addOrgUnits() {
//        orgUnitAddView = new OrgUnitAddView(this, orgUnitService);
//        return this;
//    }

    private ContextAddView addAdminDescriptos() {
        adminDescriptorsAddView = new AdminDescriptorsAddView(this);
        return this;
    }


    private HorizontalLayout footer;

    private final Button save = new Button("Save", (ClickListener) this);

    private final Button cancel = new Button("Cancel", (ClickListener) this);

    private HorizontalLayout addFooter() {
        footer = new HorizontalLayout();
        footer.setSpacing(true);

        footer.addComponent(save);
        footer.addComponent(cancel);

//        setFooter(footer);
        return footer;
    }

    public void buttonClick(final ClickEvent event) {
        final Button clickedButton = event.getButton();
        if (clickedButton == save) {
            save();
        }
        else if (clickedButton == cancel) {
            clear();
        }
        else {
            throw new RuntimeException("Unknown Button: " + clickedButton);
        }
    }

    private void save() {
        /*
    	if (isValid()) {
            try {
                final String contextName = enteredName();
                final String contextDescription = enteredDescription();
                final String contextType = enteredType();
                final AdminDescriptors adminDescriptors =
                    enteredAdminDescriptors();
                final OrganizationalUnitRefs selectedOrgUnitRefs =
                    enteredOrgUnits();

                final Context newContext =
                    contextService.create(contextName, contextDescription,
                        contextType, selectedOrgUnitRefs, adminDescriptors);
                contextListView.addContext(newContext);
                contextListView.sort();
                contextListView.select(newContext.getObjid());
                setComponentError(null);
            }
            catch (final EscidocException e) {
                System.out.println("root cause: "
                    + ExceptionUtils.getRootCauseMessage(e));

                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final InternalClientException e) {
                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final TransportException e) {
                setComponentError(new UserError(e.getMessage()));
                e.printStackTrace();
            }
            catch (final ParserConfigurationException e) {
                setComponentError(new SystemError(e.getMessage()));
                e.printStackTrace();
            }
        }
        else {
            setValidationVisible(true);
        }
        */
    }

    private String enteredType() {
//        return (String) getField(ViewConstants.TYPE_ID).getValue();
    	return "";
    }

    private AdminDescriptors enteredAdminDescriptors()
        throws ParserConfigurationException {
        return adminDescriptorsAddView.getAdminDescriptors();
    }

    private OrganizationalUnitRefs enteredOrgUnits() {
        return orgUnitAddView.getSelectedOrgUnits();
    }

    private String enteredName() {
    	return "";
//       return (String) getField(ViewConstants.NAME_ID).getValue();
    }

    private String enteredDescription() {
    	return "";
//      return (String) getField(ViewConstants.DESCRIPTION_ID).getValue();
    }

    public void clear() {
//        getField(ViewConstants.NAME_ID).setValue("");
//        getField(ViewConstants.DESCRIPTION_ID).setValue("");
//        getField(ViewConstants.TYPE_ID).setValue("");
        adminDescriptorsAddView.clear();
        setComponentError(null);
    }
}