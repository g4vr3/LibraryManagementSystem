package libro_autor;

import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The DAOLibroAutor class handles CRUD operations for the Libro_Autor (Book_Author) relationship.
 *
 * @version 1.0
 */
public class DAOLibroAutor {
    private static final String CREATE = "INSERT INTO LibroAutor (libroId, autorId) VALUES (?, ?)";
    private static final String READ_BY_LIBRO = "SELECT * FROM LibroAutor WHERE libroId = ?";
    private static final String READ_BY_AUTOR = "SELECT * FROM LibroAutor WHERE autorId = ?";
    private static final String READ_ALL = "SELECT * FROM LibroAutor";
    private static Connection conexion;

    /**
     * Initializes a new DAOLibroAutor instance by establishing a database connection.
     */
    public DAOLibroAutor() {
        this.conexion = DDL.getConnection();
    }

    /**
     * Creates a new book-author relationship in the database.
     *
     * @param libroId The ID of the book.
     * @param autorId The ID of the author.
     * @return true if the relationship was successfully created, false otherwise.
     */
    public boolean create(int libroId, int autorId) {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE)) {
            pst.setInt(1, libroId);
            pst.setInt(2, autorId);
            return pst.executeUpdate() > 0; // true if successfully created
        } catch (SQLException e) {
            System.err.println("Error al crear relación libro-autor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads book-author relationships based on the book ID.
     *
     * @param libroId The ID of the book.
     * @return A list of relationships found, or an empty list if none found.
     */
    public List<DTOLibroAutor> readByLibro(int libroId) {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (PreparedStatement pst = conexion.prepareStatement(READ_BY_LIBRO)) {
            pst.setInt(1, libroId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer relaciones por libro: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Reads book-author relationships based on the author ID.
     *
     * @param autorId The ID of the author.
     * @return A list of relationships found, or an empty list if none found.
     */
    public List<DTOLibroAutor> readByAutor(int autorId) {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (PreparedStatement pst = conexion.prepareStatement(READ_BY_AUTOR)) {
            pst.setInt(1, autorId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer relaciones por autor: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Reads all book-author relationships from the database.
     *
     * @return A list of all relationships in the database.
     */
    public List<DTOLibroAutor> readAll() {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al leer todas las relaciones libro-autor: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Converts a ResultSet row into a LibroAutor object.
     *
     * @param rs The ResultSet from a query.
     * @return A LibroAutor object representing the book-author relationship.
     */
    private DTOLibroAutor getLibroAutor(ResultSet rs) {
        DTOLibroAutor libroAutor = null;
        try {
            int libroId = rs.getInt("libroId");
            int autorId = rs.getInt("autorId");
            libroAutor = new DTOLibroAutor(libroId, autorId);
        } catch (SQLException e) {
            System.err.println("Error al leer ResultSet: " + e.getMessage());
        }
        return libroAutor;
    }
}