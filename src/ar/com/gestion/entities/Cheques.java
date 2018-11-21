/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author walter
 */
@Entity
@Table(name = "cheques")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cheques.findAll", query = "SELECT c FROM Cheques c")
    , @NamedQuery(name = "Cheques.findByChId", query = "SELECT c FROM Cheques c WHERE c.chId = :chId")
    , @NamedQuery(name = "Cheques.findByChFecha", query = "SELECT c FROM Cheques c WHERE c.chFecha = :chFecha")
    , @NamedQuery(name = "Cheques.findByChFechavto", query = "SELECT c FROM Cheques c WHERE c.chFechavto = :chFechavto")
    , @NamedQuery(name = "Cheques.findByChSuc", query = "SELECT c FROM Cheques c WHERE c.chSuc = :chSuc")
    , @NamedQuery(name = "Cheques.findByChNro", query = "SELECT c FROM Cheques c WHERE c.chNro = :chNro")
    , @NamedQuery(name = "Cheques.findByTdocId", query = "SELECT c FROM Cheques c WHERE c.tdocId = :tdocId")
    , @NamedQuery(name = "Cheques.findByChNdoc", query = "SELECT c FROM Cheques c WHERE c.chNdoc = :chNdoc")
    , @NamedQuery(name = "Cheques.findByCliId", query = "SELECT c FROM Cheques c WHERE c.cliId = :cliId")
    , @NamedQuery(name = "Cheques.findByChMonto", query = "SELECT c FROM Cheques c WHERE c.chMonto = :chMonto")})
public class Cheques implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ch_id")
    private Integer chId;
    @Basic(optional = false)
    @Column(name = "ch_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chFecha;
    @Basic(optional = false)
    @Column(name = "ch_fechavto")
    @Temporal(TemporalType.TIMESTAMP)
    private Date chFechavto;
    @Basic(optional = false)
    @Column(name = "ch_suc")
    private int chSuc;
    @Basic(optional = false)
    @Column(name = "ch_nro")
    private int chNro;
    @Basic(optional = false)
    @Column(name = "tdoc_id")
    private int tdocId;
    @Basic(optional = false)
    @Column(name = "ch_ndoc")
    private int chNdoc;
    @Basic(optional = false)
    @Column(name = "cli_id")
    private int cliId;
    @Basic(optional = false)
    @Column(name = "ch_monto")
    private double chMonto;
    @JoinColumn(name = "cob_nrecibo", referencedColumnName = "cob_id")
    @ManyToOne(optional = false)
    private Cobranzas cobNrecibo;
    private Integer cobNrec;
    @JoinColumn(name = "enti_id", referencedColumnName = "entf_id")
    @ManyToOne(optional = false)
    private EntiFinanc entiId;
    private Integer enfiId;

    public Cheques() {
    }

    public Cheques(Integer chId) {
        this.chId = chId;
    }

    public Cheques(Integer chId, Date chFecha, Date chFechavto, int chSuc, int chNro, int tdocId, int chNdoc, int cliId, double chMonto) {
        this.chId = chId;
        this.chFecha = chFecha;
        this.chFechavto = chFechavto;
        this.chSuc = chSuc;
        this.chNro = chNro;
        this.tdocId = tdocId;
        this.chNdoc = chNdoc;
        this.cliId = cliId;
        this.chMonto = chMonto;
    }

    public Cheques(Integer chId, Date chFecha, Date chFechavto, int chSuc,int entiId, int chNro, int tdocId, int chNdoc, int cliId, double chMonto, int cob_nrecibo) {
        this.chId = chId;
        this.chFecha = chFecha;
        this.chFechavto = chFechavto;
        this.chSuc = chSuc;
        this.enfiId = entiId;
        this.chNro = chNro;
        this.tdocId = tdocId;
        this.chNdoc = chNdoc;
        this.cliId = cliId;
        this.chMonto = chMonto;
        this.cobNrec = cob_nrecibo;
    }

    public Integer getChId() {
        return chId;
    }

    public void setChId(Integer chId) {
        this.chId = chId;
    }

    public Date getChFecha() {
        return chFecha;
    }

    public void setChFecha(Date chFecha) {
        this.chFecha = chFecha;
    }

    public Date getChFechavto() {
        return chFechavto;
    }

    public void setChFechavto(Date chFechavto) {
        this.chFechavto = chFechavto;
    }

    public int getChSuc() {
        return chSuc;
    }

    public void setChSuc(int chSuc) {
        this.chSuc = chSuc;
    }

    public int getChNro() {
        return chNro;
    }

    public void setChNro(int chNro) {
        this.chNro = chNro;
    }

    public int getTdocId() {
        return tdocId;
    }

    public void setTdocId(int tdocId) {
        this.tdocId = tdocId;
    }

    public int getChNdoc() {
        return chNdoc;
    }

    public void setChNdoc(int chNdoc) {
        this.chNdoc = chNdoc;
    }

    public int getCliId() {
        return cliId;
    }

    public void setCliId(int cliId) {
        this.cliId = cliId;
    }

    public double getChMonto() {
        return chMonto;
    }

    public void setChMonto(double chMonto) {
        this.chMonto = chMonto;
    }

    public Cobranzas getCobNrecibo() {
        return cobNrecibo;
    }

    public void setCobNrecibo(Cobranzas cobNrecibo) {
        this.cobNrecibo = cobNrecibo;
    }

    public EntiFinanc getEntiId() {
        return entiId;
    }

    public void setEntiId(EntiFinanc entiId) {
        this.entiId = entiId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (chId != null ? chId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cheques)) {
            return false;
        }
        Cheques other = (Cheques) object;
        if ((this.chId == null && other.chId != null) || (this.chId != null && !this.chId.equals(other.chId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Cheques[ chId=" + chId + " ]";
    }
    
}
