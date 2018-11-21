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
import ar.com.gestion.entities.Empleado;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Cliente;
import ar.com.gestion.entities.Tdoc;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.event.EventType;
import javafx.scene.control.ComboBox;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class TdocJpaController implements Serializable {

    Connection conn;
    
    public TdocJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public TdocJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tdoc tdoc) {
        if (tdoc.getEmpleadoList() == null) {
            tdoc.setEmpleadoList(new ArrayList<Empleado>());
        }
        if (tdoc.getClienteList() == null) {
            tdoc.setClienteList(new ArrayList<Cliente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = tdoc.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getProveId());
                tdoc.setProveedor(proveedor);
            }
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : tdoc.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getEmpId());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            tdoc.setEmpleadoList(attachedEmpleadoList);
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : tdoc.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCliId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            tdoc.setClienteList(attachedClienteList);
            em.persist(tdoc);
            if (proveedor != null) {
                Tdoc oldTdocIdOfProveedor = proveedor.getTdocId();
                if (oldTdocIdOfProveedor != null) {
                    oldTdocIdOfProveedor.setProveedor(null);
                    oldTdocIdOfProveedor = em.merge(oldTdocIdOfProveedor);
                }
                proveedor.setTdocId(tdoc);
                proveedor = em.merge(proveedor);
            }
            for (Empleado empleadoListEmpleado : tdoc.getEmpleadoList()) {
                Tdoc oldTdocIdOfEmpleadoListEmpleado = empleadoListEmpleado.getTdocId();
                empleadoListEmpleado.setTdocId(tdoc);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldTdocIdOfEmpleadoListEmpleado != null) {
                    oldTdocIdOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldTdocIdOfEmpleadoListEmpleado = em.merge(oldTdocIdOfEmpleadoListEmpleado);
                }
            }
            for (Cliente clienteListCliente : tdoc.getClienteList()) {
                Tdoc oldTdocIdOfClienteListCliente = clienteListCliente.getTdocId();
                clienteListCliente.setTdocId(tdoc);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldTdocIdOfClienteListCliente != null) {
                    oldTdocIdOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldTdocIdOfClienteListCliente = em.merge(oldTdocIdOfClienteListCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tdoc tdoc) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tdoc persistentTdoc = em.find(Tdoc.class, tdoc.getTdocId());
            Proveedor proveedorOld = persistentTdoc.getProveedor();
            Proveedor proveedorNew = tdoc.getProveedor();
            List<Empleado> empleadoListOld = persistentTdoc.getEmpleadoList();
            List<Empleado> empleadoListNew = tdoc.getEmpleadoList();
            List<Cliente> clienteListOld = persistentTdoc.getClienteList();
            List<Cliente> clienteListNew = tdoc.getClienteList();
            List<String> illegalOrphanMessages = null;
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Proveedor " + proveedorOld + " since its tdocId field is not nullable.");
            }
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its tdocId field is not nullable.");
                }
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its tdocId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getProveId());
                tdoc.setProveedor(proveedorNew);
            }
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getEmpId());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            tdoc.setEmpleadoList(empleadoListNew);
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCliId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            tdoc.setClienteList(clienteListNew);
            tdoc = em.merge(tdoc);
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                Tdoc oldTdocIdOfProveedor = proveedorNew.getTdocId();
                if (oldTdocIdOfProveedor != null) {
                    oldTdocIdOfProveedor.setProveedor(null);
                    oldTdocIdOfProveedor = em.merge(oldTdocIdOfProveedor);
                }
                proveedorNew.setTdocId(tdoc);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Tdoc oldTdocIdOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getTdocId();
                    empleadoListNewEmpleado.setTdocId(tdoc);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldTdocIdOfEmpleadoListNewEmpleado != null && !oldTdocIdOfEmpleadoListNewEmpleado.equals(tdoc)) {
                        oldTdocIdOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldTdocIdOfEmpleadoListNewEmpleado = em.merge(oldTdocIdOfEmpleadoListNewEmpleado);
                    }
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Tdoc oldTdocIdOfClienteListNewCliente = clienteListNewCliente.getTdocId();
                    clienteListNewCliente.setTdocId(tdoc);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldTdocIdOfClienteListNewCliente != null && !oldTdocIdOfClienteListNewCliente.equals(tdoc)) {
                        oldTdocIdOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldTdocIdOfClienteListNewCliente = em.merge(oldTdocIdOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tdoc.getTdocId();
                if (findTdoc(id) == null) {
                    throw new NonexistentEntityException("The tdoc with id " + id + " no longer exists.");
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
            Tdoc tdoc;
            try {
                tdoc = em.getReference(Tdoc.class, id);
                tdoc.getTdocId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tdoc with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Proveedor proveedorOrphanCheck = tdoc.getProveedor();
            if (proveedorOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tdoc (" + tdoc + ") cannot be destroyed since the Proveedor " + proveedorOrphanCheck + " in its proveedor field has a non-nullable tdocId field.");
            }
            List<Empleado> empleadoListOrphanCheck = tdoc.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tdoc (" + tdoc + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable tdocId field.");
            }
            List<Cliente> clienteListOrphanCheck = tdoc.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tdoc (" + tdoc + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable tdocId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tdoc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tdoc> findTdocEntities() {
        return findTdocEntities(true, -1, -1);
    }

    public List<Tdoc> findTdocEntities(int maxResults, int firstResult) {
        return findTdocEntities(false, maxResults, firstResult);
    }

    private List<Tdoc> findTdocEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tdoc.class));
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

    public Tdoc findTdoc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tdoc.class, id);
        } finally {
            em.close();
        }
    }

    public int getTdocCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tdoc> rt = cq.from(Tdoc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Tdoc tdoc){
        String query = "insert into tdoc (tdoc_id,tdoc_desc)"
                        + "values(?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, tdoc.getTdocId());
            ps.setString(1, tdoc.getTdocDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) tdoc.setTdocId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Tdoc getById(int id){
        List<Tdoc> lista = getByFiltro("tdoc_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Tdoc> getAll(){
        return getByFiltro("1=1");
    }
    public List<Tdoc> getByFiltro(String filtro){
        List<Tdoc> lista = new ArrayList();
        String query = "select * from tdoc where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tdoc tdoc = new Tdoc(
                        rs.getInt("tdoc_id"),
                        rs.getString("tdoc_desc")
                ); 
                lista.add(tdoc);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Tdoc> getByNombre(String desc){
        return getByFiltro("tdoc_desc ='"+desc+"'");
    }
    public List<Tdoc> getLikeNombre(String desc){
        return getByFiltro("tdoc_desc like'%"+desc+"%'");
    }
    public void remove(Tdoc tdoc){
        if(tdoc != null ){
            String query = "delete from tdoc where tdoc_id ="+tdoc.getTdocId();
        try {
            conn.createStatement().execute(query);
            tdoc = null;
            } catch (Exception e) {
                System.out.println(e);
                }   
            }
    }
    public void update(Tdoc tdoc){
        String query = "update tdoc set tdoc_desc = ?"
                + "where tdoc_id = ? ";
        try {
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(2, tdoc.getTdocDesc());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public List<Tdoc> getByDesc(){
        List<Tdoc> lista = new ArrayList();
        String query = "select * from tdoc ";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tdoc tdoc = new Tdoc(
                        rs.getInt("tdoc_id"),
                        rs.getString("tdoc_desc")
                ); 
                lista.add(tdoc);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public int getByIdOfName(String nombre){
        int id = 0;
        String query = "select tdoc_id from tdoc where tdoc_desc = '" + nombre + "'";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Tdoc tdoc = new Tdoc(
                        rs.getInt("tdoc_id")
                        
                ); 
                id = tdoc.getTdocId();
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return id;
    }
}
