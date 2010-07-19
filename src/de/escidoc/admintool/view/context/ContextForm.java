//package de.escidoc.admintool.view.context;
//
//import java.util.Arrays;
//import java.util.List;
//
//import com.vaadin.data.Item;
//import com.vaadin.data.util.ObjectProperty;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Field;
//import com.vaadin.ui.Form;
//import com.vaadin.ui.GridLayout;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.Tree;
//import com.vaadin.ui.Button.ClickEvent;
//import com.vaadin.ui.Button.ClickListener;
//
//import de.escidoc.admintool.app.AdminToolApplication;
//import de.escidoc.admintool.service.ContextService;
//import de.escidoc.admintool.view.ViewConstants;
//import de.escidoc.core.client.exceptions.EscidocException;
//import de.escidoc.core.client.exceptions.InternalClientException;
//import de.escidoc.core.client.exceptions.TransportException;
//import de.escidoc.core.resources.om.context.Context;
//
//public class ContextForm extends Form implements ClickListener {
//
//    private static final long serialVersionUID = 9134021803564933668L;
//
//    private Context context;
//
//    private final AdminToolApplication app;
//
//    private final ContextService service;
//
//    public ContextForm(final AdminToolApplication app,
//        final ContextService service) {
//        this.app = app;
//        this.service = service;
//        buildUI();
//    }
//
//    // TODO refactor this method
//    public void setSelected(final Context context) {
//        this.context = context;
//        nameField.setPropertyDataSource(new ObjectProperty(context
//            .getProperties().getName(), String.class));
//        objectId.setValue(context.getObjid());
//        descriptionField.setValue(context.getProperties().getDescription());
//        descriptionField.setPropertyDataSource(new ObjectProperty(context
//            .getProperties().getDescription(), String.class));
//    }
//
//    public void setSelected(final Item item) {
//        // nameField.setPropertyDataSource(new ObjectProperty(context
//        // .getProperties().getName(), String.class));
//        // objectId.setValue(context.getObjid());
//        // descriptionField.setValue(context.getProperties().getDescription());
//        // descriptionField.setPropertyDataSource(new ObjectProperty(context
//        // .getProperties().getDescription(), String.class));
//
//        if (item != getItemDataSource()) {
//            this.setItemDataSource(item);
//        }
//        showFooter();
//        setReadOnly(true);
//    }
//
//    public void showFooter() {
//        footer.setVisible(true);
//    }
//
//    private GridLayout layout;
//
//    private void buildUI() {
//        setValidationVisible(true);
//        layout = new GridLayout(10, 10);
//        layout.setMargin(true, false, false, true);
//        layout.setSpacing(true);
//
//        setLayout(layout);
//        footer = createHeader();
//        setFooter(footer);
//
//        name().objectId().description();// .orgUnits();
//
//        setWriteThrough(false);
//        setInvalidCommitted(false);
//        setImmediate(true);
//    }
//
//    private TextField nameField;
//
//    private ContextForm name() {
//        nameField = new TextField(ViewConstants.NAME_LABEL);
//        nameField.setWidth("400px");
//        nameField.setRequired(true);
//        nameField.setRequiredError("Name is required");
//        nameField.setImmediate(true);
//        addField(ViewConstants.NAME_ID, nameField);
//
//        return this;
//    }
//
//    private ContextForm objectId() {
//        objectId = new Label();
//        layout.addComponent(objectId, 2, 1);
//        return this;
//    }
//
//    private ContextForm description() {
//        descriptionField = new TextField(ViewConstants.DESCRIPTION_LABEL);
//        descriptionField.setWidth("400px");
//        descriptionField.setReadOnly(false);
//        descriptionField.setRequired(true);
//        descriptionField.setRequiredError("Description is required");
//
//        addField(ViewConstants.DESCRIPTION_ID, descriptionField);
//        return this;
//    }
//
//    /*
//     * Override to get control over where fields are placed.
//     */
//    @Override
//    protected void attachField(final Object propertyId, final Field field) {
//        if (propertyId.equals(ViewConstants.NAME_ID)) {
//            layout.addComponent(nameField, 0, 1);
//        }
//        else if (propertyId.equals(ViewConstants.DESCRIPTION_ID)) {
//            layout.addComponent(descriptionField, 0, 3);
//        }
//    }
//
//    private HorizontalLayout header;
//
//    private final Button save = new Button("Save", (ClickListener) this);
//
//    private final Button cancel = new Button("Cancel", (ClickListener) this);
//
//    private Field descriptionField;
//
//    private Label objectId;
//
//    private HorizontalLayout createHeader() {
//        header = new HorizontalLayout();
//        header.setMargin(true);
//        header.setSpacing(true);
//        header.addComponent(save);
//        header.addComponent(cancel);
//
//        header.setVisible(true);
//        return header;
//    }
//
//    public void buttonClick(final ClickEvent event) {
//        final Button clickedButton = event.getButton();
//        if (clickedButton == save) {
//            save();
//        }
//        else if (clickedButton == cancel) {
//            app.getMainWindow().showNotification(
//                "Cancel? " + context.getProperties().getName());
//            discard();
//        }
//        else {
//            throw new RuntimeException("Unknown Button " + clickedButton);
//        }
//    }
//
//    private void save() {
//        if (isValid()) {
//            app.getMainWindow().showNotification("Saving... ");
//
//            // final Set<String> selectedOrgUnits =
//            // (Set<String>) orgUnitTree.getValue();
//            //
//            // for (final String string : selectedOrgUnits) {
//            // System.out.println(string);
//            // }
//
//            try {
//                context =
//                    service.update("", (String) getField(ViewConstants.NAME_ID)
//                        .getValue(), (String) getField(
//                        ViewConstants.DESCRIPTION_ID).getValue());
//                commit();
//                app.getMainWindow().showNotification("Saved");
//            }
//            catch (final EscidocException e) {
//
//                e.printStackTrace();
//            }
//            catch (final InternalClientException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            catch (final TransportException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private Tree orgUnitTree;
//
//    private ContextForm orgUnits() {
//        orgUnitTree = new Tree(ViewConstants.ORGANIZATION_UNITS_LABEL);
//        // Set multiselect mode
//        orgUnitTree.setMultiSelect(true);
//        orgUnitTree.setSelectable(true);
//        orgUnitTree.setImmediate(true);
//
//        /* Add planets as root items in the tree. */
//        for (final Object[] planet2 : planets) {
//            final String planet = (String) (planet2[0]);
//            orgUnitTree.addItem(planet);
//
//            if (planet2.length == 1) {
//                // The planet has no moons so make it a leaf.
//                orgUnitTree.setChildrenAllowed(planet, false);
//            }
//            else {
//                // Add children (moons) under the planets.
//                for (int j = 1; j < planet2.length; j++) {
//                    final String moon = (String) planet2[j];
//
//                    // Add the item as a regular item.
//                    final Item addItem = orgUnitTree.addItem(moon);
//
//                    // Set it to be a child.
//                    orgUnitTree.setParent(moon, planet);
//
//                    // Make the moons look like leaves.
//                    orgUnitTree.setChildrenAllowed(moon, false);
//                }
//                // Expand the subtree.
//                orgUnitTree.expandItemsRecursively(planet);
//            }
//        }
//        layout.addComponent(orgUnitTree, 0, 4);
//
//        final List<String> selected =
//            Arrays.asList(new String[] { "Mercury", "Ariel" });
//
//        orgUnitTree.setValue(selected);
//        return this;
//    }
//
//    final Object[][] planets =
//        new Object[][] {
//            new Object[] { "Mercury" },
//            new Object[] { "Venus" },
//            new Object[] { "Earth", "The Moon" },
//            new Object[] { "Mars", "Phobos", "Deimos" },
//            new Object[] { "Jupiter", "Io", "Europa", "Ganymedes", "Callisto" },
//            new Object[] { "Saturn", "Titan", "Tethys", "Dione", "Rhea",
//                "Iapetus" },
//            new Object[] { "Uranus", "Miranda", "Ariel", "Umbriel", "Titania",
//                "Oberon" },
//            new Object[] { "Neptune", "Triton", "Proteus", "Nereid", "Larissa" } };
//
//    private HorizontalLayout footer;
//
// }