package libro;

import exception.ServiceException;
import libro_autor.LibroAutorService;

import java.util.List;

/**
 * Service class for managing books (DTOLibro) and their relations with authors through LibroAutorService.
 *
 * @version 1.1
 */
public class LibroService {
    private List<DTOLibro> librosInMemory;
    private DAOLibro daoLibro;
    private LibroAutorService libroAutorService;

    /**
     * Initializes the service, loading all books into memory.
     *
     * @param libroAutorService Service for managing book-author relations.
     * @throws ServiceException If an error occurs while loading books.
     */
    public LibroService(LibroAutorService libroAutorService) throws ServiceException {
        this.daoLibro = new DAOLibro();
        this.libroAutorService = libroAutorService;
        librosInMemory = daoLibro.readAll(); // Load all books into memory
    }

    /**
     * Creates a new book and syncs it with the in-memory list.
     *
     * @param titulo The title of the book.
     * @param isbn   The ISBN of the book.
     * @throws ServiceException If an error occurs during creation.
     */
    public void createLibro(String titulo, String isbn) throws ServiceException {
        DTOLibro dtoLibro = new DTOLibro(titulo, isbn);
        daoLibro.create(dtoLibro);
        librosInMemory.add(dtoLibro); // Sync with in-memory list
    }

    /**
     * Reads a book by ID.
     *
     * @param id The ID of the book.
     * @return The DTOLibro object.
     * @throws ServiceException If the book is not found or an error occurs.
     */
    public DTOLibro readLibro(Integer id) throws ServiceException {
        return daoLibro.read(id); // Read the book from the database
    }

    /**
     * Updates a book's details.
     *
     * @param id     The ID of the book.
     * @param titulo The new title of the book.
     * @param isbn   The new ISBN of the book.
     * @throws ServiceException If the book is not found or an error occurs.
     */
    public void updateLibro(Integer id, String titulo, String isbn) throws ServiceException {
        DTOLibro dtoLibro = findLibroById(id);
        if (dtoLibro != null) {
            if (titulo != null && !titulo.isBlank() && !titulo.isEmpty()){
                dtoLibro.setTitulo(titulo);
            }
            if (isbn != null && !isbn.isBlank() && !isbn.isEmpty()) {
                dtoLibro.setIsbn(isbn); // Update the book details
            }
            daoLibro.update(dtoLibro); // Sync with the database
        } else {
            throw new ServiceException("El libro que intentas actualizar no existe");
        }
    }

    /**
     * Deletes a book and its associated relations with authors.
     *
     * @param id The ID of the book.
     * @throws ServiceException If the book is not found or an error occurs.
     */
    public void deleteLibro(Integer id) throws ServiceException {
        DTOLibro dtoLibro = findLibroById(id);
        if (dtoLibro != null) {
            daoLibro.delete(dtoLibro); // Delete from the database
            librosInMemory.remove(dtoLibro); // Remove from the in-memory list
            libroAutorService.deleteRelationsByLibroId(dtoLibro.getId()); // Remove relations with authors
        } else {
            throw new ServiceException("El libro que intentas eliminar no existe");
        }
    }

    /**
     * Finds a book by ID from the in-memory list.
     *
     * @param id The ID of the book.
     * @return The DTOLibro object.
     * @throws ServiceException If the book is not found.
     */
    public DTOLibro findLibroById(Integer id) throws ServiceException {
        for (DTOLibro dtoLibro : librosInMemory) {
            if (dtoLibro.getId().equals(id)) {
                return dtoLibro;
            }
        }
        throw new ServiceException("Libro no encontrado");
    }
}
