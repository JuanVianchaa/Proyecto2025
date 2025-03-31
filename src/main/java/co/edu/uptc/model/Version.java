package co.edu.uptc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Version implements Serializable {
    private String id;
    private String content;
    private Date creationDate;
    private Version parent;
    private List<Version> subversions;
    private boolean isDeleted;

    public Version(String id, String content) {
        this.id = id;
        this.content = content;
        this.creationDate = new Date();
        this.subversions = new ArrayList<>();
        this.isDeleted = false;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Version getParent() {
        return parent;
    }

    public void setParent(Version parent) {
        this.parent = parent;
    }

    public List<Version> getSubversions() {
        return subversions;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void addSubversion(Version subversion) {
        subversion.setParent(this);
        this.subversions.add(subversion);
    }

    public boolean hasSubversions() {
        return !this.subversions.isEmpty();
    }

    public boolean isMainVersion() {
        return parent == null;
    }

    @Override
    public String toString() {
        String versionType = isMainVersion() ? "[PRINCIPAL]" : "[SUBVERSIÓN]";
        String deletedStatus = isDeleted ? " [ELIMINADO]" : "";
        return "Versión: " + id + " " + versionType + deletedStatus + " - Fecha: " + creationDate;
    }
}