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
package de.escidoc.admintool.domain;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.common.base.Preconditions;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;

public class MetadataExtractor {

    private final OrganizationalUnit ou;

    public MetadataExtractor(final OrganizationalUnit ou) {
        Preconditions.checkNotNull(ou, "final OrgUnit must final not be null.");
        this.ou = ou;
    }

    private String get(final MetadataRecords metadataRecords, final String nodeName) {
        final MetadataRecord escidocMdRecord = metadataRecords.get("escidoc");

        if (escidocMdRecord == null) {
            return "";
        }

        final Element content = escidocMdRecord.getContent();

        for (int index = 0; index < content.getChildNodes().getLength(); index++) {
            final Node item = content.getChildNodes().item(index);

            if (item.getNodeName().equals(nodeName)) {
                return item.getTextContent();
            }
        }
        return "";
    }

    public String get(final String nodeName) {
        return get(ou.getMetadataRecords(), nodeName);
    }
}