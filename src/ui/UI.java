package ui;

import autor.AutorService;
import autor.DTOAutor;
import exception.ServiceException;
import libro.DTOLibro;
import libro.LibroService;
import libro_autor.DTOLibroAutor;
import libro_autor.LibroAutorService;
import prestamo.DTOPrestamo;
import prestamo.PrestamoService;
import usuario.DTOUsuario;
import usuario.UsuarioService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

/**
 * User interface for managing books, authors, users, and loans.
 *
 * Provides CRUD functionalities and input validation,
 * displaying error and confirmation messages to the user.
 *
 * @author Izan Gómez de la Fuente
 * @author Javier Agudo Fernández
 * @version 1.0
 */
public class UI extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPane;
    // key: input field name, value: field
    private final HashMap<String, JTextField> inputFields;
    private JLabel operationLabel;
    private JPanel fieldsPanel;
    private JButton confirmButton;
    private JToolBar crudToolbar;
    private String currentEntity;
    private String currentAction;
    // Service integration
    private LibroService libroService;
    private AutorService autorService;
    private UsuarioService usuarioService;
    private PrestamoService prestamoService;
    private LibroAutorService libroAutorService;

    /**
     * Instantiates a new Ui.
     */
    public UI() {
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

    /**
     * Creates and returns the main menu panel for the library management system.
     * The panel includes a title label, an information label, and buttons for managing different entities.
     * Each button corresponds to an entity (LIBRO, AUTOR, USUARIO, PRÉSTAMO, LIBRO_AUTOR) and triggers the display
     * of the respective management panel when clicked.
     *
     * @return a JPanel containing the main menu interface.
     */
    private JPanel getMainMenuPanel() {
        JPanel mainMenuPane = new JPanel(new GridLayout(7, 1, 10, 10));
        mainMenuPane.setBorder(new EmptyBorder(50, 200, 50, 200));
        mainMenuPane.setBackground(Color.WHITE);

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
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(new Color(108, 108, 108));
            button.addActionListener(e -> showManagementPanel(entity));
            mainMenuPane.add(button);
        }

        return mainMenuPane;
    }

    /**
     * Creates and returns the management panel for the library system.
     * The panel includes a toolbar for CRUD operations, a dynamic label to display the selected operation,
     * a dynamic form panel for input fields, and a confirm button for the selected action.
     * Also includes a button to return to the home menu.
     *
     * @return a JPanel containing the management interface for CRUD operations.
     */
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
    /**
     * Creates and returns the confirm button for the CRUD operations.
     * The button is initially hidden and becomes visible when an operation is selected.
     * It triggers the execution of a CRUD action when clicked.
     * The button is styled with a custom background, font, and cursor.
     *
     * @return a JButton for confirming CRUD actions.
     */
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
    /**
     * Creates and returns the home button that navigates back to the main menu.
     * When clicked, it resets the management panel and switches the view to the main menu panel.
     * The button is styled with a custom background and font for visual consistency.
     *
     * @return a JButton for navigating back to the main menu.
     */

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
    /**
     * Displays the management panel for the specified entity.
     * Resets the management panel and updates the CRUD toolbar before showing the panel.
     *
     * @param entity the entity to manage (e.g., LIBRO, AUTOR, USUARIO).
     */
    private void showManagementPanel(String entity) {
        currentEntity = entity;
        resetManagementPanel();
        updateCrudToolbar();
        cardLayout.show(contentPane, "managementPanel");
    }
    /**
     * Updates the CRUD toolbar based on the current entity.
     * Clears existing buttons, defines new action buttons, and sets the operation label.
     */
    private void updateCrudToolbar() {
        crudToolbar.removeAll(); // Clear panel

        // Operation definitions for each entity
        String[] actions;
        switch (currentEntity) {
            case "PRÉSTAMO" -> actions = new String[]{"Crear", "Buscar por Libro", "Buscar por Usuario", "Eliminar"};
            case "LIBRO_AUTOR" -> actions = new String[]{"Crear", "Buscar por Libro", "Buscar por Autor"};
            default -> actions = new String[]{"Crear", "Buscar", "Actualizar", "Eliminar"};
        }

        // Operation Buttons definitions
        for (String action : actions) {
            JButton button = new JButton(action);
            button.setFont(new Font("Tahoma", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(new Color(108, 108, 108));
            button.addActionListener(e -> showCrudForm(action));
            crudToolbar.add(button);
        }

        crudToolbar.revalidate();
        crudToolbar.repaint();
        operationLabel.setText("Selecciona una operación para " + currentEntity.toLowerCase());
    }
    /**
     * Displays the CRUD form for the specified action and current entity.
     * Clears existing fields and sets up dynamic input fields based on the action.
     * Updates the operation label and adjusts the panel border according to the number of fields.
     *
     * @param action the CRUD action to perform (e.g., Crear, Buscar, Actualizar, Eliminar).
     * @throws IllegalStateException if an unexpected action is provided.
     */
    private void showCrudForm(String action) {
        currentAction = action;
        fieldsPanel.removeAll();  // Clear panel
        inputFields.clear(); // Clear input fields

        operationLabel.setText(action + " (" + currentEntity.toLowerCase() + ")");  // Operation label update

        // Initialize a count for the number of fields to be added
        int fieldCount = 0;

        // Dynamic fields for each action and entity
        switch (action) {
            case "Crear" -> {
                switch (currentEntity) {
                    case "LIBRO" -> {
                        addField("Título");
                        addField("ISBN");
                        fieldCount = 2;
                    }
                    case "AUTOR", "USUARIO" -> {
                        addField("Nombre");
                        fieldCount = 1;
                    }
                    case "PRÉSTAMO" -> {
                        addField("ID Libro");
                        addField("ID Usuario");
                        fieldCount = 2;
                    }
                    case "LIBRO_AUTOR" -> {
                        addField("ID Libro");
                        addField("ID Autor");
                        fieldCount = 2;
                    }
                }
            }
            // Case when the action starts with "Buscar"
            case String s when s.startsWith("Buscar") -> {
                addField("ID");
                fieldCount = 1;
            }
            case "Actualizar" -> {
                switch (currentEntity) {
                    case "LIBRO" -> {
                        addField("ID");
                        addField("Título");
                        addField("ISBN");
                        fieldCount = 3;
                    }
                    case "AUTOR", "USUARIO" -> {
                        addField("ID");
                        addField("Nombre");
                        fieldCount = 2;
                    }
                }
            }
            case "Eliminar" -> {
                addField("ID");
                fieldCount = 1;
            }
            default -> throw new IllegalStateException("Unexpected value: " + action);
        }

        // Set the border based on the number of fields added
        if (fieldCount == 1) {
            fieldsPanel.setBorder(new EmptyBorder(42, 0, 42, 0));
        } else if (fieldCount == 2) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        } else if (fieldCount == 3) {
            fieldsPanel.setBorder(null); // No border
        }


        confirmButton.setVisible(true);
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    /**
     * Adds a labeled input field to the fields panel.
     * Stores the JTextField in a map for later access.
     * @param labelText the text to display on the label for the input field.
     */
    private void addField(String labelText) {
        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        JTextField field = new JTextField();
        inputFields.put(labelText, field); // Almacena el campo en el mapa
        fieldsPanel.add(label);
        fieldsPanel.add(field);
    }
    /**
     * Resets the management panel by clearing all input fields and hiding the confirm button.
     * Updates the panel to show the changes.
     */

    private void resetManagementPanel() {
        fieldsPanel.removeAll();          // Clear panel
        confirmButton.setVisible(false);  // Hide confirm button
        inputFields.clear();              // Clear input fields
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }
    /**
     * Executes the current CRUD action based on the selected entity and action.
     * Instantiate service classes and performs the requested operation, showing appropriate
     * messages based on the success or the failure of this operations.
     */
    private void doCrudAction() {
        try {
            libroAutorService = new LibroAutorService();
            libroService = new LibroService(libroAutorService);
            autorService = new AutorService(libroAutorService);
            usuarioService = new UsuarioService();
            prestamoService = new PrestamoService(libroService,usuarioService);

        } catch (ServiceException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        switch (currentEntity) {
            case "LIBRO":

                switch (currentAction) {
                    case "Crear":
                        // check the fields are completed
                        if (!areFieldsFilled("Título", "ISBN")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;  // stop
                        }

                        try {
                            String title = inputFields.get("Título").getText();
                            String isbn = inputFields.get("ISBN").getText();
                            libroService.createLibro(title, isbn);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Buscar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;  // stop
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            DTOLibro libro = libroService.findLibroById(id);
                            if (libro != null) {
                                JOptionPane.showMessageDialog(this, libro.toString(), "Libro encontrado", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Actualizar":
                        try {
                            if (!areFieldsFilled("ID")) {
                                showWarningMessage("El ID es obligatorio para actualizar", "Campos incompletos");
                                return;  // stop
                            } else if (!areFieldsFilled("Título") && !areFieldsFilled("ISBN")) {
                                showWarningMessage("Rellena al menos un campo para actualizar", "Campos incompletos");
                                return;
                            }
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            String title = inputFields.get("Título").getText();
                            String isbn = inputFields.get("ISBN").getText();
                            libroService.updateLibro(id, title, isbn);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Eliminar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para eliminar", "Campos incompletos");
                            return;  // stop
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            libroService.deleteLibro(id);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;
                }
                break;

            case "AUTOR":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("Nombre")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }
                        try {
                            String nombre = inputFields.get("Nombre").getText();
                            autorService.createAutor(nombre);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }

                        break;

                    case "Buscar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;
                        }

                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            DTOAutor autor = autorService.findAutorById(id);
                            if (autor != null) {
                                JOptionPane.showMessageDialog(this, autor.toString(), "Autor encontrado", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Actualizar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para actualizar", "Campos incompletos");
                            return;  // stop
                        } else if (!areFieldsFilled("Nombre")) {
                            showWarningMessage("Rellena al menos un campo para actualizar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            String nombre = inputFields.get("Nombre").getText();
                            autorService.updateAutor(id, nombre);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Eliminar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para eliminar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            autorService.deleteAutor(id);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;
                }
                break;

            case "USUARIO":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("Nombre")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }

                        try {
                            String nombre = inputFields.get("Nombre").getText();
                            usuarioService.createUsuario(nombre);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Buscar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            DTOUsuario usuario = usuarioService.findUsuarioById(id);
                            if (usuario != null) {
                                JOptionPane.showMessageDialog(this, usuario.toString(), "Usuario encontrado", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Actualizar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para actualizar", "Campos incompletos");
                            return;  // stop
                        } else if (!areFieldsFilled("Nombre")) {
                            showWarningMessage("Rellena al menos un campo para actualizar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            String nombre = inputFields.get("Nombre").getText();
                            usuarioService.updateUsuario(id, nombre);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Eliminar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            usuarioService.deleteUsuario(id);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;
                }
                break;

            case "PRÉSTAMO":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("ID Libro", "ID Usuario")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }
                        try {
                            int usuarioId = Integer.parseInt(inputFields.get("ID Usuario").getText());
                            int libroId = Integer.parseInt(inputFields.get("ID Libro").getText());
                            prestamoService.createPrestamo(usuarioId, libroId);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Buscar por Libro":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer libroId = Integer.parseInt(inputFields.get("ID").getText());
                            List<DTOPrestamo> prestamos = prestamoService.findPrestamosByLibroId(libroId);
                            if (!prestamos.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Historial de préstamos del libro con ID: ").append(libroId).append("\n");
                                for (DTOPrestamo prestamo : prestamos) {
                                    sb.append("-\n").append(prestamo.toString()).append("\n");
                                }
                                JOptionPane.showMessageDialog(this, sb.toString(), "Préstamos encontrados", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Buscar por Usuario":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer usuarioId = Integer.parseInt(inputFields.get("ID").getText());
                            List<DTOPrestamo> prestamos = prestamoService.findPrestamosByUsuarioId(usuarioId);
                            if (!prestamos.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Historial de préstamos del usuario con ID: ").append(usuarioId).append("\n");
                                for (DTOPrestamo prestamo : prestamos) {
                                    sb.append("-\n").append(prestamo.toString()).append("\n");
                                }
                                JOptionPane.showMessageDialog(this, sb.toString(), "Préstamos encontrados", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Eliminar":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para eliminar", "Campos incompletos");
                            return;
                        }
                        try {
                            Integer id = Integer.parseInt(inputFields.get("ID").getText());
                            prestamoService.deletePrestamo(id);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;
                }
                break;

            case "LIBRO_AUTOR":
                switch (currentAction) {
                    case "Crear":
                        if (!areFieldsFilled("ID Libro", "ID Autor")) {
                            showWarningMessage("Rellena todos los campos", "Campos incompletos");
                            return;
                        }
                        try {
                            int libroId = Integer.parseInt(inputFields.get("ID Libro").getText());
                            int autorId = Integer.parseInt(inputFields.get("ID Autor").getText());
                            libroAutorService.createLibroAutor(libroId, autorId);
                            showConfirmMessage();
                        } catch (ServiceException e) {
                            if (e.getMessage().contains("foreign")){
                                JOptionPane.showMessageDialog(this, "El libro o el autor no existen", "Error al " + currentAction.toLowerCase() + " " + currentEntity.toLowerCase(), JOptionPane.ERROR_MESSAGE);
                            } else {
                                showErrorMessage(e);
                            }
                        }
                        break;

                    case "Buscar por Libro":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;
                        }
                        try {
                            int libroId = Integer.parseInt(inputFields.get("ID").getText());
                            List<DTOLibroAutor> libroAutores = libroAutorService.findRelationsByLibroId(libroId);
                            if (!libroAutores.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Autores del libro con ID: ").append(libroId).append("\n");
                                for (DTOLibroAutor libroAutor : libroAutores) {
                                    sb.append("-\n").append("Autor con ID: ").append(libroAutor.getAutorId()).append("\n");
                                }
                                JOptionPane.showMessageDialog(this, sb.toString(), "Autores encontrados para el libro", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;

                    case "Buscar por Autor":
                        if (!areFieldsFilled("ID")) {
                            showWarningMessage("El ID es obligatorio para buscar", "Campos incompletos");
                            return;
                        }
                        try {
                            int autorId = Integer.parseInt(inputFields.get("ID").getText());
                            List<DTOLibroAutor> librosAutor = libroAutorService.findRelationsByAutorId(autorId);
                            if (!librosAutor.isEmpty()) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Libros del autor con ID: ").append(autorId).append("\n");
                                for (DTOLibroAutor libroAutor : librosAutor) {
                                    sb.append("-\n").append("Libro con ID: ").append(libroAutor.getLibroId()).append("\n");
                                }
                                JOptionPane.showMessageDialog(this, sb.toString(), "Libros encontrados para el autor", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } catch (ServiceException e) {
                            showErrorMessage(e);
                        }
                        break;
                }
                break;
        }

    }

    /**
     * Checks if the specified fields are filled
     *
     * @param fieldKeys the keys of the fields to check
     * @return true if all specified fields are filled; false if not
     */
    private boolean areFieldsFilled(String... fieldKeys) {
        for (String key : fieldKeys) {
            JTextField field = inputFields.get(key);
            if (field == null || field.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Displays a warning message dialog with a specific message and a title.
     *
     * @param message the message to display
     * @param title the title of the dialog
     */
    private void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays a confirmation message dialog indicating the completion of the current action.
     */
    private void showConfirmMessage() {
        JOptionPane.showMessageDialog(this, currentAction + " " + currentEntity.toLowerCase() + " completado!");
    }

    /**
     * Displays an error message dialog with the specified exception message.
     *
     * @param e the exception whose message is going to be displayed
     */
    private void showErrorMessage(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), "Error al " + currentAction.toLowerCase() + " " + currentEntity.toLowerCase(), JOptionPane.ERROR_MESSAGE);
    }

}
