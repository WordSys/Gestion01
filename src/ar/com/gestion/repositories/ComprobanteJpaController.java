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
import ar.com.gestion.entities.Compra;
import ar.com.gestion.entities.Comprobante;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class ComprobanteJpaController implements Serializable {
    
    Connection conn;
    
    public ComprobanteJpaController() {
     conn = ConnectorMySql.getConnection();
    }

    public ComprobanteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comprobante comprobante) {
        if (comprobante.getCompraList() == null) {
            comprobante.setCompraList(new ArrayList<Compra>());
        }
        if (comprobante.getCompraList1() == null) {
            comprobante.setCompraList1(new ArrayList<Compra>());
        }
        if (comprobante.getCompraList2() == null) {
            comprobante.setCompraList2(new ArrayList<Compra>());
        }
        if (comprobante.getVentaList() == null) {
            comprobante.setVentaList(new ArrayList<Venta>());
        }
        if (comprobante.getVentaList1() == null) {
            comprobante.setVentaList1(new ArrayList<Venta>());
        }
        if (comprobante.getVentaList2() == null) {
            comprobante.setVentaList2(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : comprobante.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getCompId());
                attachedCompraList.add(compraListCompraToAttach);
            }
            comprobante.setCompraList(attachedCompraList);
            List<Compra> attachedCompraList1 = new ArrayList<Compra>();
            for (Compra compraList1CompraToAttach : comprobante.getCompraList1()) {
                compraList1CompraToAttach = em.getReference(compraList1CompraToAttach.getClass(), compraList1CompraToAttach.getCompId());
                attachedCompraList1.add(compraList1CompraToAttach);
            }
            comprobante.setCompraList1(attachedCompraList1);
            List<Compra> attachedCompraList2 = new ArrayList<Compra>();
            for (Compra compraList2CompraToAttach : comprobante.getCompraList2()) {
                compraList2CompraToAttach = em.getReference(compraList2CompraToAttach.getClass(), compraList2CompraToAttach.getCompId());
                attachedCompraList2.add(compraList2CompraToAttach);
            }
            comprobante.setCompraList2(attachedCompraList2);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : comprobante.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVtaId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            comprobante.setVentaList(attachedVentaList);
            List<Venta> attachedVentaList1 = new ArrayList<Venta>();
            for (Venta ventaList1VentaToAttach : comprobante.getVentaList1()) {
                ventaList1VentaToAttach = em.getReference(ventaList1VentaToAttach.getClass(), ventaList1VentaToAttach.getVtaId());
                attachedVentaList1.add(ventaList1VentaToAttach);
            }
            comprobante.setVentaList1(attachedVentaList1);
            List<Venta> attachedVentaList2 = new ArrayList<Venta>();
            for (Venta ventaList2VentaToAttach : comprobante.getVentaList2()) {
                ventaList2VentaToAttach = em.getReference(ventaList2VentaToAttach.getClass(), ventaList2VentaToAttach.getVtaId());
                attachedVentaList2.add(ventaList2VentaToAttach);
            }
            comprobante.setVentaList2(attachedVentaList2);
            em.persist(comprobante);
            for (Compra compraListCompra : comprobante.getCompraList()) {
                Comprobante oldComprobLetraOfCompraListCompra = compraListCompra.getComprobLetra();
                compraListCompra.setComprobLetra(comprobante);
                compraListCompra = em.merge(compraListCompra);
                if (oldComprobLetraOfCompraListCompra != null) {
                    oldComprobLetraOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldComprobLetraOfCompraListCompra = em.merge(oldComprobLetraOfCompraListCompra);
                }
            }
            for (Compra compraList1Compra : comprobante.getCompraList1()) {
                Comprobante oldComprobNroOfCompraList1Compra = compraList1Compra.getComprobNro();
                compraList1Compra.setComprobNro(comprobante);
                compraList1Compra = em.merge(compraList1Compra);
                if (oldComprobNroOfCompraList1Compra != null) {
                    oldComprobNroOfCompraList1Compra.getCompraList1().remove(compraList1Compra);
                    oldComprobNroOfCompraList1Compra = em.merge(oldComprobNroOfCompraList1Compra);
                }
            }
            for (Compra compraList2Compra : comprobante.getCompraList2()) {
                Comprobante oldComprobPrefijoOfCompraList2Compra = compraList2Compra.getComprobPrefijo();
                compraList2Compra.setComprobPrefijo(comprobante);
                compraList2Compra = em.merge(compraList2Compra);
                if (oldComprobPrefijoOfCompraList2Compra != null) {
                    oldComprobPrefijoOfCompraList2Compra.getCompraList2().remove(compraList2Compra);
                    oldComprobPrefijoOfCompraList2Compra = em.merge(oldComprobPrefijoOfCompraList2Compra);
                }
            }
            for (Venta ventaListVenta : comprobante.getVentaList()) {
                Comprobante oldComprobLetraOfVentaListVenta = ventaListVenta.getComprobLetra();
                ventaListVenta.setComprobLetra(comprobante);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldComprobLetraOfVentaListVenta != null) {
                    oldComprobLetraOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldComprobLetraOfVentaListVenta = em.merge(oldComprobLetraOfVentaListVenta);
                }
            }
            for (Venta ventaList1Venta : comprobante.getVentaList1()) {
                Comprobante oldComprobNroOfVentaList1Venta = ventaList1Venta.getComprobNro();
                ventaList1Venta.setComprobNro(comprobante);
                ventaList1Venta = em.merge(ventaList1Venta);
                if (oldComprobNroOfVentaList1Venta != null) {
                    oldComprobNroOfVentaList1Venta.getVentaList1().remove(ventaList1Venta);
                    oldComprobNroOfVentaList1Venta = em.merge(oldComprobNroOfVentaList1Venta);
                }
            }
            for (Venta ventaList2Venta : comprobante.getVentaList2()) {
                Comprobante oldComprobPrefijoOfVentaList2Venta = ventaList2Venta.getComprobPrefijo();
                ventaList2Venta.setComprobPrefijo(comprobante);
                ventaList2Venta = em.merge(ventaList2Venta);
                if (oldComprobPrefijoOfVentaList2Venta != null) {
                    oldComprobPrefijoOfVentaList2Venta.getVentaList2().remove(ventaList2Venta);
                    oldComprobPrefijoOfVentaList2Venta = em.merge(oldComprobPrefijoOfVentaList2Venta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comprobante comprobante) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprobante persistentComprobante = em.find(Comprobante.class, comprobante.getComprobId());
            List<Compra> compraListOld = persistentComprobante.getCompraList();
            List<Compra> compraListNew = comprobante.getCompraList();
            List<Compra> compraList1Old = persistentComprobante.getCompraList1();
            List<Compra> compraList1New = comprobante.getCompraList1();
            List<Compra> compraList2Old = persistentComprobante.getCompraList2();
            List<Compra> compraList2New = comprobante.getCompraList2();
            List<Venta> ventaListOld = persistentComprobante.getVentaList();
            List<Venta> ventaListNew = comprobante.getVentaList();
            List<Venta> ventaList1Old = persistentComprobante.getVentaList1();
            List<Venta> ventaList1New = comprobante.getVentaList1();
            List<Venta> ventaList2Old = persistentComprobante.getVentaList2();
            List<Venta> ventaList2New = comprobante.getVentaList2();
            List<String> illegalOrphanMessages = null;
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its comprobLetra field is not nullable.");
                }
            }
            for (Compra compraList1OldCompra : compraList1Old) {
                if (!compraList1New.contains(compraList1OldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraList1OldCompra + " since its comprobNro field is not nullable.");
                }
            }
            for (Compra compraList2OldCompra : compraList2Old) {
                if (!compraList2New.contains(compraList2OldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraList2OldCompra + " since its comprobPrefijo field is not nullable.");
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its comprobLetra field is not nullable.");
                }
            }
            for (Venta ventaList1OldVenta : ventaList1Old) {
                if (!ventaList1New.contains(ventaList1OldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaList1OldVenta + " since its comprobNro field is not nullable.");
                }
            }
            for (Venta ventaList2OldVenta : ventaList2Old) {
                if (!ventaList2New.contains(ventaList2OldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaList2OldVenta + " since its comprobPrefijo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getCompId());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            comprobante.setCompraList(compraListNew);
            List<Compra> attachedCompraList1New = new ArrayList<Compra>();
            for (Compra compraList1NewCompraToAttach : compraList1New) {
                compraList1NewCompraToAttach = em.getReference(compraList1NewCompraToAttach.getClass(), compraList1NewCompraToAttach.getCompId());
                attachedCompraList1New.add(compraList1NewCompraToAttach);
            }
            compraList1New = attachedCompraList1New;
            comprobante.setCompraList1(compraList1New);
            List<Compra> attachedCompraList2New = new ArrayList<Compra>();
            for (Compra compraList2NewCompraToAttach : compraList2New) {
                compraList2NewCompraToAttach = em.getReference(compraList2NewCompraToAttach.getClass(), compraList2NewCompraToAttach.getCompId());
                attachedCompraList2New.add(compraList2NewCompraToAttach);
            }
            compraList2New = attachedCompraList2New;
            comprobante.setCompraList2(compraList2New);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVtaId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            comprobante.setVentaList(ventaListNew);
            List<Venta> attachedVentaList1New = new ArrayList<Venta>();
            for (Venta ventaList1NewVentaToAttach : ventaList1New) {
                ventaList1NewVentaToAttach = em.getReference(ventaList1NewVentaToAttach.getClass(), ventaList1NewVentaToAttach.getVtaId());
                attachedVentaList1New.add(ventaList1NewVentaToAttach);
            }
            ventaList1New = attachedVentaList1New;
            comprobante.setVentaList1(ventaList1New);
            List<Venta> attachedVentaList2New = new ArrayList<Venta>();
            for (Venta ventaList2NewVentaToAttach : ventaList2New) {
                ventaList2NewVentaToAttach = em.getReference(ventaList2NewVentaToAttach.getClass(), ventaList2NewVentaToAttach.getVtaId());
                attachedVentaList2New.add(ventaList2NewVentaToAttach);
            }
            ventaList2New = attachedVentaList2New;
            comprobante.setVentaList2(ventaList2New);
            comprobante = em.merge(comprobante);
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Comprobante oldComprobLetraOfCompraListNewCompra = compraListNewCompra.getComprobLetra();
                    compraListNewCompra.setComprobLetra(comprobante);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldComprobLetraOfCompraListNewCompra != null && !oldComprobLetraOfCompraListNewCompra.equals(comprobante)) {
                        oldComprobLetraOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldComprobLetraOfCompraListNewCompra = em.merge(oldComprobLetraOfCompraListNewCompra);
                    }
                }
            }
            for (Compra compraList1NewCompra : compraList1New) {
                if (!compraList1Old.contains(compraList1NewCompra)) {
                    Comprobante oldComprobNroOfCompraList1NewCompra = compraList1NewCompra.getComprobNro();
                    compraList1NewCompra.setComprobNro(comprobante);
                    compraList1NewCompra = em.merge(compraList1NewCompra);
                    if (oldComprobNroOfCompraList1NewCompra != null && !oldComprobNroOfCompraList1NewCompra.equals(comprobante)) {
                        oldComprobNroOfCompraList1NewCompra.getCompraList1().remove(compraList1NewCompra);
                        oldComprobNroOfCompraList1NewCompra = em.merge(oldComprobNroOfCompraList1NewCompra);
                    }
                }
            }
            for (Compra compraList2NewCompra : compraList2New) {
                if (!compraList2Old.contains(compraList2NewCompra)) {
                    Comprobante oldComprobPrefijoOfCompraList2NewCompra = compraList2NewCompra.getComprobPrefijo();
                    compraList2NewCompra.setComprobPrefijo(comprobante);
                    compraList2NewCompra = em.merge(compraList2NewCompra);
                    if (oldComprobPrefijoOfCompraList2NewCompra != null && !oldComprobPrefijoOfCompraList2NewCompra.equals(comprobante)) {
                        oldComprobPrefijoOfCompraList2NewCompra.getCompraList2().remove(compraList2NewCompra);
                        oldComprobPrefijoOfCompraList2NewCompra = em.merge(oldComprobPrefijoOfCompraList2NewCompra);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Comprobante oldComprobLetraOfVentaListNewVenta = ventaListNewVenta.getComprobLetra();
                    ventaListNewVenta.setComprobLetra(comprobante);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldComprobLetraOfVentaListNewVenta != null && !oldComprobLetraOfVentaListNewVenta.equals(comprobante)) {
                        oldComprobLetraOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldComprobLetraOfVentaListNewVenta = em.merge(oldComprobLetraOfVentaListNewVenta);
                    }
                }
            }
            for (Venta ventaList1NewVenta : ventaList1New) {
                if (!ventaList1Old.contains(ventaList1NewVenta)) {
                    Comprobante oldComprobNroOfVentaList1NewVenta = ventaList1NewVenta.getComprobNro();
                    ventaList1NewVenta.setComprobNro(comprobante);
                    ventaList1NewVenta = em.merge(ventaList1NewVenta);
                    if (oldComprobNroOfVentaList1NewVenta != null && !oldComprobNroOfVentaList1NewVenta.equals(comprobante)) {
                        oldComprobNroOfVentaList1NewVenta.getVentaList1().remove(ventaList1NewVenta);
                        oldComprobNroOfVentaList1NewVenta = em.merge(oldComprobNroOfVentaList1NewVenta);
                    }
                }
            }
            for (Venta ventaList2NewVenta : ventaList2New) {
                if (!ventaList2Old.contains(ventaList2NewVenta)) {
                    Comprobante oldComprobPrefijoOfVentaList2NewVenta = ventaList2NewVenta.getComprobPrefijo();
                    ventaList2NewVenta.setComprobPrefijo(comprobante);
                    ventaList2NewVenta = em.merge(ventaList2NewVenta);
                    if (oldComprobPrefijoOfVentaList2NewVenta != null && !oldComprobPrefijoOfVentaList2NewVenta.equals(comprobante)) {
                        oldComprobPrefijoOfVentaList2NewVenta.getVentaList2().remove(ventaList2NewVenta);
                        oldComprobPrefijoOfVentaList2NewVenta = em.merge(oldComprobPrefijoOfVentaList2NewVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comprobante.getComprobId();
                if (findComprobante(id) == null) {
                    throw new NonexistentEntityException("The comprobante with id " + id + " no longer exists.");
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
            Comprobante comprobante;
            try {
                comprobante = em.getReference(Comprobante.class, id);
                comprobante.getComprobId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprobante with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = comprobante.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable comprobLetra field.");
            }
            List<Compra> compraList1OrphanCheck = comprobante.getCompraList1();
            for (Compra compraList1OrphanCheckCompra : compraList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Compra " + compraList1OrphanCheckCompra + " in its compraList1 field has a non-nullable comprobNro field.");
            }
            List<Compra> compraList2OrphanCheck = comprobante.getCompraList2();
            for (Compra compraList2OrphanCheckCompra : compraList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Compra " + compraList2OrphanCheckCompra + " in its compraList2 field has a non-nullable comprobPrefijo field.");
            }
            List<Venta> ventaListOrphanCheck = comprobante.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable comprobLetra field.");
            }
            List<Venta> ventaList1OrphanCheck = comprobante.getVentaList1();
            for (Venta ventaList1OrphanCheckVenta : ventaList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Venta " + ventaList1OrphanCheckVenta + " in its ventaList1 field has a non-nullable comprobNro field.");
            }
            List<Venta> ventaList2OrphanCheck = comprobante.getVentaList2();
            for (Venta ventaList2OrphanCheckVenta : ventaList2OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprobante (" + comprobante + ") cannot be destroyed since the Venta " + ventaList2OrphanCheckVenta + " in its ventaList2 field has a non-nullable comprobPrefijo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comprobante);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comprobante> findComprobanteEntities() {
        return findComprobanteEntities(true, -1, -1);
    }

    public List<Comprobante> findComprobanteEntities(int maxResults, int firstResult) {
        return findComprobanteEntities(false, maxResults, firstResult);
    }

    private List<Comprobante> findComprobanteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comprobante.class));
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

    public Comprobante findComprobante(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comprobante.class, id);
        } finally {
            em.close();
        }
    }

    public int getComprobanteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comprobante> rt = cq.from(Comprobante.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public void save (Comprobante comprobante){
        
        String query = "insert into comprobante ( comprob_id,comprob_desc,comprob_letra,comprob_prefijo,comprob_nro)"
                        + "values(?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, comprobante.getComprobId());
            ps.setString(1, comprobante.getComprobDesc());
            ps.setString(2, comprobante.getComprobLetra());
            ps.setInt(3, comprobante.getComprobPrefijo());
            ps.setInt(4, comprobante.getComprobNro());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) comprobante.setComprobId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Comprobante getById(int id){
        List<Comprobante> lista = getByFiltro("comprob_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Comprobante> getAll(){
        return getByFiltro("1=1");
    }
    public List<Comprobante> getByFiltro(String filtro){
        List<Comprobante> lista = new ArrayList();
        String query = "select * from comprobante where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Comprobante compra = new Comprobante(
                        rs.getInt("comprob_id"),
                        rs.getString("comprob_desc"),
                        rs.getString("comprob_letra"),
                        rs.getInt("comprob_prefijo"),
                        rs.getInt("comprob_nro")
                ); 
                lista.add(compra);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Comprobante> getByLetra(String letra){
        return getByFiltro("comprob_letra ='"+letra+"'");
    }
    public List<Comprobante> getByComprobNro(int comprobNro){
        return getByFiltro("comprob_nro ='"+comprobNro+"'");
    }
    
    
    public void remove(Comprobante comprobante){
        if(comprobante != null ){
            String query = "delete from comprobante where comprob_id ="+comprobante.getComprobId();
        try {
            conn.createStatement().execute(query);
            comprobante = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Comprobante comprobante){
        String query = "update comprobante set comprob_desc = ?,comprob_letra = ?,comprob_prefijo = ?,comprob_nro = ?"
                + "where comprob_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, String.valueOf(comprobante.getComprobDesc()) );
            ps.setString(2, String.valueOf(comprobante.getComprobLetra()) );
            ps.setInt(3, comprobante.getComprobPrefijo());
            ps.setInt(4, comprobante.getComprobNro());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
