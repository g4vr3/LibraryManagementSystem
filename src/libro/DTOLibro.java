package libro;

/**
 * The type Dto libro (Book).
 *
 * @version 1.1
 */
public class DTOLibro {
    private Integer id;
    private String titulo;
    private Integer isbn;

    /**
     * Instantiates a new Dto libro (Book).
     *
     * @param titulo the title
     * @param isbn   the isbn
     */
    public DTOLibro(String titulo, Integer isbn) {
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
    public Integer getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(Integer isbn) {
        this.isbn = isbn;
    }

    /**
     * Returns a string representation of the DTOLibro object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "DTOLibro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", isbn=" + isbn +
                '}';
    }
}
