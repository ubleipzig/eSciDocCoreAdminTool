package de.escidoc.admintool.view.context;

/**
 * @author ASP
 * 
 */
public enum PublicStatus {
    CREATED("Created"), OPENED("Opened"), CLOSED("Closed");

    private final String name;

    private PublicStatus(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
