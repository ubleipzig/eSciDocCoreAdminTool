package de.escidoc.admintool.view.admintask;

public enum ResourceType {

    ITEM("Item"), CONTAINER("Container"), CONTEXT("Context"), ORGANIZATIONAL_UNIT(
        "Organizational Unit");
    // , NONE("None");

    private String label;

    ResourceType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
