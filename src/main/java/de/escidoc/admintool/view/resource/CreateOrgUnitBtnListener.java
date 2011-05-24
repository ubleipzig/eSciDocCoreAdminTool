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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class CreateOrgUnitBtnListener extends AbstractResourceBtnListener {

    private static final Logger LOG = LoggerFactory.getLogger(CreateOrgUnitBtnListener.class);

    private static final long serialVersionUID = 6514709536247207829L;

    private OrganizationalUnit build;

    private final ResourceContainer rContainer;

    private OrganizationalUnit created;

    private String parentId;

    protected CreateOrgUnitBtnListener(final Collection<Field> allFields, final Map<String, Field> fieldByName,
        final Window mainWindow, final ResourceView resourceView, final ResourceService resourceService,
        final ResourceContainer rContainer) {
        super(allFields, fieldByName, mainWindow, resourceView, resourceService);
        this.rContainer = rContainer;
    }

    @Override
    protected void updateModel() throws ParserConfigurationException {
        final Field parentField = getParentField();
        final Property property = parentField.getPropertyDataSource();
        final Object value = property.getValue();

        if (value instanceof ResourceRefDisplay) {
            final ResourceRefDisplay parentDisplay = (ResourceRefDisplay) value;
            parentId = parentDisplay.getObjectId();
            final Set<String> parents = new HashSet<String>();

            if (!parentId.isEmpty()) {
                parents.add(parentId);
                build =
                    new de.escidoc.admintool.domain.OrgUnit.BuilderImpl(getTitle(), getDescription())
                        .parents(parents).country(getCountry()).city(getCity()).alternative(getAlternative())
                        .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();
            }
            else {
                build =
                    new de.escidoc.admintool.domain.OrgUnit.BuilderImpl(getTitle(), getDescription())
                        .country(getCountry()).city(getCity()).alternative(getAlternative())
                        .identifier(getIdentifier()).coordinates(getCoordinates()).type(getType()).build();
            }

        }
        else {
            LOG.error("Unknown type: " + value.getClass());
        }
    }

    @Override
    protected void updatePersistence() throws EscidocClientException {
        created = (OrganizationalUnit) getData().resourceService.create(build);
    }

    @Override
    protected void updateResourceContainer() {
        if (parentId.isEmpty()) {
            rContainer.add(created);
        }
        else {
            Resource parent;
            try {
                parent = getData().resourceService.findById(parentId);
                rContainer.addChild(parent, created);
            }
            catch (final EscidocClientException e) {
            }
        }
    }

    @Override
    protected String getSucessMessage() {
        return ViewConstants.ORGANIZATIONAL_UNIT_IS_CREATED;
    }

    @Override
    protected void showInEditView() {
        getResourceView().selectInFolderView(created);
        final Item item = rContainer.getItem(created);
        super.getResourceView().showEditView(item);
    }

    @Override
    protected void commitAllFields() {
        // Do nothing
    }
}