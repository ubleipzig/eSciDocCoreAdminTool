package de.escidoc.admintool.view.orgunit;

import java.util.Collections;
import java.util.Set;

public class OrgUnit {

    private final String title;

    private final String description;

    private final String identifier;

    private final Set<String> parentObjectIds;

    public static class Builder {
        // required
        private final String title;

        private final String description;

        // optional
        private String identifier;

        private Set<String> parentObjectIds = Collections.emptySet();

        public Builder(final String title, final String description) {
            this.title = title;
            this.description = description;
        }

        public Builder identifier(final String value) {
            this.identifier = value;
            return this;
        }

        public Builder parents(final Set<String> parentObjectIds) {
            this.parentObjectIds = parentObjectIds;
            return this;
        }

        public OrgUnit build() {
            return new OrgUnit(this);
        }
    }

    private OrgUnit(final Builder builder) {
        this.title = builder.title;
        this.description = builder.description;

        this.identifier = builder.identifier;
        this.parentObjectIds = builder.parentObjectIds;
    }
}
