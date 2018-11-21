/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "empleado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleado.findAll", query = "SELECT e FROM Empleado e")
    , @NamedQuery(name = "Empleado.findByEmpId", query = "SELECT e FROM Empleado e WHERE e.empId = :empId")
    , @NamedQuery(name = "Empleado.findByEmpNom", query = "SELECT e FROM Empleado e WHERE e.empNom = :empNom")
    , @NamedQuery(name = "Empleado.findByEmpApe", query = "SELECT e FROM Empleado e WHERE e.empApe = :empApe")
    , @NamedQuery(name = "Empleado.findByEmpFnac", query = "SELECT e FROM Empleado e WHERE e.empFnac = :empFnac")
    , @NamedQuery(name = "Empleado.findByEmpNdoc", query = "SELECT e FROM Empleado e WHERE e.empNdoc = :empNdoc")
    , @NamedQuery(name = "Empleado.findByEmpFing", query = "SELECT e FROM Empleado e WHERE e.empFing = :empFing")
    , @NamedQuery(name = "Empleado.findByEmpEcivil", query = "SELECT e FROM Empleado e WHERE e.empEcivil = :empEcivil")
    , @NamedQuery(name = "Empleado.findByEmpHijos", query = "SELECT e FROM Empleado e WHERE e.empHijos = :empHijos")})
public class Empleado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "emp_id")
    private Integer empId;
    @Basic(optional = false)
    @Column(name = "emp_nom")
    private String empNom;
    @Basic(optional = false)
    @Column(name = "emp_ape")
    private String empApe;
    @Basic(optional = false)
    @Column(name = "emp_fnac")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empFnac;
    @Basic(optional = false)
    @Column(name = "emp_ndoc")
    private int empNdoc;
    @Basic(optional = false)
    @Column(name = "emp_fing")
    @Temporal(TemporalType.TIMESTAMP)
    private Date empFing;
    @Basic(optional = false)
    @Column(name = "emp_ecivil")
    private String empEcivil;
    @Basic(optional = false)
    @Column(name = "emp_hijos")
    private int empHijos;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empId")
    private List<Compra> compraList;
    @JoinColumn(name = "cat_id", referencedColumnName = "cat_id")
    @OneToOne(optional = false)
    private Categoria catId;
    private Integer cat_Id;
    @JoinColumn(name = "dir_id", referencedColumnName = "dir_id")
    @OneToOne(optional = false)
    private Direccion dirId;
    private Integer dir_Id;
    @JoinColumn(name = "tdoc_id", referencedColumnName = "tdoc_id")
    @ManyToOne(optional = false)
    private Tdoc tdocId;
    private Integer tdoc_Id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empId")
    private List<Venta> ventaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empId")
    private List<Users> usersList;

    public Empleado() {
    }

    public Empleado(Integer empId) {
        this.empId = empId;
    }

    public Empleado(Integer empId, String empNom, String empApe, Date empFnac, int empNdoc, Date empFing, String empEcivil, int empHijos) {
        this.empId = empId;
        this.empNom = empNom;
        this.empApe = empApe;
        this.empFnac = empFnac;
        this.empNdoc = empNdoc;
        this.empFing = empFing;
        this.empEcivil = empEcivil;
        this.empHijos = empHijos;
    }
    
    public Empleado(Integer empId, String empNom, String empApe, Date empFnac,int tdoc_Id, int empNdoc,
            int dir_Id, Date empFing, String empEcivil, int empHijos, int cat_Id) {
        this.empId = empId;
        this.empNom = empNom;
        this.empApe = empApe;
        this.empFnac = empFnac;
        this.tdoc_Id = tdoc_Id;
        this.empNdoc = empNdoc;
        this.dir_Id = dir_Id;
        this.empFing = empFing;
        this.empEcivil = empEcivil;
        this.empHijos = empHijos;
        this.cat_Id = cat_Id;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getEmpNom() {
        return empNom;
    }

    public void setEmpNom(String empNom) {
        this.empNom = empNom;
    }

    public String getEmpApe() {
        return empApe;
    }

    public void setEmpApe(String empApe) {
        this.empApe = empApe;
    }

    public Date getEmpFnac() {
        return empFnac;
    }

    public void setEmpFnac(Date empFnac) {
        this.empFnac = empFnac;
    }

    public int getEmpNdoc() {
        return empNdoc;
    }

    public void setEmpNdoc(int empNdoc) {
        this.empNdoc = empNdoc;
    }

    public Date getEmpFing() {
        return empFing;
    }

    public void setEmpFing(Date empFing) {
        this.empFing = empFing;
    }

    public String getEmpEcivil() {
        return empEcivil;
    }

    public void setEmpEcivil(String empEcivil) {
        this.empEcivil = empEcivil;
    }

    public int getEmpHijos() {
        return empHijos;
    }

    public void setEmpHijos(int empHijos) {
        this.empHijos = empHijos;
    }

    @XmlTransient
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }

    public Categoria getCatId() {
        return catId;
    }

    public void setCatId(Categoria catId) {
        this.catId = catId;
    }

    public Direccion getDirId() {
        return dirId;
    }

    public void setDirId(Direccion dirId) {
        this.dirId = dirId;
    }

    public Tdoc getTdocId() {
        return tdocId;
    }

    public void setTdocId(Tdoc tdocId) {
        this.tdocId = tdocId;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (empId != null ? empId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleado)) {
            return false;
        }
        Empleado other = (Empleado) object;
        if ((this.empId == null && other.empId != null) || (this.empId != null && !this.empId.equals(other.empId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Empleado[ empId=" + empId + " ]";
    }
    
}
