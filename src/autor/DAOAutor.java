package autor;

import exception.ServiceException;
import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOAutor class handles CRUD operations for the Autor (Author) entity.
 *
 * @version 1.1
 */
public class DAOAutor {
    private static final String CREATE = "INSERT INTO Autor (nombre) VALUES (?)";
    private static final String READ = "SELECT * FROM Autor WHERE ID = ?";
    private static final String READ_ALL = "SELECT * FROM Autor";
    private static final String UPDATE = "UPDATE Autor SET nombre = ? WHERE ID = ?";
    private static final String DELETE = "DELETE FROM Autor WHERE ID = ?";
    private static Connection conexion;

    /**
     * Initializes a new DAOAutor instance by establishing a database connection.
     */
    public DAOAutor() {
        this.conexion = DDL.getConnection();
    }

    /**
     * Creates a new author in the database.
     *
     * @param autor The author object containing the name of the author.
     * @throws ServiceException if there is an error during the creation.
     */
    public void create(DTOAutor autor) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, autor.getNombre());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    autor.setId(rs.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al crear autor: " + e.getMessage());
        }
    }

    /**
     * Reads an author from the database based on the ID provided.
     *
     * @param id The ID of the author to read.
     * @return A DTOAutor object containing the author's information, or null if not found.
     * @throws ServiceException if there is an error during the read.
     */
    public DTOAutor read(int id) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return getAutor(rs); // Convert the result to a DTOAutor
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer autor: " + e.getMessage());
        }
    }

    /**
     * Reads all authors from the database.
     *
     * @return An ArrayList of DTOAutor objects representing all authors in the database.
     * @throws ServiceException if there is an error during the read.
     */
    public ArrayList<DTOAutor> readAll() throws ServiceException {
        ArrayList<DTOAutor> autores = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                autores.add(getAutor(rs)); // Convert each result to a DTOAutor
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los autores: " + e.getMessage());
        }
        return autores;
    }

    /**
     * Updates an existing author's information in the database.
     *
     * @param autor The DTOAutor object containing the updated author information.
     * @throws IllegalArgumentException if the author's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the update.
     */
    public void update(DTOAutor autor) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, autor.getNombre());
            pst.setInt(2, autor.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar autor: " + e.getMessage());
        }
    }

    /**
     * Deletes an author from the database.
     *
     * @param autor The DTOAutor object containing the author's ID to delete.
     * @throws IllegalArgumentException if the author's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the deletion.
     */
    public void delete(DTOAutor autor) throws ServiceException {
        if (autor.getId() <= 0) {
            throw new IllegalArgumentException("ID de autor no válido");
        }
        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, autor.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar autor: " + e.getMessage());
        }
    }

    /**
     * Converts a ResultSet row into a DTOAutor object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOAutor object representing the author.
     * @throws ServiceException if there is an error during the conversion.
     */
    private DTOAutor getAutor(ResultSet rs) throws ServiceException {
        try {
            String nombre = rs.getString("nombre");
            DTOAutor autor = new DTOAutor(nombre);
            autor.setId(rs.getInt("id")); // Assign the ID from the database
            return autor;
        } catch (SQLException e) {
            throw new ServiceException("Error al leer ResultSet: " + e.getMessage());
        }
    }
}
