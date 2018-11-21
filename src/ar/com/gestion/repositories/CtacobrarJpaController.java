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
import ar.com.gestion.entities.Ctacobrar;
import ar.com.gestion.entities.Venta;
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
public class CtacobrarJpaController implements Serializable {

    Connection conn;
    
    public CtacobrarJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public CtacobrarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ctacobrar ctacobrar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliId = ctacobrar.getCliId();
            if (cliId != null) {
                cliId = em.getReference(cliId.getClass(), cliId.getCliId());
                ctacobrar.setCliId(cliId);
            }
            Venta vtaId = ctacobrar.getVtaId();
            if (vtaId != null) {
                vtaId = em.getReference(vtaId.getClass(), vtaId.getVtaId());
                ctacobrar.setVtaId(vtaId);
            }
            em.persist(ctacobrar);
            if (cliId != null) {
                cliId.getCtacobrarList().add(ctacobrar);
                cliId = em.merge(cliId);
            }
            if (vtaId != null) {
                vtaId.getCtacobrarList().add(ctacobrar);
                vtaId = em.merge(vtaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ctacobrar ctacobrar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ctacobrar persistentCtacobrar = em.find(Ctacobrar.class, ctacobrar.getCcId());
            Cliente cliIdOld = persistentCtacobrar.getCliId();
            Cliente cliIdNew = ctacobrar.getCliId();
            Venta vtaIdOld = persistentCtacobrar.getVtaId();
            Venta vtaIdNew = ctacobrar.getVtaId();
            if (cliIdNew != null) {
                cliIdNew = em.getReference(cliIdNew.getClass(), cliIdNew.getCliId());
                ctacobrar.setCliId(cliIdNew);
            }
            if (vtaIdNew != null) {
                vtaIdNew = em.getReference(vtaIdNew.getClass(), vtaIdNew.getVtaId());
                ctacobrar.setVtaId(vtaIdNew);
            }
            ctacobrar = em.merge(ctacobrar);
            if (cliIdOld != null && !cliIdOld.equals(cliIdNew)) {
                cliIdOld.getCtacobrarList().remove(ctacobrar);
                cliIdOld = em.merge(cliIdOld);
            }
            if (cliIdNew != null && !cliIdNew.equals(cliIdOld)) {
                cliIdNew.getCtacobrarList().add(ctacobrar);
                cliIdNew = em.merge(cliIdNew);
            }
            if (vtaIdOld != null && !vtaIdOld.equals(vtaIdNew)) {
                vtaIdOld.getCtacobrarList().remove(ctacobrar);
                vtaIdOld = em.merge(vtaIdOld);
            }
            if (vtaIdNew != null && !vtaIdNew.equals(vtaIdOld)) {
                vtaIdNew.getCtacobrarList().add(ctacobrar);
                vtaIdNew = em.merge(vtaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ctacobrar.getCcId();
                if (findCtacobrar(id) == null) {
                    throw new NonexistentEntityException("The ctacobrar with id " + id + " no longer exists.");
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
            Ctacobrar ctacobrar;
            try {
                ctacobrar = em.getReference(Ctacobrar.class, id);
                ctacobrar.getCcId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ctacobrar with id " + id + " no longer exists.", enfe);
            }
            Cliente cliId = ctacobrar.getCliId();
            if (cliId != null) {
                cliId.getCtacobrarList().remove(ctacobrar);
                cliId = em.merge(cliId);
            }
            Venta vtaId = ctacobrar.getVtaId();
            if (vtaId != null) {
                vtaId.getCtacobrarList().remove(ctacobrar);
                vtaId = em.merge(vtaId);
            }
            em.remove(ctacobrar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ctacobrar> findCtacobrarEntities() {
        return findCtacobrarEntities(true, -1, -1);
    }

    public List<Ctacobrar> findCtacobrarEntities(int maxResults, int firstResult) {
        return findCtacobrarEntities(false, maxResults, firstResult);
    }

    private List<Ctacobrar> findCtacobrarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ctacobrar.class));
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

    public Ctacobrar findCtacobrar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ctacobrar.class, id);
        } finally {
            em.close();
        }
    }

    public int getCtacobrarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ctacobrar> rt = cq.from(Ctacobrar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Ctacobrar ctacobrar){
        
        String query = "insert into ctacobrar ( cc_id,cli_id,vta_id,vta_monto)"
                        + "values(?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, ctacobrar.getCcId());
            ps.setInt(1, ctacobrar.getCliId().getCliId());
            ps.setInt(2, ctacobrar.getVtaId().getVtaId());
            ps.setDouble(3, ctacobrar.getVtaMonto());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) ctacobrar.setCcId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Ctacobrar getById(int id){
        List<Ctacobrar> lista = getByFiltro("condtrib_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Ctacobrar> getAll(){
        return getByFiltro("1=1");
    }
    public List<Ctacobrar> getByFiltro(String filtro){
        List<Ctacobrar> lista = new ArrayList();
        String query = "select * from ctacobrar where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Ctacobrar ctacobrar = new Ctacobrar(
                        rs.getInt("cc_id"),
                        rs.getInt("cli_id"),
                        rs.getInt("vta_id"),
                        rs.getDouble("vta_monto")
                ); 
                lista.add(ctacobrar);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Ctacobrar> getByCliId(String cliId){
        return getByFiltro("cli_id ='"+cliId+"'");
    }
    
    
    public void remove(Ctacobrar ctacobrar){
        if(ctacobrar != null ){
            String query = "delete from ctacobrar where cc_id ="+ctacobrar.getCcId();
        try {
            conn.createStatement().execute(query);
            ctacobrar = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Ctacobrar ctacobrar){
        String query = "update ctacobrar set cli_id = ?, vta_id = ?, vta_monto = ?"
                + "where cc_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, ctacobrar.getCliId().getCliId());
            ps.setInt(2, ctacobrar.getVtaId().getVtaId());
            ps.setDouble(3, ctacobrar.getVtaMonto());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
