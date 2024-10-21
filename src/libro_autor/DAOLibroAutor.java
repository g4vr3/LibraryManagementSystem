package libro_autor;

import exception.ServiceException;
import jdbc.DDL;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * The DAOLibroAutor class handles CRUD operations for the Libro_Autor (Book_Author) relationship.
 *
 * @version 1.1
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
     * @param libroAutor The DTOLibroAutor object containing book and author id.
     * @throws ServiceException if there is an error during creation.
     */
        public void create(DTOLibroAutor libroAutor) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE)) {
            pst.setInt(1, libroAutor.getLibroId());
            pst.setInt(2, libroAutor.getAutorId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al crear relación libro-autor: " + e.getMessage());
        }
    }

    /**
     * Reads book-author relationships based on the book ID.
     *
     * @param libroId The ID of the book.
     * @return A list of relationships found, or an empty list if none found.
     * @throws ServiceException if there is an error during reading.
     */
    public List<DTOLibroAutor> readByLibro(int libroId) throws ServiceException {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (PreparedStatement pst = conexion.prepareStatement(READ_BY_LIBRO)) {
            pst.setInt(1, libroId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer relaciones por libro: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Reads book-author relationships based on the author ID.
     *
     * @param autorId The ID of the author.
     * @return A list of relationships found, or an empty list if none found.
     * @throws ServiceException if there is an error during reading.
     */
    public List<DTOLibroAutor> readByAutor(int autorId) throws ServiceException {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (PreparedStatement pst = conexion.prepareStatement(READ_BY_AUTOR)) {
            pst.setInt(1, autorId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer relaciones por autor: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Reads all book-author relationships from the database.
     *
     * @return A list of all relationships in the database.
     * @throws ServiceException if there is an error during reading.
     */
    public List<DTOLibroAutor> readAll() throws ServiceException {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                relaciones.add(getLibroAutor(rs));
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todas las relaciones libro-autor: " + e.getMessage());
        }
        return relaciones;
    }

    /**
     * Converts a ResultSet row into a LibroAutor object.
     *
     * @param rs The ResultSet from a query.
     * @return A LibroAutor object representing the book-author relationship.
     * @throws ServiceException if there is an error during the conversion.
     */
    private DTOLibroAutor getLibroAutor(ResultSet rs) throws ServiceException {
        try {
            int libroId = rs.getInt("libroId");
            int autorId = rs.getInt("autorId");
            return new DTOLibroAutor(libroId, autorId);
        } catch (SQLException e) {
            throw new ServiceException("Error al leer ResultSet: " + e.getMessage());
        }
    }
}
