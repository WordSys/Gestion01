/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "comprobante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Comprobante.findAll", query = "SELECT c FROM Comprobante c")
    , @NamedQuery(name = "Comprobante.findByComprobId", query = "SELECT c FROM Comprobante c WHERE c.comprobId = :comprobId")
    , @NamedQuery(name = "Comprobante.findByComprobDesc", query = "SELECT c FROM Comprobante c WHERE c.comprobDesc = :comprobDesc")
    , @NamedQuery(name = "Comprobante.findByComprobLetra", query = "SELECT c FROM Comprobante c WHERE c.comprobLetra = :comprobLetra")
    , @NamedQuery(name = "Comprobante.findByComprobPrefijo", query = "SELECT c FROM Comprobante c WHERE c.comprobPrefijo = :comprobPrefijo")
    , @NamedQuery(name = "Comprobante.findByComprobNro", query = "SELECT c FROM Comprobante c WHERE c.comprobNro = :comprobNro")})
public class Comprobante implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "comprob_id")
    private Integer comprobId;
    @Basic(optional = false)
    @Column(name = "comprob_desc")
    private String comprobDesc;
    @Basic(optional = false)
    @Column(name = "comprob_letra")
    private String comprobLetra;
    @Basic(optional = false)
    @Column(name = "comprob_prefijo")
    private int comprobPrefijo;
    @Basic(optional = false)
    @Column(name = "comprob_nro")
    private int comprobNro;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobLetra")
    private List<Compra> compraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobNro")
    private List<Compra> compraList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobPrefijo")
    private List<Compra> compraList2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobLetra")
    private List<Venta> ventaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobNro")
    private List<Venta> ventaList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comprobPrefijo")
    private List<Venta> ventaList2;

    public Comprobante() {
    }

    public Comprobante(Integer comprobId) {
        this.comprobId = comprobId;
    }

    public Comprobante(Integer comprobId, String comprobDesc, String comprobLetra, int comprobPrefijo, int comprobNro) {
        this.comprobId = comprobId;
        this.comprobDesc = comprobDesc;
        this.comprobLetra = comprobLetra;
        this.comprobPrefijo = comprobPrefijo;
        this.comprobNro = comprobNro;
    }

    public Integer getComprobId() {
        return comprobId;
    }

    public void setComprobId(Integer comprobId) {
        this.comprobId = comprobId;
    }

    public String getComprobDesc() {
        return comprobDesc;
    }

    public void setComprobDesc(String comprobDesc) {
        this.comprobDesc = comprobDesc;
    }

    public String getComprobLetra() {
        return comprobLetra;
    }

    public void setComprobLetra(String comprobLetra) {
        this.comprobLetra = comprobLetra;
    }

    public int getComprobPrefijo() {
        return comprobPrefijo;
    }

    public void setComprobPrefijo(int comprobPrefijo) {
        this.comprobPrefijo = comprobPrefijo;
    }

    public int getComprobNro() {
        return comprobNro;
    }

    public void setComprobNro(int comprobNro) {
        this.comprobNro = comprobNro;
    }

    @XmlTransient
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }

    @XmlTransient
    public List<Compra> getCompraList1() {
        return compraList1;
    }

    public void setCompraList1(List<Compra> compraList1) {
        this.compraList1 = compraList1;
    }

    @XmlTransient
    public List<Compra> getCompraList2() {
        return compraList2;
    }

    public void setCompraList2(List<Compra> compraList2) {
        this.compraList2 = compraList2;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @XmlTransient
    public List<Venta> getVentaList1() {
        return ventaList1;
    }

    public void setVentaList1(List<Venta> ventaList1) {
        this.ventaList1 = ventaList1;
    }

    @XmlTransient
    public List<Venta> getVentaList2() {
        return ventaList2;
    }

    public void setVentaList2(List<Venta> ventaList2) {
        this.ventaList2 = ventaList2;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (comprobId != null ? comprobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Comprobante)) {
            return false;
        }
        Comprobante other = (Comprobante) object;
        if ((this.comprobId == null && other.comprobId != null) || (this.comprobId != null && !this.comprobId.equals(other.comprobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Comprobante[ comprobId=" + comprobId + " ]";
    }
    
}
