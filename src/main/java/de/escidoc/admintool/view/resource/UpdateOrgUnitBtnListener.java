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
package de.escidoc.admintool.view.resource;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.app.PropertyId;
import de.escidoc.admintool.domain.OrgUnitBuilder;
import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class UpdateOrgUnitBtnListener extends AbstractResourceBtnListener {

    private static final long serialVersionUID = 4095932748716005999L;

    public UpdateOrgUnitBtnListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceView resourceView, final ResourceService resourceService) {
        super(allFields, fieldByName, mainWindow, resourceView, resourceService);
    }

    @Override
    protected void updateModel() throws ParserConfigurationException, SAXException, IOException, EscidocClientException {
        getData().toBeUpdated =
            createBuilder()
                .with(getTitle(), getDescription()).country(getCountry()).city(getCity()).alternative(getAlternative())
                .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();
    }

    private OrgUnitBuilder createBuilder() throws EscidocClientException {
        return new OrgUnitBuilder(getOldOrgUnit());
    }

    private OrganizationalUnit getOldOrgUnit() throws EscidocClientException {
        return (OrganizationalUnit) getData().resourceService.findById((String) getData().item.getItemProperty(
            PropertyId.OBJECT_ID).getValue());
    }

    @Override
    protected void updatePersistence() throws EscidocClientException {
        getData().resourceService.update(getData().toBeUpdated);
    }

    @Override
    protected String getSucessMessage() {
        return ViewConstants.SUCCESFULLY_UPDATED_ORGANIZATIONAL_UNIT;
    }

    @Override
    protected void updateResourceContainer() {
        // do nothing
    }

    @Override
    protected void showInEditView() {
        // do nothing
        final OrganizationalUnit updated = getData().toBeUpdated;
        getData().resourceView.selectInFolderView(updated);

    }

    @Override
    protected void commitAllFields() {
        for (final Field field : getData().allFields) {
            // FIX ME: this is a hack, please repair it.
            if (field.getCaption().equals(ViewConstants.PUBLIC_STATUS_LABEL)) {
                return;
            }
            field.commit();
        }
    }
}