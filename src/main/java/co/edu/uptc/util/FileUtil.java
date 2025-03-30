package co.edu.uptc.util;

import co.edu.uptc.model.Document;

import java.io.*;

public class FileUtil {
    private static final String FILE_EXTENSION = ".txt";
    private static final String DOCUMENTS_DIR = "documents/";

    static {
        File dir = new File(DOCUMENTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static void saveDocument(Document document) {
        String fileName = DOCUMENTS_DIR + document.getName() + FILE_EXTENSION;

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(document);
            System.out.println("Documento guardado correctamente en: " + fileName);
        } catch (IOException e) {
            System.err.println("Error al guardar el documento: " + e.getMessage());
        }
    }

    public static Document loadDocument(String documentName) {
        String fileName = DOCUMENTS_DIR + documentName + FILE_EXTENSION;
        File file = new File(fileName);

        if (!file.exists()) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Document document = (Document) ois.readObject();
            System.out.println("Documento cargado correctamente desde: " + fileName);
            return document;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar el documento: " + e.getMessage());
            return null;
        }
    }
}