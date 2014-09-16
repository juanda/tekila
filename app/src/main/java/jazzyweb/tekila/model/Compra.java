package jazzyweb.tekila.model;

import java.util.List;

public class Compra {
    private Long id;
    private String nombre;
    private Double cantidad;
    private Grupo grupo;
    private Long datetime;
    private List<Participacion> participaciones;
    private List<Pago> pagos;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Participacion> getParticipaciones() {
        return participaciones;
    }

    public void setParticipaciones(List<Participacion> participaciones) {
        this.participaciones = participaciones;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
