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
import javax.persistence.JoinColumn;
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
@Table(name = "direccion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Direccion.findAll", query = "SELECT d FROM Direccion d")
    , @NamedQuery(name = "Direccion.findByDirId", query = "SELECT d FROM Direccion d WHERE d.dirId = :dirId")
    , @NamedQuery(name = "Direccion.findByDirCalle", query = "SELECT d FROM Direccion d WHERE d.dirCalle = :dirCalle")
    , @NamedQuery(name = "Direccion.findByDirNro", query = "SELECT d FROM Direccion d WHERE d.dirNro = :dirNro")
    , @NamedQuery(name = "Direccion.findByDirCruce1", query = "SELECT d FROM Direccion d WHERE d.dirCruce1 = :dirCruce1")
    , @NamedQuery(name = "Direccion.findByDirCruce2", query = "SELECT d FROM Direccion d WHERE d.dirCruce2 = :dirCruce2")
    , @NamedQuery(name = "Direccion.findByDirPiso", query = "SELECT d FROM Direccion d WHERE d.dirPiso = :dirPiso")
    , @NamedQuery(name = "Direccion.findByDirDpto", query = "SELECT d FROM Direccion d WHERE d.dirDpto = :dirDpto")})
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "dir_id")
    private Integer dirId;
    @Basic(optional = false)
    @Column(name = "dir_calle")
    private String dirCalle;
    @Basic(optional = false)
    @Column(name = "dir_nro")
    private int dirNro;
    @Basic(optional = false)
    @Column(name = "dir_cruce1")
    private String dirCruce1;
    @Basic(optional = false)
    @Column(name = "dir_cruce2")
    private String dirCruce2;
    @Basic(optional = false)
    @Column(name = "dir_piso")
    private int dirPiso;
    @Basic(optional = false)
    @Column(name = "dir_dpto")
    private String dirDpto;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dirId")
    private Empleado empleado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dirId")
    private List<Libretacontacto> libretacontactoList;
    @JoinColumn(name = "dir_cp", referencedColumnName = "codigopostal")
    @OneToOne(optional = false)
    private Localidad dirCp;
    private Integer dir_Cp;
    private Short dir_CpShort;
    @JoinColumn(name = "localidad_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Localidad localidadId;
    private Integer localidad_Id;
    @JoinColumn(name = "provincia_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Provincia provinciaId;
    private Integer provincia_Id;
    private Short provincia_IdShort;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "dirId")
    private List<Cliente> clienteList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "dirId")
    private Proveedor proveedor;

    public Direccion() {
    }

    public Direccion(Integer dirId) {
        this.dirId = dirId;
    }

    public Direccion(Integer dirId, String dirCalle, int dirNro, String dirCruce1, String dirCruce2, int dirPiso, String dirDpto) {
        this.dirId = dirId;
        this.dirCalle = dirCalle;
        this.dirNro = dirNro;
        this.dirCruce1 = dirCruce1;
        this.dirCruce2 = dirCruce2;
        this.dirPiso = dirPiso;
        this.dirDpto = dirDpto;
    }
    
    public Direccion(String dirCalle, int dirNro, String dirCruce1, String dirCruce2, int dirPiso, String dirDpto, 
            short dir_Cp, Integer localidad_Id, short provincia_Id) {

        this.dirCalle = dirCalle;
        this.dirNro = dirNro;
        this.dirCruce1 = dirCruce1;
        this.dirCruce2 = dirCruce2;
        this.dirPiso = dirPiso;
        this.dirDpto = dirDpto;
        this.dir_CpShort = dir_Cp;
        this.localidad_Id = localidad_Id;
        this.provincia_IdShort = provincia_Id;
    }

    public Direccion(String dirCalle, int dirNro, String dirCruce1, String dirCruce2, int dirPiso, String dirDpto, Integer dir_Cp, Localidad localidadId, Provincia provinciaId) {
        this.dirCalle = dirCalle;
        this.dirNro = dirNro;
        this.dirCruce1 = dirCruce1;
        this.dirCruce2 = dirCruce2;
        this.dirPiso = dirPiso;
        this.dirDpto = dirDpto;
        this.dir_Cp = dir_Cp;
        this.localidadId = localidadId;
        this.provinciaId = provinciaId;
    }
    
    

    public Integer getDirId() {
        return dirId;
    }

    public void setDirId(Integer dirId) {
        this.dirId = dirId;
    }

    public String getDirCalle() {
        return dirCalle;
    }

    public void setDirCalle(String dirCalle) {
        this.dirCalle = dirCalle;
    }

    public int getDirNro() {
        return dirNro;
    }

    public void setDirNro(int dirNro) {
        this.dirNro = dirNro;
    }

    public String getDirCruce1() {
        return dirCruce1;
    }

    public void setDirCruce1(String dirCruce1) {
        this.dirCruce1 = dirCruce1;
    }

    public String getDirCruce2() {
        return dirCruce2;
    }

    public void setDirCruce2(String dirCruce2) {
        this.dirCruce2 = dirCruce2;
    }

    public int getDirPiso() {
        return dirPiso;
    }

    public void setDirPiso(int dirPiso) {
        this.dirPiso = dirPiso;
    }

    public String getDirDpto() {
        return dirDpto;
    }

    public void setDirDpto(String dirDpto) {
        this.dirDpto = dirDpto;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    @XmlTransient
    public List<Libretacontacto> getLibretacontactoList() {
        return libretacontactoList;
    }

    public void setLibretacontactoList(List<Libretacontacto> libretacontactoList) {
        this.libretacontactoList = libretacontactoList;
    }

    public Localidad getDirCp() {
        return dirCp;
    }

    public void setDirCp(Localidad dirCp) {
        this.dirCp = dirCp;
    }

    public Localidad getLocalidadId() {
        return localidadId;
    }

    public void setLocalidadId(Localidad localidadId) {
        this.localidadId = localidadId;
    }

    public Provincia getProvinciaId() {
        return provinciaId;
    }

    public void setProvinciaId(Provincia provinciaId) {
        this.provinciaId = provinciaId;
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

    public Integer getDir_Cp() {
        return dir_Cp;
    }

    public void setDir_Cp(Integer dir_Cp) {
        this.dir_Cp = dir_Cp;
    }

    public Short getDir_CpShort() {
        return dir_CpShort;
    }

    public void setDir_CpShort(Short dir_CpShort) {
        this.dir_CpShort = dir_CpShort;
    }

    public Integer getLocalidad_Id() {
        return localidad_Id;
    }

    public void setLocalidad_Id(Integer localidad_Id) {
        this.localidad_Id = localidad_Id;
    }

    public Integer getProvincia_Id() {
        return provincia_Id;
    }

    public void setProvincia_Id(Integer provincia_Id) {
        this.provincia_Id = provincia_Id;
    }

    public Short getProvincia_IdShort() {
        return provincia_IdShort;
    }

    public void setProvincia_IdShort(Short provincia_IdShort) {
        this.provincia_IdShort = provincia_IdShort;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dirId != null ? dirId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Direccion)) {
            return false;
        }
        Direccion other = (Direccion) object;
        if ((this.dirId == null && other.dirId != null) || (this.dirId != null && !this.dirId.equals(other.dirId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Direccion[ dirId=" + dirId + " ]";
    }
    
}
