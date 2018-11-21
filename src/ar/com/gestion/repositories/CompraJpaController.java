/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Compra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Comprobante;
import ar.com.gestion.entities.Empleado;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.entities.Detallecomp;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Pago;
import ar.com.gestion.entities.Ctapagar;
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
public class CompraJpaController implements Serializable {
    
    Connection conn;
    
    public CompraJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) {
        if (compra.getDetallecompList() == null) {
            compra.setDetallecompList(new ArrayList<Detallecomp>());
        }
        if (compra.getPagoList() == null) {
            compra.setPagoList(new ArrayList<Pago>());
        }
        if (compra.getCtapagarList() == null) {
            compra.setCtapagarList(new ArrayList<Ctapagar>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprobante comprobLetra = compra.getComprobLetra();
            if (comprobLetra != null) {
                comprobLetra = em.getReference(comprobLetra.getClass(), comprobLetra.getComprobId());
                compra.setComprobLetra(comprobLetra);
            }
            Comprobante comprobNro = compra.getComprobNro();
            if (comprobNro != null) {
                comprobNro = em.getReference(comprobNro.getClass(), comprobNro.getComprobId());
                compra.setComprobNro(comprobNro);
            }
            Comprobante comprobPrefijo = compra.getComprobPrefijo();
            if (comprobPrefijo != null) {
                comprobPrefijo = em.getReference(comprobPrefijo.getClass(), comprobPrefijo.getComprobId());
                compra.setComprobPrefijo(comprobPrefijo);
            }
            Empleado empId = compra.getEmpId();
            if (empId != null) {
                empId = em.getReference(empId.getClass(), empId.getEmpId());
                compra.setEmpId(empId);
            }
            Proveedor proveeId = compra.getProveeId();
            if (proveeId != null) {
                proveeId = em.getReference(proveeId.getClass(), proveeId.getProveId());
                compra.setProveeId(proveeId);
            }
            List<Detallecomp> attachedDetallecompList = new ArrayList<Detallecomp>();
            for (Detallecomp detallecompListDetallecompToAttach : compra.getDetallecompList()) {
                detallecompListDetallecompToAttach = em.getReference(detallecompListDetallecompToAttach.getClass(), detallecompListDetallecompToAttach.getDetcompId());
                attachedDetallecompList.add(detallecompListDetallecompToAttach);
            }
            compra.setDetallecompList(attachedDetallecompList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : compra.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getPagoId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            compra.setPagoList(attachedPagoList);
            List<Ctapagar> attachedCtapagarList = new ArrayList<Ctapagar>();
            for (Ctapagar ctapagarListCtapagarToAttach : compra.getCtapagarList()) {
                ctapagarListCtapagarToAttach = em.getReference(ctapagarListCtapagarToAttach.getClass(), ctapagarListCtapagarToAttach.getCpId());
                attachedCtapagarList.add(ctapagarListCtapagarToAttach);
            }
            compra.setCtapagarList(attachedCtapagarList);
            em.persist(compra);
            if (comprobLetra != null) {
                comprobLetra.getCompraList().add(compra);
                comprobLetra = em.merge(comprobLetra);
            }
            if (comprobNro != null) {
                comprobNro.getCompraList().add(compra);
                comprobNro = em.merge(comprobNro);
            }
            if (comprobPrefijo != null) {
                comprobPrefijo.getCompraList().add(compra);
                comprobPrefijo = em.merge(comprobPrefijo);
            }
            if (empId != null) {
                empId.getCompraList().add(compra);
                empId = em.merge(empId);
            }
            if (proveeId != null) {
                proveeId.getCompraList().add(compra);
                proveeId = em.merge(proveeId);
            }
            for (Detallecomp detallecompListDetallecomp : compra.getDetallecompList()) {
                Compra oldCompIdOfDetallecompListDetallecomp = detallecompListDetallecomp.getCompId();
                detallecompListDetallecomp.setCompId(compra);
                detallecompListDetallecomp = em.merge(detallecompListDetallecomp);
                if (oldCompIdOfDetallecompListDetallecomp != null) {
                    oldCompIdOfDetallecompListDetallecomp.getDetallecompList().remove(detallecompListDetallecomp);
                    oldCompIdOfDetallecompListDetallecomp = em.merge(oldCompIdOfDetallecompListDetallecomp);
                }
            }
            for (Pago pagoListPago : compra.getPagoList()) {
                Compra oldPagoNfacturaOfPagoListPago = pagoListPago.getPagoNfactura();
                pagoListPago.setPagoNfactura(compra);
                pagoListPago = em.merge(pagoListPago);
                if (oldPagoNfacturaOfPagoListPago != null) {
                    oldPagoNfacturaOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldPagoNfacturaOfPagoListPago = em.merge(oldPagoNfacturaOfPagoListPago);
                }
            }
            for (Ctapagar ctapagarListCtapagar : compra.getCtapagarList()) {
                Compra oldCompIdOfCtapagarListCtapagar = ctapagarListCtapagar.getCompId();
                ctapagarListCtapagar.setCompId(compra);
                ctapagarListCtapagar = em.merge(ctapagarListCtapagar);
                if (oldCompIdOfCtapagarListCtapagar != null) {
                    oldCompIdOfCtapagarListCtapagar.getCtapagarList().remove(ctapagarListCtapagar);
                    oldCompIdOfCtapagarListCtapagar = em.merge(oldCompIdOfCtapagarListCtapagar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getCompId());
            Comprobante comprobLetraOld = persistentCompra.getComprobLetra();
            Comprobante comprobLetraNew = compra.getComprobLetra();
            Comprobante comprobNroOld = persistentCompra.getComprobNro();
            Comprobante comprobNroNew = compra.getComprobNro();
            Comprobante comprobPrefijoOld = persistentCompra.getComprobPrefijo();
            Comprobante comprobPrefijoNew = compra.getComprobPrefijo();
            Empleado empIdOld = persistentCompra.getEmpId();
            Empleado empIdNew = compra.getEmpId();
            Proveedor proveeIdOld = persistentCompra.getProveeId();
            Proveedor proveeIdNew = compra.getProveeId();
            List<Detallecomp> detallecompListOld = persistentCompra.getDetallecompList();
            List<Detallecomp> detallecompListNew = compra.getDetallecompList();
            List<Pago> pagoListOld = persistentCompra.getPagoList();
            List<Pago> pagoListNew = compra.getPagoList();
            List<Ctapagar> ctapagarListOld = persistentCompra.getCtapagarList();
            List<Ctapagar> ctapagarListNew = compra.getCtapagarList();
            List<String> illegalOrphanMessages = null;
            for (Detallecomp detallecompListOldDetallecomp : detallecompListOld) {
                if (!detallecompListNew.contains(detallecompListOldDetallecomp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecomp " + detallecompListOldDetallecomp + " since its compId field is not nullable.");
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoListOldPago + " since its pagoNfactura field is not nullable.");
                }
            }
            for (Ctapagar ctapagarListOldCtapagar : ctapagarListOld) {
                if (!ctapagarListNew.contains(ctapagarListOldCtapagar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ctapagar " + ctapagarListOldCtapagar + " since its compId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (comprobLetraNew != null) {
                comprobLetraNew = em.getReference(comprobLetraNew.getClass(), comprobLetraNew.getComprobId());
                compra.setComprobLetra(comprobLetraNew);
            }
            if (comprobNroNew != null) {
                comprobNroNew = em.getReference(comprobNroNew.getClass(), comprobNroNew.getComprobId());
                compra.setComprobNro(comprobNroNew);
            }
            if (comprobPrefijoNew != null) {
                comprobPrefijoNew = em.getReference(comprobPrefijoNew.getClass(), comprobPrefijoNew.getComprobId());
                compra.setComprobPrefijo(comprobPrefijoNew);
            }
            if (empIdNew != null) {
                empIdNew = em.getReference(empIdNew.getClass(), empIdNew.getEmpId());
                compra.setEmpId(empIdNew);
            }
            if (proveeIdNew != null) {
                proveeIdNew = em.getReference(proveeIdNew.getClass(), proveeIdNew.getProveId());
                compra.setProveeId(proveeIdNew);
            }
            List<Detallecomp> attachedDetallecompListNew = new ArrayList<Detallecomp>();
            for (Detallecomp detallecompListNewDetallecompToAttach : detallecompListNew) {
                detallecompListNewDetallecompToAttach = em.getReference(detallecompListNewDetallecompToAttach.getClass(), detallecompListNewDetallecompToAttach.getDetcompId());
                attachedDetallecompListNew.add(detallecompListNewDetallecompToAttach);
            }
            detallecompListNew = attachedDetallecompListNew;
            compra.setDetallecompList(detallecompListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getPagoId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            compra.setPagoList(pagoListNew);
            List<Ctapagar> attachedCtapagarListNew = new ArrayList<Ctapagar>();
            for (Ctapagar ctapagarListNewCtapagarToAttach : ctapagarListNew) {
                ctapagarListNewCtapagarToAttach = em.getReference(ctapagarListNewCtapagarToAttach.getClass(), ctapagarListNewCtapagarToAttach.getCpId());
                attachedCtapagarListNew.add(ctapagarListNewCtapagarToAttach);
            }
            ctapagarListNew = attachedCtapagarListNew;
            compra.setCtapagarList(ctapagarListNew);
            compra = em.merge(compra);
            if (comprobLetraOld != null && !comprobLetraOld.equals(comprobLetraNew)) {
                comprobLetraOld.getCompraList().remove(compra);
                comprobLetraOld = em.merge(comprobLetraOld);
            }
            if (comprobLetraNew != null && !comprobLetraNew.equals(comprobLetraOld)) {
                comprobLetraNew.getCompraList().add(compra);
                comprobLetraNew = em.merge(comprobLetraNew);
            }
            if (comprobNroOld != null && !comprobNroOld.equals(comprobNroNew)) {
                comprobNroOld.getCompraList().remove(compra);
                comprobNroOld = em.merge(comprobNroOld);
            }
            if (comprobNroNew != null && !comprobNroNew.equals(comprobNroOld)) {
                comprobNroNew.getCompraList().add(compra);
                comprobNroNew = em.merge(comprobNroNew);
            }
            if (comprobPrefijoOld != null && !comprobPrefijoOld.equals(comprobPrefijoNew)) {
                comprobPrefijoOld.getCompraList().remove(compra);
                comprobPrefijoOld = em.merge(comprobPrefijoOld);
            }
            if (comprobPrefijoNew != null && !comprobPrefijoNew.equals(comprobPrefijoOld)) {
                comprobPrefijoNew.getCompraList().add(compra);
                comprobPrefijoNew = em.merge(comprobPrefijoNew);
            }
            if (empIdOld != null && !empIdOld.equals(empIdNew)) {
                empIdOld.getCompraList().remove(compra);
                empIdOld = em.merge(empIdOld);
            }
            if (empIdNew != null && !empIdNew.equals(empIdOld)) {
                empIdNew.getCompraList().add(compra);
                empIdNew = em.merge(empIdNew);
            }
            if (proveeIdOld != null && !proveeIdOld.equals(proveeIdNew)) {
                proveeIdOld.getCompraList().remove(compra);
                proveeIdOld = em.merge(proveeIdOld);
            }
            if (proveeIdNew != null && !proveeIdNew.equals(proveeIdOld)) {
                proveeIdNew.getCompraList().add(compra);
                proveeIdNew = em.merge(proveeIdNew);
            }
            for (Detallecomp detallecompListNewDetallecomp : detallecompListNew) {
                if (!detallecompListOld.contains(detallecompListNewDetallecomp)) {
                    Compra oldCompIdOfDetallecompListNewDetallecomp = detallecompListNewDetallecomp.getCompId();
                    detallecompListNewDetallecomp.setCompId(compra);
                    detallecompListNewDetallecomp = em.merge(detallecompListNewDetallecomp);
                    if (oldCompIdOfDetallecompListNewDetallecomp != null && !oldCompIdOfDetallecompListNewDetallecomp.equals(compra)) {
                        oldCompIdOfDetallecompListNewDetallecomp.getDetallecompList().remove(detallecompListNewDetallecomp);
                        oldCompIdOfDetallecompListNewDetallecomp = em.merge(oldCompIdOfDetallecompListNewDetallecomp);
                    }
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Compra oldPagoNfacturaOfPagoListNewPago = pagoListNewPago.getPagoNfactura();
                    pagoListNewPago.setPagoNfactura(compra);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldPagoNfacturaOfPagoListNewPago != null && !oldPagoNfacturaOfPagoListNewPago.equals(compra)) {
                        oldPagoNfacturaOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldPagoNfacturaOfPagoListNewPago = em.merge(oldPagoNfacturaOfPagoListNewPago);
                    }
                }
            }
            for (Ctapagar ctapagarListNewCtapagar : ctapagarListNew) {
                if (!ctapagarListOld.contains(ctapagarListNewCtapagar)) {
                    Compra oldCompIdOfCtapagarListNewCtapagar = ctapagarListNewCtapagar.getCompId();
                    ctapagarListNewCtapagar.setCompId(compra);
                    ctapagarListNewCtapagar = em.merge(ctapagarListNewCtapagar);
                    if (oldCompIdOfCtapagarListNewCtapagar != null && !oldCompIdOfCtapagarListNewCtapagar.equals(compra)) {
                        oldCompIdOfCtapagarListNewCtapagar.getCtapagarList().remove(ctapagarListNewCtapagar);
                        oldCompIdOfCtapagarListNewCtapagar = em.merge(oldCompIdOfCtapagarListNewCtapagar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compra.getCompId();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
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
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getCompId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallecomp> detallecompListOrphanCheck = compra.getDetallecompList();
            for (Detallecomp detallecompListOrphanCheckDetallecomp : detallecompListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compra (" + compra + ") cannot be destroyed since the Detallecomp " + detallecompListOrphanCheckDetallecomp + " in its detallecompList field has a non-nullable compId field.");
            }
            List<Pago> pagoListOrphanCheck = compra.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compra (" + compra + ") cannot be destroyed since the Pago " + pagoListOrphanCheckPago + " in its pagoList field has a non-nullable pagoNfactura field.");
            }
            List<Ctapagar> ctapagarListOrphanCheck = compra.getCtapagarList();
            for (Ctapagar ctapagarListOrphanCheckCtapagar : ctapagarListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compra (" + compra + ") cannot be destroyed since the Ctapagar " + ctapagarListOrphanCheckCtapagar + " in its ctapagarList field has a non-nullable compId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Comprobante comprobLetra = compra.getComprobLetra();
            if (comprobLetra != null) {
                comprobLetra.getCompraList().remove(compra);
                comprobLetra = em.merge(comprobLetra);
            }
            Comprobante comprobNro = compra.getComprobNro();
            if (comprobNro != null) {
                comprobNro.getCompraList().remove(compra);
                comprobNro = em.merge(comprobNro);
            }
            Comprobante comprobPrefijo = compra.getComprobPrefijo();
            if (comprobPrefijo != null) {
                comprobPrefijo.getCompraList().remove(compra);
                comprobPrefijo = em.merge(comprobPrefijo);
            }
            Empleado empId = compra.getEmpId();
            if (empId != null) {
                empId.getCompraList().remove(compra);
                empId = em.merge(empId);
            }
            Proveedor proveeId = compra.getProveeId();
            if (proveeId != null) {
                proveeId.getCompraList().remove(compra);
                proveeId = em.merge(proveeId);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Compra compra){
        
        String query = "insert into compra ( comp_id, comp_fecha, comprob_letra, comprob_prefijo, comprob_nro"
                + "emp_id, provee_id, comp_subt, comp_perc, comp_iva, comp_total)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, compra.getCompId());
            ps.setDate(1, Date.valueOf(compra.getCompFecha().toString()));
            ps.setString(2, String.valueOf(compra.getComprobLetra()) );
            ps.setInt(3, Integer.valueOf(compra.getComprobPrefijo().toString()));
            ps.setInt(4, Integer.valueOf(compra.getComprobNro().toString()));
            ps.setInt(5, Integer.valueOf(compra.getEmpId().toString()));
            ps.setInt(6, Integer.valueOf(compra.getProveeId().toString()));
            ps.setDouble(7, compra.getCompSubt());
            ps.setDouble(8, compra.getCompPerc());
            ps.setDouble(9, compra.getCompIva());
            ps.setDouble(10, compra.getCompTotal());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) compra.setCompId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Compra getById(int id){
        List<Compra> lista = getByFiltro("comp_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Compra> getAll(){
        return getByFiltro("1=1");
    }
    public List<Compra> getByFiltro(String filtro){
        List<Compra> lista = new ArrayList();
        String query = "select * from compra where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Compra compra = new Compra(
                        rs.getInt("comp_id"),
                        rs.getDate("comp_fecha"),
                        rs.getString("comprob_letra"),
                        rs.getInt("comprob_prefijo"),
                        rs.getInt("comprob_nro"),
                        rs.getInt("emp_id"),
                        rs.getInt("provee_id"),
                        rs.getDouble("comp_subt"),
                        rs.getDouble("comp_perc"),
                        rs.getDouble("comp_iva"),
                        rs.getDouble("comp_total")
                ); 
                lista.add(compra);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Compra> getByFecha(Date fecha){
        return getByFiltro("comp_fecha ='"+fecha+"'");
    }
    public List<Compra> getByLetra(String letra){
        return getByFiltro("comprob_letra ='"+letra+"'");
    }
    public List<Compra> getByComprobNro(int comprobNro){
        return getByFiltro("comprob_nro ='"+comprobNro+"'");
    }
    public List<Compra> getByEmpleado(int empleado){
        return getByFiltro("emp_id ='"+empleado+"'");
    }
    public List<Compra> getByProveedor(int proveedor){
        return getByFiltro("provee_id ='"+proveedor+"'");
    }
    public void remove(Compra compra){
        if(compra != null ){
            String query = "delete from compra where comp_id ="+compra.getCompId();
        try {
            conn.createStatement().execute(query);
            compra = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Compra compra){
        String query = "update compra set comp_fecha = ?, comprob_letra = ?, comprob_prefijo = ?, comprob_nro = ?"
                + "emp_id = ?, provee_id = ?, comp_subt = ?, comp_perc = ?, comp_iva = ?, comp_total = ?"
                + "where comp_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(compra.getCompFecha().toString()));
            ps.setString(2, String.valueOf(compra.getComprobLetra()) );
            ps.setInt(3, Integer.valueOf(compra.getComprobPrefijo().toString()));
            ps.setInt(4, Integer.valueOf(compra.getComprobNro().toString()));
            ps.setInt(5, Integer.valueOf(compra.getEmpId().toString()));
            ps.setInt(6, Integer.valueOf(compra.getProveeId().toString()));
            ps.setDouble(7, compra.getCompSubt());
            ps.setDouble(8, compra.getCompPerc());
            ps.setDouble(9, compra.getCompIva());
            ps.setDouble(10, compra.getCompTotal());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
