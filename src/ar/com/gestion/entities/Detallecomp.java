/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "detallecomp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallecomp.findAll", query = "SELECT d FROM Detallecomp d")
    , @NamedQuery(name = "Detallecomp.findByDetcompId", query = "SELECT d FROM Detallecomp d WHERE d.detcompId = :detcompId")
    , @NamedQuery(name = "Detallecomp.findByProdPu", query = "SELECT d FROM Detallecomp d WHERE d.prodPu = :prodPu")
    , @NamedQuery(name = "Detallecomp.findByDetcompMonto", query = "SELECT d FROM Detallecomp d WHERE d.detcompMonto = :detcompMonto")})
public class Detallecomp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detcomp_id")
    private Integer detcompId;
    @Basic(optional = false)
    @Column(name = "prod_pu")
    private double prodPu;
    @Basic(optional = false)
    @Column(name = "detcomp_monto")
    private double detcompMonto;
    @JoinColumn(name = "comp_id", referencedColumnName = "comp_id")
    @ManyToOne(optional = false)
    private Compra compId;
    private Integer comp_Id;
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id")
    @ManyToOne(optional = false)
    private Producto prodId;
    private Integer prod_Id;

    public Detallecomp() {
    }

    public Detallecomp(Integer detcompId) {
        this.detcompId = detcompId;
    }

    public Detallecomp(Integer detcompId, double prodPu, double detcompMonto) {
        this.detcompId = detcompId;
        this.prodPu = prodPu;
        this.detcompMonto = detcompMonto;
    }
    
    public Detallecomp(Integer detcompId, int comp_Id, int prod_Id, double prodPu, double detcompMonto) {
        this.detcompId = detcompId;
        this.comp_Id = comp_Id;
        this.prod_Id = prod_Id;
        this.prodPu = prodPu;
        this.detcompMonto = detcompMonto;
    }

    public Integer getDetcompId() {
        return detcompId;
    }

    public void setDetcompId(Integer detcompId) {
        this.detcompId = detcompId;
    }

    public double getProdPu() {
        return prodPu;
    }

    public void setProdPu(double prodPu) {
        this.prodPu = prodPu;
    }

    public double getDetcompMonto() {
        return detcompMonto;
    }

    public void setDetcompMonto(double detcompMonto) {
        this.detcompMonto = detcompMonto;
    }

    public Compra getCompId() {
        return compId;
    }

    public void setCompId(Compra compId) {
        this.compId = compId;
    }

    public Producto getProdId() {
        return prodId;
    }

    public void setProdId(Producto prodId) {
        this.prodId = prodId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detcompId != null ? detcompId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallecomp)) {
            return false;
        }
        Detallecomp other = (Detallecomp) object;
        if ((this.detcompId == null && other.detcompId != null) || (this.detcompId != null && !this.detcompId.equals(other.detcompId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Detallecomp[ detcompId=" + detcompId + " ]";
    }
    
}
