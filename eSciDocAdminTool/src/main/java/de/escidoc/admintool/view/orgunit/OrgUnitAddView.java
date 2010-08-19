package de.escidoc.admintool.view.orgunit;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.AdminToolApplication;
import de.escidoc.admintool.domain.OrgUnitFactory;
import de.escidoc.admintool.service.OrgUnitService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.orgunit.editor.IPredecessorEditor;
import de.escidoc.admintool.view.validator.EmptyFieldValidator;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.PredecessorForm;
import de.escidoc.vaadin.dialog.ErrorDialog;

public class OrgUnitAddView extends AbstractOrgUnitView
    implements ClickListener, Serializable {
    private static final long serialVersionUID = 8351229526921020901L;

    private static final Logger log = LoggerFactory
        .getLogger(OrgUnitAddView.class);

    private final ObjectProperty titleProperty = new ObjectProperty("",
        String.class);

    private final ObjectProperty descProperty = new ObjectProperty("",
        String.class);

    public OrgUnitAddView(final AdminToolApplication app,
        final OrgUnitService service) {
        super(app, service);
        super.postInit();
        super.orgUnitList = app.getOrgUnitTable();
        mapObjectWithProperty();
    }

    private void mapObjectWithProperty() {
        titleField.setPropertyDataSource(titleProperty);
        descriptionField.setPropertyDataSource(descProperty);
    }

    public void showAddedPredecessors(
        final AbstractComponent addedPredecessorView) {
        predecessorLayout.replaceComponent(predecessorResult,
            addedPredecessorView);
        predecessorResult = addedPredecessorView;
    }

    private boolean validate() {
        boolean valid = true;
        valid =
            EmptyFieldValidator.isValid(titleField, "Please enter a "
                + ViewConstants.TITLE_ID);
        valid &=
            (EmptyFieldValidator.isValid(descriptionField, "Please enter a "
                + ViewConstants.DESCRIPTION_ID));
        return valid;
    }

    private OrganizationalUnit storeInRepository(
        final OrganizationalUnit orgUnit) throws EscidocException,
        InternalClientException, TransportException {
        return service.create(orgUnit);
    }

    private void updateOrgUnitTableView(final OrganizationalUnit createdOrgUnit) {
        // TODO: fix me. Bad! everytime we do changes in the repository we have
        // to change model and the view
        // we should only have to change the model, and the view will get notify
        // and update it self.
        // Investigate how to use Vaadin's DataBinding
        app.getOrgUnitTable().addOrgUnit(createdOrgUnit);
        orgUnitList.addOrgUnit(createdOrgUnit);
    }

    @Override
    protected String getViewCaption() {
        return "Add " + ViewConstants.ORGANIZATION_UNITS_LABEL;
    }

    @Override
    protected void onAddPredecessorClicked() {
        try {
            if (titleField.getValue() != null
                && (!((String) titleField.getValue()).isEmpty())) {
                Object selectObject = predecessorTypeSelect.getValue();
                if (selectObject == null) {
                    selectObject = PredecessorType.BLANK;
                }
                final Class<?>[] argsClass =
                    new Class<?>[] { OrgUnitService.class };
                final Object[] args = new Object[] { this.service };

                final Class<?> c =
                    Class.forName(((PredecessorType) selectObject)
                        .getExecutionClass());
                Constructor<?> constructor = c.getConstructor(argsClass);

                Object object = constructor.newInstance(args);

                final IPredecessorEditor editor = (IPredecessorEditor) object;
                editor.setNewOrgUnit((String) titleField.getValue());
                final Window window = editor.getWidget();
                window.setModal(true);
                editor.setMainWindow(app.getMainWindow());
                editor.setList(predecessorTypeSelect);
                editor.setOrgUnitAddView(this);
                app.getMainWindow().addWindow(window);
            }
            else {
                app.getMainWindow().addWindow(
                    new ErrorDialog(app.getMainWindow(), "Error",
                        "Enter a title first, please."));
            }
        }
        catch (final ClassNotFoundException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (final InstantiationException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (final IllegalAccessException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();
        }
        catch (SecurityException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (NoSuchMethodException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (IllegalArgumentException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
        catch (InvocationTargetException e) {
            log.error("An unexpected error occured! See log for details.", e);
            e.printStackTrace();

        }
    }

    @Override
    protected void saveClicked(ClickEvent event) {
        if (validate()) {
            titleField.setComponentError(null);
            descriptionField.setComponentError(null);

            // TODO this is crazy. Data binding to the rescue for later.
            Set<String> parents = null;
            Set<String> predecessors = null;
            try {
                PredecessorType predecessorType =
                    (PredecessorType) predecessorTypeSelect.getValue();
                PredecessorForm predecessorForm = null;
                if (predecessorType != null) {
                    predecessorForm =
                        PredecessorForm.fromString(predecessorType
                            .toString().toLowerCase());
                }

                final OrganizationalUnit createdOrgUnit =
                    storeInRepository(new OrgUnitFactory()
                        .create((String) titleProperty.getValue(),
                            (String) descProperty.getValue()).parents(parents)
                        .predecessors(predecessors, predecessorForm)
                        .identifier((String) identifierField.getValue())
                        .alternative((String) alternativeField.getValue())
                        .orgType((String) orgTypeField.getValue())
                        .country((String) countryField.getValue())
                        .city((String) cityField.getValue())
                        .coordinates((String) coordinatesField.getValue())
                        .build());
                orgUnitList.addOrgUnit(createdOrgUnit);
                orgUnitList.select(createdOrgUnit);
            }
            catch (EscidocException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (InternalClientException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (TransportException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (SAXException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            catch (EscidocClientException e) {
                // TODO Auto-generated catch block
                log.error("An unexpected error occured! See log for details.",
                    e);
                e.printStackTrace();

            }
            // updateOrgUnitTableView(createdOrgUnit);
            app.showOrganizationalUnitView();
            // }
        }

        final Set<String> predecessors = null;
        final PredecessorForm predecessorType = null;

        final boolean addPredecessor = false;
        // TODO fix this! due possible bug in Vaadin.
        if (addPredecessor) {
            System.out.println("adding");
            // predecessors =
            // (Set<String>) getField(ViewConstants.PREDECESSORS_ID)
            // .getValue();
            // predecessorType =
            // (PredecessorForm) getField(ViewConstants.PREDECESSORS_TYPE_ID)
            // .getValue();

            System.out.println("type: " + predecessorType);

            for (final String predecessor : predecessors) {
                System.out.println("pre: " + predecessor);
            }
        }
        // try {
        // if (isValid()) {
        // final OrganizationalUnit createdOrgUnit =
        // storeInRepository(new OrgUnitFactory()
        // .create(title, description).parents(parents)
        // .predecessors(predecessors, predecessorType)
        // .identifier(identifier).alternative(alternative)
        // .orgType(orgType).country(country).city(city)
        // .coordinates(coordinates).build());
        // commit();
        // updateOrgUnitTableView(createdOrgUnit);
        // app.showOrganizationalUnitView();
        // }
        // }
        // TODO refactor exception handling
        // catch (final EscidocException e) {
        // if (e instanceof
        // de.escidoc.core.client.exceptions.application.invalid.InvalidStatusException)
        // {
        // ((ListSelect) getField(ViewConstants.PREDECESSORS_ID))
        // .setComponentError(new UserError(e.getHttpStatusMsg()));
        // System.out.println(e.getHttpStatusMsg());
        // }
        // e.printStackTrace();
        // }
        // catch (final InternalClientException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final TransportException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final ParserConfigurationException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final SAXException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // catch (final IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    @Override
    protected void cancelClicked(ClickEvent event) {
        super.app.showOrganizationalUnitView();
    }
}