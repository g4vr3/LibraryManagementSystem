package libro;

import exception.ServiceException;
import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOLibro class handles CRUD operations for the Libro (Book) entity.
 *
 * @version 1.1
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
     * @throws ServiceException if there is an error during the creation.
     */
    public void create(DTOLibro libro) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, libro.getTitulo());
            pst.setString(2, libro.getIsbn());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al crear libro: " + e.getMessage());
        }
    }

    /**
     * Reads a book from the database based on the ID provided.
     *
     * @param id The ID of the book to read.
     * @return A DTOLibro object containing the book's information, or null if not found.
     * @throws ServiceException if there is an error during the read.
     */
    public DTOLibro read(int id) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return getLibro(rs); // Convert the result to a DTOLibro
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer libro: " + e.getMessage());
        }
    }

    /**
     * Reads all books from the database.
     *
     * @return An ArrayList of DTOLibro objects representing all books in the database.
     * @throws ServiceException if there is an error during the read.
     */
    public ArrayList<DTOLibro> readAll() throws ServiceException {
        ArrayList<DTOLibro> libros = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                libros.add(getLibro(rs)); // Convert each result to a DTOLibro
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los libros: " + e.getMessage());
        }
        return libros;
    }

    /**
     * Updates an existing book's information in the database.
     *
     * @param libro The DTOLibro object containing the updated book information.
     * @throws IllegalArgumentException if the book's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the update.
     */
    public void update(DTOLibro libro) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setString(1, libro.getTitulo());
            pst.setString(2, libro.getIsbn());
            pst.setInt(3, libro.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar libro: " + e.getMessage());
        }
    }

    /**
     * Deletes a book from the database.
     *
     * @param libro The DTOLibro object containing the book's ID to delete.
     * @throws IllegalArgumentException if the book's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the deletion.
     */
    public void delete(DTOLibro libro) throws ServiceException {
        if (libro.getId() <= 0) {
            throw new IllegalArgumentException("ID de libro no válido");
        }
        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, libro.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar libro: " + e.getMessage());
        }
    }

    /**
     * Converts a ResultSet row into a DTOLibro object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOLibro object representing the book.
     * @throws ServiceException if there is an error during the conversion.
     */
    private DTOLibro getLibro(ResultSet rs) throws ServiceException {
        try {
            String titulo = rs.getString("titulo");
            String isbn = rs.getString("isbn");
            DTOLibro libro = new DTOLibro(titulo, isbn);
            libro.setId(rs.getInt("id")); // Assign the ID from the database
            return libro;
        } catch (SQLException e) {
            throw new ServiceException("Error al leer ResultSet: " + e.getMessage());
        }
    }
}
