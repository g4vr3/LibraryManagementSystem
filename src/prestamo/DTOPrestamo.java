package prestamo;

import java.sql.Date;

/**
 * The type Dto prestamo (Loan).
 *
 * @version 1.0
 */
public class DTOPrestamo {
    private Integer id;
    private Date fechaInicio;
    private Date fechaFin;

    /**
     * Instantiates a new Dto prestamo (Loan).
     *
     * @param fechaInicio the start date
     * @param fechaFin    the end date
     */
    public DTOPrestamo(Date fechaInicio, Date fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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
     * Gets fecha inicio.
     *
     * @return the start date
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Sets fecha inicio.
     *
     * @param fechaInicio the start date
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Gets fecha fin.
     *
     * @return the end date
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * Sets fecha fin.
     *
     * @param fechaFin the end date
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    // toString
    @Override
    public String toString() {
        return "DTOPrestamo{" +
                "id=" + id +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                '}';
    }
}
