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

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.core.resources.common.MetadataRecord;

public class EscidocMdRecord extends MetadataRecord {

    public static class BuilderImpl implements Builder<MetadataRecord> {
        final MetadataRecord escidocMdRecord = new MetadataRecord();

        private final String title;

        private final String description;

        private Document doc;

        private Element pubman;

        private String alternative;

        private String identifier;

        private String country;

        private String city;

        private String orgType;

        private String coordinates;

        public BuilderImpl(final String title, final String description) {
            this.title = title;
            this.description = description;
        }

        public MetadataRecord build() throws ParserConfigurationException {

            escidocMdRecord.setName("escidoc");
            buildNewDocument();
            pubman =
                doc.createElementNS("http://purl.org/escidoc/metadata/profiles/0.1/organizationalunit",
                    "organizational-unit");
            pubman.setPrefix("mdou");

            escidocMdRecord.setContent(pubman);

            title(title);
            description(description);

            addAlternative();
            addCountry();
            addCity();
            addCoordinates();
            addIdentifier();
            addOrgType();

            return escidocMdRecord;
        }

        private void addCity() {
            final Element element = doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS, "city");
            element.setPrefix("eterms");
            element.setTextContent(city);
            pubman.appendChild(element);
        }

        private void addCoordinates() {
            final Element element = doc.createElementNS("http://www.opengis.net/kml/2.2", "coordinates");
            element.setPrefix("kml");
            element.setTextContent(coordinates);
            pubman.appendChild(element);
        }

        private void addOrgType() {
            final Element element = doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS, "organization-type");
            element.setPrefix("eterms");
            element.setTextContent(orgType);
            pubman.appendChild(element);
        }

        private void addIdentifier() {
            final Element identifierElmt = doc.createElementNS("http://purl.org/dc/elements/1.1/", "identifier");
            identifierElmt.setPrefix("dc");
            identifierElmt.setTextContent(identifier);
            pubman.appendChild(identifierElmt);

        }

        private void addCountry() {
            final Element element = doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS, "country");
            element.setPrefix("eterms");
            element.setTextContent(country);
            pubman.appendChild(element);
        }

        private void addAlternative() {
            final Element alternativeElement = doc.createElementNS("http://purl.org/dc/terms/", "alternative");
            alternativeElement.setPrefix("dcterms");
            alternativeElement.setTextContent(alternative);
            pubman.appendChild(alternativeElement);
        }

        private Node title(final String title) {
            final Element titleElmt = doc.createElementNS("http://purl.org/dc/elements/1.1/", "title");
            titleElmt.setPrefix("dc");
            titleElmt.setTextContent(title);
            pubman.appendChild(titleElmt);
            return pubman;
        }

        private Node description(final String description) {
            final Element descriptionElmt = doc.createElementNS("http://purl.org/dc/elements/1.1/", "description");
            descriptionElmt.setPrefix("dc");
            descriptionElmt.setTextContent(description);
            pubman.appendChild(descriptionElmt);
            return pubman;
        }

        public BuilderImpl alternative(final String alternative) {
            this.alternative = alternative;
            return this;
        }

        public BuilderImpl identifier(final String identifier) {
            this.identifier = identifier;
            return this;
        }

        public BuilderImpl country(final String country) {
            this.country = country;
            return this;
        }

        public BuilderImpl city(final String city) {
            this.city = city;
            return this;
        }

        public BuilderImpl orgType(final String orgType) {
            this.orgType = orgType;
            return this;
        }

        public BuilderImpl coordinates(final String coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        private void buildNewDocument() throws ParserConfigurationException {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setCoalescing(true);
            factory.setValidating(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        }
    }
}
