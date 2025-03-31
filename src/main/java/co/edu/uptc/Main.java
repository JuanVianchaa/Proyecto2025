package co.edu.uptc;

import co.edu.uptc.view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Intentar establecer el look and feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}

