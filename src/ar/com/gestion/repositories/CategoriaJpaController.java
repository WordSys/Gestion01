/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Categoria;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Empleado;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import ar.com.gestion.repositories.exceptions.PreexistingEntityException;
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
public class CategoriaJpaController implements Serializable {
    
    Connection conn;
    
    public CategoriaJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado = categoria.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getEmpId());
                categoria.setEmpleado(empleado);
            }
            em.persist(categoria);
            if (empleado != null) {
                Categoria oldCatIdOfEmpleado = empleado.getCatId();
                if (oldCatIdOfEmpleado != null) {
                    oldCatIdOfEmpleado.setEmpleado(null);
                    oldCatIdOfEmpleado = em.merge(oldCatIdOfEmpleado);
                }
                empleado.setCatId(categoria);
                empleado = em.merge(empleado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategoria(categoria.getCatId()) != null) {
                throw new PreexistingEntityException("Categoria " + categoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getCatId());
            Empleado empleadoOld = persistentCategoria.getEmpleado();
            Empleado empleadoNew = categoria.getEmpleado();
            List<String> illegalOrphanMessages = null;
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empleado " + empleadoOld + " since its catId field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getEmpId());
                categoria.setEmpleado(empleadoNew);
            }
            categoria = em.merge(categoria);
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                Categoria oldCatIdOfEmpleado = empleadoNew.getCatId();
                if (oldCatIdOfEmpleado != null) {
                    oldCatIdOfEmpleado.setEmpleado(null);
                    oldCatIdOfEmpleado = em.merge(oldCatIdOfEmpleado);
                }
                empleadoNew.setCatId(categoria);
                empleadoNew = em.merge(empleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getCatId();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getCatId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empleado empleadoOrphanCheck = categoria.getEmpleado();
            if (empleadoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Categoria (" + categoria + ") cannot be destroyed since the Empleado " + empleadoOrphanCheck + " in its empleado field has a non-nullable catId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Categoria categoria){
        
        String query = "insert into categoria (cat_id,cat_desc)"
                        + "values(?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, categoria.getCatId());
            ps.setString(1, categoria.getCatDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) categoria.setCatId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Categoria getById(int id){
        List<Categoria> lista = getByFiltro("cat_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Categoria> getAll(){
        return getByFiltro("1=1");
    }
    public List<Categoria> getByFiltro(String filtro){
        List<Categoria> lista = new ArrayList();
        String query = "select * from categoria where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Categoria categoria = new Categoria(
                        rs.getInt("cat_id"),
                        rs.getString("cat_desc")
                ); 
                lista.add(categoria);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Categoria> getByDescripcion(String descripcion){
        return getByFiltro("cat_desc ='"+descripcion+"'");
    }
    public List<Categoria> getLikeDescripcion(String descripcion){
        return getByFiltro("cat_desc like'%"+descripcion+"%'");
    }
    public void remove(Categoria categoria){
        if(categoria != null ){
            String query = "delete from categoria where cat_id ="+categoria.getCatId();
        try {
            conn.createStatement().execute(query);
            categoria = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Categoria categoria){
        String query = "update categoria set cat_desc = ? "
                + "where cat_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, categoria.getCatDesc());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
