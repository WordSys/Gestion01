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
import ar.com.gestion.entities.Empleado;
import ar.com.gestion.entities.Users;
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
public class UsersJpaController implements Serializable {

    Connection conn;
    
    public UsersJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empId = users.getEmpId();
            if (empId != null) {
                empId = em.getReference(empId.getClass(), empId.getEmpId());
                users.setEmpId(empId);
            }
            em.persist(users);
            if (empId != null) {
                empId.getUsersList().add(users);
                empId = em.merge(empId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getUsersId());
            Empleado empIdOld = persistentUsers.getEmpId();
            Empleado empIdNew = users.getEmpId();
            if (empIdNew != null) {
                empIdNew = em.getReference(empIdNew.getClass(), empIdNew.getEmpId());
                users.setEmpId(empIdNew);
            }
            users = em.merge(users);
            if (empIdOld != null && !empIdOld.equals(empIdNew)) {
                empIdOld.getUsersList().remove(users);
                empIdOld = em.merge(empIdOld);
            }
            if (empIdNew != null && !empIdNew.equals(empIdOld)) {
                empIdNew.getUsersList().add(users);
                empIdNew = em.merge(empIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getUsersId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getUsersId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Empleado empId = users.getEmpId();
            if (empId != null) {
                empId.getUsersList().remove(users);
                empId = em.merge(empId);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Users users){
        String query = "insert into users (users_id,emp_id,users_user,users_pass)"
                        + "values(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, users.getUsersId());
            ps.setInt(1, users.getEmpId().getEmpId());
            ps.setString(1, users.getUsersUser());
            ps.setString(1, users.getUsersPass());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) users.setUsersId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Users getById(int id){
        List<Users> lista = getByFiltro("users_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Users> getAll(){
        return getByFiltro("1=1");
    }
    public List<Users> getByFiltro(String filtro){
        List<Users> lista = new ArrayList();
        String query = "select * from users where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Users users = new Users(
                        rs.getInt("users_id"),
                        rs.getInt("emp_id"),
                        rs.getString("users_user"),
                        rs.getString("users_pass")
                ); 
                lista.add(users);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Users> getByEmpleado(int emp_id){
        return getByFiltro("emp_id ='"+emp_id+"'");
    }
    public List<Users> getByUser(String user){
        return getByFiltro("users_user ='"+user+"'");
    }
    public void remove(Users users){
        if(users != null ){
            String query = "delete from users where users_id ="+users.getUsersId();
        try {
            conn.createStatement().execute(query);
            users = null;
            } catch (Exception e) {
                System.out.println(e);
                }   
            }
    }
    public void update(Users users){
        String query = "update users set emp_id = ?, users_user = ?, users_pass = ?"
                + "where users_id = ? ";
        try {
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, users.getEmpId().getEmpId());
            ps.setString(1, users.getUsersUser());
            ps.setString(1, users.getUsersPass());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
