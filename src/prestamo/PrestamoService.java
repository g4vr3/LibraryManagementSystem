package prestamo;

import autor.AutorService;
import exception.ServiceException;
import libro.LibroService;
import usuario.UsuarioService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing loans (prestamos).
 * Provides methods for creating, reading, updating, and deleting loans.
 *
 * @version 1.2
 */
public class PrestamoService {
    private LibroService libroService;
    private UsuarioService usuarioService;
    private List<DTOPrestamo> prestamosInMemory;
    private DAOPrestamo daoPrestamo;

    /**
     * Initializes a new instance of PrestamoService.
     * Loads all loans from the data source into memory.
     *
     * @throws ServiceException if there is an error while reading loans from the data source
     */
    public PrestamoService(LibroService libroService, UsuarioService usuarioService) throws ServiceException {
        this.libroService = libroService;
        this.usuarioService = usuarioService;
        this.daoPrestamo = new DAOPrestamo();
        prestamosInMemory = daoPrestamo.readAll();
    }

    /**
     * Creates a new loan with the specified end date, user ID, and book ID.
     * The start date is set to the current date.
     *
     * @param usuarioId the ID of the user borrowing the book
     * @param libroId the ID of the book being borrowed
     * @throws ServiceException if the end date is before the start date or if the book is already loaned
     */
    public void createPrestamo(int usuarioId, int libroId) throws ServiceException {
        // Check if user and book exists
        try {
            usuarioService.findUsuarioById(usuarioId);
            libroService.findLibroById(libroId);
        } catch (ServiceException e){
            throw new ServiceException(e.getMessage());
        }
        // Get today's date as the start date
        Date fechaInicio = Date.valueOf(LocalDate.now());

        // Calculate the end date, which is 15 days after the start date
        Date fechaFin = Date.valueOf(LocalDate.now().plusDays(15));

        // Validate that the end date is after the start date
        if (fechaFin.before(fechaInicio)) {
            throw new ServiceException("La fecha de fin no puede ser antígua a la de inicio.");
        }

        // Check if the book is already loaned between the start and end dates
        if (isLibroPrestado(libroId, fechaInicio, fechaFin)) {
            throw new ServiceException("El libro ya está prestado");
        }

        // Create the new loan if there are no overlaps
        DTOPrestamo dtoPrestamo = new DTOPrestamo(fechaInicio, fechaFin, usuarioId, libroId);
        daoPrestamo.create(dtoPrestamo);
        prestamosInMemory.add(dtoPrestamo);  // Synchronize with the in-memory list
    }

    /**
     * Reads a loan by its ID.
     *
     * @param id the ID of the loan to be read
     * @return the loan DTO
     * @throws ServiceException if the loan does not exist
     */
    public DTOPrestamo readPrestamo(Integer id) throws ServiceException {
        return daoPrestamo.read(id); // Return the read loan
    }

    /**
     * Updates an existing loan.
     *
     * @param id the ID of the loan to be updated
     * @param fechaFin the new end date for the loan
     * @param usuarioId the ID of the user borrowing the book
     * @param libroId the ID of the book being borrowed
     * @throws ServiceException if the loan does not exist or if the new end date is invalid
     */
    public void updatePrestamo(Integer id, Date fechaFin, int usuarioId, int libroId) throws ServiceException {
        DTOPrestamo dtoPrestamo = findPrestamoById(id);
        if (dtoPrestamo != null) {
            // Validate that the end date is after the start date
            if (fechaFin.before(dtoPrestamo.getFechaInicio())) {
                throw new ServiceException("La fecha de fin no puede ser antígua a la de inicio.");
            }

            // Check if the book is already loaned between the start and end dates
            if (isLibroPrestado(libroId, dtoPrestamo.getFechaInicio(), fechaFin)) {
                throw new ServiceException("El libro ya está prestado.");
            }
            dtoPrestamo.setFechaFin(fechaFin);
            dtoPrestamo.setUsuarioId(usuarioId);
            dtoPrestamo.setLibroId(libroId);
            daoPrestamo.update(dtoPrestamo);
        } else {
            throw new ServiceException("El préstamo que intentas actualizar no existe.");
        }
    }

    /**
     * Deletes a loan by its ID.
     *
     * @param id the ID of the loan to be deleted
     * @throws ServiceException if the loan does not exist
     */
    public void deletePrestamo(Integer id) throws ServiceException {
        DTOPrestamo dtoPrestamo = findPrestamoById(id);
        if (dtoPrestamo != null) {
            daoPrestamo.delete(dtoPrestamo);
            prestamosInMemory.remove(dtoPrestamo);
        } else {
            throw new ServiceException("El préstamo que intentas eliminar no existe.");
        }
    }

    /**
     * Finds a loan by its ID.
     *
     * @param id the ID of the loan to be found
     * @return the loan DTO
     * @throws ServiceException if the loan is not found
     */
    public DTOPrestamo findPrestamoById(Integer id) throws ServiceException {
        for (DTOPrestamo dtoPrestamo : prestamosInMemory) {
            if (dtoPrestamo.getId().equals(id)) {
                return dtoPrestamo;
            }
        }
        throw new ServiceException("Préstamo no encontrado.");
    }

    /**
     * Finds all loans by the user id.
     *
     * @param usuarioId the user id
     * @return a list of all loan DTOs with this user id
     * @throws ServiceException if no loans are found for the given user id
     */
    public List<DTOPrestamo> findPrestamosByUsuarioId(Integer usuarioId) throws ServiceException {
        List<DTOPrestamo> prestamosPorUsuario = new ArrayList<>();
        for (DTOPrestamo dtoPrestamo : prestamosInMemory) {
            if (dtoPrestamo.getUsuarioId() == usuarioId) {
                prestamosPorUsuario.add(dtoPrestamo);
            }
        }

        // Throw exception if no loans are found
        if (prestamosPorUsuario.isEmpty()) {
            throw new ServiceException("Ningún préstamo encontrado para el usuario con ID: " + usuarioId);
        }

        return prestamosPorUsuario;
    }

    /**
     * Finds all loans by the book id.
     *
     * @param libroId the book id
     * @return a list of all loan DTOs with this book id
     * @throws ServiceException if no loans are found for the given book id
     */
    public List<DTOPrestamo> findPrestamosByLibroId(Integer libroId) throws ServiceException {
        List<DTOPrestamo> prestamosPorLibro = new ArrayList<>();
        for (DTOPrestamo dtoPrestamo : prestamosInMemory) {
            if (dtoPrestamo.getLibroId() == libroId) {
                prestamosPorLibro.add(dtoPrestamo);
            }
        }

        // Throw exception if no loans are found
        if (prestamosPorLibro.isEmpty()) {
            throw new ServiceException("Ningún préstamo encontrado para el libro con ID: " + libroId);
        }

        return prestamosPorLibro;
    }

    /**
     * Checks if a book is currently loaned between the specified dates.
     *
     * @param libroId the ID of the book to check
     * @param fechaInicio the start date to check
     * @param fechaFin the end date to check
     * @return true if the book is loaned during the specified period, false otherwise
     */
    private boolean isLibroPrestado(int libroId, Date fechaInicio, Date fechaFin) {
        for (DTOPrestamo dtoPrestamo : prestamosInMemory) {
            // Check for overlaps
            if (dtoPrestamo.getLibroId() == libroId &&
                    !(dtoPrestamo.getFechaFin().before(fechaInicio) || dtoPrestamo.getFechaInicio().after(fechaFin))) {
                return true; // The book is loaned in the date range
            }
        }
        return false; // No overlap found
    }
}
