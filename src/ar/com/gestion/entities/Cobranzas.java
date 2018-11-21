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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "cobranzas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cobranzas.findAll", query = "SELECT c FROM Cobranzas c")
    , @NamedQuery(name = "Cobranzas.findByCobId", query = "SELECT c FROM Cobranzas c WHERE c.cobId = :cobId")
    , @NamedQuery(name = "Cobranzas.findByCobFecha", query = "SELECT c FROM Cobranzas c WHERE c.cobFecha = :cobFecha")
    , @NamedQuery(name = "Cobranzas.findByCobNrecibo", query = "SELECT c FROM Cobranzas c WHERE c.cobNrecibo = :cobNrecibo")
    , @NamedQuery(name = "Cobranzas.findByCobMonto", query = "SELECT c FROM Cobranzas c WHERE c.cobMonto = :cobMonto")})
public class Cobranzas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "cob_id")
    private Integer cobId;
    @Basic(optional = false)
    @Column(name = "cob_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cobFecha;
    @Basic(optional = false)
    @Column(name = "cob_nrecibo")
    private int cobNrecibo;
    @Basic(optional = false)
    @Column(name = "cob_monto")
    private double cobMonto;
    @JoinColumn(name = "cli_id", referencedColumnName = "cli_id")
    @ManyToOne(optional = false)
    private Cliente cliId;
    private Integer cli_Id;
    @JoinColumn(name = "cob_nfact", referencedColumnName = "comprob_nro")
    @ManyToOne(optional = false)
    private Venta cobNfact;
    private Integer cob_Nfact;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cobNrecibo")
    private List<Cheques> chequesList;

    public Cobranzas() {
    }

    public Cobranzas(Integer cobId) {
        this.cobId = cobId;
    }

    public Cobranzas(Integer cobId, Date cobFecha, int cobNrecibo, double cobMonto) {
        this.cobId = cobId;
        this.cobFecha = cobFecha;
        this.cobNrecibo = cobNrecibo;
        this.cobMonto = cobMonto;
    }

    public Cobranzas(Integer cobId, Date cobFecha, int cobNrecibo,int cob_Nfact, int cli_Id, double cobMonto) {
        this.cobId = cobId;
        this.cobFecha = cobFecha;
        this.cobNrecibo = cobNrecibo;
        this.cob_Nfact = cob_Nfact;
        this.cli_Id = cli_Id;
        this.cobMonto = cobMonto;
    }

    public Integer getCobId() {
        return cobId;
    }

    public void setCobId(Integer cobId) {
        this.cobId = cobId;
    }

    public Date getCobFecha() {
        return cobFecha;
    }

    public void setCobFecha(Date cobFecha) {
        this.cobFecha = cobFecha;
    }

    public int getCobNrecibo() {
        return cobNrecibo;
    }

    public void setCobNrecibo(int cobNrecibo) {
        this.cobNrecibo = cobNrecibo;
    }

    public double getCobMonto() {
        return cobMonto;
    }

    public void setCobMonto(double cobMonto) {
        this.cobMonto = cobMonto;
    }

    public Cliente getCliId() {
        return cliId;
    }

    public void setCliId(Cliente cliId) {
        this.cliId = cliId;
    }

    public Venta getCobNfact() {
        return cobNfact;
    }

    public void setCobNfact(Venta cobNfact) {
        this.cobNfact = cobNfact;
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
        hash += (cobId != null ? cobId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cobranzas)) {
            return false;
        }
        Cobranzas other = (Cobranzas) object;
        if ((this.cobId == null && other.cobId != null) || (this.cobId != null && !this.cobId.equals(other.cobId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Cobranzas[ cobId=" + cobId + " ]";
    }
    
}
