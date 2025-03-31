package co.edu.uptc.controller;


import co.edu.uptc.model.Document;
import co.edu.uptc.model.Version;
import co.edu.uptc.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class DocumentController {

    private Document document;

    public DocumentController(String documentName) {
        Document loadedDocument = FileUtil.loadDocument(documentName);
        if (loadedDocument != null) {
            this.document = loadedDocument;
        } else {
            this.document = new Document(documentName);
        }
    }

    public Document getDocument() {
        return document;
    }

    public void addMainVersion(String id, String content) {
        Version newVersion = new Version(id, content);
        document.addMainVersion(newVersion);
        saveDocument();
    }

    public void addSubversion(String id, String content, String parentId) {
        Version parent = document.findVersion(parentId);
        if (parent != null) {
            Version newVersion = new Version(id, content);
            document.addSubversion(newVersion, parent);
            saveDocument();
        }
    }

    public List<Version> getVersionHistory() {
        List<Version> history = new ArrayList<>(document.getAllVersions());
        history.addAll(document.getDeletedVersions());
        return history;
    }

    public List<Version> getMainVersions() {
        return document.getMainVersions();
    }

    public boolean restoreVersion(String id) {
        Version version = document.findDeletedVersion(id);

        if (version != null) {
            if (version.getParent() == null) {
                version.setDeleted(false);
                document.addMainVersion(version);
                document.getDeletedVersions().remove(version);
                saveDocument();
                return true;
            }
        }

        return false;
    }

    public boolean deleteVersion(String id) {
        boolean result = document.removeVersion(id);
        if (result) {
            saveDocument();
        }
        return result;
    }

    public List<Version> getPreOrderTraversal() {
        return document.getPreOrderTraversal();
    }

    public List<Version> getPostOrderTraversal() {
        return document.getPostOrderTraversal();
    }

    private void saveDocument() {
        FileUtil.saveDocument(document);
    }

    public boolean existsVersion(String id) {
        return document.findVersion(id) != null;
    }
}