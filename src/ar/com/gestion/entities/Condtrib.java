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
import javax.persistence.Id;
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
@Table(name = "condtrib")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Condtrib.findAll", query = "SELECT c FROM Condtrib c")
    , @NamedQuery(name = "Condtrib.findByCondtribId", query = "SELECT c FROM Condtrib c WHERE c.condtribId = :condtribId")
    , @NamedQuery(name = "Condtrib.findByCondtribDesc", query = "SELECT c FROM Condtrib c WHERE c.condtribDesc = :condtribDesc")})
public class Condtrib implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "condtrib_id")
    private Integer condtribId;
    @Basic(optional = false)
    @Column(name = "condtrib_desc")
    private String condtribDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "condtribId")
    private List<Cliente> clienteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "condtribId")
    private Proveedor proveedor;

    public Condtrib() {
    }

    public Condtrib(Integer condtribId) {
        this.condtribId = condtribId;
    }

    public Condtrib(Integer condtribId, String condtribDesc) {
        this.condtribId = condtribId;
        this.condtribDesc = condtribDesc;
    }

    public Integer getCondtribId() {
        return condtribId;
    }

    public void setCondtribId(Integer condtribId) {
        this.condtribId = condtribId;
    }

    public String getCondtribDesc() {
        return condtribDesc;
    }

    public void setCondtribDesc(String condtribDesc) {
        this.condtribDesc = condtribDesc;
    }

    @XmlTransient
    public List<Cliente> getClienteList() {
        return clienteList;
    }

    public void setClienteList(List<Cliente> clienteList) {
        this.clienteList = clienteList;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (condtribId != null ? condtribId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Condtrib)) {
            return false;
        }
        Condtrib other = (Condtrib) object;
        if ((this.condtribId == null && other.condtribId != null) || (this.condtribId != null && !this.condtribId.equals(other.condtribId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return condtribDesc ;
    }
    
}
