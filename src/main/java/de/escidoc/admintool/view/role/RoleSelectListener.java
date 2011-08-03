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
package de.escidoc.admintool.view.role;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;

import de.escidoc.admintool.view.admintask.ResourceType;
import de.escidoc.core.resources.aa.role.Role;
import de.escidoc.core.resources.aa.role.ScopeDef;

@SuppressWarnings("serial")
class RoleSelectListener implements ValueChangeListener {

    private final NativeSelect resourceTypeComboBox;

    private final TextField searchBox;

    private final Button searchButton;

    RoleSelectListener(final NativeSelect resourceTypeComboBox, final TextField searchBox, final Button searchButton) {
        Preconditions.checkNotNull(resourceTypeComboBox, "resourceTypeComboBox is null: %s", resourceTypeComboBox);
        Preconditions.checkNotNull(searchButton, "searchBox is null: %s", searchButton);
        Preconditions.checkNotNull(searchBox, "searchButton is null: %s", searchBox);
        this.resourceTypeComboBox = resourceTypeComboBox;
        this.searchBox = searchBox;
        this.searchButton = searchButton;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        onSelectedRole(event);
    }

    private void onSelectedRole(final ValueChangeEvent event) {
        if (event.getProperty().getValue() instanceof Role) {
            final List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
            for (final ScopeDef scopeDef : getScopeDefinitions((Role) event.getProperty().getValue())) {
                resourceTypeList.add(ResourceType.convert(scopeDef.getRelationAttributeObjectType()));
            }
            bindView(resourceTypeList, (Role) event.getProperty().getValue());
        }
    }

    private void bindView(final List<ResourceType> resourceTypeList, final Role role) {
        resourceTypeComboBox.setContainerDataSource(new BeanItemContainer<ResourceType>(ResourceType.class,
            resourceTypeList));
        enableScoping(isScopingEnable(role));
    }

    private List<ScopeDef> getScopeDefinitions(final Role role) {
        return role.getScope().getScopeDefinitions();
    }

    private boolean isScopingEnable(final Role role) {
        return !(role.getObjid().equals(RoleType.SYSTEM_ADMINISTRATOR.getObjectId()) || role.getObjid().equals(
            RoleType.SYSTEM_INSPECTOR.getObjectId()));
    }

    private void enableScoping(final boolean isScopingEnabled) {
        resourceTypeComboBox.setEnabled(isScopingEnabled);
        searchBox.setEnabled(isScopingEnabled);
        searchButton.setEnabled(isScopingEnabled);
    }
}