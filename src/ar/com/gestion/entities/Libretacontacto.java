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
@Table(name = "libretacontacto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libretacontacto.findAll", query = "SELECT l FROM Libretacontacto l")
    , @NamedQuery(name = "Libretacontacto.findByLibId", query = "SELECT l FROM Libretacontacto l WHERE l.libId = :libId")
    , @NamedQuery(name = "Libretacontacto.findByLibContacto", query = "SELECT l FROM Libretacontacto l WHERE l.libContacto = :libContacto")})
public class Libretacontacto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "lib_id")
    private Integer libId;
    @Basic(optional = false)
    @Column(name = "lib_contacto")
    private String libContacto;
    @JoinColumn(name = "dir_id", referencedColumnName = "dir_id")
    @ManyToOne(optional = false)
    private Direccion dirId;
    private Integer dir_Id;
    @JoinColumn(name = "tcontacto_id", referencedColumnName = "tcontacto_id")
    @ManyToOne(optional = false)
    private Tcontacto tcontactoId;
    private Integer tcontacto_Id;

    public Libretacontacto() {
    }

    public Libretacontacto(Integer libId) {
        this.libId = libId;
    }

    public Libretacontacto(Integer libId, String libContacto) {
        this.libId = libId;
        this.libContacto = libContacto;
    }

    public Libretacontacto(Integer libId, Integer tcontacto_Id, Integer dir_Id, String libContacto) {
        this.libId = libId;
        this.tcontacto_Id = tcontacto_Id;
        this.dir_Id = dir_Id;
        this.libContacto = libContacto;
    }
    
    public Libretacontacto(Integer tcontacto_Id, Integer dir_Id, String libContacto) {
        this.tcontacto_Id = tcontacto_Id;
        this.dir_Id = dir_Id;
        this.libContacto = libContacto;
    }

    public Integer getLibId() {
        return libId;
    }

    public void setLibId(Integer libId) {
        this.libId = libId;
    }

    public String getLibContacto() {
        return libContacto;
    }

    public void setLibContacto(String libContacto) {
        this.libContacto = libContacto;
    }

    public Direccion getDirId() {
        return dirId;
    }

    public void setDirId(Direccion dirId) {
        this.dirId = dirId;
    }

    public Tcontacto getTcontactoId() {
        return tcontactoId;
    }

    public void setTcontactoId(Tcontacto tcontactoId) {
        this.tcontactoId = tcontactoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (libId != null ? libId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libretacontacto)) {
            return false;
        }
        Libretacontacto other = (Libretacontacto) object;
        if ((this.libId == null && other.libId != null) || (this.libId != null && !this.libId.equals(other.libId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Libretacontacto[ libId=" + libId + " ]";
    }

    public Integer getDir_Id() {
        return dir_Id;
    }

    public void setDir_Id(Integer dir_Id) {
        this.dir_Id = dir_Id;
    }

    public Integer getTcontacto_Id() {
        return tcontacto_Id;
    }

    public void setTcontacto_Id(Integer tcontacto_Id) {
        this.tcontacto_Id = tcontacto_Id;
    }
    
}
