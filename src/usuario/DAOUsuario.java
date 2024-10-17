package usuario;

import exception.ServiceException;
import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOUsuario class handles CRUD operations for the Usuario (User) entity.
 *
 * @version 1.1
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
     * @throws ServiceException if there is an error during the creation.
     */
    public void create(DTOUsuario usuario) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, usuario.getNombre());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al crear usuario: " + e.getMessage());
        }
    }

    /**
     * Reads a user from the database based on the ID provided.
     *
     * @param id The ID of the user to read.
     * @return A DTOUsuario object containing the user's information, or null if not found.
     * @throws ServiceException if there is an error during the read.
     */
    public DTOUsuario read(int id) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return getUsuario(rs); // Convert the result to a DTOUsuario
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer usuario: " + e.getMessage());
        }
    }

    /**
     * Reads all users from the database.
     *
     * @return An ArrayList of DTOUsuario objects representing all users in the database.
     * @throws ServiceException if there is an error during the read.
     */
    public ArrayList<DTOUsuario> readAll() throws ServiceException {
        ArrayList<DTOUsuario> usuarios = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                usuarios.add(getUsuario(rs)); // Convert each result to a DTOUsuario
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    /**
     * Updates an existing user's information in the database.
     *
     * @param usuario The DTOUsuario object containing the updated user information.
     * @throws IllegalArgumentException if the user's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the update.
     */
    public void update(DTOUsuario usuario) throws ServiceException {
        if (usuario.getId() <= 0) {
            throw new IllegalArgumentException("ID de usuario no válido");
        }
        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, usuario.getNombre());
            pst.setInt(2, usuario.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Deletes a user from the database.
     *
     * @param usuario The DTOUsuario object containing the user's ID to delete.
     * @throws IllegalArgumentException if the user's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the deletion.
     */
    public void delete(DTOUsuario usuario) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, usuario.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar usuario: " + e.getMessage());
        }
    }

    /**
     * Converts a ResultSet row into a DTOUsuario object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOUsuario object representing the user.
     * @throws ServiceException if there is an error during the conversion.
     */
    private DTOUsuario getUsuario(ResultSet rs) throws ServiceException {
        try {
            String nombre = rs.getString("nombre");
            DTOUsuario usuario = new DTOUsuario(nombre);
            usuario.setId(rs.getInt("id")); // Assign the ID from the database
            return usuario;
        } catch (SQLException e) {
            throw new ServiceException("Error al leer ResultSet: " + e.getMessage());
        }
    }
}
