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
@Table(name = "condop")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Condop.findAll", query = "SELECT c FROM Condop c")
    , @NamedQuery(name = "Condop.findByCondopId", query = "SELECT c FROM Condop c WHERE c.condopId = :condopId")
    , @NamedQuery(name = "Condop.findByCondopDesc", query = "SELECT c FROM Condop c WHERE c.condopDesc = :condopDesc")})
public class Condop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "condop_id")
    private Integer condopId;
    @Basic(optional = false)
    @Column(name = "condop_desc")
    private String condopDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "condopId")
    private List<Venta> ventaList;

    public Condop() {
    }

    public Condop(Integer condopId) {
        this.condopId = condopId;
    }

    public Condop(Integer condopId, String condopDesc) {
        this.condopId = condopId;
        this.condopDesc = condopDesc;
    }

    public Integer getCondopId() {
        return condopId;
    }

    public void setCondopId(Integer condopId) {
        this.condopId = condopId;
    }

    public String getCondopDesc() {
        return condopDesc;
    }

    public void setCondopDesc(String condopDesc) {
        this.condopDesc = condopDesc;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (condopId != null ? condopId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Condop)) {
            return false;
        }
        Condop other = (Condop) object;
        if ((this.condopId == null && other.condopId != null) || (this.condopId != null && !this.condopId.equals(other.condopId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Condop[ condopId=" + condopId + " ]";
    }
    
}
