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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "provincia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Provincia.findAll", query = "SELECT p FROM Provincia p")
    , @NamedQuery(name = "Provincia.findById", query = "SELECT p FROM Provincia p WHERE p.id = :id")
    , @NamedQuery(name = "Provincia.findByNombre", query = "SELECT p FROM Provincia p WHERE p.nombre = :nombre")
    , @NamedQuery(name = "Provincia.findByCodigo31662", query = "SELECT p FROM Provincia p WHERE p.codigo31662 = :codigo31662")})
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigo31662")
    private String codigo31662;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provinciaId")
    private List<Localidad> localidadList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "provinciaId")
    private Direccion direccion;

    public Provincia() {
    }

    public Provincia(Short id) {
        this.id = id;
    }
    
    public Provincia(String nombre) {
        this.nombre = nombre;
    }

    public Provincia(Short id, String nombre, String codigo31662) {
        this.id = id;
        this.nombre = nombre;
        this.codigo31662 = codigo31662;
    }
    
    public Provincia(Short id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public Provincia(String nombre, String codigo31662) {
        this.nombre = nombre;
        this.codigo31662 = codigo31662;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo31662() {
        return codigo31662;
    }

    public void setCodigo31662(String codigo31662) {
        this.codigo31662 = codigo31662;
    }

    @XmlTransient
    public List<Localidad> getLocalidadList() {
        return localidadList;
    }

    public void setLocalidadList(List<Localidad> localidadList) {
        this.localidadList = localidadList;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Provincia)) {
            return false;
        }
        Provincia other = (Provincia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
}
