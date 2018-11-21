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
@Table(name = "compra")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Compra.findAll", query = "SELECT c FROM Compra c")
    , @NamedQuery(name = "Compra.findByCompId", query = "SELECT c FROM Compra c WHERE c.compId = :compId")
    , @NamedQuery(name = "Compra.findByCompFecha", query = "SELECT c FROM Compra c WHERE c.compFecha = :compFecha")
    , @NamedQuery(name = "Compra.findByCompSubt", query = "SELECT c FROM Compra c WHERE c.compSubt = :compSubt")
    , @NamedQuery(name = "Compra.findByCompPerc", query = "SELECT c FROM Compra c WHERE c.compPerc = :compPerc")
    , @NamedQuery(name = "Compra.findByCompIva", query = "SELECT c FROM Compra c WHERE c.compIva = :compIva")
    , @NamedQuery(name = "Compra.findByCompTotal", query = "SELECT c FROM Compra c WHERE c.compTotal = :compTotal")})
public class Compra implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "comp_id")
    private Integer compId;
    @Basic(optional = false)
    @Column(name = "comp_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date compFecha;
    @Basic(optional = false)
    @Column(name = "comp_subt")
    private double compSubt;
    @Basic(optional = false)
    @Column(name = "comp_perc")
    private double compPerc;
    @Basic(optional = false)
    @Column(name = "comp_iva")
    private double compIva;
    @Basic(optional = false)
    @Column(name = "comp_total")
    private double compTotal;
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
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    @ManyToOne(optional = false)
    private Empleado empId;
    private Integer emp_Id;
    @JoinColumn(name = "provee_id", referencedColumnName = "prove_id")
    @ManyToOne(optional = false)
    private Proveedor proveeId;
    private Integer provee_Id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compId")
    private List<Detallecomp> detallecompList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pagoNfactura")
    private List<Pago> pagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "compId")
    private List<Ctapagar> ctapagarList;

    public Compra() {
    }

    public Compra(Integer compId) {
        this.compId = compId;
    }

    public Compra(Integer compId, Date compFecha, double compSubt, double compPerc, double compIva, double compTotal) {
        this.compId = compId;
        this.compFecha = compFecha;
        this.compSubt = compSubt;
        this.compPerc = compPerc;
        this.compIva = compIva;
        this.compTotal = compTotal;
    }

    public Compra(Integer compId, Date compFecha,String comprob_Letra,Integer comprob_Prefijo, Integer comprob_Nro,
            Integer emp_Id,Integer provee_Id,double compSubt, double compPerc, double compIva, double compTotal) {
        this.compId = compId;
        this.compFecha = compFecha;
        this.comprob_Letra = comprob_Letra;
        this.comprob_Prefijo = comprob_Prefijo;
        this.comprob_Nro = comprob_Nro;
        this.emp_Id = emp_Id;
        this.provee_Id = provee_Id;
        this.compSubt = compSubt;
        this.compPerc = compPerc;
        this.compIva = compIva;
        this.compTotal = compTotal;
    }

    public Integer getCompId() {
        return compId;
    }

    public void setCompId(Integer compId) {
        this.compId = compId;
    }

    public Date getCompFecha() {
        return compFecha;
    }

    public void setCompFecha(Date compFecha) {
        this.compFecha = compFecha;
    }

    public double getCompSubt() {
        return compSubt;
    }

    public void setCompSubt(double compSubt) {
        this.compSubt = compSubt;
    }

    public double getCompPerc() {
        return compPerc;
    }

    public void setCompPerc(double compPerc) {
        this.compPerc = compPerc;
    }

    public double getCompIva() {
        return compIva;
    }

    public void setCompIva(double compIva) {
        this.compIva = compIva;
    }

    public double getCompTotal() {
        return compTotal;
    }

    public void setCompTotal(double compTotal) {
        this.compTotal = compTotal;
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

    public Empleado getEmpId() {
        return empId;
    }

    public void setEmpId(Empleado empId) {
        this.empId = empId;
    }

    public Proveedor getProveeId() {
        return proveeId;
    }

    public void setProveeId(Proveedor proveeId) {
        this.proveeId = proveeId;
    }

    @XmlTransient
    public List<Detallecomp> getDetallecompList() {
        return detallecompList;
    }

    public void setDetallecompList(List<Detallecomp> detallecompList) {
        this.detallecompList = detallecompList;
    }

    @XmlTransient
    public List<Pago> getPagoList() {
        return pagoList;
    }

    public void setPagoList(List<Pago> pagoList) {
        this.pagoList = pagoList;
    }

    @XmlTransient
    public List<Ctapagar> getCtapagarList() {
        return ctapagarList;
    }

    public void setCtapagarList(List<Ctapagar> ctapagarList) {
        this.ctapagarList = ctapagarList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (compId != null ? compId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compra)) {
            return false;
        }
        Compra other = (Compra) object;
        if ((this.compId == null && other.compId != null) || (this.compId != null && !this.compId.equals(other.compId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Compra[ compId=" + compId + " ]";
    }
    
}
