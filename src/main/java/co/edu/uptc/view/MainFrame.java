package co.edu.uptc.view;

import co.edu.uptc.controller.DocumentController;
import co.edu.uptc.model.Version;
import co.edu.uptc.util.TreePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame extends JFrame {

    private DocumentController controller;
    private JPanel contentPane;
    private JTabbedPane tabbedPane;
    private JTextArea contentTextArea;
    private JList<Version> versionsList;
    private DefaultListModel<Version> versionsModel;
    private TreePanel treePanel;

    public MainFrame() {

        setTitle("Sistema de Gestión de Versiones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setLocationRelativeTo(null);

        initComponents();

        String documentName = JOptionPane.showInputDialog(this,
                "Ingrese el nombre del documento:",
                "Nuevo Documento",
                JOptionPane.QUESTION_MESSAGE);

        if (documentName == null || documentName.trim().isEmpty()) {
            System.exit(0);
        }

        controller = new DocumentController(documentName);
        setTitle("Sistema de Gestión de Versiones - " + documentName);

        if (controller.getMainVersions().isEmpty()) {
            createMainVersion();
        }

        updateVersionsList();
        updateTreeView();
    }

    private void initComponents() {

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(6, 1, 0, 10));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton btnAddMainVersion = new JButton("Agregar Versión Principal");
        JButton btnAddSubversion = new JButton("Agregar Subversión");
        JButton btnRestoreVersion = new JButton("Restaurar Versión");
        JButton btnDeleteVersion = new JButton("Eliminar Versión");
        JButton btnRefreshView = new JButton("Actualizar Vista");
        JButton btnPreOrder = new JButton("Recorrer en Preorden");
        JButton btnPostOrder = new JButton("Recorrer en Postorden");

        buttonPanel.add(btnPreOrder);
        buttonPanel.add(btnPostOrder);
        buttonPanel.add(btnAddMainVersion);
        buttonPanel.add(btnAddSubversion);
        buttonPanel.add(btnRestoreVersion);
        buttonPanel.add(btnDeleteVersion);
        buttonPanel.add(btnRefreshView);

        contentPane.add(buttonPanel, BorderLayout.WEST);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout(0, 0));
        tabbedPane.addTab("Historial de Versiones", null, historyPanel, null);

        versionsModel = new DefaultListModel<>();
        versionsList = new JList<>(versionsModel);
        versionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollVersions = new JScrollPane(versionsList);
        historyPanel.add(scrollVersions, BorderLayout.WEST);

        contentTextArea = new JTextArea();
        contentTextArea.setEditable(false);
        JScrollPane scrollContent = new JScrollPane(contentTextArea);
        historyPanel.add(scrollContent, BorderLayout.CENTER);

        treePanel = new TreePanel();
        JScrollPane scrollTree = new JScrollPane(treePanel);
        tabbedPane.addTab("Visualización de Árbol", null, scrollTree, null);

        btnPreOrder.addActionListener(e -> updateVersionList(controller.getPreOrderTraversal()));
        btnPostOrder.addActionListener(e -> updateVersionList(controller.getPostOrderTraversal()));

        btnAddMainVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createMainVersion();
            }
        });

        btnAddSubversion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSubversion();
            }
        });

        btnRestoreVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreVersion();
            }
        });

        btnDeleteVersion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteVersion();
            }
        });

        btnRefreshView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVersionsList();
                updateTreeView();
            }
        });

        versionsList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Version selected = versionsList.getSelectedValue();
                if (selected != null) {
                    contentTextArea.setText(selected.getContent());
                } else {
                    contentTextArea.setText("");
                }
            }
        });
    }

    private void updateVersionList(List<Version> versions) {
        versionsModel.clear();
        for (Version v : versions) {
            versionsModel.addElement(v);
        }
    }

    private void createMainVersion() {
        String id = JOptionPane.showInputDialog(this,
                "Ingrese el ID de la versión principal:",
                "Nueva Versión Principal",
                JOptionPane.QUESTION_MESSAGE);

        if (id != null && !id.trim().isEmpty()) {
            String content = JOptionPane.showInputDialog(this,
                    "Ingrese el contenido de la versión principal:",
                    "Nueva Versión Principal",
                    JOptionPane.QUESTION_MESSAGE);

            if (content != null) {
                controller.addMainVersion(id, content);
                updateVersionsList();
                updateTreeView();
                JOptionPane.showMessageDialog(this,
                        "Versión principal creada con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void createSubversion() {
        String parentId = JOptionPane.showInputDialog(this,
                "Ingrese el ID de la versión padre:",
                "Nueva Subversión",
                JOptionPane.QUESTION_MESSAGE);

        if (parentId != null && !parentId.trim().isEmpty()) {
            if (!controller.existsVersion(parentId)) { // Verifica si el padre existe
                JOptionPane.showMessageDialog(this,
                        "La versión padre no existe.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return; // Detiene la ejecución
            }

            String id = JOptionPane.showInputDialog(this,
                    "Ingrese el ID de la subversión:",
                    "Nueva Subversión",
                    JOptionPane.QUESTION_MESSAGE);

            if (id != null && !id.trim().isEmpty()) {
                String content = JOptionPane.showInputDialog(this,
                        "Ingrese el contenido de la subversión:",
                        "Nueva Subversión",
                        JOptionPane.QUESTION_MESSAGE);

                if (content != null) {
                    controller.addSubversion(id, content, parentId);
                    updateVersionsList();
                    updateTreeView();
                    JOptionPane.showMessageDialog(this,
                            "Subversión creada con éxito.",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

    private void restoreVersion() {
        Version selected = versionsList.getSelectedValue();

        if (selected != null) {
            if (selected.getParent() != null) {
                JOptionPane.showMessageDialog(this,
                        "No se puede restaurar una subversión. Solo se pueden restaurar versiones principales.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean restored = controller.restoreVersion(selected.getId());

            if (restored) {
                JOptionPane.showMessageDialog(this,
                        "Versión restaurada con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo restaurar la versión. Asegúrese de que sea una versión principal eliminada.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una versión para restaurar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVersion() {
        Version selected = versionsList.getSelectedValue();
        if (selected != null) {
            if (!controller.existsVersion(selected.getId())) { // Verifica si la versión ya fue eliminada
                JOptionPane.showMessageDialog(this,
                        "La versión ya ha sido eliminada.",
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (controller.deleteVersion(selected.getId())) {
                updateVersionsList();
                updateTreeView();
                JOptionPane.showMessageDialog(this,
                        "Versión eliminada con éxito.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se puede eliminar una versión con subversiones.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione una versión para eliminar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private void updateVersionsList() {
        versionsModel.clear();
        List<Version> versions = controller.getVersionHistory();
        for (Version version : versions) {
            versionsModel.addElement(version);
        }
    }

    private void updateTreeView() {
        treePanel.setDocument(controller.getDocument());
        treePanel.repaint();
    }
}