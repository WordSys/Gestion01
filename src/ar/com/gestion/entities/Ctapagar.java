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
@Table(name = "ctapagar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ctapagar.findAll", query = "SELECT c FROM Ctapagar c")
    , @NamedQuery(name = "Ctapagar.findByCpId", query = "SELECT c FROM Ctapagar c WHERE c.cpId = :cpId")
    , @NamedQuery(name = "Ctapagar.findByCompMonto", query = "SELECT c FROM Ctapagar c WHERE c.compMonto = :compMonto")})
public class Ctapagar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cp_id")
    private Integer cpId;
    @Basic(optional = false)
    @Column(name = "comp_monto")
    private double compMonto;
    @JoinColumn(name = "comp_id", referencedColumnName = "comp_id")
    @ManyToOne(optional = false)
    private Compra compId;
    private Integer comp_Id;
    @JoinColumn(name = "prove_id", referencedColumnName = "prove_id")
    @ManyToOne(optional = false)
    private Proveedor proveId;
    private Integer prove_Id;

    public Ctapagar() {
    }

    public Ctapagar(Integer cpId) {
        this.cpId = cpId;
    }

    public Ctapagar(Integer cpId, double compMonto) {
        this.cpId = cpId;
        this.compMonto = compMonto;
    }
    
    public Ctapagar(Integer cpId, Integer comp_Id, Integer prove_id, double compMonto) {
        this.cpId = cpId;
        this.comp_Id = comp_Id;
        this.prove_Id = prove_id;
        this.compMonto = compMonto;
    }

    public Integer getCpId() {
        return cpId;
    }

    public void setCpId(Integer cpId) {
        this.cpId = cpId;
    }

    public double getCompMonto() {
        return compMonto;
    }

    public void setCompMonto(double compMonto) {
        this.compMonto = compMonto;
    }

    public Compra getCompId() {
        return compId;
    }

    public void setCompId(Compra compId) {
        this.compId = compId;
    }

    public Proveedor getProveId() {
        return proveId;
    }

    public void setProveId(Proveedor proveId) {
        this.proveId = proveId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpId != null ? cpId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ctapagar)) {
            return false;
        }
        Ctapagar other = (Ctapagar) object;
        if ((this.cpId == null && other.cpId != null) || (this.cpId != null && !this.cpId.equals(other.cpId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Ctapagar[ cpId=" + cpId + " ]";
    }
    
}
