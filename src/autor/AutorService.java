package autor;

import exception.ServiceException;
import libro_autor.LibroAutorService;

import java.util.List;

/**
 * Service for managing authors (DTOAutor) and their relations with books via LibroAutorService.
 *
 * @version 1.1
 */
public class AutorService {
    private List<DTOAutor> autoresInMemory;
    private DAOAutor daoAutor;
    private LibroAutorService libroAutorService;

    /**
     * Initializes the service, loading authors into memory.
     *
     * @param libroAutorService Service for managing book-author relations.
     * @throws ServiceException If an error occurs while reading authors.
     */
    public AutorService(LibroAutorService libroAutorService) throws ServiceException {
        this.daoAutor = new DAOAutor();
        this.libroAutorService = libroAutorService;
        autoresInMemory = daoAutor.readAll(); // Load authors into memory
    }

    /**
     * Creates a new author and syncs with memory.
     *
     * @param nombre The author's name.
     * @throws ServiceException If an error occurs during creation.
     */
    public void createAutor(String nombre) throws ServiceException {
        DTOAutor dtoAutor = new DTOAutor(nombre);
        daoAutor.create(dtoAutor);
        autoresInMemory.add(dtoAutor); // Sync with memory
    }

    /**
     * Reads an author by ID.
     *
     * @param id The author's ID.
     * @return The DTOAutor object.
     * @throws ServiceException If an error occurs during reading.
     */
    public DTOAutor readAutor(Integer id) throws ServiceException {
        return daoAutor.read(id); // Read author from database
    }

    /**
     * Updates an author's name by ID.
     *
     * @param id     The author's ID.
     * @param nombre The new name.
     * @throws ServiceException If the author is not found or if an error occurs.
     */
    public void updateAutor(Integer id, String nombre) throws ServiceException {
        DTOAutor dtoAutor = findAutorById(id);
        if (dtoAutor != null) {
            if (nombre != null && !nombre.isBlank() && !nombre.isEmpty()){
                dtoAutor.setNombre(nombre); // Update the name
            }
            daoAutor.update(dtoAutor);
        } else {
            throw new ServiceException("El autor que estas intentando actualizar no existe");
        }
    }

    /**
     * Deletes an author and their associated relations.
     *
     * @param id The author's ID.
     * @throws ServiceException If the author is not found or if an error occurs.
     */
    public void deleteAutor(Integer id) throws ServiceException {
        DTOAutor dtoAutor = findAutorById(id);
        if (dtoAutor != null) {
            // Delete author's relations from libroAutorService
            libroAutorService.deleteRelationsByAutorId(dtoAutor.getId());
            daoAutor.delete(dtoAutor); // Delete from the database
            autoresInMemory.remove(dtoAutor); // Sync with memory
        } else {
            throw new ServiceException("El autor que estas intentando eliminar no existe");
        }
    }

    /**
     * Finds an author by ID from the in-memory list.
     *
     * @param id The author's ID.
     * @return The DTOAutor object.
     * @throws ServiceException If the author is not found.
     */
    public DTOAutor findAutorById(Integer id) throws ServiceException {
        for (DTOAutor dtoAutor : autoresInMemory) {
            if (dtoAutor.getId().equals(id)) {
                return dtoAutor;
            }
        }
        throw new ServiceException("Autor no encontrado");
    }
}
