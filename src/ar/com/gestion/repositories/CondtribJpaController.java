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
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.entities.Cliente;
import ar.com.gestion.entities.Condtrib;
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
public class CondtribJpaController implements Serializable {
    
    Connection conn;
    
    public CondtribJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public CondtribJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Condtrib condtrib) throws PreexistingEntityException, Exception {
        if (condtrib.getClienteList() == null) {
            condtrib.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = condtrib.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getProveId());
                condtrib.setProveedor(proveedor);
            }
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : condtrib.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCliId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            condtrib.setClienteList(attachedClienteList);
            em.persist(condtrib);
            if (proveedor != null) {
                Condtrib oldCondtribIdOfProveedor = proveedor.getCondtribId();
                if (oldCondtribIdOfProveedor != null) {
                    oldCondtribIdOfProveedor.setProveedor(null);
                    oldCondtribIdOfProveedor = em.merge(oldCondtribIdOfProveedor);
                }
                proveedor.setCondtribId(condtrib);
                proveedor = em.merge(proveedor);
            }
            for (Cliente clienteListCliente : condtrib.getClienteList()) {
                Condtrib oldCondtribIdOfClienteListCliente = clienteListCliente.getCondtribId();
                clienteListCliente.setCondtribId(condtrib);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldCondtribIdOfClienteListCliente != null) {
                    oldCondtribIdOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldCondtribIdOfClienteListCliente = em.merge(oldCondtribIdOfClienteListCliente);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCondtrib(condtrib.getCondtribId()) != null) {
                throw new PreexistingEntityException("Condtrib " + condtrib + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Condtrib condtrib) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Condtrib persistentCondtrib = em.find(Condtrib.class, condtrib.getCondtribId());
            Proveedor proveedorOld = persistentCondtrib.getProveedor();
            Proveedor proveedorNew = condtrib.getProveedor();
            List<Cliente> clienteListOld = persistentCondtrib.getClienteList();
            List<Cliente> clienteListNew = condtrib.getClienteList();
            List<String> illegalOrphanMessages = null;
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Proveedor " + proveedorOld + " since its condtribId field is not nullable.");
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its condtribId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getProveId());
                condtrib.setProveedor(proveedorNew);
            }
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCliId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            condtrib.setClienteList(clienteListNew);
            condtrib = em.merge(condtrib);
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                Condtrib oldCondtribIdOfProveedor = proveedorNew.getCondtribId();
                if (oldCondtribIdOfProveedor != null) {
                    oldCondtribIdOfProveedor.setProveedor(null);
                    oldCondtribIdOfProveedor = em.merge(oldCondtribIdOfProveedor);
                }
                proveedorNew.setCondtribId(condtrib);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Condtrib oldCondtribIdOfClienteListNewCliente = clienteListNewCliente.getCondtribId();
                    clienteListNewCliente.setCondtribId(condtrib);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldCondtribIdOfClienteListNewCliente != null && !oldCondtribIdOfClienteListNewCliente.equals(condtrib)) {
                        oldCondtribIdOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldCondtribIdOfClienteListNewCliente = em.merge(oldCondtribIdOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = condtrib.getCondtribId();
                if (findCondtrib(id) == null) {
                    throw new NonexistentEntityException("The condtrib with id " + id + " no longer exists.");
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
            Condtrib condtrib;
            try {
                condtrib = em.getReference(Condtrib.class, id);
                condtrib.getCondtribId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The condtrib with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Proveedor proveedorOrphanCheck = condtrib.getProveedor();
            if (proveedorOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Condtrib (" + condtrib + ") cannot be destroyed since the Proveedor " + proveedorOrphanCheck + " in its proveedor field has a non-nullable condtribId field.");
            }
            List<Cliente> clienteListOrphanCheck = condtrib.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Condtrib (" + condtrib + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable condtribId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(condtrib);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Condtrib> findCondtribEntities() {
        return findCondtribEntities(true, -1, -1);
    }

    public List<Condtrib> findCondtribEntities(int maxResults, int firstResult) {
        return findCondtribEntities(false, maxResults, firstResult);
    }

    private List<Condtrib> findCondtribEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Condtrib.class));
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

    public Condtrib findCondtrib(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Condtrib.class, id);
        } finally {
            em.close();
        }
    }

    public int getCondtribCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Condtrib> rt = cq.from(Condtrib.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Condtrib condtrib){
        
        String query = "insert into condtrib ( condtrib_id,condtrib_desc)"
                        + "values(?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, condtrib.getCondtribId());
            ps.setString(1, condtrib.getCondtribDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) condtrib.setCondtribId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Condtrib getById(int id){
        List<Condtrib> lista = getByFiltro("condtrib_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Condtrib> getAll(){
        return getByFiltro("1=1");
    }
    public List<Condtrib> getByFiltro(String filtro){
        List<Condtrib> lista = new ArrayList();
        String query = "select * from condtrib where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Condtrib condtrib = new Condtrib(
                        rs.getInt("condtrib_id"),
                        rs.getString("condtrib_desc")
                ); 
                lista.add(condtrib);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Condtrib> getByCondtrib(String desc){
        return getByFiltro("condtrib_desc ='"+desc+"'");
    }
    
    
    public void remove(Condtrib condtrib){
        if(condtrib != null ){
            String query = "delete from condtrib where condtrib_id ="+condtrib.getCondtribId();
        try {
            conn.createStatement().execute(query);
            condtrib = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Condtrib condtrib){
        String query = "update condtrib set condtrib_id = ?"
                + "where condtrib_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, condtrib.getCondtribDesc());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public List<Condtrib> getByDesc(){
        List<Condtrib> lista = new ArrayList();
        String query = "select * from condtrib ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Condtrib condtrib = new Condtrib(
                        rs.getInt("condtrib_id"),
                        rs.getString("condtrib_desc")
                ); 
                lista.add(condtrib);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public int getByIdOfName(String nombre){
        int id = 0;
        String query = "select condtrib_id from condtrib where condtrib_desc = '" + nombre + "'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Condtrib condTrib = new Condtrib(
                        rs.getInt("condtrib_id")
                        
                ); 
                id = condTrib.getCondtribId();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }
    
}
