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
import ar.com.gestion.entities.Pago;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class PagoJpaController implements Serializable {
    
    Connection conn;
    
    public PagoJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra pagoNfactura = pago.getPagoNfactura();
            if (pagoNfactura != null) {
                pagoNfactura = em.getReference(pagoNfactura.getClass(), pagoNfactura.getCompId());
                pago.setPagoNfactura(pagoNfactura);
            }
            Proveedor proveeId = pago.getProveeId();
            if (proveeId != null) {
                proveeId = em.getReference(proveeId.getClass(), proveeId.getProveId());
                pago.setProveeId(proveeId);
            }
            em.persist(pago);
            if (pagoNfactura != null) {
                pagoNfactura.getPagoList().add(pago);
                pagoNfactura = em.merge(pagoNfactura);
            }
            if (proveeId != null) {
                proveeId.getPagoList().add(pago);
                proveeId = em.merge(proveeId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getPagoId());
            Compra pagoNfacturaOld = persistentPago.getPagoNfactura();
            Compra pagoNfacturaNew = pago.getPagoNfactura();
            Proveedor proveeIdOld = persistentPago.getProveeId();
            Proveedor proveeIdNew = pago.getProveeId();
            if (pagoNfacturaNew != null) {
                pagoNfacturaNew = em.getReference(pagoNfacturaNew.getClass(), pagoNfacturaNew.getCompId());
                pago.setPagoNfactura(pagoNfacturaNew);
            }
            if (proveeIdNew != null) {
                proveeIdNew = em.getReference(proveeIdNew.getClass(), proveeIdNew.getProveId());
                pago.setProveeId(proveeIdNew);
            }
            pago = em.merge(pago);
            if (pagoNfacturaOld != null && !pagoNfacturaOld.equals(pagoNfacturaNew)) {
                pagoNfacturaOld.getPagoList().remove(pago);
                pagoNfacturaOld = em.merge(pagoNfacturaOld);
            }
            if (pagoNfacturaNew != null && !pagoNfacturaNew.equals(pagoNfacturaOld)) {
                pagoNfacturaNew.getPagoList().add(pago);
                pagoNfacturaNew = em.merge(pagoNfacturaNew);
            }
            if (proveeIdOld != null && !proveeIdOld.equals(proveeIdNew)) {
                proveeIdOld.getPagoList().remove(pago);
                proveeIdOld = em.merge(proveeIdOld);
            }
            if (proveeIdNew != null && !proveeIdNew.equals(proveeIdOld)) {
                proveeIdNew.getPagoList().add(pago);
                proveeIdNew = em.merge(proveeIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getPagoId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getPagoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Compra pagoNfactura = pago.getPagoNfactura();
            if (pagoNfactura != null) {
                pagoNfactura.getPagoList().remove(pago);
                pagoNfactura = em.merge(pagoNfactura);
            }
            Proveedor proveeId = pago.getProveeId();
            if (proveeId != null) {
                proveeId.getPagoList().remove(pago);
                proveeId = em.merge(proveeId);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Pago pago){
        
        String query = "insert into pago (pago_id,pago_fecha,pago_nfactura,pago_nrecibo,provee_id,pago_monto)"
                        + "values(?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, pago.getPagoId());
            ps.setDate(1, Date.valueOf(pago.getPagoFecha().toString()));
            ps.setInt(2, pago.getPagoNfactura().getComprobNro().getComprobNro());
            ps.setInt(3, pago.getPagoNrecibo());
            ps.setInt(4, pago.getProveeId().getProveId());
            ps.setDouble(5, Double.parseDouble(pago.getPagoMonto().toString()));
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) pago.setPagoId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Pago getById(int id){
        List<Pago> lista = getByFiltro("pago_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Pago> getAll(){
        return getByFiltro("1=1");
    }
    public List<Pago> getByFiltro(String filtro){
        List<Pago> lista = new ArrayList();
        String query = "select * from pago where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Pago pago = new Pago(
                        rs.getInt("id"),
                        rs.getDate("pago_fecha"),
                        rs.getInt("pago_nfactura"),
                        rs.getInt("pago_nrecibo"),
                        rs.getInt("provee_id"),
                        rs.getDouble("pago_monto")
                ); 
                lista.add(pago);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Pago> getByProveedor(int proveId){
        return getByFiltro("'provee_id ='"+proveId+"'");
    }
    public List<Pago> getByNfactura(int nfactura){
        return getByFiltro("pago_nfactura ='"+nfactura+"'");
    }
    public List<Pago> getByNrecibo(int nRecibo){
        return getByFiltro("pago_nrecibo ='"+nRecibo+"'");
    }
    public void remove(Pago pago){
        if(pago != null ){
            String query = "delete from pago where pago_id ="+pago.getPagoId();
        try {
            conn.createStatement().execute(query);
            pago = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Pago pago){
        String query = "update pago set pago_fecha = ?,pago_nfactura = ?,pago_nrecibo = ?,provee_id = ?,pago_monto = ?"
                + "where pago_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(pago.getPagoFecha().toString()));
            ps.setInt(2, pago.getPagoNfactura().getComprobNro().getComprobNro());
            ps.setInt(3, pago.getPagoNrecibo());
            ps.setInt(4, pago.getProveeId().getProveId());
            ps.setDouble(5, Double.parseDouble(pago.getPagoMonto().toString()));
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
