package de.escidoc.admintool.view.role;

// TODO retrieve predefined roles from repository.
public enum RoleType {

    SYSTEM_ADMINISTRATOR("escidoc:role-system-administrator", "System Administrator", false), SYSTEM_INSPECTOR(
        "escidoc:role-system-inspector", "System Inspector", false), AUTHOR("", "Author", true), ADMINISTRATOR(
        "escidoc:role-administrator", "Administrator", true), MD_EDITOR("", "MD-Editor", true), Moderator("",
        "Moderator", true), DEPOSITOR("", "Depositor", true), INSPECTOR("", "Inspector", true), COLLABOLATOR("",
        "Collaborator", true);

    private String objectId;

    private String name;

    private boolean canBeScoped;

    RoleType(final String objectId, final String name, final boolean canBeScoped) {
        this.objectId = objectId;
        this.name = name;
        this.canBeScoped = canBeScoped;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean canBeScoped() {
        return canBeScoped;
    }

    public String getObjectId() {
        return objectId;
    }
}