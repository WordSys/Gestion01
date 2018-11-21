package ar.com.gestion.entities;

public class DetalleProveedor {
    private Integer codigo;
    private String razon;
    private String t_doc;
    private Long n_doc;
    private String condicion;
    private String calle;
    private Integer nro;
    private String calle1;
    private String calle2;
    private Integer piso;
    private String dpto;
    private String localidad;
    private Short cp;
    private String provincia;

    public DetalleProveedor(Integer codigo, String razon, String t_doc, Long n_doc, String condicion, String calle, Integer nro, String calle1, String calle2, Integer piso, String dpto, String localidad, Short cp, String provincia) {
        this.codigo = codigo;
        this.razon = razon;
        this.t_doc = t_doc;
        this.n_doc = n_doc;
        this.condicion = condicion;
        this.calle = calle;
        this.nro = nro;
        this.calle1 = calle1;
        this.calle2 = calle2;
        this.piso = piso;
        this.dpto = dpto;
        this.localidad = localidad;
        this.cp = cp;
        this.provincia = provincia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getRazon() {
        return razon;
    }

    public void setRazon(String razon) {
        this.razon = razon;
    }

    public String getT_doc() {
        return t_doc;
    }

    public void setT_doc(String t_doc) {
        this.t_doc = t_doc;
    }

    public Long getN_doc() {
        return n_doc;
    }

    public void setN_doc(Long n_doc) {
        this.n_doc = n_doc;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public Short getCp() {
        return cp;
    }

    public void setCp(Short cp) {
        this.cp = cp;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getNro() {
        return nro;
    }

    public void setNro(Integer nro) {
        this.nro = nro;
    }

    public String getCalle1() {
        return calle1;
    }

    public void setCalle1(String calle1) {
        this.calle1 = calle1;
    }

    public String getCalle2() {
        return calle2;
    }

    public void setCalle2(String calle2) {
        this.calle2 = calle2;
    }

    public Integer getPiso() {
        return piso;
    }

    public void setPiso(Integer piso) {
        this.piso = piso;
    }

    public String getDpto() {
        return dpto;
    }

    public void setDpto(String dpto) {
        this.dpto = dpto;
    }

    @Override
    public String toString() {
        return "DetalleProveedor{" + "codigo=" + codigo + ", razon=" + razon + ", t_doc=" + t_doc + ", n_doc=" + n_doc + ", condicion=" + condicion + ", calle=" + calle + ", nro=" + nro + ", calle1=" + calle1 + ", calle2=" + calle2 + ", piso=" + piso + ", dpto=" + dpto + ", localidad=" + localidad + ", cp=" + cp + ", provincia=" + provincia + '}';
    }
    
    
    
}
