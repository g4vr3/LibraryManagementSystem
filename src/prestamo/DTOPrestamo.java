package prestamo;

import java.sql.Date;

/**
 * The type DTOPrestamo class encapsulate the details for the Prestamo (Loan) entity.
 *
 * @version 2.0
 */
public class DTOPrestamo {
    private Integer id;
    private Date fechaInicio;
    private Date fechaFin;
    private int usuarioId;
    private int libroId;

    /**
     * Instantiates a new Dto prestamo (Loan).
     *
     * @param fechaInicio the loan start date
     * @param fechaFin    the loan end date
     * @param usuarioId  the user id borrowing the book
     * @param libroId the being borrowed book id
     */
    public DTOPrestamo(Date fechaInicio, Date fechaFin, int usuarioId, int libroId) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
    }

    /**
     * Gets id.
     *
     * @return the loan id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the loan id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets fechaInicio.
     *
     * @return the loan start date
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets fechaInicio.
     *
     * @param fechaInicio the loan start date
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Gets fechaFin.
     *
     * @return the loan end date
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * Sets fechaFin.
     *
     * @param fechaFin the loan end date
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * Gets usuarioId.
     *
     * @return the user id borrowing the book
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Sets usuarioId.
     *
     * @param usuarioId the user id borrowing the book
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Gets libroId.
     *
     * @return the being borrowed book id
     */
    public int getLibroId() {
        return libroId;
    }

    /**
     * Sets libroId.
     *
     * @param libroId the being borrowed book id
     */
    public void setLibroId(int libroId) {
        this.libroId = libroId;
    }

    /**
     * Returns a string representation of the DTOPrestamo object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "ID: " + id + "\n" +
                "Fecha de Inicio: " + fechaInicio + "\n" +
                "Fecha de Fin: " + fechaFin + "\n" +
                "Usuario ID: " + usuarioId + "\n" +
                "Libro ID: " + libroId;
    }
}
