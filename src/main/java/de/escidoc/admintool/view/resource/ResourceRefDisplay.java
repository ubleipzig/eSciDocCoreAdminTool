package de.escidoc.admintool.view.resource;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ResourceRefDisplay implements Serializable {

    private final String objectId;

    private final String title;

    public ResourceRefDisplay(final String objectId, final String title) {
        this.objectId = objectId;
        this.title = title;
    }

    public ResourceRefDisplay() {
        objectId = "";
        title = "";
    }

    public String getObjectId() {
        return objectId;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime * result + ((objectId == null) ? 0 : objectId.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourceRefDisplay other = (ResourceRefDisplay) obj;
        if (objectId == null) {
            if (other.objectId != null) {
                return false;
            }
        }
        else if (!objectId.equals(other.objectId)) {
            return false;
        }
        if (title == null) {
            if (other.title != null) {
                return false;
            }
        }
        else if (!title.equals(other.title)) {
            return false;
        }
        return true;
    }
}
