//package de.escidoc.admintool.view.resource;
//
//import de.escidoc.admintool.domain.OrgUnitFactory;
//import de.escidoc.admintool.service.OrgUnitServiceLab;
//import de.escidoc.admintool.service.ResourceService;
//import de.escidoc.core.resources.oum.OrganizationalUnit;
//
//public class UpdateOrgUnitCommandImpl implements UpdateOrgUnitCommand {
//
//    private final OrgUnitServiceLab resourceService;
//
//    public UpdateOrgUnitCommandImpl(final ResourceService resourceService) {
//        this.resourceService = (OrgUnitServiceLab) resourceService;
//    }
//
//    @Override
//    public void execute() {
////        final OrganizationalUnit model =
////            updateModel(findOrgUnitBeforeUpdated());
////        orgUnit = resourceService.update(model);
////        updateView(model);
//    }
//
//    private OrganizationalUnit updateModel(final Object oldOrgUnit) {
////        return new OrgUnitFactory()
////            .update(oldOrgUnit, (String) titleField.getValue(),
////                (String) descriptionField.getValue())
////            .alternative((String) alternativeField.getValue())
////            .identifier((String) identifierField.getValue())
////            .orgType((String) orgTypeField.getValue())
////            .country((String) countryField.getValue())
////            .city((String) cityField.getValue())
////            .coordinates((String) coordinatesField.getValue())
////            .parents(getSelectedParents()).build();
//    }
//
//    private Object findOrgUnitBeforeUpdated() {
//        return resourceService.find(objectId);
//    }
// }
