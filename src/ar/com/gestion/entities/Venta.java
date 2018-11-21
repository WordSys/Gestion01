/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "venta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v")
    , @NamedQuery(name = "Venta.findByVtaId", query = "SELECT v FROM Venta v WHERE v.vtaId = :vtaId")
    , @NamedQuery(name = "Venta.findByVtaFecha", query = "SELECT v FROM Venta v WHERE v.vtaFecha = :vtaFecha")
    , @NamedQuery(name = "Venta.findByVtaSubt", query = "SELECT v FROM Venta v WHERE v.vtaSubt = :vtaSubt")
    , @NamedQuery(name = "Venta.findByVtaPrec", query = "SELECT v FROM Venta v WHERE v.vtaPrec = :vtaPrec")
    , @NamedQuery(name = "Venta.findByVtaIva", query = "SELECT v FROM Venta v WHERE v.vtaIva = :vtaIva")
    , @NamedQuery(name = "Venta.findByVtaTotal", query = "SELECT v FROM Venta v WHERE v.vtaTotal = :vtaTotal")})
public class Venta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "vta_id")
    private Integer vtaId;
    @Basic(optional = false)
    @Column(name = "vta_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vtaFecha;
    @Basic(optional = false)
    @Column(name = "vta_subt")
    private double vtaSubt;
    @Basic(optional = false)
    @Column(name = "vta_prec")
    private double vtaPrec;
    @Basic(optional = false)
    @Column(name = "vta_iva")
    private double vtaIva;
    @Basic(optional = false)
    @Column(name = "vta_total")
    private double vtaTotal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vtaId")
    private List<Ctacobrar> ctacobrarList;
    @JoinColumn(name = "cli_id", referencedColumnName = "cli_id")
    @ManyToOne(optional = false)
    private Cliente cliId;
    private Integer cli_Id;
    @JoinColumn(name = "comprob_letra", referencedColumnName = "comprob_letra")
    @ManyToOne(optional = false)
    private Comprobante comprobLetra;
    private String comprob_Letra;
    @JoinColumn(name = "comprob_nro", referencedColumnName = "comprob_nro")
    @ManyToOne(optional = false)
    private Comprobante comprobNro;
    private Integer comprob_Nro;
    @JoinColumn(name = "comprob_prefijo", referencedColumnName = "comprob_prefijo")
    @ManyToOne(optional = false)
    private Comprobante comprobPrefijo;
    private Integer comprob_Prefijo;
    @JoinColumn(name = "condop_id", referencedColumnName = "condop_id")
    @ManyToOne(optional = false)
    private Condop condopId;
    private Integer condop_Id;
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    @ManyToOne(optional = false)
    private Empleado empId;
    private Integer emp_Id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cobNfact")
    private List<Cobranzas> cobranzasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vtaId")
    private List<Detallevta> detallevtaList;

    public Venta() {
    }

    public Venta(Integer vtaId) {
        this.vtaId = vtaId;
    }

    public Venta(Integer vtaId, Date vtaFecha, double vtaSubt, double vtaPrec, double vtaIva, double vtaTotal) {
        this.vtaId = vtaId;
        this.vtaFecha = vtaFecha;
        this.vtaSubt = vtaSubt;
        this.vtaPrec = vtaPrec;
        this.vtaIva = vtaIva;
        this.vtaTotal = vtaTotal;
    }
    
    public Venta(Integer vtaId, Date vtaFecha,String comprob_Letra,Integer comprob_Prefijo, Integer comprob_Nro,
            Integer emp_Id,Integer cli_Id,Integer condop_Id, double vtaSubt, double vtaPrec, double vtaIva, double vtaTotal) {
        this.vtaId = vtaId;
        this.vtaFecha = vtaFecha;
        this.comprob_Letra = comprob_Letra;
        this.comprob_Prefijo = comprob_Prefijo;
        this.comprob_Nro = comprob_Nro;
        this.emp_Id = emp_Id;
        this.cli_Id = cli_Id;
        this.condop_Id = condop_Id;
        this.vtaSubt = vtaSubt;
        this.vtaPrec = vtaPrec;
        this.vtaIva = vtaIva;
        this.vtaTotal = vtaTotal;
    }

    public Integer getVtaId() {
        return vtaId;
    }

    public void setVtaId(Integer vtaId) {
        this.vtaId = vtaId;
    }

    public Date getVtaFecha() {
        return vtaFecha;
    }

    public void setVtaFecha(Date vtaFecha) {
        this.vtaFecha = vtaFecha;
    }

    public double getVtaSubt() {
        return vtaSubt;
    }

    public void setVtaSubt(double vtaSubt) {
        this.vtaSubt = vtaSubt;
    }

    public double getVtaPrec() {
        return vtaPrec;
    }

    public void setVtaPrec(double vtaPrec) {
        this.vtaPrec = vtaPrec;
    }

    public double getVtaIva() {
        return vtaIva;
    }

    public void setVtaIva(double vtaIva) {
        this.vtaIva = vtaIva;
    }

    public double getVtaTotal() {
        return vtaTotal;
    }

    public void setVtaTotal(double vtaTotal) {
        this.vtaTotal = vtaTotal;
    }

    @XmlTransient
    public List<Ctacobrar> getCtacobrarList() {
        return ctacobrarList;
    }

    public void setCtacobrarList(List<Ctacobrar> ctacobrarList) {
        this.ctacobrarList = ctacobrarList;
    }

    public Cliente getCliId() {
        return cliId;
    }

    public void setCliId(Cliente cliId) {
        this.cliId = cliId;
    }

    public Comprobante getComprobLetra() {
        return comprobLetra;
    }

    public void setComprobLetra(Comprobante comprobLetra) {
        this.comprobLetra = comprobLetra;
    }

    public Comprobante getComprobNro() {
        return comprobNro;
    }

    public void setComprobNro(Comprobante comprobNro) {
        this.comprobNro = comprobNro;
    }

    public Comprobante getComprobPrefijo() {
        return comprobPrefijo;
    }

    public void setComprobPrefijo(Comprobante comprobPrefijo) {
        this.comprobPrefijo = comprobPrefijo;
    }

    public Condop getCondopId() {
        return condopId;
    }

    public void setCondopId(Condop condopId) {
        this.condopId = condopId;
    }

    public Empleado getEmpId() {
        return empId;
    }

    public void setEmpId(Empleado empId) {
        this.empId = empId;
    }

    @XmlTransient
    public List<Cobranzas> getCobranzasList() {
        return cobranzasList;
    }

    public void setCobranzasList(List<Cobranzas> cobranzasList) {
        this.cobranzasList = cobranzasList;
    }

    @XmlTransient
    public List<Detallevta> getDetallevtaList() {
        return detallevtaList;
    }

    public void setDetallevtaList(List<Detallevta> detallevtaList) {
        this.detallevtaList = detallevtaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vtaId != null ? vtaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.vtaId == null && other.vtaId != null) || (this.vtaId != null && !this.vtaId.equals(other.vtaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Venta[ vtaId=" + vtaId + " ]";
    }
    
}
