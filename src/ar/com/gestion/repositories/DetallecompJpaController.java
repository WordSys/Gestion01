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
import ar.com.gestion.entities.Detallecomp;
import ar.com.gestion.entities.Producto;
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
public class DetallecompJpaController implements Serializable {

    Connection conn;
    
    public DetallecompJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public DetallecompJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallecomp detallecomp) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compId = detallecomp.getCompId();
            if (compId != null) {
                compId = em.getReference(compId.getClass(), compId.getCompId());
                detallecomp.setCompId(compId);
            }
            Producto prodId = detallecomp.getProdId();
            if (prodId != null) {
                prodId = em.getReference(prodId.getClass(), prodId.getProdId());
                detallecomp.setProdId(prodId);
            }
            em.persist(detallecomp);
            if (compId != null) {
                compId.getDetallecompList().add(detallecomp);
                compId = em.merge(compId);
            }
            if (prodId != null) {
                prodId.getDetallecompList().add(detallecomp);
                prodId = em.merge(prodId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallecomp detallecomp) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallecomp persistentDetallecomp = em.find(Detallecomp.class, detallecomp.getDetcompId());
            Compra compIdOld = persistentDetallecomp.getCompId();
            Compra compIdNew = detallecomp.getCompId();
            Producto prodIdOld = persistentDetallecomp.getProdId();
            Producto prodIdNew = detallecomp.getProdId();
            if (compIdNew != null) {
                compIdNew = em.getReference(compIdNew.getClass(), compIdNew.getCompId());
                detallecomp.setCompId(compIdNew);
            }
            if (prodIdNew != null) {
                prodIdNew = em.getReference(prodIdNew.getClass(), prodIdNew.getProdId());
                detallecomp.setProdId(prodIdNew);
            }
            detallecomp = em.merge(detallecomp);
            if (compIdOld != null && !compIdOld.equals(compIdNew)) {
                compIdOld.getDetallecompList().remove(detallecomp);
                compIdOld = em.merge(compIdOld);
            }
            if (compIdNew != null && !compIdNew.equals(compIdOld)) {
                compIdNew.getDetallecompList().add(detallecomp);
                compIdNew = em.merge(compIdNew);
            }
            if (prodIdOld != null && !prodIdOld.equals(prodIdNew)) {
                prodIdOld.getDetallecompList().remove(detallecomp);
                prodIdOld = em.merge(prodIdOld);
            }
            if (prodIdNew != null && !prodIdNew.equals(prodIdOld)) {
                prodIdNew.getDetallecompList().add(detallecomp);
                prodIdNew = em.merge(prodIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallecomp.getDetcompId();
                if (findDetallecomp(id) == null) {
                    throw new NonexistentEntityException("The detallecomp with id " + id + " no longer exists.");
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
            Detallecomp detallecomp;
            try {
                detallecomp = em.getReference(Detallecomp.class, id);
                detallecomp.getDetcompId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallecomp with id " + id + " no longer exists.", enfe);
            }
            Compra compId = detallecomp.getCompId();
            if (compId != null) {
                compId.getDetallecompList().remove(detallecomp);
                compId = em.merge(compId);
            }
            Producto prodId = detallecomp.getProdId();
            if (prodId != null) {
                prodId.getDetallecompList().remove(detallecomp);
                prodId = em.merge(prodId);
            }
            em.remove(detallecomp);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallecomp> findDetallecompEntities() {
        return findDetallecompEntities(true, -1, -1);
    }

    public List<Detallecomp> findDetallecompEntities(int maxResults, int firstResult) {
        return findDetallecompEntities(false, maxResults, firstResult);
    }

    private List<Detallecomp> findDetallecompEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallecomp.class));
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

    public Detallecomp findDetallecomp(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallecomp.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallecompCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallecomp> rt = cq.from(Detallecomp.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Detallecomp detallecomp){
        
        String query = "insert into detallecomp (detcomp_id, comp_id, prod_id, prod_pu, detcomp_monto)"
                        + "values(?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, detallecomp.getDetcompId());
            ps.setInt(1, detallecomp.getCompId().getCompId());
            ps.setInt(2, detallecomp.getProdId().getProdId());
            ps.setDouble(3, detallecomp.getProdPu());
            ps.setDouble(4, detallecomp.getDetcompMonto());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) detallecomp.setDetcompId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Detallecomp getById(int id){
        List<Detallecomp> lista = getByFiltro("detcomp_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Detallecomp> getAll(){
        return getByFiltro("1=1");
    }
    public List<Detallecomp> getByFiltro(String filtro){
        List<Detallecomp> lista = new ArrayList();
        String query = "select * from detallecomp where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Detallecomp detallecomp = new Detallecomp(
                        rs.getInt("detcomp_id"),
                        rs.getInt("comp_id"),
                        rs.getInt("prod_id"),
                        rs.getDouble("prod_pu"),
                        rs.getDouble("comp_monto")
                ); 
                lista.add(detallecomp);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Detallecomp> getByProdId(int prodId){
        return getByFiltro("prod_id ='"+prodId+"'");
    }
    
    public List<Detallecomp> getByCompId(int compId){
        return getByFiltro("comp_id ='"+compId+"'");
    }
    
    public void remove(Detallecomp detallecomp){
        if(detallecomp != null ){
            String query = "delete from detallecomp where detcomp_id ="+detallecomp.getDetcompId();
        try {
            conn.createStatement().execute(query);
            detallecomp = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Detallecomp detallecomp){
        String query = "update detallecomp set comp_id = ?, prod_id = ?, prod_pu = ?, detcomp_monto = ?"
                + "where detcomp_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, detallecomp.getCompId().getCompId());
            ps.setInt(2, detallecomp.getProdId().getProdId());
            ps.setDouble(3, detallecomp.getProdPu());
            ps.setDouble(4, detallecomp.getDetcompMonto());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
