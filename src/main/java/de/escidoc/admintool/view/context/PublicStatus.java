package de.escidoc.admintool.view.context;

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

    public static PublicStatus convert(
        final de.escidoc.core.resources.common.properties.PublicStatus fromCore) {
        return PublicStatus.valueOf(fromCore.toString());
    }
}
