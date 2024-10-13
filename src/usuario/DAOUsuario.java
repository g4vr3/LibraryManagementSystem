package usuario;

import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOUsuario class handles CRUD operations for the Usuario (User) entity.
 *
 * @version 1.0
 */
public class DAOUsuario {
    private static final String CREATE = "INSERT INTO Usuario (nombre) VALUES (?)";
    private static final String READ = "SELECT * FROM Usuario WHERE ID = ?";
    private static final String READ_ALL = "SELECT * FROM Usuario";
    private static final String UPDATE = "UPDATE Usuario SET nombre = ? WHERE ID = ?";
    private static final String DELETE = "DELETE FROM Usuario WHERE ID = ?";

    private static Connection conexion;

    /**
     * Initializes a new DAOUsuario instance by establishing a database connection.
     */
    public DAOUsuario() {
        this.conexion = DDL.getConnection();
    }

    /**
     * Creates a new user in the database.
     *
     * @param usuario The user object containing the name of the user.
     * @return true if the user was successfully created, false otherwise.
     */
    public boolean create(DTOUsuario usuario) {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, usuario.getNombre());
            boolean successful = pst.executeUpdate() > 0; // true if successfully created

            if (successful) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1)); // Set the generated ID
                    }
                }
            }
            return successful;
        } catch (SQLException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a user from the database based on the ID provided.
     *
     * @param id The ID of the user to read.
     * @return A DTOUsuario object containing the user's information, or null if not found.
     */
    public DTOUsuario read(int id) {
        DTOUsuario usuario = null;
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                usuario = getUsuario(rs); // Convert the result to a DTOUsuario
            }
        } catch (SQLException e) {
            System.err.println("Error al leer usuario: " + e.getMessage());
        }
        return usuario;
    }

    /**
     * Reads all users from the database.
     *
     * @return An ArrayList of DTOUsuario objects representing all users in the database.
     */
    public ArrayList<DTOUsuario> readAll() {
        ArrayList<DTOUsuario> usuarios = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                usuarios.add(getUsuario(rs)); // Convert each result to a DTOUsuario
            }
        } catch (SQLException e) {
            System.err.println("Error al leer todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Updates an existing user's information in the database.
     *
     * @param usuario The DTOUsuario object containing the updated user information.
     * @return true if the update was successful, false otherwise.
     * @throws IllegalArgumentException if the user's ID is invalid (less than or equal to 0).
     */
    public boolean update(DTOUsuario usuario) {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("ID de usuario no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, usuario.getNombre());
            pst.setInt(2, usuario.getId());
            return pst.executeUpdate() > 0; // true if updated successfully
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param usuario The DTOUsuario object containing the user's ID to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws IllegalArgumentException if the user's ID is invalid (less than or equal to 0).
     */
    public boolean delete(DTOUsuario usuario) {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("ID de usuario no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, usuario.getId());
            return pst.executeUpdate() > 0; // true if deleted successfully
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a ResultSet row into a DTOUsuario object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOUsuario object representing the user.
     */
    private DTOUsuario getUsuario(ResultSet rs) {
        DTOUsuario usuario = null;
        try {
            String nombre = rs.getString("nombre");
            usuario = new DTOUsuario(nombre);
            usuario.setId(rs.getInt("ID")); // Assign the ID from the database
        } catch (SQLException e) {
            System.err.println("Error al leer ResultSet: " + e.getMessage());
        }
        return usuario;
    }
}
