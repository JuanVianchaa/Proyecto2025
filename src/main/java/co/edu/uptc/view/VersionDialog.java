package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VersionDialog extends JDialog {
    private JTextField idField;
    private JTextArea contentArea;
    private boolean confirmed;
    private String id;
    private String content;

    public VersionDialog(JFrame parent, String title) {
        super(parent, title, true);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("ID de Versión:"));
        idField = new JTextField(20);
        formPanel.add(idField);

        formPanel.add(new JLabel("Contenido:"));
        contentArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(contentArea);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("Aceptar");
        JButton cancelButton = new JButton("Cancelar");

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                id = idField.getText().trim();
                content = contentArea.getText();

                if (id.isEmpty()) {
                    JOptionPane.showMessageDialog(VersionDialog.this,
                            "El ID de versión no puede estar vacío.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmed = false;
                dispose();
            }
        });
    }
}
