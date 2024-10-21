package ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UI extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel contentPane;
    private JLabel operationLabel;  // Etiqueta para mostrar la operación seleccionada
    private JPanel fieldsPanel;     // Panel que contendrá los campos de formulario
    private JButton confirmButton;  // Botón para confirmar la operación
    private JToolBar crudToolbar;   // Cambiamos JPanel por JToolBar
    private String currentEntity;   // Entidad seleccionada
    private String currentAction;   // Acción CRUD seleccionada
    private JButton backButton;

    public UI() {
        setTitle("Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 450);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);
        setContentPane(contentPane);

        // Crear el panel principal (menú)
        contentPane.add(createMainMenuPane(), "mainPanel");

        // Panel de gestión reutilizable para CRUD
        contentPane.add(createGestionPanel(), "gestionPanel");

        cardLayout.show(contentPane, "mainPanel");
    }

    private JPanel createMainMenuPane() {
        JPanel mainMenuPane = new JPanel(new GridLayout(5, 1, 10, 10));
        mainMenuPane.setBorder(new EmptyBorder(100, 100, 100, 100));
        mainMenuPane.setBackground(Color.WHITE);  // Fondo blanco

        // Añadir los botones para cada entidad
        String[] entities = {"LIBROS", "AUTORES", "USUARIOS", "PRESTAMOS", "LIBRO_AUTOR"};
        for (String entity : entities) {
            JButton button = new JButton(entity);
            button.setFont(new Font("Tahoma", Font.PLAIN, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setBackground(new Color(240, 240, 240));  // Fondo gris claro
            button.setForeground(new Color(108, 108, 108));  // Texto gris oscuro
            button.addActionListener(e -> showGestionPanel(entity));  // Usamos un único método para cada entidad
            mainMenuPane.add(button);
        }

        return mainMenuPane;
    }

    private JPanel createGestionPanel() {
        JPanel gestionPanel = new JPanel();
        gestionPanel.setLayout(new BorderLayout(10, 10)); // Uso de BorderLayout para separar las secciones
        gestionPanel.setBackground(Color.WHITE);  // Fondo blanco

        // Barra de herramientas CRUD (dinámica) - parte superior
        crudToolbar = new JToolBar();
        crudToolbar.setFloatable(false); // Deshabilitar que la barra sea movible
        crudToolbar.setLayout(new FlowLayout(FlowLayout.CENTER));  // Centrar los botones
        crudToolbar.setBackground(new Color(240, 240, 240));  // Fondo blanco
        gestionPanel.add(crudToolbar, BorderLayout.NORTH);

        // Panel central que contendrá la etiqueta de operación, el formulario y el botón de confirmación
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBorder(new EmptyBorder(50, 70, 50, 70));
        centerPanel.setBackground(Color.WHITE);  // Fondo blanco

        // Etiqueta de operación seleccionada
        operationLabel = new JLabel("Selecciona una operación", SwingConstants.CENTER);
        operationLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
        centerPanel.add(operationLabel, BorderLayout.NORTH);

        // Panel para los campos dinámicos del formulario
        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(0, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);  // Fondo blanco
        centerPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Botón de confirmación
        confirmButton = new JButton("Confirmar");
        confirmButton.setVisible(false); // Ocultamos el botón inicialmente
        confirmButton.addActionListener(e -> confirmCrudAction());
        confirmButton.setPreferredSize(new Dimension(200, 50)); // Tamaño adecuado

        // Estilos de botón
        confirmButton.setFont(new Font("Tahoma", Font.PLAIN, 14));  // Fuente más simple
        confirmButton.setFocusPainted(false);  // Sin foco visible
        confirmButton.setBorderPainted(false);  // Sin borde
        confirmButton.setBackground(new Color(240, 240, 240));  // Gris claro de fondo
        confirmButton.setForeground(new Color(108, 108, 108));  // Gris oscuro para el texto
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Cursor tipo mano al pasar por encima

        centerPanel.add(confirmButton, BorderLayout.SOUTH);

        // Añadir panel central al centro del panel principal
        gestionPanel.add(centerPanel, BorderLayout.CENTER);

        // Botón de "Volver al Menú Principal"
        backButton = new JButton("Volver al Menú Principal");
        backButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setBackground(new Color(255, 255, 255));  // Fondo gris claro
        backButton.setForeground(new Color(108, 108, 108));  // Texto gris oscuro
        backButton.addActionListener(e -> {
            resetGestionPanel(); // Limpiar el formulario y restablecer la etiqueta al volver
            cardLayout.show(contentPane, "mainPanel"); // Mostrar el menú principal
        });
        gestionPanel.add(backButton, BorderLayout.SOUTH);

        return gestionPanel;
    }

    // Este método maneja tanto la generación del panel como la visualización del mismo
    private void showGestionPanel(String entity) {
        currentEntity = entity;
        resetGestionPanel();  // Limpiar el panel y restablecer la etiqueta
        updateCrudToolbar();  // Actualizamos la barra de botones CRUD según la entidad seleccionada
        cardLayout.show(contentPane, "gestionPanel");
    }

    // Actualizar la barra de herramientas CRUD dinámicamente según la entidad
    private void updateCrudToolbar() {
        crudToolbar.removeAll(); // Limpiar los botones anteriores

        // Definir acciones disponibles para cada entidad
        String[] actions = currentEntity.equals("PRESTAMOS") || currentEntity.equals("LIBRO_AUTOR")
                ? new String[]{"Crear", "Buscar"}
                : new String[]{"Crear", "Buscar", "Actualizar", "Eliminar"};

        // Añadir botones para las acciones
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
        operationLabel.setText("Selecciona una operación para " + currentEntity);
        confirmButton.setVisible(false); // Ocultar el botón hasta que se seleccione una acción
    }

    // Mostrar el formulario CRUD según la acción seleccionada
    private void showCrudForm(String action) {
        currentAction = action;
        fieldsPanel.removeAll();  // Limpiamos el fieldsPanel cuando se selecciona una nueva operación

        operationLabel.setText(action + " " + currentEntity);  // Actualizamos la etiqueta de operación

        // Añadir campos dinámicos según la entidad y la acción
        if (action.equals("Buscar") || action.equals("Eliminar")) {
            fieldsPanel.setBorder(new EmptyBorder(45, 0, 45, 0));
            addField("ID");
        } else if (currentEntity.equals("LIBROS")) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
            addField("Título");
            addField("ISBN");
        } else if (currentEntity.equals("AUTORES") || currentEntity.equals("USUARIOS")) {
            fieldsPanel.setBorder(new EmptyBorder(45, 0, 45, 0));
            addField("Nombre");
        } else if (currentEntity.equals("PRESTAMOS") || currentEntity.equals("LIBRO_AUTOR")) {
            fieldsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
            addField("ID Libro");
            addField("ID Autor");
        }

        confirmButton.setVisible(true);  // Mostrar el botón de confirmación una vez seleccionada una acción
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    // Método auxiliar para agregar campos de texto dinámicos
    private void addField(String labelText) {
        JLabel label = new JLabel(labelText, SwingConstants.LEFT);
        JTextField field = new JTextField();
        fieldsPanel.add(label);
        fieldsPanel.add(field);
    }

    // Confirmar la operación CRUD
    private void confirmCrudAction() {
        JOptionPane.showMessageDialog(this, currentAction + " de " + currentEntity + " completado!");
    }

    // Método para limpiar el fieldsPanel y restablecer la etiqueta de operación
    private void resetGestionPanel() {
        fieldsPanel.removeAll();          // Limpiar los campos anteriores
        operationLabel.setText("Selecciona una operación");  // Restablecer el texto de la etiqueta
        confirmButton.setVisible(false);  // Ocultar el botón de confirmación hasta que se seleccione una operación
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }
}
