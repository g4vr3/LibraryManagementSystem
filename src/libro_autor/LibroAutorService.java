package libro_autor;

import exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing the many-to-many relationship between books and authors.
 *
 * @version 1.0.3
 */
public class LibroAutorService {
    private List<DTOLibroAutor> libroAutorInMemory;
    private DAOLibroAutor daoLibroAutor;

    /**
     * Initializes the service by loading all book-author relationships into memory.
     *
     * @throws ServiceException If an error occurs while loading data.
     */
    public LibroAutorService() throws ServiceException {
        this.daoLibroAutor = new DAOLibroAutor();
        libroAutorInMemory = daoLibroAutor.readAll(); // Load all relations into memory
    }

    /**
     * Creates a new book-author relationship and syncs it with the in-memory list.
     *
     * @param libroId The ID of the book.
     * @param autorId The ID of the author.
     * @throws ServiceException If an error occurs during the creation.
     */
    public void createLibroAutor(int libroId, int autorId) throws ServiceException {
        DTOLibroAutor dtoLibroAutor = new DTOLibroAutor(libroId, autorId);
        daoLibroAutor.create(dtoLibroAutor);
        libroAutorInMemory.add(dtoLibroAutor); // Sync with in-memory list
    }

    /**
     * Finds all book-author relationships by the book's ID.
     *
     * @param libroId The ID of the book.
     * @return A list of book-author relationships associated with the book.
     * @throws ServiceException If an error occurs during the search.
     */
    public List<DTOLibroAutor> findRelationsByLibroId(int libroId) throws ServiceException {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        for (DTOLibroAutor dtoLibroAutor : libroAutorInMemory) {
            if (dtoLibroAutor.getLibroId() == libroId) {
                relaciones.add(dtoLibroAutor);
            }
        }
        return relaciones;
    }

    /**
     * Finds all book-author relationships by the author's ID.
     *
     * @param autorId The ID of the author.
     * @return A list of book-author relationships associated with the author.
     * @throws ServiceException If an error occurs during the search.
     */
    public List<DTOLibroAutor> findRelationsByAutorId(int autorId) throws ServiceException {
        List<DTOLibroAutor> relaciones = new ArrayList<>();
        for (DTOLibroAutor dtoLibroAutor : libroAutorInMemory) {
            if (dtoLibroAutor.getAutorId() == autorId) {
                relaciones.add(dtoLibroAutor);
            }
        }
        return relaciones;
    }

    /**
     * Deletes all book-author relationships associated with a specific book ID.
     *
     * @param libroId The ID of the book.
     * @throws ServiceException if there are no relations to delete for the given book ID
     */
    public void deleteRelationsByLibroId(int libroId) throws ServiceException {
        // Check if there are relations to delete
        boolean removed = libroAutorInMemory.removeIf(dtoLibroAutor -> dtoLibroAutor.getLibroId() == libroId);

        // Throw exception if no relations were found for the given book ID
        if (!removed) {
            throw new ServiceException("No se encontraron relaciones para eliminar para el libro con ID: " + libroId);
        }
    }

    /**
     * Deletes all book-author relationships associated with a specific author ID.
     *
     * @param autorId The ID of the author.
     */
    public void deleteRelationsByAutorId(int autorId) {
        libroAutorInMemory.removeIf(dtoLibroAutor -> dtoLibroAutor.getAutorId() == autorId);
    }
}
