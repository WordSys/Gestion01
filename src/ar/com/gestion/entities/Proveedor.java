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
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "proveedor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Proveedor.findAll", query = "SELECT p FROM Proveedor p")
    , @NamedQuery(name = "Proveedor.findByProveId", query = "SELECT p FROM Proveedor p WHERE p.proveId = :proveId")
    , @NamedQuery(name = "Proveedor.findByProveRazon", query = "SELECT p FROM Proveedor p WHERE p.proveRazon = :proveRazon")
    , @NamedQuery(name = "Proveedor.findByProveNdoc", query = "SELECT p FROM Proveedor p WHERE p.proveNdoc = :proveNdoc")})
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "prove_id")
    private Integer proveId;
    @Basic(optional = false)
    @Column(name = "prove_razon")
    private String proveRazon;
    @Basic(optional = false)
    @Column(name = "prove_ndoc")
    private long proveNdoc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveeId")
    private List<Compra> compraList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveId")
    private List<Producto> productoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveeId")
    private List<Pago> pagoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "proveId")
    private List<Ctapagar> ctapagarList;
    @JoinColumn(name = "condtrib_id", referencedColumnName = "condtrib_id")
    @OneToOne(optional = false)
    private Condtrib condtribId;
    private Integer condtrib_Id;
    @JoinColumn(name = "dir_id", referencedColumnName = "dir_id")
    @OneToOne(optional = false)
    private Direccion dirId;
    private Integer dir_Id;
    @JoinColumn(name = "tdoc_id", referencedColumnName = "tdoc_id")
    @OneToOne(optional = false)
    private Tdoc tdocId;
    private Integer tdoc_Id;

    public Proveedor() {
    }

    public Proveedor(Integer proveId) {
        this.proveId = proveId;
    }

    public Proveedor(Integer proveId, String proveRazon, int proveNdoc) {
        this.proveId = proveId;
        this.proveRazon = proveRazon;
        this.proveNdoc = proveNdoc;
    }
    
    public Proveedor(Integer proveId, String proveRazon) {
        this.proveId = proveId;
        this.proveRazon = proveRazon;
    }
    
    public Proveedor(Integer proveId, String proveRazon,int tdoc_Id, long proveNdoc,int condtrib_Id,int dir_Id) {
        
        this.proveId = proveId;
        this.proveRazon = proveRazon;
        this.tdoc_Id = tdoc_Id;
        this.proveNdoc = proveNdoc;
        this.condtrib_Id = condtrib_Id;
        this.dir_Id = dir_Id;
    }
    
    public Proveedor(String proveRazon,int tdoc_Id, long proveNdoc,int condtrib_Id,int dir_Id) {
        
        this.proveRazon = proveRazon;
        this.tdoc_Id = tdoc_Id;
        this.proveNdoc = proveNdoc;
        this.condtrib_Id = condtrib_Id;
        this.dir_Id = dir_Id;
    }
    
    public Proveedor(String proveRazon,int tdoc_Id, long proveNdoc,int condtrib_Id) {
        
        this.proveRazon = proveRazon;
        this.tdoc_Id = tdoc_Id;
        this.proveNdoc = proveNdoc;
        this.condtrib_Id = condtrib_Id;
    }

    public Proveedor(String proveRazon, long proveNdoc, Condtrib condtribId, Direccion dirId, Tdoc tdocId) {
        this.proveRazon = proveRazon;
        this.proveNdoc = proveNdoc;
        this.condtribId = condtribId;
        this.dirId = dirId;
        this.tdocId = tdocId;
    }
    
    public Integer getProveId() {
        return proveId;
    }

    public void setProveId(Integer proveId) {
        this.proveId = proveId;
    }

    public String getProveRazon() {
        return proveRazon;
    }

    public void setProveRazon(String proveRazon) {
        this.proveRazon = proveRazon;
    }

    public long getProveNdoc() {
        return proveNdoc;
    }

    public void setProveNdoc(int proveNdoc) {
        this.proveNdoc = proveNdoc;
    }

    @XmlTransient
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }

    @XmlTransient
    public List<Producto> getProductoList() {
        return productoList;
    }

    public void setProductoList(List<Producto> productoList) {
        this.productoList = productoList;
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

    public Condtrib getCondtribId() {
        return condtribId;
    }

    public void setCondtribId(Condtrib condtribId) {
        this.condtribId = condtribId;
    }

    public Direccion getDirId() {
        return dirId;
    }

    public void setDirId(Direccion dirId) {
        this.dirId = dirId;
    }

    public Tdoc getTdocId() {
        return tdocId;
    }

    public void setTdocId(Tdoc tdocId) {
        this.tdocId = tdocId;
    }

    public Integer getCondtrib_Id() {
        return condtrib_Id;
    }

    public void setCondtrib_Id(Integer condtrib_Id) {
        this.condtrib_Id = condtrib_Id;
    }

    public Integer getDir_Id() {
        return dir_Id;
    }

    public void setDir_Id(Integer dir_Id) {
        this.dir_Id = dir_Id;
    }

    public Integer getTdoc_Id() {
        return tdoc_Id;
    }

    public void setTdoc_Id(Integer tdoc_Id) {
        this.tdoc_Id = tdoc_Id;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (proveId != null ? proveId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.proveId == null && other.proveId != null) || (this.proveId != null && !this.proveId.equals(other.proveId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  proveId + " - " + proveRazon;
    }
    
}
