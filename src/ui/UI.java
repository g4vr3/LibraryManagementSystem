package ui;

import autor.AutorService;
import exception.ServiceException;
import libro.LibroService;
import libro_autor.LibroAutorService;
import prestamo.PrestamoService;
import usuario.UsuarioService;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UI extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPane;
    private JLabel operationLabel;
    private JPanel fieldsPanel;
    private JButton confirmButton;
    private JToolBar crudToolbar;
    private String currentEntity;
    private String currentAction;

    // Mapa para almacenar los campos por nombre
    private HashMap<String, JTextField> inputFields;

    // Service integration
    private LibroService libroService;
    private AutorService autorService;
    private UsuarioService usuarioService;
    private PrestamoService prestamoService;
    private LibroAutorService libroAutorService;

    public UI() {
        // Inicializar el HashMap para almacenar los campos de entrada
        inputFields = new HashMap<>();

        // Frame settings
        setTitle("Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        setContentPane(contentPane);

        // Main panel creation
        contentPane.add(getMainMenuPanel(), "mainPanel");

        // Management panel creation (CRUD)
        contentPane.add(getManagementPanel(), "managementPanel");

        cardLayout.show(contentPane, "mainPanel");
    }

    private JPanel getMainMenuPanel() {
        JPanel mainMenuPane = new JPanel(new GridLayout(7, 1, 10, 10));
        mainMenuPane.setBorder(new EmptyBorder(50, 200, 50, 200));
        mainMenuPane.setBackground(Color.WHITE);  // Fondo blanco

        // Operation dynamic label
        operationLabel = new JLabel("Gestión Bibliotecaria", SwingConstants.CENTER);
        operationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        mainMenuPane.add(operationLabel);

        // Main menu information label
        JLabel menuInfoLabel = new JLabel("Selecciona una opción para su gestión", SwingConstants.CENTER);
        menuInfoLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
        mainMenuPane.add(menuInfoLabel);

        // Entity management buttons
        String[] entities = {"LIBRO", "AUTOR", "USUARIO", "PRÉSTAMO", "LIBRO_AUTOR"};
        for (String entity : entities) {
            JButton button = new JButton(entity);
            button.setFont(new Font("Tahoma", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(new Color(240, 240, 240));  // Fondo gris claro
            button.setForeground(new Color(108, 108, 108));  // Texto gris oscuro
            button.addActionListener(e -> showManagementPanel(entity));  // Usamos un único método para cada entidad
            mainMenuPane.add(button);
        }

        return mainMenuPane;
    }

    private JPanel getManagementPanel() {
        JPanel managementPanel = new JPanel();
        managementPanel.setLayout(new BorderLayout(10, 10));
        managementPanel.setBackground(Color.WHITE);

        // CRUD toolbar (dynamic)
        crudToolbar = new JToolBar();
        crudToolbar.setFloatable(false);
        crudToolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
        crudToolbar.setBackground(new Color(240, 240, 240));
        managementPanel.add(crudToolbar, BorderLayout.NORTH);

        // Center panel (contains operation label, crud form and confirm button)
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(50, 170, 50, 170));
        centerPanel.setBackground(Color.WHITE);  // Fondo blanco

        // Dynamic operation label
        operationLabel = new JLabel("Selecciona una operación", SwingConstants.CENTER);
        operationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        centerPanel.add(operationLabel, BorderLayout.NORTH);

        // Dynamic CRUD form panel
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(0, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);
        centerPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Confirm button
        confirmButton = getConfirmButton();
        centerPanel.add(confirmButton, BorderLayout.SOUTH);

        // Center panel on the main panel center
        managementPanel.add(centerPanel, BorderLayout.CENTER);

        // Home Button
        JButton homeButton = getHomeButton();
        managementPanel.add(homeButton, BorderLayout.SOUTH);

        return managementPanel;
    }

    private JButton getConfirmButton() {
        confirmButton = new JButton("Confirmar");
        confirmButton.setVisible(false); // hidden at start
        confirmButton.addActionListener(e -> doCrudAction());
        confirmButton.setPreferredSize(new Dimension(200, 50));
        // Button style
        confirmButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setBackground(new Color(240, 240, 240));
        confirmButton.setForeground(new Color(108, 108, 108));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return confirmButton;
    }

    private JButton getHomeButton() {
        JButton homeButton = new JButton("Volver al Menú Principal");
        homeButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        homeButton.setFocusPainted(false);
        homeButton.setBorderPainted(false);
        homeButton.setBackground(new Color(255, 255, 255));
        homeButton.setForeground(new Color(108, 108, 108));
        homeButton.addActionListener(e -> {
            resetManagementPanel();
            cardLayout.show(contentPane, "mainPanel"); // Show main menu
        });
        return homeButton;
    }

    private void showManagementPanel(String entity) {
        currentEntity = entity;
        resetManagementPanel();
        updateCrudToolbar();
        cardLayout.show(contentPane, "managementPanel");
    }

    private void updateCrudToolbar() {
        crudToolbar.removeAll(); // Clear panel

        // Operation definitions for each entity
        String[] actions;
        switch (currentEntity) {
            case "PRÉSTAMO" -> actions = new String[]{"Crear", "Buscar por Libro", "Buscar por Usuario"};
            case "LIBRO_AUTOR" -> actions = new String[]{"Crear", "Buscar por Libro", "Buscar por Autor"};
            default -> actions = new String[]{"Crear", "Buscar", "Actualizar", "Eliminar"};
        }

        // Operation Buttons definitions
        for (String action : actions) {
            JButton button = new JButton(action);
            button.setFont(new Font("Tahoma", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(new Color(240, 240, 240));  // Fondo gris claro
            button.setForeground(new Color(108, 108, 108));  // Texto gris oscuro
            button.addActionListener(e -> showCrudForm(action)); // Llamamos al método para mostrar el formulario
            crudToolbar.add(button);
        }

        crudToolbar.revalidate();
        crudToolbar.repaint();
        operationLabel.setText("Selecciona una operación para " + currentEntity.toLowerCase());
    }

    private void showCrudForm(String action) {
        currentAction = action;
        fieldsPanel.removeAll();  // Clear panel
        inputFields.clear(); // Limpiar los campos previos

        operationLabel.setText(action + " (" + currentEntity.toLowerCase() + ")");  // Operation label update

        // Dynamic fields for current entity
        if (action.startsWith("Buscar") || action.equals("Eliminar")) {
            fieldsPanel.setBorder(new EmptyBorder(45, 0, 45, 0));
            addField("ID");
        } else if (currentEntity.equals("LIBRO")) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
            addField("Título");
            addField("ISBN");
        } else if (currentEntity.equals("AUTOR") || currentEntity.equals("USUARIO")) {
            fieldsPanel.setBorder(new EmptyBorder(45, 0, 45, 0));
            addField("Nombre");
        } else if (currentEntity.equals("PRÉSTAMO")) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
            addField("ID Libro");
            addField("ID Usuario");
        } else if (currentEntity.equals("LIBRO_AUTOR")) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
            addField("ID Libro");
            addField("ID Autor");
        }

        confirmButton.setVisible(true);
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    private void addField(String labelText) {
        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        JTextField field = new JTextField();
        inputFields.put(labelText, field); // Almacena el campo en el mapa
        fieldsPanel.add(label);
        fieldsPanel.add(field);
    }

    private void resetManagementPanel() {
        fieldsPanel.removeAll();          // Clear panel
        confirmButton.setVisible(false);  // Hide confirm button
        inputFields.clear();              // Clear input fields
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    private void doCrudAction() {
        switch (currentEntity) {
            case "LIBRO":
                try {
                    libroService = new LibroService(new LibroAutorService());
                    JOptionPane.showMessageDialog(this, currentAction + " " + currentEntity + " completado!");
                } catch (ServiceException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                switch (currentAction) {
                    case "Crear":
                        // check the fields are completed
                        if (!areFieldsFilled("Título", "ISBN")) {
                            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return;  // stop
                        }
                        String title = inputFields.get("Título").getText();
                        String isbn = inputFields.get("ISBN").getText();
                        try {
                            libroService.createLibro(title, isbn);
                        } catch (ServiceException e) {
                            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;

                    case "Buscar":
                        String id = inputFields.get("ID").getText();
                        break;

                    case "Actualizar":
                        break;

                    case "Eliminar":
                        id = inputFields.get("ID").getText();
                        break;
                }
                break;

            case "AUTOR":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("Nombre")) {
                            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        String nombre = inputFields.get("Nombre").getText();
                        break;

                    case "Buscar":
                        String id = inputFields.get("ID").getText();
                        break;

                    case "Actualizar":
                        break;

                    case "Eliminar":
                        id = inputFields.get("ID").getText();
                        break;
                }
                break;

            case "USUARIO":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("Nombre")) {
                            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        String nombre = inputFields.get("Nombre").getText();
                        break;

                    case "Buscar":
                        String id = inputFields.get("ID").getText();
                        break;

                    case "Actualizar":
                        break;

                    case "Eliminar":
                        id = inputFields.get("ID").getText();
                        break;
                }
                break;

            case "PRÉSTAMO":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("ID Libro", "ID Usuario")) {
                            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        String libroId = inputFields.get("ID Libro").getText();
                        String usuarioId = inputFields.get("ID Usuario").getText();
                        break;

                    case "Buscar por Libro":
                        libroId = inputFields.get("ID Libro").getText();
                        break;

                    case "Buscar por Usuario":
                        usuarioId = inputFields.get("ID Usuario").getText();
                        break;
                }
                break;

            case "LIBRO_AUTOR":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("ID Libro", "ID Autor")) {
                            JOptionPane.showMessageDialog(this, "Por favor, rellena todos los campos.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                        String libroId = inputFields.get("ID Libro").getText();
                        String autorId = inputFields.get("ID Autor").getText();
                        break;

                    case "Buscar por Libro":
                        libroId = inputFields.get("ID Libro").getText();
                        break;

                    case "Buscar por Autor":
                        autorId = inputFields.get("ID Autor").getText();
                        break;
                }
                break;
        }
    }

    private boolean areFieldsFilled(String... fieldKeys) {
        for (String key : fieldKeys) {
            JTextField field = inputFields.get(key);
            if (field == null || field.getText().trim().isEmpty()) {
                return false;  // one field is uncompleted at least
            }
        }
        return true;  // all the fields are completed
    }


}
