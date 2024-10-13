package libro;

import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOLibro class handles CRUD operations for the Libro (Book) entity.
 *
 * @version 1.0
 */
public class DAOLibro {
    private static final String CREATE = "INSERT INTO Libro (titulo, isbn) VALUES (?, ?)";
    private static final String READ = "SELECT * FROM Libro WHERE ID = ?";
    private static final String READ_ALL = "SELECT * FROM Libro";
    private static final String UPDATE = "UPDATE Libro SET titulo = ?, isbn = ? WHERE ID = ?";
    private static final String DELETE = "DELETE FROM Libro WHERE ID = ?";

    private static Connection conexion;

    /**
     * Initializes a new DAOLibro instance by establishing a database connection.
     */
    public DAOLibro() {
        this.conexion = DDL.getConnection();
    }

    /**
     * Creates a new book in the database.
     *
     * @param libro The book object containing the title and ISBN of the book.
     * @return true if the book was successfully created, false otherwise.
     */
    public boolean create(DTOLibro libro) {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, libro.getTitulo());
            pst.setString(2, libro.getIsbn());
            boolean successful = pst.executeUpdate() > 0; // true if successfully created

            if (successful) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getInt(1)); // Set the generated ID
                    }
                }
            }
            return successful;
        } catch (SQLException e) {
            System.err.println("Error al crear libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a book from the database based on the ID provided.
     *
     * @param id The ID of the book to read.
     * @return A DTOLibro object containing the book's information, or null if not found.
     */
    public DTOLibro read(int id) {
        DTOLibro libro = null;
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                libro = getLibro(rs); // Convert the result to a DTOLibro
            }
        } catch (SQLException e) {
            System.err.println("Error al leer libro: " + e.getMessage());
        }
        return libro;
    }

    /**
     * Reads all books from the database.
     *
     * @return An ArrayList of DTOLibro objects representing all books in the database.
     */
    public ArrayList<DTOLibro> readAll() {
        ArrayList<DTOLibro> libros = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                libros.add(getLibro(rs)); // Convert each result to a DTOLibro
            }
        } catch (SQLException e) {
            System.err.println("Error al leer todos los libros: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Updates an existing book's information in the database.
     *
     * @param libro The DTOLibro object containing the updated book information.
     * @return true if the update was successful, false otherwise.
     * @throws IllegalArgumentException if the book's ID is invalid (less than or equal to 0).
     */
    public boolean update(DTOLibro libro) {
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("ID de libro no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, libro.getTitulo());
            pst.setString(2, libro.getIsbn());
            pst.setInt(3, libro.getId());
            return pst.executeUpdate() > 0; // true if updated successfully
        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param libro The DTOLibro object containing the book's ID to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws IllegalArgumentException if the book's ID is invalid (less than or equal to 0).
     */
    public boolean delete(DTOLibro libro) {
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("ID de libro no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, libro.getId());
            return pst.executeUpdate() > 0; // true if deleted successfully
        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a ResultSet row into a DTOLibro object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOLibro object representing the book.
     */
    private DTOLibro getLibro(ResultSet rs) {
        DTOLibro libro = null;
        try {
            String titulo = rs.getString("titulo");
            String isbn = rs.getString("isbn");
            libro = new DTOLibro(titulo, isbn);
            libro.setId(rs.getInt("ID")); // Assign the ID from the database
        } catch (SQLException e) {
            System.err.println("Error al leer ResultSet: " + e.getMessage());
        }
        return libro;
    }
}
