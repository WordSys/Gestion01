/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "localidad")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Localidad.findAll", query = "SELECT l FROM Localidad l")
    , @NamedQuery(name = "Localidad.findById", query = "SELECT l FROM Localidad l WHERE l.id = :id")
    , @NamedQuery(name = "Localidad.findByNombre", query = "SELECT l FROM Localidad l WHERE l.nombre = :nombre")
    , @NamedQuery(name = "Localidad.findByCodigopostal", query = "SELECT l FROM Localidad l WHERE l.codigopostal = :codigopostal")})
public class Localidad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "codigopostal")
    private short codigopostal;
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Provincia provinciaId;
    private Integer provincia_Id;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dirCp")
    private Direccion direccion;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "localidadId")
    private Direccion direccion1;

    public Localidad() {
    }

    public Localidad(Integer id) {
        this.id = id;
    }

    public Localidad(Integer id, String nombre, short codigopostal) {
        this.id = id;
        this.nombre = nombre;
        this.codigopostal = codigopostal;
    }
    
    public Localidad(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
    
    public Localidad(String nombre) {
        this.nombre = nombre;
    }
    
    public Localidad(Integer id,Integer provincia_id, String nombre, short codigopostal) {
        this.id = id;
        this.provincia_Id = provincia_id;
        this.nombre = nombre;
        this.codigopostal = codigopostal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getCodigopostal() {
        return codigopostal;
    }

    public void setCodigopostal(short codigopostal) {
        this.codigopostal = codigopostal;
    }

    public Provincia getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Provincia provinciaId) {
        this.provinciaId = provinciaId;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Direccion getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(Direccion direccion1) {
        this.direccion1 = direccion1;
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
        if (!(object instanceof Localidad)) {
            return false;
        }
        Localidad other = (Localidad) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre ;
    }
    
}
