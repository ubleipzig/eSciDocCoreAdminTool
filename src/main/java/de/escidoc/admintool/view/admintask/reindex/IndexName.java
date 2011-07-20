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
package de.escidoc.admintool.view.admintask.reindex;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;

public enum IndexName {

    REINDEX_ALL("All"), CONTEXT_ADMIN("Context Admin"), CONTENT_RELATION_ADMIN("Content Relation Admin"), REINDEX_ESCIDOC_OU(
        "eSciDoc OU All"), OU_ADMIN("OU Admin"), ESCIDOCOAIPMH_ALL("eSciDoc OAIPMH All"), ITEM_CONTAINER_ADMIN(
        "Item Container Admin"), REINDEX_ESCIDOC("eSciDoc All"), CONTENT_MODEL_ADMIN("Content Model Admin");

    private String label;

    IndexName(final String label) {
        Preconditions.checkNotNull(label, "label is null: %s", label);
        this.label = label;
    }

    public String asLabel() {
        return label;
    }

    private final static Set<String> set = new HashSet<String>(values().length);

    public static Collection<?> all() {
        for (final IndexName indexName : values()) {
            set.add(indexName.asLabel());
        }
        return set;
    }

}
