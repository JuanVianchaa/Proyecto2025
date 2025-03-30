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
    private boolean isMainVersion;

    public Version(String id, String content, boolean isMainVersion) {
        this.id = id;
        this.content = content;
        this.creationDate = new Date();
        this.subversions = new ArrayList<>();
        this.isMainVersion = isMainVersion;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Date getCreationDate() {
        return creationDate;
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

    public void addSubversion(Version subversion) {
        subversion.setParent(this);
        this.subversions.add(subversion);
    }

    public boolean hasSubversions() {
        return !this.subversions.isEmpty();
    }

    public boolean isMainVersion() {
        return isMainVersion;
    }

    @Override
    public String toString() {
        String versionType = isMainVersion ? "[PRINCIPAL]" : "[SUBVERSIÓN]";
        return "Versión: " + id + " " + versionType + " - Fecha: " + creationDate;
    }
}