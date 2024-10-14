package autor;

import jdbc.DDL;

import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOAutor class handles CRUD operations for the Autor (Author) entity.
 *
 * @version 1.0.1
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
     * @return true if the author was successfully created, false otherwise.
     */
    public boolean create(DTOAutor autor) {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, autor.getNombre());
            boolean successful = pst.executeUpdate() > 0; // true if successfully created

            if (successful) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        autor.setId(rs.getInt(1)); // Set the generated ID
                    }
                }
            }
            return successful;
        } catch (SQLException e) {
            System.err.println("Error al crear autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads an author from the database based on the ID provided.
     *
     * @param id The ID of the author to read.
     * @return A DTOAutor object containing the author's information, or null if not found.
     */
    public DTOAutor read(int id) {
        DTOAutor autor = null;
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                autor = getAutor(rs); // Convert the result to a DTOAutor
            }
        } catch (SQLException e) {
            System.err.println("Error al leer autor: " + e.getMessage());
        }
        return autor;
    }

    /**
     * Reads all authors from the database.
     *
     * @return An ArrayList of DTOAutor objects representing all authors in the database.
     */
    public ArrayList<DTOAutor> readAll() {
        ArrayList<DTOAutor> autores = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                autores.add(getAutor(rs)); // Convert each result to a DTOAutor
            }
        } catch (SQLException e) {
            System.err.println("Error al leer todos los autores: " + e.getMessage());
        }
        return autores;
    }

    /**
     * Updates an existing author's information in the database.
     *
     * @param autor The DTOAutor object containing the updated author information.
     * @return true if the update was successful, false otherwise.
     * @throws IllegalArgumentException if the author's ID is invalid (less than or equal to 0).
     */
    public boolean update(DTOAutor autor) {
        if (autor.getId() <= 0) {
            throw new IllegalArgumentException("ID de autor no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, autor.getNombre());
            pst.setInt(2, autor.getId());
            return pst.executeUpdate() > 0; // true if updated successfully
        } catch (SQLException e) {
            System.err.println("Error al actualizar autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes an author from the database.
     *
     * @param autor The DTOAutor object containing the author's ID to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws IllegalArgumentException if the author's ID is invalid (less than or equal to 0).
     */
    public boolean delete(DTOAutor autor) {
        if (autor.getId() <= 0) {
            throw new IllegalArgumentException("ID de autor no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, autor.getId());
            return pst.executeUpdate() > 0; // true if deleted successfully
        } catch (SQLException e) {
            System.err.println("Error al eliminar autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a ResultSet row into a DTOAutor object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOAutor object representing the author.
     */
    private DTOAutor getAutor(ResultSet rs) {
        DTOAutor autor = null;
        try {
            String nombre = rs.getString("nombre");
            autor = new DTOAutor(nombre);
            autor.setId(rs.getInt("id")); // Assign the ID from the database
        } catch (SQLException e) {
            System.err.println("Error al leer ResultSet: " + e.getMessage());
        }
        return autor;
    }
}
