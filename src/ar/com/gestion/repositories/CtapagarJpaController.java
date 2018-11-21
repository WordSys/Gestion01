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
import ar.com.gestion.entities.Ctapagar;
import ar.com.gestion.entities.Proveedor;
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
public class CtapagarJpaController implements Serializable {

    Connection conn;
    
    public CtapagarJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public CtapagarJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ctapagar ctapagar) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compId = ctapagar.getCompId();
            if (compId != null) {
                compId = em.getReference(compId.getClass(), compId.getCompId());
                ctapagar.setCompId(compId);
            }
            Proveedor proveId = ctapagar.getProveId();
            if (proveId != null) {
                proveId = em.getReference(proveId.getClass(), proveId.getProveId());
                ctapagar.setProveId(proveId);
            }
            em.persist(ctapagar);
            if (compId != null) {
                compId.getCtapagarList().add(ctapagar);
                compId = em.merge(compId);
            }
            if (proveId != null) {
                proveId.getCtapagarList().add(ctapagar);
                proveId = em.merge(proveId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ctapagar ctapagar) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ctapagar persistentCtapagar = em.find(Ctapagar.class, ctapagar.getCpId());
            Compra compIdOld = persistentCtapagar.getCompId();
            Compra compIdNew = ctapagar.getCompId();
            Proveedor proveIdOld = persistentCtapagar.getProveId();
            Proveedor proveIdNew = ctapagar.getProveId();
            if (compIdNew != null) {
                compIdNew = em.getReference(compIdNew.getClass(), compIdNew.getCompId());
                ctapagar.setCompId(compIdNew);
            }
            if (proveIdNew != null) {
                proveIdNew = em.getReference(proveIdNew.getClass(), proveIdNew.getProveId());
                ctapagar.setProveId(proveIdNew);
            }
            ctapagar = em.merge(ctapagar);
            if (compIdOld != null && !compIdOld.equals(compIdNew)) {
                compIdOld.getCtapagarList().remove(ctapagar);
                compIdOld = em.merge(compIdOld);
            }
            if (compIdNew != null && !compIdNew.equals(compIdOld)) {
                compIdNew.getCtapagarList().add(ctapagar);
                compIdNew = em.merge(compIdNew);
            }
            if (proveIdOld != null && !proveIdOld.equals(proveIdNew)) {
                proveIdOld.getCtapagarList().remove(ctapagar);
                proveIdOld = em.merge(proveIdOld);
            }
            if (proveIdNew != null && !proveIdNew.equals(proveIdOld)) {
                proveIdNew.getCtapagarList().add(ctapagar);
                proveIdNew = em.merge(proveIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ctapagar.getCpId();
                if (findCtapagar(id) == null) {
                    throw new NonexistentEntityException("The ctapagar with id " + id + " no longer exists.");
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
            Ctapagar ctapagar;
            try {
                ctapagar = em.getReference(Ctapagar.class, id);
                ctapagar.getCpId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ctapagar with id " + id + " no longer exists.", enfe);
            }
            Compra compId = ctapagar.getCompId();
            if (compId != null) {
                compId.getCtapagarList().remove(ctapagar);
                compId = em.merge(compId);
            }
            Proveedor proveId = ctapagar.getProveId();
            if (proveId != null) {
                proveId.getCtapagarList().remove(ctapagar);
                proveId = em.merge(proveId);
            }
            em.remove(ctapagar);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ctapagar> findCtapagarEntities() {
        return findCtapagarEntities(true, -1, -1);
    }

    public List<Ctapagar> findCtapagarEntities(int maxResults, int firstResult) {
        return findCtapagarEntities(false, maxResults, firstResult);
    }

    private List<Ctapagar> findCtapagarEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ctapagar.class));
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

    public Ctapagar findCtapagar(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ctapagar.class, id);
        } finally {
            em.close();
        }
    }

    public int getCtapagarCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ctapagar> rt = cq.from(Ctapagar.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Ctapagar ctapagar){
        
        String query = "insert into ctapagar (cp_id,prove_id,comp_id,comp_monto)"
                        + "values(?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, ctapagar.getCpId());
            ps.setInt(1, ctapagar.getProveId().getProveId());
            ps.setInt(2, ctapagar.getCompId().getCompId());
            ps.setDouble(3, ctapagar.getCompMonto());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) ctapagar.setCpId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Ctapagar getById(int id){
        List<Ctapagar> lista = getByFiltro("cp_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Ctapagar> getAll(){
        return getByFiltro("1=1");
    }
    public List<Ctapagar> getByFiltro(String filtro){
        List<Ctapagar> lista = new ArrayList();
        String query = "select * from ctapagar where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Ctapagar ctapagar = new Ctapagar(
                        rs.getInt("cp_id"),
                        rs.getInt("prove_id"),
                        rs.getInt("comp_id"),
                        rs.getDouble("comp_monto")
                ); 
                lista.add(ctapagar);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Ctapagar> getByProveId(String proveId){
        return getByFiltro("prove_id ='"+proveId+"'");
    }
    
    public void remove(Ctapagar ctapagar){
        if(ctapagar != null ){
            String query = "delete from ctapagar where cp_id ="+ctapagar.getCpId();
        try {
            conn.createStatement().execute(query);
            ctapagar = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Ctapagar ctapagar){
        String query = "update ctapagar set prove_id = ?, comp_id = ?, comp_monto = ?"
                + "where cp_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, ctapagar.getProveId().getProveId());
            ps.setInt(2, ctapagar.getCompId().getCompId());
            ps.setDouble(3, ctapagar.getCompMonto());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
