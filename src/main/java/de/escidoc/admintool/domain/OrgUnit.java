package de.escidoc.admintool.domain;

import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.escidoc.admintool.builder.RawXmlMetadata;

import de.escidoc.core.resources.common.MetadataRecord;
import de.escidoc.core.resources.common.MetadataRecords;
import de.escidoc.core.resources.oum.OrganizationalUnit;
import de.escidoc.core.resources.oum.Parent;
import de.escidoc.core.resources.oum.Parents;

public class OrgUnit extends OrganizationalUnit {

    public OrgUnit(final BuilderImpl builder) {
        super.setMetadataRecords(builder.mdRecords);
        super.setParents(builder.parents);
    }

    public static class BuilderImpl implements Builder<OrganizationalUnit> {

        private final MetadataRecords mdRecords = new MetadataRecords();

        private String name;

        private String disc;

        private Parents parents;

        private MetadataRecord escidocMdRecord;

        private String alternative;

        private String identifier;

        private String country;

        private String city;

        private String coordinates;

        private String type;

        public BuilderImpl(final String name, final String disc) {
            this.name = name;
            this.disc = disc;
        }

        public BuilderImpl(final OrganizationalUnit before) {
            name = before.getProperties().getName();
            disc = before.getProperties().getDescription();
        }

        public BuilderImpl parents(final Set<String> parentObjectIds) {
            parents = new Parents();

            if (parentObjectIds != null && !parentObjectIds.isEmpty()) {
                for (final String parentObjectId : parentObjectIds) {
                    parents.add(new Parent(parentObjectId));
                }
            }

            return this;
        }

        public BuilderImpl name(final String name) {
            this.name = name;
            return this;
        }

        public BuilderImpl description(final String desc) {
            disc = desc;
            return this;
        }

        @Override
        public OrganizationalUnit build() throws ParserConfigurationException {
            escidocMdRecord =
                new EscidocMdRecord.BuilderImpl(name, disc)
                    .alternative(alternative).identifier(identifier)
                    .coordinates(coordinates).country(country).city(city)
                    .orgType(type).build();
            mdRecords.add(escidocMdRecord);
            return new OrgUnit(this);
        }

        public BuilderImpl alternative(final String alternative) {
            this.alternative = alternative;
            return this;
        }

        public BuilderImpl identifier(final String identifier) {
            this.identifier = identifier;
            return this;
        }

        public BuilderImpl country(final String value) {
            country = value;
            return this;
        }

        public BuilderImpl city(final String value) {
            city = value;
            return this;
        }

        public BuilderImpl coordinates(final String value) {
            coordinates = value;
            return this;
        }

        public BuilderImpl type(final String value) {
            type = value;
            return this;
        }

        public BuilderImpl metadata(final RawXmlMetadata rawXmlMetadata) {
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }
}