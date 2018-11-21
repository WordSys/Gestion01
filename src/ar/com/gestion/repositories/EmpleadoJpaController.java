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
import ar.com.gestion.entities.Categoria;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Tdoc;
import ar.com.gestion.entities.Compra;
import ar.com.gestion.entities.Empleado;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.entities.Users;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import ar.com.gestion.repositories.exceptions.PreexistingEntityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class EmpleadoJpaController implements Serializable {
    
    Connection conn;
    
    public EmpleadoJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws IllegalOrphanException, PreexistingEntityException, Exception {
        if (empleado.getCompraList() == null) {
            empleado.setCompraList(new ArrayList<Compra>());
        }
        if (empleado.getVentaList() == null) {
            empleado.setVentaList(new ArrayList<Venta>());
        }
        if (empleado.getUsersList() == null) {
            empleado.setUsersList(new ArrayList<Users>());
        }
        List<String> illegalOrphanMessages = null;
        Categoria catIdOrphanCheck = empleado.getCatId();
        if (catIdOrphanCheck != null) {
            Empleado oldEmpleadoOfCatId = catIdOrphanCheck.getEmpleado();
            if (oldEmpleadoOfCatId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Categoria " + catIdOrphanCheck + " already has an item of type Empleado whose catId column cannot be null. Please make another selection for the catId field.");
            }
        }
        Direccion dirIdOrphanCheck = empleado.getDirId();
        if (dirIdOrphanCheck != null) {
            Empleado oldEmpleadoOfDirId = dirIdOrphanCheck.getEmpleado();
            if (oldEmpleadoOfDirId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Direccion " + dirIdOrphanCheck + " already has an item of type Empleado whose dirId column cannot be null. Please make another selection for the dirId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria catId = empleado.getCatId();
            if (catId != null) {
                catId = em.getReference(catId.getClass(), catId.getCatId());
                empleado.setCatId(catId);
            }
            Direccion dirId = empleado.getDirId();
            if (dirId != null) {
                dirId = em.getReference(dirId.getClass(), dirId.getDirId());
                empleado.setDirId(dirId);
            }
            Tdoc tdocId = empleado.getTdocId();
            if (tdocId != null) {
                tdocId = em.getReference(tdocId.getClass(), tdocId.getTdocId());
                empleado.setTdocId(tdocId);
            }
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : empleado.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getCompId());
                attachedCompraList.add(compraListCompraToAttach);
            }
            empleado.setCompraList(attachedCompraList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : empleado.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVtaId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            empleado.setVentaList(attachedVentaList);
            List<Users> attachedUsersList = new ArrayList<Users>();
            for (Users usersListUsersToAttach : empleado.getUsersList()) {
                usersListUsersToAttach = em.getReference(usersListUsersToAttach.getClass(), usersListUsersToAttach.getUsersId());
                attachedUsersList.add(usersListUsersToAttach);
            }
            empleado.setUsersList(attachedUsersList);
            em.persist(empleado);
            if (catId != null) {
                catId.setEmpleado(empleado);
                catId = em.merge(catId);
            }
            if (dirId != null) {
                dirId.setEmpleado(empleado);
                dirId = em.merge(dirId);
            }
            if (tdocId != null) {
                tdocId.getEmpleadoList().add(empleado);
                tdocId = em.merge(tdocId);
            }
            for (Compra compraListCompra : empleado.getCompraList()) {
                Empleado oldEmpIdOfCompraListCompra = compraListCompra.getEmpId();
                compraListCompra.setEmpId(empleado);
                compraListCompra = em.merge(compraListCompra);
                if (oldEmpIdOfCompraListCompra != null) {
                    oldEmpIdOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldEmpIdOfCompraListCompra = em.merge(oldEmpIdOfCompraListCompra);
                }
            }
            for (Venta ventaListVenta : empleado.getVentaList()) {
                Empleado oldEmpIdOfVentaListVenta = ventaListVenta.getEmpId();
                ventaListVenta.setEmpId(empleado);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldEmpIdOfVentaListVenta != null) {
                    oldEmpIdOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldEmpIdOfVentaListVenta = em.merge(oldEmpIdOfVentaListVenta);
                }
            }
            for (Users usersListUsers : empleado.getUsersList()) {
                Empleado oldEmpIdOfUsersListUsers = usersListUsers.getEmpId();
                usersListUsers.setEmpId(empleado);
                usersListUsers = em.merge(usersListUsers);
                if (oldEmpIdOfUsersListUsers != null) {
                    oldEmpIdOfUsersListUsers.getUsersList().remove(usersListUsers);
                    oldEmpIdOfUsersListUsers = em.merge(oldEmpIdOfUsersListUsers);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getEmpId()) != null) {
                throw new PreexistingEntityException("Empleado " + empleado + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getEmpId());
            Categoria catIdOld = persistentEmpleado.getCatId();
            Categoria catIdNew = empleado.getCatId();
            Direccion dirIdOld = persistentEmpleado.getDirId();
            Direccion dirIdNew = empleado.getDirId();
            Tdoc tdocIdOld = persistentEmpleado.getTdocId();
            Tdoc tdocIdNew = empleado.getTdocId();
            List<Compra> compraListOld = persistentEmpleado.getCompraList();
            List<Compra> compraListNew = empleado.getCompraList();
            List<Venta> ventaListOld = persistentEmpleado.getVentaList();
            List<Venta> ventaListNew = empleado.getVentaList();
            List<Users> usersListOld = persistentEmpleado.getUsersList();
            List<Users> usersListNew = empleado.getUsersList();
            List<String> illegalOrphanMessages = null;
            if (catIdNew != null && !catIdNew.equals(catIdOld)) {
                Empleado oldEmpleadoOfCatId = catIdNew.getEmpleado();
                if (oldEmpleadoOfCatId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Categoria " + catIdNew + " already has an item of type Empleado whose catId column cannot be null. Please make another selection for the catId field.");
                }
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                Empleado oldEmpleadoOfDirId = dirIdNew.getEmpleado();
                if (oldEmpleadoOfDirId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Direccion " + dirIdNew + " already has an item of type Empleado whose dirId column cannot be null. Please make another selection for the dirId field.");
                }
            }
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its empId field is not nullable.");
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its empId field is not nullable.");
                }
            }
            for (Users usersListOldUsers : usersListOld) {
                if (!usersListNew.contains(usersListOldUsers)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Users " + usersListOldUsers + " since its empId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (catIdNew != null) {
                catIdNew = em.getReference(catIdNew.getClass(), catIdNew.getCatId());
                empleado.setCatId(catIdNew);
            }
            if (dirIdNew != null) {
                dirIdNew = em.getReference(dirIdNew.getClass(), dirIdNew.getDirId());
                empleado.setDirId(dirIdNew);
            }
            if (tdocIdNew != null) {
                tdocIdNew = em.getReference(tdocIdNew.getClass(), tdocIdNew.getTdocId());
                empleado.setTdocId(tdocIdNew);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getCompId());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            empleado.setCompraList(compraListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVtaId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            empleado.setVentaList(ventaListNew);
            List<Users> attachedUsersListNew = new ArrayList<Users>();
            for (Users usersListNewUsersToAttach : usersListNew) {
                usersListNewUsersToAttach = em.getReference(usersListNewUsersToAttach.getClass(), usersListNewUsersToAttach.getUsersId());
                attachedUsersListNew.add(usersListNewUsersToAttach);
            }
            usersListNew = attachedUsersListNew;
            empleado.setUsersList(usersListNew);
            empleado = em.merge(empleado);
            if (catIdOld != null && !catIdOld.equals(catIdNew)) {
                catIdOld.setEmpleado(null);
                catIdOld = em.merge(catIdOld);
            }
            if (catIdNew != null && !catIdNew.equals(catIdOld)) {
                catIdNew.setEmpleado(empleado);
                catIdNew = em.merge(catIdNew);
            }
            if (dirIdOld != null && !dirIdOld.equals(dirIdNew)) {
                dirIdOld.setEmpleado(null);
                dirIdOld = em.merge(dirIdOld);
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                dirIdNew.setEmpleado(empleado);
                dirIdNew = em.merge(dirIdNew);
            }
            if (tdocIdOld != null && !tdocIdOld.equals(tdocIdNew)) {
                tdocIdOld.getEmpleadoList().remove(empleado);
                tdocIdOld = em.merge(tdocIdOld);
            }
            if (tdocIdNew != null && !tdocIdNew.equals(tdocIdOld)) {
                tdocIdNew.getEmpleadoList().add(empleado);
                tdocIdNew = em.merge(tdocIdNew);
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Empleado oldEmpIdOfCompraListNewCompra = compraListNewCompra.getEmpId();
                    compraListNewCompra.setEmpId(empleado);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldEmpIdOfCompraListNewCompra != null && !oldEmpIdOfCompraListNewCompra.equals(empleado)) {
                        oldEmpIdOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldEmpIdOfCompraListNewCompra = em.merge(oldEmpIdOfCompraListNewCompra);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Empleado oldEmpIdOfVentaListNewVenta = ventaListNewVenta.getEmpId();
                    ventaListNewVenta.setEmpId(empleado);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldEmpIdOfVentaListNewVenta != null && !oldEmpIdOfVentaListNewVenta.equals(empleado)) {
                        oldEmpIdOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldEmpIdOfVentaListNewVenta = em.merge(oldEmpIdOfVentaListNewVenta);
                    }
                }
            }
            for (Users usersListNewUsers : usersListNew) {
                if (!usersListOld.contains(usersListNewUsers)) {
                    Empleado oldEmpIdOfUsersListNewUsers = usersListNewUsers.getEmpId();
                    usersListNewUsers.setEmpId(empleado);
                    usersListNewUsers = em.merge(usersListNewUsers);
                    if (oldEmpIdOfUsersListNewUsers != null && !oldEmpIdOfUsersListNewUsers.equals(empleado)) {
                        oldEmpIdOfUsersListNewUsers.getUsersList().remove(usersListNewUsers);
                        oldEmpIdOfUsersListNewUsers = em.merge(oldEmpIdOfUsersListNewUsers);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getEmpId();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
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
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getEmpId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = empleado.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable empId field.");
            }
            List<Venta> ventaListOrphanCheck = empleado.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable empId field.");
            }
            List<Users> usersListOrphanCheck = empleado.getUsersList();
            for (Users usersListOrphanCheckUsers : usersListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Users " + usersListOrphanCheckUsers + " in its usersList field has a non-nullable empId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Categoria catId = empleado.getCatId();
            if (catId != null) {
                catId.setEmpleado(null);
                catId = em.merge(catId);
            }
            Direccion dirId = empleado.getDirId();
            if (dirId != null) {
                dirId.setEmpleado(null);
                dirId = em.merge(dirId);
            }
            Tdoc tdocId = empleado.getTdocId();
            if (tdocId != null) {
                tdocId.getEmpleadoList().remove(empleado);
                tdocId = em.merge(tdocId);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
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

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Empleado empleado){
        String query = "insert into empleado (emp_id,emp_nom,emp_ape,emp_fnac,tdoc_id,emp_ndoc,dir_id,emp_fing,emp_ecivil,emp_hijos,cat_id)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, empleado.getEmpId());
            ps.setString(1, empleado.getEmpNom());
            ps.setString(2, empleado.getEmpApe());
            ps.setDate(3, Date.valueOf(empleado.getEmpFnac().toString()));
            ps.setInt(4, empleado.getTdocId().getTdocId());
            ps.setInt(5, empleado.getEmpNdoc());
            ps.setInt(6, empleado.getDirId().getDirId());
            ps.setDate(7, Date.valueOf(empleado.getEmpFing().toString()));
            ps.setString(8, empleado.getEmpEcivil());
            ps.setInt(9, empleado.getEmpHijos());
            ps.setInt(10, empleado.getCatId().getCatId());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) empleado.setEmpId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Empleado getById(int id){
        List<Empleado> lista = getByFiltro("emp_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Empleado> getAll(){
        return getByFiltro("1=1");
    }
    public List<Empleado> getByFiltro(String filtro){
        List<Empleado> lista = new ArrayList();
        String query = "select * from empleado where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Empleado empleado = new Empleado(
                        rs.getInt("emp_id"),
                        rs.getString("emp_nom"),
                        rs.getString("emp_ape"),
                        rs.getDate("emp_fnac"),
                        rs.getInt("emp_ndoc"),
                        rs.getDate("emp_fing"),
                        rs.getString("emp_ecivil"),
                        rs.getInt("emp_hijos")
                ); 
                lista.add(empleado);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Empleado> getByNobre(String nombre){
        return getByFiltro("emp_nom ='"+nombre+"'");
    }
    public List<Empleado> getLikeApellido(String apellido){
        return getByFiltro("emp_ape like'%"+apellido+"%'");
    }
    public List<Empleado> getByNdoc(Empleado ndoc){
        return getByFiltro("emp_ndoc ='"+ndoc+"'");
    }
    public List<Empleado> getByDirId(Direccion dirId){
        return getByFiltro("dir_id ='"+dirId+"'");
    }
    public void remove(Empleado empleado){
        if(empleado != null ){
            String query = "delete from empleado where emp_id ="+empleado.getEmpId();
        try {
            conn.createStatement().execute(query);
            empleado = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Empleado empleado){
        String query = "update empleado set emp_nom = ?,emp_ape = ?, emp_fnac = ?, tdoc_id = ?, emp_ndoc = ?, dir_id = ?, emp_fing = ?,"
                + " emp_ecivil = ?, emp_hijos = ?, cat_id = ?"
                + "where cli_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, empleado.getEmpNom());
            ps.setString(2, empleado.getEmpApe());
            ps.setDate(3, Date.valueOf(empleado.getEmpFnac().toString()));
            ps.setInt(4, empleado.getTdocId().getTdocId());
            ps.setInt(5, empleado.getEmpNdoc());
            ps.setInt(6, empleado.getDirId().getDirId());
            ps.setDate(7, Date.valueOf(empleado.getEmpFing().toString()));
            ps.setString(8, empleado.getEmpEcivil());
            ps.setInt(9, empleado.getEmpHijos());
            ps.setInt(10, empleado.getCatId().getCatId());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
