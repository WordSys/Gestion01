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
@Table(name = "ctacobrar")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ctacobrar.findAll", query = "SELECT c FROM Ctacobrar c")
    , @NamedQuery(name = "Ctacobrar.findByCcId", query = "SELECT c FROM Ctacobrar c WHERE c.ccId = :ccId")
    , @NamedQuery(name = "Ctacobrar.findByVtaMonto", query = "SELECT c FROM Ctacobrar c WHERE c.vtaMonto = :vtaMonto")})
public class Ctacobrar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cc_id")
    private Integer ccId;
    @Basic(optional = false)
    @Column(name = "vta_monto")
    private double vtaMonto;
    @JoinColumn(name = "cli_id", referencedColumnName = "cli_id")
    @ManyToOne(optional = false)
    private Cliente cliId;
    private Integer cli_Id;
    @JoinColumn(name = "vta_id", referencedColumnName = "vta_id")
    @ManyToOne(optional = false)
    private Venta vtaId;
    private Integer vta_Id;

    public Ctacobrar() {
    }

    public Ctacobrar(Integer ccId) {
        this.ccId = ccId;
    }

    public Ctacobrar(Integer ccId, double vtaMonto) {
        this.ccId = ccId;
        this.vtaMonto = vtaMonto;
    }

    public Ctacobrar(Integer ccId,Integer cli_Id,Integer vta_Id, double vtaMonto) {
        this.ccId = ccId;
        this.cli_Id = cli_Id;
        this.vta_Id = vta_Id;
        this.vtaMonto = vtaMonto;
    }

    public Integer getCcId() {
        return ccId;
    }

    public void setCcId(Integer ccId) {
        this.ccId = ccId;
    }

    public double getVtaMonto() {
        return vtaMonto;
    }

    public void setVtaMonto(double vtaMonto) {
        this.vtaMonto = vtaMonto;
    }

    public Cliente getCliId() {
        return cliId;
    }

    public void setCliId(Cliente cliId) {
        this.cliId = cliId;
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
        hash += (ccId != null ? ccId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ctacobrar)) {
            return false;
        }
        Ctacobrar other = (Ctacobrar) object;
        if ((this.ccId == null && other.ccId != null) || (this.ccId != null && !this.ccId.equals(other.ccId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Ctacobrar[ ccId=" + ccId + " ]";
    }
    
}
