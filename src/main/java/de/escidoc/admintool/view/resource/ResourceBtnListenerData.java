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
import java.util.Map;

import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import com.vaadin.ui.Window;

import de.escidoc.admintool.service.internal.ResourceService;
import de.escidoc.core.resources.Resource;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class ResourceBtnListenerData {
    public Collection<Field> allFields;

    public Window mainWindow;

    public ResourceService resourceService;

    public Item item;

    public Resource oldOrgUnit;

    public OrganizationalUnit toBeUpdated;

    public Map<String, Field> fieldByName;

    public ResourceView resourceView;

    public ResourceBtnListenerData() {
    }
}