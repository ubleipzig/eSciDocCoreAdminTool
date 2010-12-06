package org.escidoc.admintool.builder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.escidoc.admintool.app.AppConstants;
import de.escidoc.admintool.domain.Builder;
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
                doc
                    .createElementNS(
                        "http://purl.org/escidoc/metadata/profiles/0.1/organizationalunit",
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
            final Element element =
                doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS,
                    "city");
            element.setPrefix("eterms");
            element.setTextContent(city);
            pubman.appendChild(element);
        }

        private void addCoordinates() {
            final Element element =
                doc.createElementNS("http://www.opengis.net/kml/2.2",
                    "coordinates");
            element.setPrefix("kml");
            element.setTextContent(coordinates);
            pubman.appendChild(element);
        }

        private void addOrgType() {
            final Element element =
                doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS,
                    "organization-type");
            element.setPrefix("eterms");
            element.setTextContent(orgType);
            pubman.appendChild(element);
        }

        private void addIdentifier() {
            final Element identifierElmt =
                doc.createElementNS("http://purl.org/dc/elements/1.1/",
                    "identifier");
            identifierElmt.setPrefix("dc");
            identifierElmt.setTextContent(identifier);
            pubman.appendChild(identifierElmt);

        }

        private void addCountry() {
            final Element element =
                doc.createElementNS(AppConstants.ESCIDOC_METADATA_TERMS_NS,
                    "country");
            element.setPrefix("eterms");
            element.setTextContent(country);
            pubman.appendChild(element);
        }

        private void addAlternative() {
            final Element alternativeElement =
                doc.createElementNS("http://purl.org/dc/terms/", "alternative");
            alternativeElement.setPrefix("dcterms");
            alternativeElement.setTextContent(alternative);
            pubman.appendChild(alternativeElement);
        }

        private Node title(final String title) {
            final Element titleElmt =
                doc
                    .createElementNS("http://purl.org/dc/elements/1.1/",
                        "title");
            titleElmt.setPrefix("dc");
            titleElmt.setTextContent(title);
            pubman.appendChild(titleElmt);
            return pubman;
        }

        private Node description(final String description) {
            final Element descriptionElmt =
                doc.createElementNS("http://purl.org/dc/elements/1.1/",
                    "description");
            descriptionElmt.setPrefix("dc");
            descriptionElmt.setTextContent(description);
            pubman.appendChild(descriptionElmt);
            return pubman;
        }

        public BuilderImpl alternative(final String alternative) {
            this.alternative = alternative;
            return this;
        }

        private boolean isNotSet(final String identifier) {
            return identifier == null || identifier.isEmpty();
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
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setCoalescing(true);
            factory.setValidating(true);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        }

        public BuilderImpl metadata(final PubmanMetadata pubman) {
            alternative = pubman.getAlternative();
            return this;
        }
    }
}