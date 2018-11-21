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
@Table(name = "tdoc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tdoc.findAll", query = "SELECT t FROM Tdoc t")
    , @NamedQuery(name = "Tdoc.findByTdocId", query = "SELECT t FROM Tdoc t WHERE t.tdocId = :tdocId")
    , @NamedQuery(name = "Tdoc.findByTdocDesc", query = "SELECT t FROM Tdoc t WHERE t.tdocDesc = :tdocDesc")})
public class Tdoc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "tdoc_id")
    private Integer tdocId;
    @Basic(optional = false)
    @Column(name = "tdoc_desc")
    private String tdocDesc;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tdocId")
    private List<Empleado> empleadoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tdocId")
    private List<Cliente> clienteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "tdocId")
    private Proveedor proveedor;
    private List<Tdoc> tdocList;

    public Tdoc() {
    }

    public Tdoc(Integer tdocId) {
        this.tdocId = tdocId;
    }
    
    public Tdoc(String tdocDesc) {
        this.tdocDesc = tdocDesc;
    }

    public Tdoc(Integer tdocId, String tdocDesc) {
        this.tdocId = tdocId;
        this.tdocDesc = tdocDesc;
    }

    public Integer getTdocId() {
        return tdocId;
    }

    public void setTdocId(Integer tdocId) {
        this.tdocId = tdocId;
    }

    public String getTdocDesc() {
        return tdocDesc;
    }

    public void setTdocDesc(String tdocDesc) {
        this.tdocDesc = tdocDesc;
    }
    
    @XmlTransient
    public List<Tdoc> getTdocList() {
        return tdocList;
    }

    public void setTdocList(List<Tdoc> tdocList) {
        this.tdocList = tdocList;
    }

    @XmlTransient
    public List<Empleado> getEmpleadoList() {
        return empleadoList;
    }

    public void setEmpleadoList(List<Empleado> empleadoList) {
        this.empleadoList = empleadoList;
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
        hash += (tdocId != null ? tdocId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tdoc)) {
            return false;
        }
        Tdoc other = (Tdoc) object;
        if ((this.tdocId == null && other.tdocId != null) || (this.tdocId != null && !this.tdocId.equals(other.tdocId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  tdocDesc;
    }
    
}
