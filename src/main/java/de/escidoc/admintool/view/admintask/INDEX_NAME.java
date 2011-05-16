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
package de.escidoc.admintool.view.admintask;

import java.util.Arrays;
import java.util.List;

public class INDEX_NAME {

    public static final String REINDEX_ALL = "all";

    public static final String CONTEXT_ADMIN = "context_admin";

    public static final String CONTENT_RELATION_ADMIN = "content_relation_admin";

    public static final String REINDEX_ESCIDOC_OU = "escidocou_all";

    public static final String OU_ADMIN = "ou_admin";

    public static final String ESCIDOCOAIPMH_ALL = "escidocoaipmh_all";

    public static final String ITEM_CONTAINER_ADMIN = "item_container_admin";

    public static final String REINDEX_ESCIDOC = "escidoc_all";

    public static final String CONTENT_MODEL_ADMIN = "content_model_admin";

    public static final List<String> ALL_NAME = Arrays.asList(new String[] { REINDEX_ALL, REINDEX_ESCIDOC,
        CONTEXT_ADMIN, CONTENT_RELATION_ADMIN, CONTENT_MODEL_ADMIN, OU_ADMIN, REINDEX_ESCIDOC_OU, ESCIDOCOAIPMH_ALL });
}
