/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "pago")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pago.findAll", query = "SELECT p FROM Pago p")
    , @NamedQuery(name = "Pago.findByPagoId", query = "SELECT p FROM Pago p WHERE p.pagoId = :pagoId")
    , @NamedQuery(name = "Pago.findByPagoFecha", query = "SELECT p FROM Pago p WHERE p.pagoFecha = :pagoFecha")
    , @NamedQuery(name = "Pago.findByPagoNrecibo", query = "SELECT p FROM Pago p WHERE p.pagoNrecibo = :pagoNrecibo")
    , @NamedQuery(name = "Pago.findByPagoMonto", query = "SELECT p FROM Pago p WHERE p.pagoMonto = :pagoMonto")})
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pago_id")
    private Integer pagoId;
    @Basic(optional = false)
    @Column(name = "pago_fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date pagoFecha;
    @Basic(optional = false)
    @Column(name = "pago_nrecibo")
    private int pagoNrecibo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "pago_monto")
    private BigDecimal pagoMonto;
    private Double pago_Monto;
    @JoinColumn(name = "pago_nfactura", referencedColumnName = "comprob_nro")
    @ManyToOne(optional = false)
    private Compra pagoNfactura;
    private Integer pago_Nfactura;
    @JoinColumn(name = "provee_id", referencedColumnName = "prove_id")
    @ManyToOne(optional = false)
    private Proveedor proveeId;
    private Integer provee_Id;

    public Pago() {
    }

    public Pago(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Pago(Integer pagoId, Date pagoFecha, int pagoNrecibo, BigDecimal pagoMonto) {
        this.pagoId = pagoId;
        this.pagoFecha = pagoFecha;
        this.pagoNrecibo = pagoNrecibo;
        this.pagoMonto = pagoMonto;
    }
    
    public Pago(Integer pagoId, Date pagoFecha, int pago_Nfactura, int pagoNrecibo, int provee_id, Double pago_Monto) {
        this.pagoId = pagoId;
        this.pagoFecha = pagoFecha;
        this.pago_Nfactura = pago_Nfactura;
        this.pagoNrecibo = pagoNrecibo;
        this.provee_Id = provee_id;
        this.pago_Monto = pago_Monto;
    }

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public Date getPagoFecha() {
        return pagoFecha;
    }

    public void setPagoFecha(Date pagoFecha) {
        this.pagoFecha = pagoFecha;
    }

    public int getPagoNrecibo() {
        return pagoNrecibo;
    }

    public void setPagoNrecibo(int pagoNrecibo) {
        this.pagoNrecibo = pagoNrecibo;
    }

    public BigDecimal getPagoMonto() {
        return pagoMonto;
    }

    public void setPagoMonto(BigDecimal pagoMonto) {
        this.pagoMonto = pagoMonto;
    }

    public Compra getPagoNfactura() {
        return pagoNfactura;
    }

    public void setPagoNfactura(Compra pagoNfactura) {
        this.pagoNfactura = pagoNfactura;
    }

    public Proveedor getProveeId() {
        return proveeId;
    }

    public void setProveeId(Proveedor proveeId) {
        this.proveeId = proveeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pagoId != null ? pagoId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pago)) {
            return false;
        }
        Pago other = (Pago) object;
        if ((this.pagoId == null && other.pagoId != null) || (this.pagoId != null && !this.pagoId.equals(other.pagoId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.com.gestion.entities.Pago[ pagoId=" + pagoId + " ]";
    }
    
}
