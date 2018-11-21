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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "enti_financ")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EntiFinanc.findAll", query = "SELECT e FROM EntiFinanc e")
    , @NamedQuery(name = "EntiFinanc.findByEntfId", query = "SELECT e FROM EntiFinanc e WHERE e.entfId = :entfId")
    , @NamedQuery(name = "EntiFinanc.findByEntfDesc", query = "SELECT e FROM EntiFinanc e WHERE e.entfDesc = :entfDesc")})
public class EntiFinanc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "entf_id")
    private Integer entfId;
    @Column(name = "entf_desc")
    private String entfDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entiId")
    private List<Cheques> chequesList;

    public EntiFinanc() {
    }

    public EntiFinanc(Integer entfId) {
        this.entfId = entfId;
    }

    public EntiFinanc(Integer entfId, String entfDesc) {
        this.entfId = entfId;
        this.entfDesc = entfDesc;
    }

    public Integer getEntfId() {
        return entfId;
    }

    public void setEntfId(Integer entfId) {
        this.entfId = entfId;
    }

    public String getEntfDesc() {
        return entfDesc;
    }

    public void setEntfDesc(String entfDesc) {
        this.entfDesc = entfDesc;
    }

    @XmlTransient
    public List<Cheques> getChequesList() {
        return chequesList;
    }

    public void setChequesList(List<Cheques> chequesList) {
        this.chequesList = chequesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (entfId != null ? entfId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EntiFinanc)) {
            return false;
        }
        EntiFinanc other = (EntiFinanc) object;
        if ((this.entfId == null && other.entfId != null) || (this.entfId != null && !this.entfId.equals(other.entfId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.EntiFinanc[ entfId=" + entfId + " ]";
    }
    
}
