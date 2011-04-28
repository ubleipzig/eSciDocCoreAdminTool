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
 * Copyright 2011 Fachinformationszentrum Karlsruhe Gesellschaft
 * fuer wissenschaftlich-technische Information mbH and Max-Planck-
 * Gesellschaft zur Foerderung der Wissenschaft e.V.
 * All rights reserved.  Use is subject to license terms.
 */
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
