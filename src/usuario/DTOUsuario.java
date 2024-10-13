package usuario;

/**
 * The type Dto usuario (User).
 *
 * @version 1.0
 */
public class DTOUsuario {
    private Integer id;
    private String nombre;

    /**
     * Instantiates a new Dto usuario (User).
     *
     * @param nombre the name
     */
    public DTOUsuario(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
// Getters, Setters
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
     * Gets nombre.
     *
     * @return the name
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets nombre.
     *
     * @param nombre the name
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // toString

    @Override
    public String toString() {
        return "DTOUsuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
