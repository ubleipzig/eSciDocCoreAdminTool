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
 * 
 * Copyright 2015 Leipzig University Library
 * 
 * This code is the result of the project
 * "Die Bibliothek der Milliarden Woerter". This project is funded by
 * the European Social Fund. "Die Bibliothek der Milliarden Woerter" is
 * a cooperation project between the Leipzig University Library, the
 * Natural Language Processing Group at the Institute of Computer
 * Science at Leipzig University, and the Image and Signal Processing
 * Group at the Institute of Computer Science at Leipzig University.
 * 
 * All rights reserved.  Use is subject to license terms.
 * 
 * @author Uwe Kretschmer <u.kretschmer@denk-selbst.de>
 * 
 */
package de.uni_leipzig.ubl.admintool.view.role;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
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

    private final Component saveBtn;

    private final ListSelect resourceResult;

    RoleSelectListener(final NativeSelect resourceTypeComboBox, final TextField searchBox, final Button searchButton,
        final Component saveBtn, final ListSelect resourceResult) {
        Preconditions.checkNotNull(resourceTypeComboBox, "resourceTypeComboBox is null: %s", resourceTypeComboBox);
        Preconditions.checkNotNull(searchButton, "searchBox is null: %s", searchButton);
        Preconditions.checkNotNull(searchBox, "searchButton is null: %s", searchBox);
        Preconditions.checkNotNull(saveBtn, "footer is null: %s", saveBtn);
        Preconditions.checkNotNull(resourceResult, "resouceResult is null: %s", resourceResult);
        this.resourceTypeComboBox = resourceTypeComboBox;
        this.searchBox = searchBox;
        this.searchButton = searchButton;
        this.saveBtn = saveBtn;
        this.resourceResult = resourceResult;
    }

    @Override
    public void valueChange(final ValueChangeEvent event) {
        onSelectedRole(event);
    }

    private void onSelectedRole(final ValueChangeEvent event) {
        if (event.getProperty().getValue() instanceof Role) {
            final List<ResourceType> resourceTypeList = new ArrayList<ResourceType>();
            for (final ScopeDef scopeDef : getScopeDefinitions((Role) event.getProperty().getValue())) {

                final ResourceType resourceType = ResourceType.convert(scopeDef.getRelationAttributeObjectType());
                if (resourceType != null && !resourceType.equals(ResourceType.COMPONENT)) {
                    resourceTypeList.add(resourceType);
                }
            }
            bindView(resourceTypeList, (Role) event.getProperty().getValue());
            showSaveButton();
        }
    }

    private void showSaveButton() {
        saveBtn.setVisible(true);
    }

    private void bindView(final List<ResourceType> resourceTypeList, final Role role) {
        final BeanItemContainer<ResourceType> dataSource =
            new BeanItemContainer<ResourceType>(ResourceType.class, resourceTypeList);
        resourceTypeComboBox.setContainerDataSource(dataSource);
        if (dataSource.size() > 0) {
            resourceTypeComboBox.setValue(dataSource.getIdByIndex(0));
        }
        enableScoping(resourceTypeList.size() > 0);
    }

    private List<ScopeDef> getScopeDefinitions(final Role role) {
        return role.getScope().getScopeDefinitions();
    }

    private void enableScoping(final boolean isScopingEnabled) {
        resourceTypeComboBox.setEnabled(isScopingEnabled);
        searchBox.setEnabled(isScopingEnabled);
        searchButton.setEnabled(isScopingEnabled);
        resourceResult.setEnabled(isScopingEnabled);
    }
}