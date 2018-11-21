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
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Libretacontacto;
import ar.com.gestion.entities.Tcontacto;
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
public class LibretacontactoJpaController implements Serializable {

    Connection conn;
    
    public LibretacontactoJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public LibretacontactoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Libretacontacto libretacontacto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion dirId = libretacontacto.getDirId();
            if (dirId != null) {
                dirId = em.getReference(dirId.getClass(), dirId.getDirId());
                libretacontacto.setDirId(dirId);
            }
            Tcontacto tcontactoId = libretacontacto.getTcontactoId();
            if (tcontactoId != null) {
                tcontactoId = em.getReference(tcontactoId.getClass(), tcontactoId.getTcontactoId());
                libretacontacto.setTcontactoId(tcontactoId);
            }
            em.persist(libretacontacto);
            if (dirId != null) {
                dirId.getLibretacontactoList().add(libretacontacto);
                dirId = em.merge(dirId);
            }
            if (tcontactoId != null) {
                tcontactoId.getLibretacontactoList().add(libretacontacto);
                tcontactoId = em.merge(tcontactoId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Libretacontacto libretacontacto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Libretacontacto persistentLibretacontacto = em.find(Libretacontacto.class, libretacontacto.getLibId());
            Direccion dirIdOld = persistentLibretacontacto.getDirId();
            Direccion dirIdNew = libretacontacto.getDirId();
            Tcontacto tcontactoIdOld = persistentLibretacontacto.getTcontactoId();
            Tcontacto tcontactoIdNew = libretacontacto.getTcontactoId();
            if (dirIdNew != null) {
                dirIdNew = em.getReference(dirIdNew.getClass(), dirIdNew.getDirId());
                libretacontacto.setDirId(dirIdNew);
            }
            if (tcontactoIdNew != null) {
                tcontactoIdNew = em.getReference(tcontactoIdNew.getClass(), tcontactoIdNew.getTcontactoId());
                libretacontacto.setTcontactoId(tcontactoIdNew);
            }
            libretacontacto = em.merge(libretacontacto);
            if (dirIdOld != null && !dirIdOld.equals(dirIdNew)) {
                dirIdOld.getLibretacontactoList().remove(libretacontacto);
                dirIdOld = em.merge(dirIdOld);
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                dirIdNew.getLibretacontactoList().add(libretacontacto);
                dirIdNew = em.merge(dirIdNew);
            }
            if (tcontactoIdOld != null && !tcontactoIdOld.equals(tcontactoIdNew)) {
                tcontactoIdOld.getLibretacontactoList().remove(libretacontacto);
                tcontactoIdOld = em.merge(tcontactoIdOld);
            }
            if (tcontactoIdNew != null && !tcontactoIdNew.equals(tcontactoIdOld)) {
                tcontactoIdNew.getLibretacontactoList().add(libretacontacto);
                tcontactoIdNew = em.merge(tcontactoIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = libretacontacto.getLibId();
                if (findLibretacontacto(id) == null) {
                    throw new NonexistentEntityException("The libretacontacto with id " + id + " no longer exists.");
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
            Libretacontacto libretacontacto;
            try {
                libretacontacto = em.getReference(Libretacontacto.class, id);
                libretacontacto.getLibId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The libretacontacto with id " + id + " no longer exists.", enfe);
            }
            Direccion dirId = libretacontacto.getDirId();
            if (dirId != null) {
                dirId.getLibretacontactoList().remove(libretacontacto);
                dirId = em.merge(dirId);
            }
            Tcontacto tcontactoId = libretacontacto.getTcontactoId();
            if (tcontactoId != null) {
                tcontactoId.getLibretacontactoList().remove(libretacontacto);
                tcontactoId = em.merge(tcontactoId);
            }
            em.remove(libretacontacto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Libretacontacto> findLibretacontactoEntities() {
        return findLibretacontactoEntities(true, -1, -1);
    }

    public List<Libretacontacto> findLibretacontactoEntities(int maxResults, int firstResult) {
        return findLibretacontactoEntities(false, maxResults, firstResult);
    }

    private List<Libretacontacto> findLibretacontactoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Libretacontacto.class));
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

    public Libretacontacto findLibretacontacto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Libretacontacto.class, id);
        } finally {
            em.close();
        }
    }

    public int getLibretacontactoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Libretacontacto> rt = cq.from(Libretacontacto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Libretacontacto libretacontacto){
        
        String query = "insert into libretacontacto (lib_id,tcontacto_id,dir_id,lib_contacto)"
                        + "values(?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, libretacontacto.getLibId());
            ps.setInt(1, libretacontacto.getTcontactoId().getTcontactoId());
            ps.setInt(2, libretacontacto.getDirId().getDirId());
            ps.setString(3, libretacontacto.getLibContacto());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) libretacontacto.setLibId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Libretacontacto getById(int id){
        List<Libretacontacto> lista = getByFiltro("lib_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Libretacontacto> getAll(){
        return getByFiltro("1=1");
    }
    public List<Libretacontacto> getByFiltro(String filtro){
        List<Libretacontacto> lista = new ArrayList();
        String query = "select * from libretacontacto where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Libretacontacto libretacontacto = new Libretacontacto(
                        rs.getInt("lib_id"),
                        rs.getInt("tcontacto_id"),
                        rs.getInt("dir_id"),
                        rs.getString("lib_contacto")
                ); 
                lista.add(libretacontacto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Libretacontacto> getByTcontacto(String Tcontacto){
        return getByFiltro("tcontacto_id ='"+Tcontacto+"'");
    }
    public List<Libretacontacto> getLikeLibreta(String libreta){
        return getByFiltro("lib_contacto like'%"+libreta+"%'");
    }
    public void remove(Libretacontacto libretacontacto){
        if(libretacontacto != null ){
            String query = "delete from libretacontacto where lib_id ="+libretacontacto.getLibId();
        try {
            conn.createStatement().execute(query);
            libretacontacto = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Libretacontacto libretacontacto){
        String query = "update libretacontacto set tcontacto_id = ?,dir_id = ?,lib_contacto = ?"
                + "where lib_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, libretacontacto.getTcontactoId().getTcontactoId());
            ps.setInt(2, libretacontacto.getDirId().getDirId());
            ps.setString(3, libretacontacto.getLibContacto());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int getUltimoId(){
        int ultimoId = 0;
        String query = "SELECT max(lib_id) FROM libretacontacto";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Libretacontacto contactoId = new Libretacontacto(
                        ultimoId = rs.getInt("lib_id")
                ); 
                
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ultimoId;
        
        
    }
    
    
    
}
