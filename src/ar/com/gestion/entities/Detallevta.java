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
@Table(name = "detallevta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detallevta.findAll", query = "SELECT d FROM Detallevta d")
    , @NamedQuery(name = "Detallevta.findByDetvtaId", query = "SELECT d FROM Detallevta d WHERE d.detvtaId = :detvtaId")
    , @NamedQuery(name = "Detallevta.findByProdPu", query = "SELECT d FROM Detallevta d WHERE d.prodPu = :prodPu")
    , @NamedQuery(name = "Detallevta.findByDetvtaCan", query = "SELECT d FROM Detallevta d WHERE d.detvtaCan = :detvtaCan")})
public class Detallevta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "detvta_id")
    private Integer detvtaId;
    @Basic(optional = false)
    @Column(name = "prod_pu")
    private double prodPu;
    @Basic(optional = false)
    @Column(name = "detvta_can")
    private double detvtaCan;
    @JoinColumn(name = "prod_id", referencedColumnName = "prod_id")
    @ManyToOne(optional = false)
    private Producto prodId;
    private Integer prod_Id;
    @JoinColumn(name = "vta_id", referencedColumnName = "vta_id")
    @ManyToOne(optional = false)
    private Venta vtaId;
    private Integer vta_Id;

    public Detallevta() {
    }

    public Detallevta(Integer detvtaId) {
        this.detvtaId = detvtaId;
    }

    public Detallevta(Integer detvtaId, double prodPu, double detvtaCan) {
        this.detvtaId = detvtaId;
        this.prodPu = prodPu;
        this.detvtaCan = detvtaCan;
    }

    public Detallevta(Integer detvtaId, Integer vta_Id, Integer prod_Id, double prodPu, double detvtaCan) {
        this.detvtaId = detvtaId;
        this.vta_Id = vta_Id;
        this.prod_Id = prod_Id;
        this.prodPu = prodPu;
        this.detvtaCan = detvtaCan;
    }

    public Integer getDetvtaId() {
        return detvtaId;
    }

    public void setDetvtaId(Integer detvtaId) {
        this.detvtaId = detvtaId;
    }

    public double getProdPu() {
        return prodPu;
    }

    public void setProdPu(double prodPu) {
        this.prodPu = prodPu;
    }

    public double getDetvtaCan() {
        return detvtaCan;
    }

    public void setDetvtaCan(double detvtaCan) {
        this.detvtaCan = detvtaCan;
    }

    public Producto getProdId() {
        return prodId;
    }

    public void setProdId(Producto prodId) {
        this.prodId = prodId;
    }

    public Venta getVtaId() {
        return vtaId;
    }

    public void setVtaId(Venta vtaId) {
        this.vtaId = vtaId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detvtaId != null ? detvtaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detallevta)) {
            return false;
        }
        Detallevta other = (Detallevta) object;
        if ((this.detvtaId == null && other.detvtaId != null) || (this.detvtaId != null && !this.detvtaId.equals(other.detvtaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Detallevta[ detvtaId=" + detvtaId + " ]";
    }
    
}
