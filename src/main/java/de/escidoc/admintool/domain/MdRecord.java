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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;

public class MdRecord {

    private static Element pubman;

    private static Document doc;

    public static MetadataRecords createEscidocMdRecords(String name, String disc) throws ParserConfigurationException {

        final MetadataRecords mdRecords = new MetadataRecords();
        final MetadataRecord escidocMdRecord = new MetadataRecord();
        mdRecords.add(escidocMdRecord);

        escidocMdRecord.setName("escidoc");
        buildNewDocument();

        pubman =
            doc.createElementNS("http://purl.org/escidoc/metadata/profiles/0.1/organizationalunit",
                "organizational-unit");
        pubman.setPrefix("mdou");

        escidocMdRecord.setContent(pubman);

        title(name);
        description(disc);

        return mdRecords;
    }

    private static void buildNewDocument() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setCoalescing(true);
        factory.setValidating(true);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        doc = builder.newDocument();
    }

    // TODO refactor this: a lot of duplication
    private static Node title(final String title) {
        final Element titleElmt = doc.createElementNS("http://purl.org/dc/elements/1.1/", "title");
        titleElmt.setPrefix("dc");
        titleElmt.setTextContent(title);
        pubman.appendChild(titleElmt);
        return pubman;
    }

    private static Node description(final String description) {
        final Element descriptionElmt = doc.createElementNS("http://purl.org/dc/elements/1.1/", "description");
        descriptionElmt.setPrefix("dc");
        descriptionElmt.setTextContent(description);
        pubman.appendChild(descriptionElmt);
        return pubman;
    }

}
