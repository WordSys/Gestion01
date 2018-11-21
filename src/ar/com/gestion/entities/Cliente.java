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
@Table(name = "cliente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")
    , @NamedQuery(name = "Cliente.findByCliId", query = "SELECT c FROM Cliente c WHERE c.cliId = :cliId")
    , @NamedQuery(name = "Cliente.findByCliRazon", query = "SELECT c FROM Cliente c WHERE c.cliRazon = :cliRazon")
    , @NamedQuery(name = "Cliente.findByCliNdoc", query = "SELECT c FROM Cliente c WHERE c.cliNdoc = :cliNdoc")
    , @NamedQuery(name = "Cliente.findByCliFalta", query = "SELECT c FROM Cliente c WHERE c.cliFalta = :cliFalta")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cli_id")
    private Integer cliId;
    @Basic(optional = false)
    @Column(name = "cli_razon")
    private String cliRazon;
    @Basic(optional = false)
    @Column(name = "cli_ndoc")
    private int cliNdoc;
    @Basic(optional = false)
    @Column(name = "cli_falta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cliFalta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliId")
    private List<Ctacobrar> ctacobrarList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliId")
    private List<Venta> ventaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliId")
    private List<Cobranzas> cobranzasList;
    @JoinColumn(name = "condtrib_id", referencedColumnName = "condtrib_id")
    @ManyToOne(optional = false)
    private Condtrib condtribId;
    @JoinColumn(name = "dir_id", referencedColumnName = "dir_id")
    @ManyToOne(optional = false)
    private Direccion dirId;
    @JoinColumn(name = "tdoc_id", referencedColumnName = "tdoc_id")
    @ManyToOne(optional = false)
    private Tdoc tdocId;

    public Cliente() {
    }

    public Cliente(Integer cliId) {
        this.cliId = cliId;
    }

    public Cliente(Integer cliId, String cliRazon, int cliNdoc, Date cliFalta) {
        this.cliId = cliId;
        this.cliRazon = cliRazon;
        this.cliNdoc = cliNdoc;
        this.cliFalta = cliFalta;
    }

    public Integer getCliId() {
        return cliId;
    }

    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }

    public String getCliRazon() {
        return cliRazon;
    }

    public void setCliRazon(String cliRazon) {
        this.cliRazon = cliRazon;
    }

    public int getCliNdoc() {
        return cliNdoc;
    }

    public void setCliNdoc(int cliNdoc) {
        this.cliNdoc = cliNdoc;
    }

    public Date getCliFalta() {
        return cliFalta;
    }

    public void setCliFalta(Date cliFalta) {
        this.cliFalta = cliFalta;
    }

    @XmlTransient
    public List<Ctacobrar> getCtacobrarList() {
        return ctacobrarList;
    }

    public void setCtacobrarList(List<Ctacobrar> ctacobrarList) {
        this.ctacobrarList = ctacobrarList;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    @XmlTransient
    public List<Cobranzas> getCobranzasList() {
        return cobranzasList;
    }

    public void setCobranzasList(List<Cobranzas> cobranzasList) {
        this.cobranzasList = cobranzasList;
    }

    public Condtrib getCondtribId() {
        return condtribId;
    }

    public void setCondtribId(Condtrib condtribId) {
        this.condtribId = condtribId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cliId != null ? cliId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.cliId == null && other.cliId != null) || (this.cliId != null && !this.cliId.equals(other.cliId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Cliente[ cliId=" + cliId + " ]";
    }
    
}
