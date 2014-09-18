package jazzyweb.tekila.model;

import java.util.List;

public class Usuario {
    private Long id;
    private String nombre;
    private List<Grupo> grupos;
    private List<Participacion> participaciones;
    private List<Pago> pagos;

    private Double cantidadAux;

    public Double getCantidadAux() {
        return cantidadAux;
    }

    public void setCantidadAux(Double cantidadAux) {
        this.cantidadAux = cantidadAux;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
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

    public Double getPagado(Long idGrupo){
        Double pagado = Double.valueOf(0.0);

        for(int i = 0; i < pagos.size(); i++){
            if(pagos.get(i).getCompra().getGrupo().getId() == idGrupo)
                pagado += pagos.get(i).getCantidad();
        }

        return  pagado;
    }

    public Double getGastado(Long idGrupo){
        Double gastado = Double.valueOf(0.0);

        for(int i = 0; i < participaciones.size(); i++){
            if(participaciones.get(i).getCompra().getGrupo().getId() == idGrupo)
                gastado += (participaciones.get(i).getPorcentaje() / 100) * participaciones.get(i).getCompra().getCantidad();
        }

        return  gastado;
    }

    public Double getDeuda(Long idGrupo){
        Double deuda = getPagado(idGrupo) - getGastado(idGrupo);
        return deuda;
    }

    public String toString(){
        return nombre;
    }

    @Override
    public boolean equals(java.lang.Object o){
        if (o == this) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }

        Usuario u = (Usuario) o;

        return u.getId() == id;
    }
}
