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

public class AppConstants {
    private AppConstants() {
        // Utility class
    }

    public static final String ESCIDOC_USER_HANDLE = "eSciDocUserHandle";

    public static final String DEFAULT_THEME_NAME = "contacts";

    public static final String REINDEER_MODS_THEME = "reindeermods";

    public static final String ESCIDOC_THEME = "dots";

    public static final String ESCIDOC_METADATA_TERMS_NS = "http://purl.org/escidoc/metadata/terms/0.1/";

    public static final String CREATED_BY_FILTER = "http://escidoc.de/core/01/structural-relations/created-by";

    public static final String SYSADMIN_OBJECT_ID = "escidoc:exuser1";

    public static final String IS_TOP_LEVEL = "true";

    public static final String TOP_LEVEL_ORGANIZATIONAL_UNITS = "top-level-organizational-units";

    public static final String DC_IDENTIFIER = "dc:identifier";

    public static final String DCTERMS_ALTERNATIVE = "dcterms:alternative";

    public static final String ETERMS_COUNTRY = "eterms:country";

    public static final String ETERMS_CITY = "eterms:city";

    public static final String KML_COORDINATES = "kml:coordinates";

    public static final String TYPE = "";

    public static final String ETERMS_ORGANIZATION_TYPE = "eterms:organization-type";

    public static final String DEFAULT_ORG_UNIT_ATTRIBUTE_NAME = "o";

    public static final String ORGANIZATIONAL_UNIT_DEFAULT_ATTRIBUTE_NAME = "o";

    public static final String XACML_ACTION_ID = "urn:oasis:names:tc:xacml:1.0:action:action-id";

    public static final String SUBJECT_ID = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";

    public static final String RESOURCE_ID = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";

    public static final String SMALL_BUTTON = "small";

    public static final String ESCIDOC_LOGO = "images/SchriftLogo.jpg";

    public static final String ESCIDOC_URL = "escidocurl";

    public static final String LOGOUT_TARGET = "/aa/logout?target=";

    public static final String LOGIN_TARGET = "/aa/login?target=";

    static final String SPACE = " ";

    public static final String GUEST_OBJECT_ID = SPACE;

    public static final String ESCIDOC_DEFAULT_METADATA_NAME = "escidoc";

    public static final String MAX_RESULT_SIZE = "1000";

    public static final String[] CONTEXT_PROPERTY_NAMES = new String[] { PropertyId.OBJECT_ID, PropertyId.NAME,
        PropertyId.DESCRIPTION, PropertyId.PUBLIC_STATUS, PropertyId.PUBLIC_STATUS_COMMENT, PropertyId.TYPE,
        PropertyId.CREATED_ON, PropertyId.CREATED_BY, PropertyId.LAST_MODIFICATION_DATE, PropertyId.MODIFIED_BY,
        PropertyId.ORG_UNIT_REFS, PropertyId.ADMIN_DESCRIPTORS };

    public static final String EMPTY_STRING = "";

    public static final String ESCIDOC_URL_PARAMETER = "?escidocurl=";
}
