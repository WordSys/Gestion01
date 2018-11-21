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
import ar.com.gestion.entities.Provincia;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Localidad;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.ComboBox;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class LocalidadJpaController implements Serializable {
    
    Connection conn;
    
    public LocalidadJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public LocalidadJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Localidad localidad) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Provincia provinciaId = localidad.getProvinciaId();
            if (provinciaId != null) {
                provinciaId = em.getReference(provinciaId.getClass(), provinciaId.getId());
                localidad.setProvinciaId(provinciaId);
            }
            Direccion direccion = localidad.getDireccion();
            if (direccion != null) {
                direccion = em.getReference(direccion.getClass(), direccion.getDirId());
                localidad.setDireccion(direccion);
            }
            Direccion direccion1 = localidad.getDireccion1();
            if (direccion1 != null) {
                direccion1 = em.getReference(direccion1.getClass(), direccion1.getDirId());
                localidad.setDireccion1(direccion1);
            }
            em.persist(localidad);
            if (provinciaId != null) {
                provinciaId.getLocalidadList().add(localidad);
                provinciaId = em.merge(provinciaId);
            }
            if (direccion != null) {
                Localidad oldDirCpOfDireccion = direccion.getDirCp();
                if (oldDirCpOfDireccion != null) {
                    oldDirCpOfDireccion.setDireccion(null);
                    oldDirCpOfDireccion = em.merge(oldDirCpOfDireccion);
                }
                direccion.setDirCp(localidad);
                direccion = em.merge(direccion);
            }
            if (direccion1 != null) {
                Localidad oldLocalidadIdOfDireccion1 = direccion1.getLocalidadId();
                if (oldLocalidadIdOfDireccion1 != null) {
                    oldLocalidadIdOfDireccion1.setDireccion1(null);
                    oldLocalidadIdOfDireccion1 = em.merge(oldLocalidadIdOfDireccion1);
                }
                direccion1.setLocalidadId(localidad);
                direccion1 = em.merge(direccion1);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Localidad localidad) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Localidad persistentLocalidad = em.find(Localidad.class, localidad.getId());
            Provincia provinciaIdOld = persistentLocalidad.getProvinciaId();
            Provincia provinciaIdNew = localidad.getProvinciaId();
            Direccion direccionOld = persistentLocalidad.getDireccion();
            Direccion direccionNew = localidad.getDireccion();
            Direccion direccion1Old = persistentLocalidad.getDireccion1();
            Direccion direccion1New = localidad.getDireccion1();
            List<String> illegalOrphanMessages = null;
            if (direccionOld != null && !direccionOld.equals(direccionNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Direccion " + direccionOld + " since its dirCp field is not nullable.");
            }
            if (direccion1Old != null && !direccion1Old.equals(direccion1New)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Direccion " + direccion1Old + " since its localidadId field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (provinciaIdNew != null) {
                provinciaIdNew = em.getReference(provinciaIdNew.getClass(), provinciaIdNew.getId());
                localidad.setProvinciaId(provinciaIdNew);
            }
            if (direccionNew != null) {
                direccionNew = em.getReference(direccionNew.getClass(), direccionNew.getDirId());
                localidad.setDireccion(direccionNew);
            }
            if (direccion1New != null) {
                direccion1New = em.getReference(direccion1New.getClass(), direccion1New.getDirId());
                localidad.setDireccion1(direccion1New);
            }
            localidad = em.merge(localidad);
            if (provinciaIdOld != null && !provinciaIdOld.equals(provinciaIdNew)) {
                provinciaIdOld.getLocalidadList().remove(localidad);
                provinciaIdOld = em.merge(provinciaIdOld);
            }
            if (provinciaIdNew != null && !provinciaIdNew.equals(provinciaIdOld)) {
                provinciaIdNew.getLocalidadList().add(localidad);
                provinciaIdNew = em.merge(provinciaIdNew);
            }
            if (direccionNew != null && !direccionNew.equals(direccionOld)) {
                Localidad oldDirCpOfDireccion = direccionNew.getDirCp();
                if (oldDirCpOfDireccion != null) {
                    oldDirCpOfDireccion.setDireccion(null);
                    oldDirCpOfDireccion = em.merge(oldDirCpOfDireccion);
                }
                direccionNew.setDirCp(localidad);
                direccionNew = em.merge(direccionNew);
            }
            if (direccion1New != null && !direccion1New.equals(direccion1Old)) {
                Localidad oldLocalidadIdOfDireccion1 = direccion1New.getLocalidadId();
                if (oldLocalidadIdOfDireccion1 != null) {
                    oldLocalidadIdOfDireccion1.setDireccion1(null);
                    oldLocalidadIdOfDireccion1 = em.merge(oldLocalidadIdOfDireccion1);
                }
                direccion1New.setLocalidadId(localidad);
                direccion1New = em.merge(direccion1New);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = localidad.getId();
                if (findLocalidad(id) == null) {
                    throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.");
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
            Localidad localidad;
            try {
                localidad = em.getReference(Localidad.class, id);
                localidad.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidad with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Direccion direccionOrphanCheck = localidad.getDireccion();
            if (direccionOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localidad (" + localidad + ") cannot be destroyed since the Direccion " + direccionOrphanCheck + " in its direccion field has a non-nullable dirCp field.");
            }
            Direccion direccion1OrphanCheck = localidad.getDireccion1();
            if (direccion1OrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Localidad (" + localidad + ") cannot be destroyed since the Direccion " + direccion1OrphanCheck + " in its direccion1 field has a non-nullable localidadId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Provincia provinciaId = localidad.getProvinciaId();
            if (provinciaId != null) {
                provinciaId.getLocalidadList().remove(localidad);
                provinciaId = em.merge(provinciaId);
            }
            em.remove(localidad);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Localidad> findLocalidadEntities() {
        return findLocalidadEntities(true, -1, -1);
    }

    public List<Localidad> findLocalidadEntities(int maxResults, int firstResult) {
        return findLocalidadEntities(false, maxResults, firstResult);
    }

    private List<Localidad> findLocalidadEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidad.class));
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

    public Localidad findLocalidad(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidad.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidad> rt = cq.from(Localidad.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Localidad localidad){
        
        String query = "insert into localidad (id,provincia_id,nombre,codigopostal)"
                        + "values(?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, localidad.getId());
            ps.setInt(1, localidad.getProvinciaId().getId());
            ps.setString(2, localidad.getNombre());
            ps.setInt(3, localidad.getCodigopostal());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) localidad.setId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Localidad getById(int id){
        List<Localidad> lista = getByFiltro("id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Localidad> getAll(){
        return getByFiltro("1=1");
    }
    public List<Localidad> getByFiltro(String filtro){
        List<Localidad> lista = new ArrayList();
        String query = "select * from localidad where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Localidad localidad = new Localidad(
                        rs.getInt("id"),
                        rs.getInt("provincia_id"),
                        rs.getString("nombre"),
                        rs.getShort("codigopostal")
                ); 
                lista.add(localidad);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Localidad> getByProvinciaId(int provId){
        return getByFiltro("provincia_id ='"+provId+"'");
    }
    public List<Localidad> getByNombre(String nombre){
        return getByFiltro("nombre ='"+nombre+"'");
    }
    public List<Localidad> getByCodigo(int codigo){
        return getByFiltro("codigopostal ='"+codigo+"'");
    }
    public void remove(Localidad localidad){
        if(localidad != null ){
            String query = "delete from localidad where id ="+localidad.getId();
        try {
            conn.createStatement().execute(query);
            localidad = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Localidad localidad){
        String query = "update localidad set provincia_id = ?,nombre = ?,codigopostal = ?"
                + "where id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, localidad.getProvinciaId().getId());
            ps.setString(2, localidad.getNombre());
            ps.setInt(3, localidad.getCodigopostal());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public List<Localidad> getByDesc(ComboBox cbo){
        List<Localidad> lista = new ArrayList();
        String valor = cbo.getAccessibleText();
        String query = "select * from localidad ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Localidad localidad = new Localidad(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ); 
                lista.add(localidad);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Localidad> getByLocaIdOfProv(int id) {
        List<Localidad> lista = new ArrayList();
        if(id != 0){
            String query = "select nombre from localidad where provincia_id ="+id+" order by nombre";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                Localidad localidad = new Localidad(
                        rs.getString("nombre")
                    );
                    lista.add(localidad);
                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
                }
            }
            return lista;
        }
    
    public int getByCodigoPostal(String nombre, int provId) {
        int codPos = 0;
        if (!nombre.isEmpty()) {
            String query = "select codigopostal from localidad where nombre ='" + nombre + "' and provincia_id = " + provId;
            try {
                ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    Localidad localidad = new Localidad(
                            codPos = rs.getInt("codigopostal")
                    );

                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return codPos;
    }
    
    public int getIdLocalidad(String nombre) {
        int idLoc = 0;
        if (!nombre.isEmpty()) {
            String query = "select id from localidad where nombre ='" + nombre + "' ";
            try {
                ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    Localidad localidad = new Localidad(
                            idLoc = rs.getInt("id")
                    );

                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return idLoc;
    }
    public int getIdLocaByProvId(String nombre, Integer provId) {
        int idLoc = 0;
        if (!nombre.isEmpty()) {
            String query = "select id from localidad where nombre ='" + nombre + "' and provincia_id = " + provId ;
            try {
                ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    Localidad localidad = new Localidad(
                            idLoc = rs.getInt("id")
                    );

                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return idLoc;
    }
    
    public int getIndexLocakidad(String nombre, Integer provId, Integer cp) {
        int idLoc = 0;
        if (!nombre.isEmpty()) {
            String query = "select id from localidad where nombre ='" + nombre + "' and provincia_id = " + provId + " and codigopostal = " + cp;
            try {
                ResultSet rs = conn.createStatement().executeQuery(query);
                while (rs.next()) {
                    Localidad localidad = new Localidad(
                            idLoc = rs.getInt("id")
                    );

                }
                rs.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return idLoc;
    }
    
}
