package co.edu.uptc.view;

import co.edu.uptc.controller.DocumentController;
import co.edu.uptc.model.Version;

import java.util.List;
import java.util.Scanner;

public class ConsoleView {
    private DocumentController controller;
    private Scanner scanner;

    public ConsoleView() {
        scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=== SISTEMA DE GESTIÓN DE VERSIONES DE DOCUMENTOS ===");
        System.out.print("Ingrese el nombre del documento: ");
        String documentName = scanner.nextLine();

        controller = new DocumentController(documentName);

        if (controller.getMainVersions().isEmpty()) {
            System.out.println("Documento nuevo. Debe crear al menos una versión principal.");
            createMainVersion();
        }

        boolean exit = false;
        while (!exit) {
            showMenu();
            int option = readIntOption();

            switch (option) {
                case 1:
                    createMainVersion();
                    break;
                case 2:
                    addSubversion();
                    break;
                case 3:
                    showVersionHistory();
                    break;
                case 4:
                    restoreVersion();
                    break;
                case 5:
                    deleteVersion();
                    break;
                case 6:
                    showVersionStructure(true);
                    break;
                case 7:
                    showVersionStructure(false);
                    break;
                case 8:
                    exit = true;
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }

        System.out.println("¡Hasta pronto!");
    }

    private void showMenu() {
        System.out.println("\n=== MENÚ ===");
        System.out.println("1. Agregar versión principal");
        System.out.println("2. Agregar subversión");
        System.out.println("3. Ver historial de versiones");
        System.out.println("4. Restaurar versión");
        System.out.println("5. Eliminar versión");
        System.out.println("6. Visualizar estructura (Preorden)");
        System.out.println("7. Visualizar estructura (Postorden)");
        System.out.println("8. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int readIntOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createMainVersion() {
        System.out.print("Ingrese el ID de la versión principal: ");
        String id = scanner.nextLine();
        System.out.println("Ingrese el contenido de la versión principal:");
        String content = scanner.nextLine();

        controller.addMainVersion(id, content);
        System.out.println("Versión principal creada con éxito.");
    }

    private void addSubversion() {
        System.out.print("Ingrese el ID de la versión padre: ");
        String parentId = scanner.nextLine();
        System.out.print("Ingrese el ID de la nueva subversión: ");
        String id = scanner.nextLine();
        System.out.println("Ingrese el contenido de la subversión:");
        String content = scanner.nextLine();

        controller.addSubversion(id, content, parentId);
        System.out.println("Subversión agregada con éxito.");
    }

    private void showVersionHistory() {
        List<Version> versions = controller.getVersionHistory();
        System.out.println("\n=== HISTORIAL DE VERSIONES ===");

        if (versions.isEmpty()) {
            System.out.println("No hay versiones disponibles.");
        } else {
            for (Version version : versions) {
                System.out.println(version);
                System.out.println("Contenido: " + version.getContent());
                System.out.println();
            }
        }
    }

    private void restoreVersion() {
        System.out.print("Ingrese el ID de la versión a restaurar: ");
        String id = scanner.nextLine();

        boolean restored = controller.restoreVersion(id);
        if (restored) {
            System.out.println("Versión restaurada con éxito.");
        } else {
            System.out.println("No se encontró la versión especificada.");
        }
    }

    private void deleteVersion() {
        System.out.print("Ingrese el ID de la versión a eliminar: ");
        String id = scanner.nextLine();

        boolean deleted = controller.deleteVersion(id);
        if (deleted) {
            System.out.println("Versión eliminada con éxito.");
        } else {
            System.out.println("No se pudo eliminar la versión. Verifique que exista y no tenga subversiones.");
        }
    }

    private void showVersionStructure(boolean preOrder) {
        List<Version> versions;

        if (preOrder) {
            versions = controller.getPreOrderTraversal();
            System.out.println("\n=== ESTRUCTURA DE VERSIONES (PREORDEN) ===");
        } else {
            versions = controller.getPostOrderTraversal();
            System.out.println("\n=== ESTRUCTURA DE VERSIONES (POSTORDEN) ===");
        }

        if (versions.isEmpty()) {
            System.out.println("No hay versiones disponibles.");
        } else {
            for (Version version : versions) {
                String parentInfo = (version.getParent() != null) ?
                        " (Padre: " + version.getParent().getId() + ")" : " (Raíz)";
                System.out.println(version + parentInfo);
            }
        }
    }


}