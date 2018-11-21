/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Detallevta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Producto;
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
public class DetallevtaJpaController implements Serializable {
    
    Connection conn;
    
    public DetallevtaJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public DetallevtaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Detallevta detallevta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto prodId = detallevta.getProdId();
            if (prodId != null) {
                prodId = em.getReference(prodId.getClass(), prodId.getProdId());
                detallevta.setProdId(prodId);
            }
            Venta vtaId = detallevta.getVtaId();
            if (vtaId != null) {
                vtaId = em.getReference(vtaId.getClass(), vtaId.getVtaId());
                detallevta.setVtaId(vtaId);
            }
            em.persist(detallevta);
            if (prodId != null) {
                prodId.getDetallevtaList().add(detallevta);
                prodId = em.merge(prodId);
            }
            if (vtaId != null) {
                vtaId.getDetallevtaList().add(detallevta);
                vtaId = em.merge(vtaId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Detallevta detallevta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Detallevta persistentDetallevta = em.find(Detallevta.class, detallevta.getDetvtaId());
            Producto prodIdOld = persistentDetallevta.getProdId();
            Producto prodIdNew = detallevta.getProdId();
            Venta vtaIdOld = persistentDetallevta.getVtaId();
            Venta vtaIdNew = detallevta.getVtaId();
            if (prodIdNew != null) {
                prodIdNew = em.getReference(prodIdNew.getClass(), prodIdNew.getProdId());
                detallevta.setProdId(prodIdNew);
            }
            if (vtaIdNew != null) {
                vtaIdNew = em.getReference(vtaIdNew.getClass(), vtaIdNew.getVtaId());
                detallevta.setVtaId(vtaIdNew);
            }
            detallevta = em.merge(detallevta);
            if (prodIdOld != null && !prodIdOld.equals(prodIdNew)) {
                prodIdOld.getDetallevtaList().remove(detallevta);
                prodIdOld = em.merge(prodIdOld);
            }
            if (prodIdNew != null && !prodIdNew.equals(prodIdOld)) {
                prodIdNew.getDetallevtaList().add(detallevta);
                prodIdNew = em.merge(prodIdNew);
            }
            if (vtaIdOld != null && !vtaIdOld.equals(vtaIdNew)) {
                vtaIdOld.getDetallevtaList().remove(detallevta);
                vtaIdOld = em.merge(vtaIdOld);
            }
            if (vtaIdNew != null && !vtaIdNew.equals(vtaIdOld)) {
                vtaIdNew.getDetallevtaList().add(detallevta);
                vtaIdNew = em.merge(vtaIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detallevta.getDetvtaId();
                if (findDetallevta(id) == null) {
                    throw new NonexistentEntityException("The detallevta with id " + id + " no longer exists.");
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
            Detallevta detallevta;
            try {
                detallevta = em.getReference(Detallevta.class, id);
                detallevta.getDetvtaId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detallevta with id " + id + " no longer exists.", enfe);
            }
            Producto prodId = detallevta.getProdId();
            if (prodId != null) {
                prodId.getDetallevtaList().remove(detallevta);
                prodId = em.merge(prodId);
            }
            Venta vtaId = detallevta.getVtaId();
            if (vtaId != null) {
                vtaId.getDetallevtaList().remove(detallevta);
                vtaId = em.merge(vtaId);
            }
            em.remove(detallevta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Detallevta> findDetallevtaEntities() {
        return findDetallevtaEntities(true, -1, -1);
    }

    public List<Detallevta> findDetallevtaEntities(int maxResults, int firstResult) {
        return findDetallevtaEntities(false, maxResults, firstResult);
    }

    private List<Detallevta> findDetallevtaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Detallevta.class));
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

    public Detallevta findDetallevta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Detallevta.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetallevtaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Detallevta> rt = cq.from(Detallevta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Detallevta detallevta){
        
        String query = "insert into detallevta (detvta_id, vta_id, prod_id, prod_pu, detvta_can)"
                        + "values(?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, detallevta.getDetvtaId());
            ps.setInt(1, detallevta.getVtaId().getVtaId());
            ps.setInt(2, detallevta.getProdId().getProdId());
            ps.setDouble(3, detallevta.getProdPu());
            ps.setDouble(4, detallevta.getDetvtaCan());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) detallevta.setDetvtaId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Detallevta getById(int id){
        List<Detallevta> lista = getByFiltro("detvta_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Detallevta> getAll(){
        return getByFiltro("1=1");
    }
    public List<Detallevta> getByFiltro(String filtro){
        List<Detallevta> lista = new ArrayList();
        String query = "select * from detallevta where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Detallevta detallevta = new Detallevta(
                        rs.getInt("detvta_id"),
                        rs.getInt("vta_id"),
                        rs.getInt("prod_id"),
                        rs.getDouble("prod_pu"),
                        rs.getDouble("detvta_can")
                ); 
                lista.add(detallevta);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Detallevta> getByProdId(int prodId){
        return getByFiltro("prod_id ='"+prodId+"'");
    }
    
    public List<Detallevta> getByVtaId(int vtaId){
        return getByFiltro("vta_id ='"+vtaId+"'");
    }
    
    public void remove(Detallevta detallevta){
        if(detallevta != null ){
            String query = "delete from detallevta where detvta_id ="+detallevta.getDetvtaId();
        try {
            conn.createStatement().execute(query);
            detallevta = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Detallevta detallevta){
        String query = "update detallevta set vta_id = ?, prod_id = ?, prod_pu = ?, detvta_can = ?"
                + "where detvta_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, detallevta.getVtaId().getVtaId());
            ps.setInt(2, detallevta.getProdId().getProdId());
            ps.setDouble(3, detallevta.getProdPu());
            ps.setDouble(4, detallevta.getDetvtaCan());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
