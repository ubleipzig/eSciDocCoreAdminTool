package de.escidoc.admintool.domain;

/**
 * @author ASP
 * 
 */
public enum PublicStatus {

    CREATED("Created"), OPENED("Opened"), CLOSED("Closed");

    private final String name;

    PublicStatus(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static PublicStatus from(final de.escidoc.core.resources.common.properties.PublicStatus fromCore) {
        return PublicStatus.valueOf(fromCore.toString());
    }

    public static final de.escidoc.core.resources.common.properties.PublicStatus to(final PublicStatus publicStatus) {
        return de.escidoc.core.resources.common.properties.PublicStatus.valueOf(publicStatus.name().toUpperCase());

    }
}
