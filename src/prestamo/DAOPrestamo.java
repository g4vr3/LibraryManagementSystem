package prestamo;

import exception.ServiceException;
import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOPrestamo class handles CRUD operations for the Prestamo (Loan) entity.
 *
 * @version 2.1
 */
public class DAOPrestamo {
    private static final String CREATE = "INSERT INTO Prestamo (fechaInicio, fechaFin, usuarioId, libroId) VALUES (?, ?, ?, ?)";
    private static final String READ = "SELECT * FROM Prestamo WHERE ID = ?";
    private static final String READ_ALL = "SELECT * FROM Prestamo";
    private static final String UPDATE = "UPDATE Prestamo SET fechaInicio = ?, fechaFin = ?, usuarioId = ?, libroId = ? WHERE ID = ?";
    private static final String DELETE = "DELETE FROM Prestamo WHERE ID = ?";
    private static Connection conexion;

    /**
     * Initializes a new DAOPrestamo instance by establishing a database connection.
     */
    public DAOPrestamo() {
        this.conexion = DDL.getConnection();
    }

    /**
     * Creates a new loan in the database.
     *
     * @param prestamo The loan object containing the start and end dates.
     * @throws ServiceException if there is an error during the creation.
     */
    public void create(DTOPrestamo prestamo) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setDate(1, prestamo.getFechaInicio());
            pst.setDate(2, prestamo.getFechaFin());
            pst.setInt(3, prestamo.getUsuarioId());
            pst.setInt(4, prestamo.getLibroId());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    prestamo.setId(rs.getInt(1)); // Set the generated ID
                }
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al crear prestamo: " + e.getMessage());
        }
    }

    /**
     * Reads a loan from the database based on the ID provided.
     *
     * @param id The ID of the loan to read.
     * @return A DTOPrestamo object containing the loan's information, or null if not found.
     * @throws ServiceException if there is an error during the read.
     */
    public DTOPrestamo read(int id) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return getPrestamo(rs); // Convert the result to a DTOPrestamo
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer prestamo: " + e.getMessage());
        }
    }

    /**
     * Reads all loans from the database.
     *
     * @return An ArrayList of DTOPrestamo objects representing all loans in the database.
     * @throws ServiceException if there is an error during the read.
     */
    public ArrayList<DTOPrestamo> readAll() throws ServiceException {
        ArrayList<DTOPrestamo> prestamos = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                prestamos.add(getPrestamo(rs)); // Convert each result to a DTOPrestamo
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los prestamos: " + e.getMessage());
        }
        return prestamos;
    }

    /**
     * Updates an existing loan's information in the database.
     *
     * @param prestamo The DTOPrestamo object containing the updated loan information.
     * @throws IllegalArgumentException if the loan's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the update.
     */
    public void update(DTOPrestamo prestamo) throws ServiceException {
        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setDate(1, prestamo.getFechaInicio());
            pst.setDate(2, prestamo.getFechaFin());
            pst.setInt(3, prestamo.getUsuarioId());
            pst.setInt(4, prestamo.getLibroId());
            pst.setInt(5, prestamo.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar prestamo: " + e.getMessage());
        }
    }

    /**
     * Deletes a loan from the database.
     *
     * @param prestamo The DTOPrestamo object containing the loan's ID to delete.
     * @throws IllegalArgumentException if the loan's ID is invalid (less than or equal to 0).
     * @throws ServiceException if there is an error during the deletion.
     */
    public void delete(DTOPrestamo prestamo) throws ServiceException {
        if (prestamo.getId() <= 0) {
            throw new IllegalArgumentException("ID de prestamo no válido");
        }
        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, prestamo.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar prestamo: " + e.getMessage());
        }
    }

    /**
     * Converts a ResultSet row into a DTOPrestamo object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOPrestamo object representing the loan.
     * @throws ServiceException if there is an error during the conversion.
     */
    private DTOPrestamo getPrestamo(ResultSet rs) throws ServiceException {
        try {
            Date fechaInicio = rs.getDate("fechaInicio");
            Date fechaFin = rs.getDate("fechaFin");
            int usuarioId = rs.getInt("usuarioId");
            int libroId = rs.getInt("libroId");
            DTOPrestamo prestamo = new DTOPrestamo(fechaInicio, fechaFin, usuarioId, libroId);
            prestamo.setId(rs.getInt("id")); // Assign the ID from the database
            return prestamo;
        } catch (SQLException e) {
            throw new ServiceException("Error al leer ResultSet: " + e.getMessage());
        }
    }
}
