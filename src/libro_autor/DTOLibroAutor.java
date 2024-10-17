package libro_autor;

/**
 * The type DTOLibroAutor class  encapsulate the details for the Libro_Autor (Book_Author) relationship.
 *
 * @version 1.0
 */
public class DTOLibroAutor {
    private int libroId;
    private int autorId;

    /**
     * Instantiates a new DTOLibroAutor.
     *
     * @param libroId the book's id
     * @param autorId the author's id
     */
    public DTOLibroAutor(int libroId, int autorId) {
        this.libroId = libroId;
        this.autorId = autorId;
    }

    /**
     * Gets libro id.
     *
     * @return the book's id
     */
    public int getLibroId() {
        return libroId;
    }

    /**
     * Sets libro id.
     *
     * @param libroId the book's id
     */
    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    /**
     * Gets autor id.
     *
     * @return the author's id
     */
    public int getAutorId() {
        return autorId;
    }

    /**
     * Sets autor id.
     *
     * @param autorId the author's id
     */
    public void setAutorId(int autorId) {
        this.autorId = autorId;
    }

    /**
     * Returns a string representation of the DTOLibroAutor object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "DTOLibroAutor{" +
                "libroId=" + libroId +
                ", autorId=" + autorId +
                '}';
    }
}
