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
package de.escidoc.admintool.app;

public class PropertyId {

    private PropertyId() {
        // Utility class
    }

    public static final String OBJECT_ID = "objid";

    public static final String PREDECESSORS = "predecessors";

    public static final String PARENTS = "parents";

    public static final String MODIFIED_BY = "properties.modifiedBy";

    public static final String LAST_MODIFICATION_DATE = "lastModificationDate";

    public static final String CREATED_BY = "properties.createdBy";

    public static final String NAME = "properties.name";

    public static final String DESCRIPTION = "properties.description";

    public static final String CREATED_ON = "properties.creationDate";

    public static final String PUBLIC_STATUS_COMMENT = "properties.publicStatusComment";

    public static final String PUBLIC_STATUS = "properties.publicStatus";

    public static final String TYPE = "properties.type";

    public static final String ORG_UNIT_REFS = "properties.organizationalUnitRefs";

    public static final String ADMIN_DESCRIPTORS = "adminDescriptors";

    public static final String LOGIN_NAME = "properties.loginName";

    public static final String ACTIVE = "properties.active";

    public static final String ASSIGN_ON = "grantProperties.assignedOn.xLinkTitle";

    public static final String GRANT_ROLE_OBJECT_ID = "grantProperties.role.objid";

    public static final String XLINK_TITLE = "xLinkTitle";

    public static final String LABEL = "label";

    public final static String[] GENERIC_PROPERTIES = new String[] { OBJECT_ID, NAME, DESCRIPTION,
        LAST_MODIFICATION_DATE, MODIFIED_BY, CREATED_BY, CREATED_ON, PUBLIC_STATUS, PUBLIC_STATUS_COMMENT };

    public static final String X_LINK_TITLE = "xLinkTitle";

    public static final String EMAIL = "properties.email";
    
    // need properties.label for user-groups
    public static final String PROP_LABEL = "properties.label";
}