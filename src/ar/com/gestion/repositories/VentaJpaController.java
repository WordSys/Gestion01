/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Cliente;
import ar.com.gestion.entities.Comprobante;
import ar.com.gestion.entities.Condop;
import ar.com.gestion.entities.Empleado;
import ar.com.gestion.entities.Ctacobrar;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Cobranzas;
import ar.com.gestion.entities.Detallevta;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class VentaJpaController implements Serializable {

    Connection conn;
    
    public VentaJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getCtacobrarList() == null) {
            venta.setCtacobrarList(new ArrayList<Ctacobrar>());
        }
        if (venta.getCobranzasList() == null) {
            venta.setCobranzasList(new ArrayList<Cobranzas>());
        }
        if (venta.getDetallevtaList() == null) {
            venta.setDetallevtaList(new ArrayList<Detallevta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliId = venta.getCliId();
            if (cliId != null) {
                cliId = em.getReference(cliId.getClass(), cliId.getCliId());
                venta.setCliId(cliId);
            }
            Comprobante comprobLetra = venta.getComprobLetra();
            if (comprobLetra != null) {
                comprobLetra = em.getReference(comprobLetra.getClass(), comprobLetra.getComprobId());
                venta.setComprobLetra(comprobLetra);
            }
            Comprobante comprobNro = venta.getComprobNro();
            if (comprobNro != null) {
                comprobNro = em.getReference(comprobNro.getClass(), comprobNro.getComprobId());
                venta.setComprobNro(comprobNro);
            }
            Comprobante comprobPrefijo = venta.getComprobPrefijo();
            if (comprobPrefijo != null) {
                comprobPrefijo = em.getReference(comprobPrefijo.getClass(), comprobPrefijo.getComprobId());
                venta.setComprobPrefijo(comprobPrefijo);
            }
            Condop condopId = venta.getCondopId();
            if (condopId != null) {
                condopId = em.getReference(condopId.getClass(), condopId.getCondopId());
                venta.setCondopId(condopId);
            }
            Empleado empId = venta.getEmpId();
            if (empId != null) {
                empId = em.getReference(empId.getClass(), empId.getEmpId());
                venta.setEmpId(empId);
            }
            List<Ctacobrar> attachedCtacobrarList = new ArrayList<Ctacobrar>();
            for (Ctacobrar ctacobrarListCtacobrarToAttach : venta.getCtacobrarList()) {
                ctacobrarListCtacobrarToAttach = em.getReference(ctacobrarListCtacobrarToAttach.getClass(), ctacobrarListCtacobrarToAttach.getCcId());
                attachedCtacobrarList.add(ctacobrarListCtacobrarToAttach);
            }
            venta.setCtacobrarList(attachedCtacobrarList);
            List<Cobranzas> attachedCobranzasList = new ArrayList<Cobranzas>();
            for (Cobranzas cobranzasListCobranzasToAttach : venta.getCobranzasList()) {
                cobranzasListCobranzasToAttach = em.getReference(cobranzasListCobranzasToAttach.getClass(), cobranzasListCobranzasToAttach.getCobId());
                attachedCobranzasList.add(cobranzasListCobranzasToAttach);
            }
            venta.setCobranzasList(attachedCobranzasList);
            List<Detallevta> attachedDetallevtaList = new ArrayList<Detallevta>();
            for (Detallevta detallevtaListDetallevtaToAttach : venta.getDetallevtaList()) {
                detallevtaListDetallevtaToAttach = em.getReference(detallevtaListDetallevtaToAttach.getClass(), detallevtaListDetallevtaToAttach.getDetvtaId());
                attachedDetallevtaList.add(detallevtaListDetallevtaToAttach);
            }
            venta.setDetallevtaList(attachedDetallevtaList);
            em.persist(venta);
            if (cliId != null) {
                cliId.getVentaList().add(venta);
                cliId = em.merge(cliId);
            }
            if (comprobLetra != null) {
                comprobLetra.getVentaList().add(venta);
                comprobLetra = em.merge(comprobLetra);
            }
            if (comprobNro != null) {
                comprobNro.getVentaList().add(venta);
                comprobNro = em.merge(comprobNro);
            }
            if (comprobPrefijo != null) {
                comprobPrefijo.getVentaList().add(venta);
                comprobPrefijo = em.merge(comprobPrefijo);
            }
            if (condopId != null) {
                condopId.getVentaList().add(venta);
                condopId = em.merge(condopId);
            }
            if (empId != null) {
                empId.getVentaList().add(venta);
                empId = em.merge(empId);
            }
            for (Ctacobrar ctacobrarListCtacobrar : venta.getCtacobrarList()) {
                Venta oldVtaIdOfCtacobrarListCtacobrar = ctacobrarListCtacobrar.getVtaId();
                ctacobrarListCtacobrar.setVtaId(venta);
                ctacobrarListCtacobrar = em.merge(ctacobrarListCtacobrar);
                if (oldVtaIdOfCtacobrarListCtacobrar != null) {
                    oldVtaIdOfCtacobrarListCtacobrar.getCtacobrarList().remove(ctacobrarListCtacobrar);
                    oldVtaIdOfCtacobrarListCtacobrar = em.merge(oldVtaIdOfCtacobrarListCtacobrar);
                }
            }
            for (Cobranzas cobranzasListCobranzas : venta.getCobranzasList()) {
                Venta oldCobNfactOfCobranzasListCobranzas = cobranzasListCobranzas.getCobNfact();
                cobranzasListCobranzas.setCobNfact(venta);
                cobranzasListCobranzas = em.merge(cobranzasListCobranzas);
                if (oldCobNfactOfCobranzasListCobranzas != null) {
                    oldCobNfactOfCobranzasListCobranzas.getCobranzasList().remove(cobranzasListCobranzas);
                    oldCobNfactOfCobranzasListCobranzas = em.merge(oldCobNfactOfCobranzasListCobranzas);
                }
            }
            for (Detallevta detallevtaListDetallevta : venta.getDetallevtaList()) {
                Venta oldVtaIdOfDetallevtaListDetallevta = detallevtaListDetallevta.getVtaId();
                detallevtaListDetallevta.setVtaId(venta);
                detallevtaListDetallevta = em.merge(detallevtaListDetallevta);
                if (oldVtaIdOfDetallevtaListDetallevta != null) {
                    oldVtaIdOfDetallevtaListDetallevta.getDetallevtaList().remove(detallevtaListDetallevta);
                    oldVtaIdOfDetallevtaListDetallevta = em.merge(oldVtaIdOfDetallevtaListDetallevta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getVtaId());
            Cliente cliIdOld = persistentVenta.getCliId();
            Cliente cliIdNew = venta.getCliId();
            Comprobante comprobLetraOld = persistentVenta.getComprobLetra();
            Comprobante comprobLetraNew = venta.getComprobLetra();
            Comprobante comprobNroOld = persistentVenta.getComprobNro();
            Comprobante comprobNroNew = venta.getComprobNro();
            Comprobante comprobPrefijoOld = persistentVenta.getComprobPrefijo();
            Comprobante comprobPrefijoNew = venta.getComprobPrefijo();
            Condop condopIdOld = persistentVenta.getCondopId();
            Condop condopIdNew = venta.getCondopId();
            Empleado empIdOld = persistentVenta.getEmpId();
            Empleado empIdNew = venta.getEmpId();
            List<Ctacobrar> ctacobrarListOld = persistentVenta.getCtacobrarList();
            List<Ctacobrar> ctacobrarListNew = venta.getCtacobrarList();
            List<Cobranzas> cobranzasListOld = persistentVenta.getCobranzasList();
            List<Cobranzas> cobranzasListNew = venta.getCobranzasList();
            List<Detallevta> detallevtaListOld = persistentVenta.getDetallevtaList();
            List<Detallevta> detallevtaListNew = venta.getDetallevtaList();
            List<String> illegalOrphanMessages = null;
            for (Ctacobrar ctacobrarListOldCtacobrar : ctacobrarListOld) {
                if (!ctacobrarListNew.contains(ctacobrarListOldCtacobrar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ctacobrar " + ctacobrarListOldCtacobrar + " since its vtaId field is not nullable.");
                }
            }
            for (Cobranzas cobranzasListOldCobranzas : cobranzasListOld) {
                if (!cobranzasListNew.contains(cobranzasListOldCobranzas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cobranzas " + cobranzasListOldCobranzas + " since its cobNfact field is not nullable.");
                }
            }
            for (Detallevta detallevtaListOldDetallevta : detallevtaListOld) {
                if (!detallevtaListNew.contains(detallevtaListOldDetallevta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallevta " + detallevtaListOldDetallevta + " since its vtaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cliIdNew != null) {
                cliIdNew = em.getReference(cliIdNew.getClass(), cliIdNew.getCliId());
                venta.setCliId(cliIdNew);
            }
            if (comprobLetraNew != null) {
                comprobLetraNew = em.getReference(comprobLetraNew.getClass(), comprobLetraNew.getComprobId());
                venta.setComprobLetra(comprobLetraNew);
            }
            if (comprobNroNew != null) {
                comprobNroNew = em.getReference(comprobNroNew.getClass(), comprobNroNew.getComprobId());
                venta.setComprobNro(comprobNroNew);
            }
            if (comprobPrefijoNew != null) {
                comprobPrefijoNew = em.getReference(comprobPrefijoNew.getClass(), comprobPrefijoNew.getComprobId());
                venta.setComprobPrefijo(comprobPrefijoNew);
            }
            if (condopIdNew != null) {
                condopIdNew = em.getReference(condopIdNew.getClass(), condopIdNew.getCondopId());
                venta.setCondopId(condopIdNew);
            }
            if (empIdNew != null) {
                empIdNew = em.getReference(empIdNew.getClass(), empIdNew.getEmpId());
                venta.setEmpId(empIdNew);
            }
            List<Ctacobrar> attachedCtacobrarListNew = new ArrayList<Ctacobrar>();
            for (Ctacobrar ctacobrarListNewCtacobrarToAttach : ctacobrarListNew) {
                ctacobrarListNewCtacobrarToAttach = em.getReference(ctacobrarListNewCtacobrarToAttach.getClass(), ctacobrarListNewCtacobrarToAttach.getCcId());
                attachedCtacobrarListNew.add(ctacobrarListNewCtacobrarToAttach);
            }
            ctacobrarListNew = attachedCtacobrarListNew;
            venta.setCtacobrarList(ctacobrarListNew);
            List<Cobranzas> attachedCobranzasListNew = new ArrayList<Cobranzas>();
            for (Cobranzas cobranzasListNewCobranzasToAttach : cobranzasListNew) {
                cobranzasListNewCobranzasToAttach = em.getReference(cobranzasListNewCobranzasToAttach.getClass(), cobranzasListNewCobranzasToAttach.getCobId());
                attachedCobranzasListNew.add(cobranzasListNewCobranzasToAttach);
            }
            cobranzasListNew = attachedCobranzasListNew;
            venta.setCobranzasList(cobranzasListNew);
            List<Detallevta> attachedDetallevtaListNew = new ArrayList<Detallevta>();
            for (Detallevta detallevtaListNewDetallevtaToAttach : detallevtaListNew) {
                detallevtaListNewDetallevtaToAttach = em.getReference(detallevtaListNewDetallevtaToAttach.getClass(), detallevtaListNewDetallevtaToAttach.getDetvtaId());
                attachedDetallevtaListNew.add(detallevtaListNewDetallevtaToAttach);
            }
            detallevtaListNew = attachedDetallevtaListNew;
            venta.setDetallevtaList(detallevtaListNew);
            venta = em.merge(venta);
            if (cliIdOld != null && !cliIdOld.equals(cliIdNew)) {
                cliIdOld.getVentaList().remove(venta);
                cliIdOld = em.merge(cliIdOld);
            }
            if (cliIdNew != null && !cliIdNew.equals(cliIdOld)) {
                cliIdNew.getVentaList().add(venta);
                cliIdNew = em.merge(cliIdNew);
            }
            if (comprobLetraOld != null && !comprobLetraOld.equals(comprobLetraNew)) {
                comprobLetraOld.getVentaList().remove(venta);
                comprobLetraOld = em.merge(comprobLetraOld);
            }
            if (comprobLetraNew != null && !comprobLetraNew.equals(comprobLetraOld)) {
                comprobLetraNew.getVentaList().add(venta);
                comprobLetraNew = em.merge(comprobLetraNew);
            }
            if (comprobNroOld != null && !comprobNroOld.equals(comprobNroNew)) {
                comprobNroOld.getVentaList().remove(venta);
                comprobNroOld = em.merge(comprobNroOld);
            }
            if (comprobNroNew != null && !comprobNroNew.equals(comprobNroOld)) {
                comprobNroNew.getVentaList().add(venta);
                comprobNroNew = em.merge(comprobNroNew);
            }
            if (comprobPrefijoOld != null && !comprobPrefijoOld.equals(comprobPrefijoNew)) {
                comprobPrefijoOld.getVentaList().remove(venta);
                comprobPrefijoOld = em.merge(comprobPrefijoOld);
            }
            if (comprobPrefijoNew != null && !comprobPrefijoNew.equals(comprobPrefijoOld)) {
                comprobPrefijoNew.getVentaList().add(venta);
                comprobPrefijoNew = em.merge(comprobPrefijoNew);
            }
            if (condopIdOld != null && !condopIdOld.equals(condopIdNew)) {
                condopIdOld.getVentaList().remove(venta);
                condopIdOld = em.merge(condopIdOld);
            }
            if (condopIdNew != null && !condopIdNew.equals(condopIdOld)) {
                condopIdNew.getVentaList().add(venta);
                condopIdNew = em.merge(condopIdNew);
            }
            if (empIdOld != null && !empIdOld.equals(empIdNew)) {
                empIdOld.getVentaList().remove(venta);
                empIdOld = em.merge(empIdOld);
            }
            if (empIdNew != null && !empIdNew.equals(empIdOld)) {
                empIdNew.getVentaList().add(venta);
                empIdNew = em.merge(empIdNew);
            }
            for (Ctacobrar ctacobrarListNewCtacobrar : ctacobrarListNew) {
                if (!ctacobrarListOld.contains(ctacobrarListNewCtacobrar)) {
                    Venta oldVtaIdOfCtacobrarListNewCtacobrar = ctacobrarListNewCtacobrar.getVtaId();
                    ctacobrarListNewCtacobrar.setVtaId(venta);
                    ctacobrarListNewCtacobrar = em.merge(ctacobrarListNewCtacobrar);
                    if (oldVtaIdOfCtacobrarListNewCtacobrar != null && !oldVtaIdOfCtacobrarListNewCtacobrar.equals(venta)) {
                        oldVtaIdOfCtacobrarListNewCtacobrar.getCtacobrarList().remove(ctacobrarListNewCtacobrar);
                        oldVtaIdOfCtacobrarListNewCtacobrar = em.merge(oldVtaIdOfCtacobrarListNewCtacobrar);
                    }
                }
            }
            for (Cobranzas cobranzasListNewCobranzas : cobranzasListNew) {
                if (!cobranzasListOld.contains(cobranzasListNewCobranzas)) {
                    Venta oldCobNfactOfCobranzasListNewCobranzas = cobranzasListNewCobranzas.getCobNfact();
                    cobranzasListNewCobranzas.setCobNfact(venta);
                    cobranzasListNewCobranzas = em.merge(cobranzasListNewCobranzas);
                    if (oldCobNfactOfCobranzasListNewCobranzas != null && !oldCobNfactOfCobranzasListNewCobranzas.equals(venta)) {
                        oldCobNfactOfCobranzasListNewCobranzas.getCobranzasList().remove(cobranzasListNewCobranzas);
                        oldCobNfactOfCobranzasListNewCobranzas = em.merge(oldCobNfactOfCobranzasListNewCobranzas);
                    }
                }
            }
            for (Detallevta detallevtaListNewDetallevta : detallevtaListNew) {
                if (!detallevtaListOld.contains(detallevtaListNewDetallevta)) {
                    Venta oldVtaIdOfDetallevtaListNewDetallevta = detallevtaListNewDetallevta.getVtaId();
                    detallevtaListNewDetallevta.setVtaId(venta);
                    detallevtaListNewDetallevta = em.merge(detallevtaListNewDetallevta);
                    if (oldVtaIdOfDetallevtaListNewDetallevta != null && !oldVtaIdOfDetallevtaListNewDetallevta.equals(venta)) {
                        oldVtaIdOfDetallevtaListNewDetallevta.getDetallevtaList().remove(detallevtaListNewDetallevta);
                        oldVtaIdOfDetallevtaListNewDetallevta = em.merge(oldVtaIdOfDetallevtaListNewDetallevta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venta.getVtaId();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getVtaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ctacobrar> ctacobrarListOrphanCheck = venta.getCtacobrarList();
            for (Ctacobrar ctacobrarListOrphanCheckCtacobrar : ctacobrarListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Ctacobrar " + ctacobrarListOrphanCheckCtacobrar + " in its ctacobrarList field has a non-nullable vtaId field.");
            }
            List<Cobranzas> cobranzasListOrphanCheck = venta.getCobranzasList();
            for (Cobranzas cobranzasListOrphanCheckCobranzas : cobranzasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Cobranzas " + cobranzasListOrphanCheckCobranzas + " in its cobranzasList field has a non-nullable cobNfact field.");
            }
            List<Detallevta> detallevtaListOrphanCheck = venta.getDetallevtaList();
            for (Detallevta detallevtaListOrphanCheckDetallevta : detallevtaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Venta (" + venta + ") cannot be destroyed since the Detallevta " + detallevtaListOrphanCheckDetallevta + " in its detallevtaList field has a non-nullable vtaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente cliId = venta.getCliId();
            if (cliId != null) {
                cliId.getVentaList().remove(venta);
                cliId = em.merge(cliId);
            }
            Comprobante comprobLetra = venta.getComprobLetra();
            if (comprobLetra != null) {
                comprobLetra.getVentaList().remove(venta);
                comprobLetra = em.merge(comprobLetra);
            }
            Comprobante comprobNro = venta.getComprobNro();
            if (comprobNro != null) {
                comprobNro.getVentaList().remove(venta);
                comprobNro = em.merge(comprobNro);
            }
            Comprobante comprobPrefijo = venta.getComprobPrefijo();
            if (comprobPrefijo != null) {
                comprobPrefijo.getVentaList().remove(venta);
                comprobPrefijo = em.merge(comprobPrefijo);
            }
            Condop condopId = venta.getCondopId();
            if (condopId != null) {
                condopId.getVentaList().remove(venta);
                condopId = em.merge(condopId);
            }
            Empleado empId = venta.getEmpId();
            if (empId != null) {
                empId.getVentaList().remove(venta);
                empId = em.merge(empId);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Venta venta){
        
        String query = "insert into venta ( vta_id, vta_fecha, comprob_letra, comprob_prefijo, comprob_nro"
                + "emp_id, cli_id, vta_subt, vta_perc, vta_iva, vta_total)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, venta.getVtaId());
            ps.setDate(1, Date.valueOf(venta.getVtaFecha().toString()));
            ps.setString(2, venta.getComprobLetra().getComprobLetra());
            ps.setInt(3, venta.getComprobPrefijo().getComprobPrefijo());
            ps.setInt(4, venta.getComprobNro().getComprobNro());
            ps.setInt(5, venta.getEmpId().getEmpId());
            ps.setInt(6, venta.getCliId().getCliId());
            ps.setDouble(7, venta.getVtaSubt());
            ps.setDouble(8, venta.getVtaPrec());
            ps.setDouble(9, venta.getVtaIva());
            ps.setDouble(10, venta.getVtaTotal());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) venta.setVtaId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Venta getById(int id){
        List<Venta> lista = getByFiltro("comp_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Venta> getAll(){
        return getByFiltro("1=1");
    }
    public List<Venta> getByFiltro(String filtro){
        List<Venta> lista = new ArrayList();
        String query = "select * from compra where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Venta venta = new Venta(
                        rs.getInt("vta_id"),
                        rs.getDate("vta_fecha"),
                        rs.getString("comprob_letra"),
                        rs.getInt("comprob_prefijo"),
                        rs.getInt("comprob_nro"),
                        rs.getInt("emp_id"),
                        rs.getInt("cli_id"),
                        rs.getInt("condop_id"),
                        rs.getDouble("vta_subt"),
                        rs.getDouble("vta_perc"),
                        rs.getDouble("vta_iva"),
                        rs.getDouble("vta_total")
                ); 
                lista.add(venta);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Venta> getByFecha(Date fecha){
        return getByFiltro("vta_fecha ='"+fecha+"'");
    }
    public List<Venta> getByLetra(String letra){
        return getByFiltro("comprob_letra ='"+letra+"'");
    }
    public List<Venta> getByComprobNro(int comprobNro){
        return getByFiltro("comprob_nro ='"+comprobNro+"'");
    }
    public List<Venta> getByEmpleado(int empleado){
        return getByFiltro("emp_id ='"+empleado+"'");
    }
    public List<Venta> getByCliente(int cliente){
        return getByFiltro("cli_id ='"+cliente+"'");
    }
    public void remove(Venta venta){
        if(venta != null ){
            String query = "delete from venta where vta_id ="+venta.getVtaId();
        try {
            conn.createStatement().execute(query);
            venta = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Venta venta){
        String query = "update venta set vta_fecha = ?, comprob_letra = ?, comprob_prefijo = ?, comprob_nro = ?"
                + "emp_id = ?, cli_id = ?, vta_subt = ?, vta_perc = ?, vta_iva = ?, vta_total = ?"
                + "where vta_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(venta.getVtaFecha().toString()));
            ps.setString(2, venta.getComprobLetra().getComprobLetra());
            ps.setInt(3, venta.getComprobPrefijo().getComprobPrefijo());
            ps.setInt(4, venta.getComprobNro().getComprobNro());
            ps.setInt(5, venta.getEmpId().getEmpId());
            ps.setInt(6, venta.getCliId().getCliId());
            ps.setDouble(7, venta.getVtaSubt());
            ps.setDouble(8, venta.getVtaPrec());
            ps.setDouble(9, venta.getVtaIva());
            ps.setDouble(10, venta.getVtaTotal());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
