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
import ar.com.gestion.entities.Localidad;
import ar.com.gestion.entities.Provincia;
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
public class ProvinciaJpaController implements Serializable {
    
    Connection conn;
    
    public ProvinciaJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public ProvinciaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Provincia provincia) {
        if (provincia.getLocalidadList() == null) {
            provincia.setLocalidadList(new ArrayList<Localidad>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion direccion = provincia.getDireccion();
            if (direccion != null) {
                direccion = em.getReference(direccion.getClass(), direccion.getDirId());
                provincia.setDireccion(direccion);
            }
            List<Localidad> attachedLocalidadList = new ArrayList<Localidad>();
            for (Localidad localidadListLocalidadToAttach : provincia.getLocalidadList()) {
                localidadListLocalidadToAttach = em.getReference(localidadListLocalidadToAttach.getClass(), localidadListLocalidadToAttach.getId());
                attachedLocalidadList.add(localidadListLocalidadToAttach);
            }
            provincia.setLocalidadList(attachedLocalidadList);
            em.persist(provincia);
            if (direccion != null) {
                Provincia oldProvinciaIdOfDireccion = direccion.getProvinciaId();
                if (oldProvinciaIdOfDireccion != null) {
                    oldProvinciaIdOfDireccion.setDireccion(null);
                    oldProvinciaIdOfDireccion = em.merge(oldProvinciaIdOfDireccion);
                }
                direccion.setProvinciaId(provincia);
                direccion = em.merge(direccion);
            }
            for (Localidad localidadListLocalidad : provincia.getLocalidadList()) {
                Provincia oldProvinciaIdOfLocalidadListLocalidad = localidadListLocalidad.getProvinciaId();
                localidadListLocalidad.setProvinciaId(provincia);
                localidadListLocalidad = em.merge(localidadListLocalidad);
                if (oldProvinciaIdOfLocalidadListLocalidad != null) {
                    oldProvinciaIdOfLocalidadListLocalidad.getLocalidadList().remove(localidadListLocalidad);
                    oldProvinciaIdOfLocalidadListLocalidad = em.merge(oldProvinciaIdOfLocalidadListLocalidad);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Provincia provincia) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia persistentProvincia = em.find(Provincia.class, provincia.getId());
            Direccion direccionOld = persistentProvincia.getDireccion();
            Direccion direccionNew = provincia.getDireccion();
            List<Localidad> localidadListOld = persistentProvincia.getLocalidadList();
            List<Localidad> localidadListNew = provincia.getLocalidadList();
            List<String> illegalOrphanMessages = null;
            if (direccionOld != null && !direccionOld.equals(direccionNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Direccion " + direccionOld + " since its provinciaId field is not nullable.");
            }
            for (Localidad localidadListOldLocalidad : localidadListOld) {
                if (!localidadListNew.contains(localidadListOldLocalidad)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Localidad " + localidadListOldLocalidad + " since its provinciaId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (direccionNew != null) {
                direccionNew = em.getReference(direccionNew.getClass(), direccionNew.getDirId());
                provincia.setDireccion(direccionNew);
            }
            List<Localidad> attachedLocalidadListNew = new ArrayList<Localidad>();
            for (Localidad localidadListNewLocalidadToAttach : localidadListNew) {
                localidadListNewLocalidadToAttach = em.getReference(localidadListNewLocalidadToAttach.getClass(), localidadListNewLocalidadToAttach.getId());
                attachedLocalidadListNew.add(localidadListNewLocalidadToAttach);
            }
            localidadListNew = attachedLocalidadListNew;
            provincia.setLocalidadList(localidadListNew);
            provincia = em.merge(provincia);
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                Provincia oldProvinciaIdOfDireccion = direccionNew.getProvinciaId();
                if (oldProvinciaIdOfDireccion != null) {
                    oldProvinciaIdOfDireccion.setDireccion(null);
                    oldProvinciaIdOfDireccion = em.merge(oldProvinciaIdOfDireccion);
                }
                direccionNew.setProvinciaId(provincia);
                direccionNew = em.merge(direccionNew);
            }
            for (Localidad localidadListNewLocalidad : localidadListNew) {
                if (!localidadListOld.contains(localidadListNewLocalidad)) {
                    Provincia oldProvinciaIdOfLocalidadListNewLocalidad = localidadListNewLocalidad.getProvinciaId();
                    localidadListNewLocalidad.setProvinciaId(provincia);
                    localidadListNewLocalidad = em.merge(localidadListNewLocalidad);
                    if (oldProvinciaIdOfLocalidadListNewLocalidad != null && !oldProvinciaIdOfLocalidadListNewLocalidad.equals(provincia)) {
                        oldProvinciaIdOfLocalidadListNewLocalidad.getLocalidadList().remove(localidadListNewLocalidad);
                        oldProvinciaIdOfLocalidadListNewLocalidad = em.merge(oldProvinciaIdOfLocalidadListNewLocalidad);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = provincia.getId();
                if (findProvincia(id) == null) {
                    throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia provincia;
            try {
                provincia = em.getReference(Provincia.class, id);
                provincia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The provincia with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Direccion direccionOrphanCheck = provincia.getDireccion();
            if (direccionOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provincia (" + provincia + ") cannot be destroyed since the Direccion " + direccionOrphanCheck + " in its direccion field has a non-nullable provinciaId field.");
            }
            List<Localidad> localidadListOrphanCheck = provincia.getLocalidadList();
            for (Localidad localidadListOrphanCheckLocalidad : localidadListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Provincia (" + provincia + ") cannot be destroyed since the Localidad " + localidadListOrphanCheckLocalidad + " in its localidadList field has a non-nullable provinciaId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(provincia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Provincia> findProvinciaEntities() {
        return findProvinciaEntities(true, -1, -1);
    }

    public List<Provincia> findProvinciaEntities(int maxResults, int firstResult) {
        return findProvinciaEntities(false, maxResults, firstResult);
    }

    private List<Provincia> findProvinciaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Provincia.class));
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

    public Provincia findProvincia(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Provincia.class, id);
        } finally {
            em.close();
        }
    }

    public int getProvinciaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Provincia> rt = cq.from(Provincia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Provincia provincia){
        String query = "insert into proveedor (id,nombre,codigo31662)"
                        + "values(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setShort(0, provincia.getId());
            ps.setString(1, provincia.getNombre());
            ps.setString(2, provincia.getCodigo31662());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) provincia.setId(rs.getShort(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Provincia getById(int id){
        List<Provincia> lista = getByFiltro("id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Provincia> getAll(){
        return getByFiltro("1=1");
    }
    public List<Provincia> getByFiltro(String filtro){
        List<Provincia> lista = new ArrayList();
        String query = "select * from provincia where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Provincia provincia = new Provincia(
                        rs.getShort("id"),
                        rs.getString("nombre"),
                        rs.getString("codigo31662")
                ); 
                lista.add(provincia);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Provincia> getByNombre(String nombre){
        return getByFiltro("nombre ='"+nombre+"'");
    }
    public List<Provincia> getLikeNombre(String nombre){
        return getByFiltro("nombre like'%"+nombre+"%'");
    }
    public void remove(Provincia provincia){
        if(provincia != null ){
            String query = "delete from provincia where id ="+provincia.getId();
        try {
            conn.createStatement().execute(query);
            provincia = null;
            } catch (Exception e) {
                System.out.println(e);
                }   
            }
    }
    public void update(Provincia provincia){
        String query = "update provincia set nombre = ?, codigo31662 = ?"
                + "where id = ? ";
        try {
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, provincia.getNombre());
            ps.setString(2, provincia.getCodigo31662());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public List<Provincia> getByDesc(){
        List<Provincia> lista = new ArrayList();
        String query = "select * from provincia ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Provincia provincia = new Provincia(
                        //rs.getShort("id"),
                        rs.getString("nombre")
                ); 
                lista.add(provincia);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public Short getByIdOfProv(String nombre){
        Short id = 0;
        String query = "select id from provincia where nombre = '" + nombre + "'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Provincia provincia = new Provincia(
                        rs.getShort("id")
                        
                ); 
                id = provincia.getId();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }
    
}
