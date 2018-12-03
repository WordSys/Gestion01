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
import ar.com.gestion.entities.Libretacontacto;
import ar.com.gestion.entities.Tcontacto;
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
public class TcontactoJpaController implements Serializable {

    Connection conn;
    
    public TcontactoJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public TcontactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tcontacto tcontacto) {
        if (tcontacto.getLibretacontactoList() == null) {
            tcontacto.setLibretacontactoList(new ArrayList<Libretacontacto>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Libretacontacto> attachedLibretacontactoList = new ArrayList<Libretacontacto>();
            for (Libretacontacto libretacontactoListLibretacontactoToAttach : tcontacto.getLibretacontactoList()) {
                libretacontactoListLibretacontactoToAttach = em.getReference(libretacontactoListLibretacontactoToAttach.getClass(), libretacontactoListLibretacontactoToAttach.getLibId());
                attachedLibretacontactoList.add(libretacontactoListLibretacontactoToAttach);
            }
            tcontacto.setLibretacontactoList(attachedLibretacontactoList);
            em.persist(tcontacto);
            for (Libretacontacto libretacontactoListLibretacontacto : tcontacto.getLibretacontactoList()) {
                Tcontacto oldTcontactoIdOfLibretacontactoListLibretacontacto = libretacontactoListLibretacontacto.getTcontactoId();
                libretacontactoListLibretacontacto.setTcontactoId(tcontacto);
                libretacontactoListLibretacontacto = em.merge(libretacontactoListLibretacontacto);
                if (oldTcontactoIdOfLibretacontactoListLibretacontacto != null) {
                    oldTcontactoIdOfLibretacontactoListLibretacontacto.getLibretacontactoList().remove(libretacontactoListLibretacontacto);
                    oldTcontactoIdOfLibretacontactoListLibretacontacto = em.merge(oldTcontactoIdOfLibretacontactoListLibretacontacto);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tcontacto tcontacto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tcontacto persistentTcontacto = em.find(Tcontacto.class, tcontacto.getTcontactoId());
            List<Libretacontacto> libretacontactoListOld = persistentTcontacto.getLibretacontactoList();
            List<Libretacontacto> libretacontactoListNew = tcontacto.getLibretacontactoList();
            List<String> illegalOrphanMessages = null;
            for (Libretacontacto libretacontactoListOldLibretacontacto : libretacontactoListOld) {
                if (!libretacontactoListNew.contains(libretacontactoListOldLibretacontacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Libretacontacto " + libretacontactoListOldLibretacontacto + " since its tcontactoId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Libretacontacto> attachedLibretacontactoListNew = new ArrayList<Libretacontacto>();
            for (Libretacontacto libretacontactoListNewLibretacontactoToAttach : libretacontactoListNew) {
                libretacontactoListNewLibretacontactoToAttach = em.getReference(libretacontactoListNewLibretacontactoToAttach.getClass(), libretacontactoListNewLibretacontactoToAttach.getLibId());
                attachedLibretacontactoListNew.add(libretacontactoListNewLibretacontactoToAttach);
            }
            libretacontactoListNew = attachedLibretacontactoListNew;
            tcontacto.setLibretacontactoList(libretacontactoListNew);
            tcontacto = em.merge(tcontacto);
            for (Libretacontacto libretacontactoListNewLibretacontacto : libretacontactoListNew) {
                if (!libretacontactoListOld.contains(libretacontactoListNewLibretacontacto)) {
                    Tcontacto oldTcontactoIdOfLibretacontactoListNewLibretacontacto = libretacontactoListNewLibretacontacto.getTcontactoId();
                    libretacontactoListNewLibretacontacto.setTcontactoId(tcontacto);
                    libretacontactoListNewLibretacontacto = em.merge(libretacontactoListNewLibretacontacto);
                    if (oldTcontactoIdOfLibretacontactoListNewLibretacontacto != null && !oldTcontactoIdOfLibretacontactoListNewLibretacontacto.equals(tcontacto)) {
                        oldTcontactoIdOfLibretacontactoListNewLibretacontacto.getLibretacontactoList().remove(libretacontactoListNewLibretacontacto);
                        oldTcontactoIdOfLibretacontactoListNewLibretacontacto = em.merge(oldTcontactoIdOfLibretacontactoListNewLibretacontacto);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tcontacto.getTcontactoId();
                if (findTcontacto(id) == null) {
                    throw new NonexistentEntityException("The tcontacto with id " + id + " no longer exists.");
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
            Tcontacto tcontacto;
            try {
                tcontacto = em.getReference(Tcontacto.class, id);
                tcontacto.getTcontactoId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tcontacto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Libretacontacto> libretacontactoListOrphanCheck = tcontacto.getLibretacontactoList();
            for (Libretacontacto libretacontactoListOrphanCheckLibretacontacto : libretacontactoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tcontacto (" + tcontacto + ") cannot be destroyed since the Libretacontacto " + libretacontactoListOrphanCheckLibretacontacto + " in its libretacontactoList field has a non-nullable tcontactoId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tcontacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tcontacto> findTcontactoEntities() {
        return findTcontactoEntities(true, -1, -1);
    }

    public List<Tcontacto> findTcontactoEntities(int maxResults, int firstResult) {
        return findTcontactoEntities(false, maxResults, firstResult);
    }

    private List<Tcontacto> findTcontactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tcontacto.class));
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

    public Tcontacto findTcontacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tcontacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getTcontactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tcontacto> rt = cq.from(Tcontacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Tcontacto tcontacto){
        String query = "insert into tcontacto (tcontacto_id,tcontacto_desc)"
                        + "values(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, tcontacto.getTcontactoId());
            ps.setString(1, tcontacto.getTcontactoDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) tcontacto.setTcontactoId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Tcontacto getById(int id){
        List<Tcontacto> lista = getByFiltro("tcontacto_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Tcontacto> getAll(){
        return getByFiltro("1=1");
    }
    public List<Tcontacto> getByFiltro(String filtro){
        List<Tcontacto> lista = new ArrayList();
        String query = "select tcontacto_desc from tcontacto where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tcontacto tcontacto = new Tcontacto(
                        rs.getString("tcontacto_desc")
                ); 
                lista.add(tcontacto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Tcontacto> getByNombre(String desc){
        return getByFiltro("tcontacto_desc ='"+desc+"'");
    }
    public List<Tcontacto> getLikeNombre(String desc){
        return getByFiltro("tcontacto_desc like'%"+desc+"%'");
    }
    public void remove(Tcontacto tcontacto){
        if(tcontacto != null ){
            String query = "delete from tcontacto where tcontacto_id ="+tcontacto.getTcontactoId();
        try {
            conn.createStatement().execute(query);
            tcontacto = null;
            } catch (Exception e) {
                System.out.println(e);
                }   
            }
    }
    public void update(Tcontacto tcontacto){
        String query = "update tcontacto set tcontacto_desc = ?"
                + "where tcontacto_id = ? ";
        try {
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(2, tcontacto.getTcontactoDesc());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public List<Tcontacto> getByDesc(){
        List<Tcontacto> lista = new ArrayList();
        String query = "select * from tcontacto ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tcontacto tcontacto = new Tcontacto(
                        rs.getInt("tcontacto_id"),
                        rs.getString("tcontacto_desc")
                ); 
                lista.add(tcontacto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public int getByIdOfName(String nombre){
        int id = 0;
        String query = "select tcontacto_id from tcontacto where tcontacto_desc = '" + nombre + "'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tcontacto contacto = new Tcontacto(
                        rs.getInt("tcontacto_id")
                        
                ); 
                id = contacto.getTcontactoId();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }
    
}
