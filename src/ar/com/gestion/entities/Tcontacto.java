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
@Table(name = "tcontacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tcontacto.findAll", query = "SELECT t FROM Tcontacto t")
    , @NamedQuery(name = "Tcontacto.findByTcontactoId", query = "SELECT t FROM Tcontacto t WHERE t.tcontactoId = :tcontactoId")
    , @NamedQuery(name = "Tcontacto.findByTcontactoDesc", query = "SELECT t FROM Tcontacto t WHERE t.tcontactoDesc = :tcontactoDesc")})
public class Tcontacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tcontacto_id")
    private Integer tcontactoId;
    @Basic(optional = false)
    @Column(name = "tcontacto_desc")
    private String tcontactoDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tcontactoId")
    private List<Libretacontacto> libretacontactoList;

    public Tcontacto() {
    }

    public Tcontacto(Integer tcontactoId) {
        this.tcontactoId = tcontactoId;
    }
    
    public Tcontacto(String tcontactoDesc) {
        this.tcontactoDesc = tcontactoDesc;
    }

    public Tcontacto(Integer tcontactoId, String tcontactoDesc) {
        this.tcontactoId = tcontactoId;
        this.tcontactoDesc = tcontactoDesc;
    }

    public Integer getTcontactoId() {
        return tcontactoId;
    }

    public void setTcontactoId(Integer tcontactoId) {
        this.tcontactoId = tcontactoId;
    }

    public String getTcontactoDesc() {
        return tcontactoDesc;
    }

    public void setTcontactoDesc(String tcontactoDesc) {
        this.tcontactoDesc = tcontactoDesc;
    }

    @XmlTransient
    public List<Libretacontacto> getLibretacontactoList() {
        return libretacontactoList;
    }

    public void setLibretacontactoList(List<Libretacontacto> libretacontactoList) {
        this.libretacontactoList = libretacontactoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (tcontactoId != null ? tcontactoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tcontacto)) {
            return false;
        }
        Tcontacto other = (Tcontacto) object;
        if ((this.tcontactoId == null && other.tcontactoId != null) || (this.tcontactoId != null && !this.tcontactoId.equals(other.tcontactoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tcontactoDesc ;
    }
    
}
