/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p")
    , @NamedQuery(name = "Producto.findByProdId", query = "SELECT p FROM Producto p WHERE p.prodId = :prodId")
    , @NamedQuery(name = "Producto.findByProdDesc", query = "SELECT p FROM Producto p WHERE p.prodDesc = :prodDesc")
    , @NamedQuery(name = "Producto.findByProdCan", query = "SELECT p FROM Producto p WHERE p.prodCan = :prodCan")
    , @NamedQuery(name = "Producto.findByProdPu", query = "SELECT p FROM Producto p WHERE p.prodPu = :prodPu")})
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "prod_id")
    private Integer prodId;
    @Basic(optional = false)
    @Column(name = "prod_desc")
    private String prodDesc;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "prod_can")
    private Double prodCan;
    @Basic(optional = false)
    @Column(name = "prod_pu")
    private Double prodPu;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prodId")
    private List<Detallecomp> detallecompList;
    @JoinColumn(name = "prove_id", referencedColumnName = "prove_id")
    @ManyToOne(optional = false)
    private Proveedor proveId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prodId")
    private List<Detallevta> detallevtaList;
    private Integer prove_id;

    public Producto() {
    }

    public Producto(Integer prodId) {
        this.prodId = prodId;
    }

    public Producto(Integer prodId, String prodDesc, Double prodCan, Double prodPu) {
        this.prodId = prodId;
        this.prodDesc = prodDesc;
        this.prodCan = prodCan;
        this.prodPu = prodPu;
    }
    
    public Producto(String prodDesc, Double prodCan, Double prodPu, Proveedor proveId) {
        
        this.prodDesc = prodDesc;
        this.prodCan = prodCan;
        this.prodPu = prodPu;
        this.proveId = proveId;
    }
    
    public Producto(String prodDesc,Double prodCan, Double prodPu) {
        this.prodDesc = prodDesc;
        this.prodCan = prodCan;
        this.prodPu = prodPu;
    }

    public Producto(Integer prodId, String nombre, Double cantidad, Double precio, Integer prove_id) {
        this.prodId = prodId;
        this.prodDesc = nombre;
        this.prodCan = cantidad;
        this.prodPu = precio;
        this.prove_id = prove_id;
    }

    public Integer getProdId() {
        return prodId;
    }

    public void setProdId(Integer prodId) {
        this.prodId = prodId;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public Double getProdCan() {
        return prodCan;
    }

    public void setProdCan(Double prodCan) {
        this.prodCan = prodCan;
    }

    public Double getProdPu() {
        return prodPu;
    }

    public void setProdPu(Double prodPu) {
        this.prodPu = prodPu;
    }

    @XmlTransient
    public List<Detallecomp> getDetallecompList() {
        return detallecompList;
    }

    public void setDetallecompList(List<Detallecomp> detallecompList) {
        this.detallecompList = detallecompList;
    }

    public Proveedor getProveId() {
        return proveId;
    }

    public void setProveId(Proveedor proveId) {
        this.proveId = proveId;
    }
    
    public Integer getIntProveId() {
        return prove_id;
    }
    
    public void setIntProveId(Integer prove_Id) {
        this.prove_id = prove_Id;
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
        hash += (prodId != null ? prodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.prodId == null && other.prodId != null) || (this.prodId != null && !this.prodId.equals(other.prodId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return " Producto[ " + prodId + ", " + prodDesc + ", " + prodCan + ", " + prodPu + ", " + prove_id + " ]";
    }
    
}
