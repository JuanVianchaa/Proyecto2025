package co.edu.uptc.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {
    private String name;
    private List<Version> mainVersions;
    private List<Version> allVersions;
    private List<Version> deletedVersions;
    private Version currentVersion;

    public Document(String name) {
        this.name = name;
        this.mainVersions = new ArrayList<>();
        this.allVersions = new ArrayList<>();
        this.deletedVersions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Version> getMainVersions() {
        return mainVersions;
    }

    public void addMainVersion(Version version) {
        this.mainVersions.add(version);
        this.allVersions.add(version);
        currentVersion = version;
    }

    public void addSubversion(Version version, Version parent) {
        if (parent != null) {
            parent.addSubversion(version);
            this.allVersions.add(version);
            currentVersion = version;
        }
    }

    public List<Version> getAllVersions() {
        return allVersions;
    }

    public List<Version> getDeletedVersions() {
        return deletedVersions;
    }

    public Version findVersion(String id) {
        for (Version version : allVersions) {
            if (version.getId().equals(id)) {
                return version;
            }
        }
        return null;
    }

    public Version findDeletedVersion(String id) {
        for (Version version : deletedVersions) {
            if (version.getId().equals(id)) {
                return version;
            }
        }
        return null;
    }

    public boolean removeVersion(String id) {
        Version version = findVersion(id);
        if (version != null && !version.hasSubversions()) {
            if (version.getParent() != null) {
                version.getParent().getSubversions().remove(version);
            } else {
                mainVersions.remove(version);
            }
            allVersions.remove(version);

            version.setDeleted(true);
            deletedVersions.add(version);
            if (currentVersion == version) {
                currentVersion = version.getParent();
            }
            return true;
        }
        return false;
    }

    public List<Version> getPreOrderTraversal() {
        List<Version> result = new ArrayList<>();
        for (Version mainVersion : mainVersions) {
            preOrderHelper(mainVersion, result);
        }
        return result;
    }

    private void preOrderHelper(Version version, List<Version> result) {
        if (version == null) return;

        result.add(version);
        for (Version subversion : version.getSubversions()) {
            preOrderHelper(subversion, result);
        }
    }

    public List<Version> getPostOrderTraversal() {
        List<Version> result = new ArrayList<>();
        for (Version mainVersion : mainVersions) {
            postOrderHelper(mainVersion, result);
        }
        return result;
    }

    private void postOrderHelper(Version version, List<Version> result) {
        if (version == null) return;

        for (Version subversion : version.getSubversions()) {
            postOrderHelper(subversion, result);
        }
        result.add(version);
    }
}