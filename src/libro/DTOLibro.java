package libro;

/**
 * The type DTOLibro class encapsulate the details for the Libro (Book) entity.
 *
 * @version 1.2
 */
public class DTOLibro {
    private Integer id;
    private String titulo;
    private String isbn;

    /**
     * Instantiates a new Dto libro (Book).
     *
     * @param titulo the title
     * @param isbn   the isbn
     */
    public DTOLibro(String titulo, String isbn) {
        this.titulo = titulo;
        this.isbn = isbn;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets titulo.
     *
     * @return the title
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets titulo.
     *
     * @param titulo the title
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns a string representation of the DTOLibro object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Título: " + titulo + "\n" +
                "ISBN: " + isbn;
    }
}
