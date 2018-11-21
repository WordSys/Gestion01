/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Comprobante;
import ar.com.gestion.entities.Condop;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
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
public class CondopJpaController implements Serializable {

    Connection conn;
    
    public CondopJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public CondopJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Condop condop) {
        if (condop.getVentaList() == null) {
            condop.setVentaList(new ArrayList<Venta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : condop.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVtaId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            condop.setVentaList(attachedVentaList);
            em.persist(condop);
            for (Venta ventaListVenta : condop.getVentaList()) {
                Condop oldCondopIdOfVentaListVenta = ventaListVenta.getCondopId();
                ventaListVenta.setCondopId(condop);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldCondopIdOfVentaListVenta != null) {
                    oldCondopIdOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldCondopIdOfVentaListVenta = em.merge(oldCondopIdOfVentaListVenta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Condop condop) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Condop persistentCondop = em.find(Condop.class, condop.getCondopId());
            List<Venta> ventaListOld = persistentCondop.getVentaList();
            List<Venta> ventaListNew = condop.getVentaList();
            List<String> illegalOrphanMessages = null;
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its condopId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVtaId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            condop.setVentaList(ventaListNew);
            condop = em.merge(condop);
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Condop oldCondopIdOfVentaListNewVenta = ventaListNewVenta.getCondopId();
                    ventaListNewVenta.setCondopId(condop);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldCondopIdOfVentaListNewVenta != null && !oldCondopIdOfVentaListNewVenta.equals(condop)) {
                        oldCondopIdOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldCondopIdOfVentaListNewVenta = em.merge(oldCondopIdOfVentaListNewVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = condop.getCondopId();
                if (findCondop(id) == null) {
                    throw new NonexistentEntityException("The condop with id " + id + " no longer exists.");
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
            Condop condop;
            try {
                condop = em.getReference(Condop.class, id);
                condop.getCondopId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The condop with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Venta> ventaListOrphanCheck = condop.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Condop (" + condop + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable condopId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(condop);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Condop> findCondopEntities() {
        return findCondopEntities(true, -1, -1);
    }

    public List<Condop> findCondopEntities(int maxResults, int firstResult) {
        return findCondopEntities(false, maxResults, firstResult);
    }

    private List<Condop> findCondopEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Condop.class));
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

    public Condop findCondop(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Condop.class, id);
        } finally {
            em.close();
        }
    }

    public int getCondopCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Condop> rt = cq.from(Condop.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public void save (Condop condop){
        
        String query = "insert into condop ( condop_id,condop_desc)"
                        + "values(?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, condop.getCondopId());
            ps.setString(1, condop.getCondopDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) condop.setCondopId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Condop getById(int id){
        List<Condop> lista = getByFiltro("condop_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Condop> getAll(){
        return getByFiltro("1=1");
    }
    public List<Condop> getByFiltro(String filtro){
        List<Condop> lista = new ArrayList();
        String query = "select * from condop where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Condop condop = new Condop(
                        rs.getInt("condop_id"),
                        rs.getString("condop_desc")
                ); 
                lista.add(condop);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Condop> getByCondop(String desc){
        return getByFiltro("condop_des ='"+desc+"'");
    }
    
    
    public void remove(Condop condop){
        if(condop != null ){
            String query = "delete from condop where condop_id ="+condop.getCondopId();
        try {
            conn.createStatement().execute(query);
            condop = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Condop condop){
        String query = "update condop set condop_des = ?"
                + "where condop_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, condop.getCondopDesc());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
