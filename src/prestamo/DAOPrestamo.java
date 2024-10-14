package prestamo;

import jdbc.DDL;
import java.sql.*;
import java.util.ArrayList;

/**
 * The DAOPrestamo class handles CRUD operations for the Prestamo (Loan) entity.
 *
 * @version 2.0
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
     * @return true if the loan was successfully created, false otherwise.
     */
    public boolean create(DTOPrestamo prestamo) {
        try (PreparedStatement pst = conexion.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS)) {
            pst.setDate(1, prestamo.getFechaInicio());
            pst.setDate(2, prestamo.getFechaFin());
            boolean successful = pst.executeUpdate() > 0; // true if successfully created

            if (successful) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        prestamo.setId(rs.getInt(1)); // Set the generated ID
                    }
                }
            }
            return successful;
        } catch (SQLException e) {
            System.err.println("Error al crear prestamo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Reads a loan from the database based on the ID provided.
     *
     * @param id The ID of the loan to read.
     * @return A DTOPrestamo object containing the loan's information, or null if not found.
     */
    public DTOPrestamo read(int id) {
        DTOPrestamo prestamo = null;
        try (PreparedStatement pst = conexion.prepareStatement(READ)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                prestamo = getPrestamo(rs); // Convert the result to a DTOPrestamo
            }
        } catch (SQLException e) {
            System.err.println("Error al leer prestamo: " + e.getMessage());
        }
        return prestamo;
    }

    /**
     * Reads all loans from the database.
     *
     * @return An ArrayList of DTOPrestamo objects representing all loans in the database.
     */
    public ArrayList<DTOPrestamo> readAll() {
        ArrayList<DTOPrestamo> prestamos = new ArrayList<>();
        try (Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery(READ_ALL)) {
            while (rs.next()) {
                prestamos.add(getPrestamo(rs)); // Convert each result to a DTOPrestamo
            }
        } catch (SQLException e) {
            System.err.println("Error al leer todos los prestamos: " + e.getMessage());
        }
        return prestamos;
    }

    /**
     * Updates an existing loan's information in the database.
     *
     * @param prestamo The DTOPrestamo object containing the updated loan information.
     * @return true if the update was successful, false otherwise.
     * @throws IllegalArgumentException if the loan's ID is invalid (less than or equal to 0).
     */
    public boolean update(DTOPrestamo prestamo) {
        if (prestamo.getId() <= 0) {
            throw new IllegalArgumentException("ID de prestamo no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(UPDATE)) {
            pst.setDate(1, prestamo.getFechaInicio());
            pst.setDate(2, prestamo.getFechaFin());
            pst.setInt(3, prestamo.getId());
            return pst.executeUpdate() > 0; // true if updated successfully
        } catch (SQLException e) {
            System.err.println("Error al actualizar prestamo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a loan from the database.
     *
     * @param prestamo The DTOPrestamo object containing the loan's ID to delete.
     * @return true if the deletion was successful, false otherwise.
     * @throws IllegalArgumentException if the loan's ID is invalid (less than or equal to 0).
     */
    public boolean delete(DTOPrestamo prestamo) {
        if (prestamo.getId() <= 0) {
            throw new IllegalArgumentException("ID de prestamo no válido");
        }

        try (PreparedStatement pst = conexion.prepareStatement(DELETE)) {
            pst.setInt(1, prestamo.getId());
            return pst.executeUpdate() > 0; // true if deleted successfully
        } catch (SQLException e) {
            System.err.println("Error al eliminar prestamo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Converts a ResultSet row into a DTOPrestamo object.
     *
     * @param rs The ResultSet from a query.
     * @return A DTOPrestamo object representing the loan.
     */
    private DTOPrestamo getPrestamo(ResultSet rs) {
        DTOPrestamo prestamo = null;
        try {
            // get data from query result
            Date fechaInicio = rs.getDate("fechaInicio");
            Date fechaFin = rs.getDate("fechaFin");
            int usuarioId = rs.getInt("usuarioId");
            int libroId = rs.getInt("libroId");

            // create a new DTOPrestamo object with the retrieved data
            prestamo = new DTOPrestamo(fechaInicio, fechaFin, usuarioId, libroId);
            prestamo.setId(rs.getInt("id")); // Assign the ID from the database
        } catch (SQLException e) {
            System.err.println("Error al leer ResultSet: " + e.getMessage());
        }
        return prestamo;
    }
}
