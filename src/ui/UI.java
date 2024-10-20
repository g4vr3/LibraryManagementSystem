package ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UI extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPane;
    private JLabel operationLabel;
    private JPanel fieldsPanel;
    private JButton confirmButton;
    private JTextField idField, titleField, isbnField, nameField, libroIdField, autorIdField;
    private JLabel idLabel, titleLabel, isbnLabel, nameLabel, libroIdLabel, autorIdLabel;

    public UI() {
        setTitle("Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);
        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        JPanel mainMenuPane = createMainMenuPane();
        contentPane.add(mainMenuPane, "mainPanel");

        contentPane.add(createGestionPanel("Libro"), "libroPanel");
        contentPane.add(createGestionPanel("Autor"), "autorPanel");
        contentPane.add(createGestionPanel("Usuario"), "usuarioPanel");
        contentPane.add(createGestionPanel("Prestamo"), "prestamoPanel");
        contentPane.add(createGestionPanel("Libro_Autor"), "libroAutorPanel");

        setContentPane(contentPane);
        cardLayout.show(contentPane, "mainPanel");
    }

    private JPanel createMainMenuPane() {
        JPanel mainMenuPane = new JPanel(new GridLayout(5, 1, 10, 10));
        mainMenuPane.setBackground(new Color(255, 255, 255));
        mainMenuPane.setBorder(new EmptyBorder(50, 50, 50, 50));
        addMenuButton(mainMenuPane, "LIBROS", "libroPanel");
        addMenuButton(mainMenuPane, "AUTORES", "autorPanel");
        addMenuButton(mainMenuPane, "USUARIOS", "usuarioPanel");
        addMenuButton(mainMenuPane, "PRESTAMOS", "prestamoPanel");
        addMenuButton(mainMenuPane, "LIBRO_AUTOR", "libroAutorPanel");
        return mainMenuPane;
    }

    private void addMenuButton(JPanel mainMenuPane, String title, String panelName) {
        JButton button = new JButton(title);
        button.setFont(new Font("Tahoma", Font.PLAIN, 14));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, panelName);
            }
        });
        mainMenuPane.add(button);
    }

    private JPanel createGestionPanel(String entity) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        JPanel crudToolbar = createCrudToolbar(entity);
        panel.add(crudToolbar, BorderLayout.NORTH);
        JPanel centerPanel = createCenterPanel();
        panel.add(centerPanel, BorderLayout.CENTER);
        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.setPreferredSize(new Dimension(200, 40));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "mainPanel");
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCrudToolbar(String entity) {
        JPanel crudToolbar = new JPanel(new GridLayout(1, 4, 10, 10));
        crudToolbar.setBackground(new Color(255, 255, 255));
        crudToolbar.setBorder(new EmptyBorder(10, 10, 10, 10));
        String[] actions = {"Crear", "Buscar", "Actualizar", "Eliminar"};
        for (String action : actions) {
            JButton button = new JButton(action + " " + entity);
            button.setPreferredSize(new Dimension(120, 30));
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showCrudForm(action, entity);
                }
            });
            crudToolbar.add(button);
        }
        return crudToolbar;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(255, 255, 255));
        centerPanel.setBorder(new EmptyBorder(20, 50, 20, 50));

        operationLabel = new JLabel("Selecciona una operación", SwingConstants.CENTER);
        operationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        centerPanel.add(operationLabel, BorderLayout.NORTH);

        fieldsPanel = new JPanel();
        fieldsPanel.setBackground(new Color(255, 255, 255));
        centerPanel.add(fieldsPanel, BorderLayout.CENTER);

        confirmButton = new JButton("Confirmar");
        confirmButton.setVisible(false);
        confirmButton.setPreferredSize(new Dimension(120, 40));
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // CRUD logic here
                JOptionPane.showMessageDialog(null, operationLabel.getText() + " completado!");
            }
        });
        centerPanel.add(confirmButton, BorderLayout.SOUTH);

        return centerPanel;
    }

    private void showCrudForm(String action, String entity) {
        fieldsPanel.removeAll(); // Limpiamos el panel para agregar los nuevos campos

        idLabel = new JLabel("ID:", SwingConstants.LEFT);
        idField = new JTextField();
        idField.setHorizontalAlignment(SwingConstants.RIGHT);

        titleLabel = new JLabel("Título:", SwingConstants.LEFT);
        titleField = new JTextField();
        titleField.setHorizontalAlignment(SwingConstants.RIGHT);

        isbnLabel = new JLabel("ISBN:", SwingConstants.LEFT);
        isbnField = new JTextField();
        isbnField.setHorizontalAlignment(SwingConstants.RIGHT);

        nameLabel = new JLabel("Nombre:", SwingConstants.LEFT);
        nameField = new JTextField();
        nameField.setHorizontalAlignment(SwingConstants.RIGHT);

        libroIdLabel = new JLabel("ID Libro:", SwingConstants.LEFT);
        libroIdField = new JTextField();
        libroIdField.setHorizontalAlignment(SwingConstants.RIGHT);

        autorIdLabel = new JLabel("ID Autor:", SwingConstants.LEFT);
        autorIdField = new JTextField();
        autorIdField.setHorizontalAlignment(SwingConstants.RIGHT);

        switch (entity) {
            case "Libro":
                if (action.equals("Buscar") || action.equals("Eliminar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(idLabel);
                    fieldsPanel.add(idField);
                } else if (action.equals("Crear") || action.equals("Actualizar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 2, 10, 10));
                    fieldsPanel.add(titleLabel);
                    fieldsPanel.add(titleField);
                    fieldsPanel.add(isbnLabel);
                    fieldsPanel.add(isbnField);
                }
                break;
            case "Autor":
                if (action.equals("Buscar") || action.equals("Eliminar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(idLabel);
                    fieldsPanel.add(idField);
                } else if (action.equals("Crear") || action.equals("Actualizar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(nameLabel);
                    fieldsPanel.add(nameField);
                }
                break;
            case "Usuario":
                if (action.equals("Buscar") || action.equals("Eliminar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(idLabel);
                    fieldsPanel.add(idField);
                } else if (action.equals("Crear") || action.equals("Actualizar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(nameLabel);
                    fieldsPanel.add(nameField);
                }
                break;
            case "Prestamo":
                if (action.equals("Buscar") || action.equals("Eliminar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(idLabel);
                    fieldsPanel.add(idField);
                } else if (action.equals("Crear") || action.equals("Actualizar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 2, 10, 10));
                    fieldsPanel.add(libroIdLabel);
                    fieldsPanel.add(libroIdField);
                    fieldsPanel.add(autorIdLabel);
                    fieldsPanel.add(autorIdField);
                }
                break;
            case "Libro_Autor":
                if (action.equals("Buscar") || action.equals("Eliminar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 1, 10, 10));
                    fieldsPanel.add(idLabel);
                    fieldsPanel.add(idField);
                } else if (action.equals("Crear") || action.equals("Actualizar")) {
                    fieldsPanel.setLayout(new GridLayout(2, 2, 10, 10));
                    fieldsPanel.add(libroIdLabel);
                    fieldsPanel.add(libroIdField);
                    fieldsPanel.add(autorIdLabel);
                    fieldsPanel.add(autorIdField);
                }
                break;
            default:
                throw new IllegalArgumentException("Entidad no válida: " + entity);
        }

        confirmButton.setVisible(true); // Asegúrate de que el botón sea visible
        fieldsPanel.revalidate();  // Actualiza el panel
        fieldsPanel.repaint();     // Redibuja el panel
    }


}
