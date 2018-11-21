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
import ar.com.gestion.entities.Localidad;
import ar.com.gestion.entities.Provincia;
import ar.com.gestion.entities.Proveedor;
import ar.com.gestion.entities.Libretacontacto;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Cliente;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class DireccionJpaController implements Serializable {

    Connection conn;
    
    public DireccionJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public DireccionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Direccion direccion) throws IllegalOrphanException {
        if (direccion.getLibretacontactoList() == null) {
            direccion.setLibretacontactoList(new ArrayList<Libretacontacto>());
        }
        if (direccion.getClienteList() == null) {
            direccion.setClienteList(new ArrayList<Cliente>());
        }
        List<String> illegalOrphanMessages = null;
        Localidad dirCpOrphanCheck = direccion.getDirCp();
        if (dirCpOrphanCheck != null) {
            Direccion oldDireccionOfDirCp = dirCpOrphanCheck.getDireccion();
            if (oldDireccionOfDirCp != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Localidad " + dirCpOrphanCheck + " already has an item of type Direccion whose dirCp column cannot be null. Please make another selection for the dirCp field.");
            }
        }
        Localidad localidadIdOrphanCheck = direccion.getLocalidadId();
        if (localidadIdOrphanCheck != null) {
            Direccion oldDireccionOfLocalidadId = localidadIdOrphanCheck.getDireccion();
            if (oldDireccionOfLocalidadId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Localidad " + localidadIdOrphanCheck + " already has an item of type Direccion whose localidadId column cannot be null. Please make another selection for the localidadId field.");
            }
        }
        Provincia provinciaIdOrphanCheck = direccion.getProvinciaId();
        if (provinciaIdOrphanCheck != null) {
            Direccion oldDireccionOfProvinciaId = provinciaIdOrphanCheck.getDireccion();
            if (oldDireccionOfProvinciaId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Provincia " + provinciaIdOrphanCheck + " already has an item of type Direccion whose provinciaId column cannot be null. Please make another selection for the provinciaId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado = direccion.getEmpleado();
            if (empleado != null) {
                empleado = em.getReference(empleado.getClass(), empleado.getEmpId());
                direccion.setEmpleado(empleado);
            }
            Localidad dirCp = direccion.getDirCp();
            if (dirCp != null) {
                dirCp = em.getReference(dirCp.getClass(), dirCp.getId());
                direccion.setDirCp(dirCp);
            }
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId = em.getReference(localidadId.getClass(), localidadId.getId());
                direccion.setLocalidadId(localidadId);
            }
            Provincia provinciaId = direccion.getProvinciaId();
            if (provinciaId != null) {
                provinciaId = em.getReference(provinciaId.getClass(), provinciaId.getId());
                direccion.setProvinciaId(provinciaId);
            }
            Proveedor proveedor = direccion.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getProveId());
                direccion.setProveedor(proveedor);
            }
            List<Libretacontacto> attachedLibretacontactoList = new ArrayList<Libretacontacto>();
            for (Libretacontacto libretacontactoListLibretacontactoToAttach : direccion.getLibretacontactoList()) {
                libretacontactoListLibretacontactoToAttach = em.getReference(libretacontactoListLibretacontactoToAttach.getClass(), libretacontactoListLibretacontactoToAttach.getLibId());
                attachedLibretacontactoList.add(libretacontactoListLibretacontactoToAttach);
            }
            direccion.setLibretacontactoList(attachedLibretacontactoList);
            List<Cliente> attachedClienteList = new ArrayList<Cliente>();
            for (Cliente clienteListClienteToAttach : direccion.getClienteList()) {
                clienteListClienteToAttach = em.getReference(clienteListClienteToAttach.getClass(), clienteListClienteToAttach.getCliId());
                attachedClienteList.add(clienteListClienteToAttach);
            }
            direccion.setClienteList(attachedClienteList);
            em.persist(direccion);
            if (empleado != null) {
                Direccion oldDirIdOfEmpleado = empleado.getDirId();
                if (oldDirIdOfEmpleado != null) {
                    oldDirIdOfEmpleado.setEmpleado(null);
                    oldDirIdOfEmpleado = em.merge(oldDirIdOfEmpleado);
                }
                empleado.setDirId(direccion);
                empleado = em.merge(empleado);
            }
            if (dirCp != null) {
                dirCp.setDireccion(direccion);
                dirCp = em.merge(dirCp);
            }
            if (localidadId != null) {
                localidadId.setDireccion(direccion);
                localidadId = em.merge(localidadId);
            }
            if (provinciaId != null) {
                provinciaId.setDireccion(direccion);
                provinciaId = em.merge(provinciaId);
            }
            if (proveedor != null) {
                Direccion oldDirIdOfProveedor = proveedor.getDirId();
                if (oldDirIdOfProveedor != null) {
                    oldDirIdOfProveedor.setProveedor(null);
                    oldDirIdOfProveedor = em.merge(oldDirIdOfProveedor);
                }
                proveedor.setDirId(direccion);
                proveedor = em.merge(proveedor);
            }
            for (Libretacontacto libretacontactoListLibretacontacto : direccion.getLibretacontactoList()) {
                Direccion oldDirIdOfLibretacontactoListLibretacontacto = libretacontactoListLibretacontacto.getDirId();
                libretacontactoListLibretacontacto.setDirId(direccion);
                libretacontactoListLibretacontacto = em.merge(libretacontactoListLibretacontacto);
                if (oldDirIdOfLibretacontactoListLibretacontacto != null) {
                    oldDirIdOfLibretacontactoListLibretacontacto.getLibretacontactoList().remove(libretacontactoListLibretacontacto);
                    oldDirIdOfLibretacontactoListLibretacontacto = em.merge(oldDirIdOfLibretacontactoListLibretacontacto);
                }
            }
            for (Cliente clienteListCliente : direccion.getClienteList()) {
                Direccion oldDirIdOfClienteListCliente = clienteListCliente.getDirId();
                clienteListCliente.setDirId(direccion);
                clienteListCliente = em.merge(clienteListCliente);
                if (oldDirIdOfClienteListCliente != null) {
                    oldDirIdOfClienteListCliente.getClienteList().remove(clienteListCliente);
                    oldDirIdOfClienteListCliente = em.merge(oldDirIdOfClienteListCliente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Direccion direccion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Direccion persistentDireccion = em.find(Direccion.class, direccion.getDirId());
            Empleado empleadoOld = persistentDireccion.getEmpleado();
            Empleado empleadoNew = direccion.getEmpleado();
            Localidad dirCpOld = persistentDireccion.getDirCp();
            Localidad dirCpNew = direccion.getDirCp();
            Localidad localidadIdOld = persistentDireccion.getLocalidadId();
            Localidad localidadIdNew = direccion.getLocalidadId();
            Provincia provinciaIdOld = persistentDireccion.getProvinciaId();
            Provincia provinciaIdNew = direccion.getProvinciaId();
            Proveedor proveedorOld = persistentDireccion.getProveedor();
            Proveedor proveedorNew = direccion.getProveedor();
            List<Libretacontacto> libretacontactoListOld = persistentDireccion.getLibretacontactoList();
            List<Libretacontacto> libretacontactoListNew = direccion.getLibretacontactoList();
            List<Cliente> clienteListOld = persistentDireccion.getClienteList();
            List<Cliente> clienteListNew = direccion.getClienteList();
            List<String> illegalOrphanMessages = null;
            if (empleadoOld != null && !empleadoOld.equals(empleadoNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Empleado " + empleadoOld + " since its dirId field is not nullable.");
            }
            if (dirCpNew != null && !dirCpNew.equals(dirCpOld)) {
                Direccion oldDireccionOfDirCp = dirCpNew.getDireccion();
                if (oldDireccionOfDirCp != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Localidad " + dirCpNew + " already has an item of type Direccion whose dirCp column cannot be null. Please make another selection for the dirCp field.");
                }
            }
            if (localidadIdNew != null && !localidadIdNew.equals(localidadIdOld)) {
                Direccion oldDireccionOfLocalidadId = localidadIdNew.getDireccion();
                if (oldDireccionOfLocalidadId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Localidad " + localidadIdNew + " already has an item of type Direccion whose localidadId column cannot be null. Please make another selection for the localidadId field.");
                }
            }
            if (provinciaIdNew != null && !provinciaIdNew.equals(provinciaIdOld)) {
                Direccion oldDireccionOfProvinciaId = provinciaIdNew.getDireccion();
                if (oldDireccionOfProvinciaId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Provincia " + provinciaIdNew + " already has an item of type Direccion whose provinciaId column cannot be null. Please make another selection for the provinciaId field.");
                }
            }
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain Proveedor " + proveedorOld + " since its dirId field is not nullable.");
            }
            for (Libretacontacto libretacontactoListOldLibretacontacto : libretacontactoListOld) {
                if (!libretacontactoListNew.contains(libretacontactoListOldLibretacontacto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Libretacontacto " + libretacontactoListOldLibretacontacto + " since its dirId field is not nullable.");
                }
            }
            for (Cliente clienteListOldCliente : clienteListOld) {
                if (!clienteListNew.contains(clienteListOldCliente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cliente " + clienteListOldCliente + " since its dirId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (empleadoNew != null) {
                empleadoNew = em.getReference(empleadoNew.getClass(), empleadoNew.getEmpId());
                direccion.setEmpleado(empleadoNew);
            }
            if (dirCpNew != null) {
                dirCpNew = em.getReference(dirCpNew.getClass(), dirCpNew.getId());
                direccion.setDirCp(dirCpNew);
            }
            if (localidadIdNew != null) {
                localidadIdNew = em.getReference(localidadIdNew.getClass(), localidadIdNew.getId());
                direccion.setLocalidadId(localidadIdNew);
            }
            if (provinciaIdNew != null) {
                provinciaIdNew = em.getReference(provinciaIdNew.getClass(), provinciaIdNew.getId());
                direccion.setProvinciaId(provinciaIdNew);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getProveId());
                direccion.setProveedor(proveedorNew);
            }
            List<Libretacontacto> attachedLibretacontactoListNew = new ArrayList<Libretacontacto>();
            for (Libretacontacto libretacontactoListNewLibretacontactoToAttach : libretacontactoListNew) {
                libretacontactoListNewLibretacontactoToAttach = em.getReference(libretacontactoListNewLibretacontactoToAttach.getClass(), libretacontactoListNewLibretacontactoToAttach.getLibId());
                attachedLibretacontactoListNew.add(libretacontactoListNewLibretacontactoToAttach);
            }
            libretacontactoListNew = attachedLibretacontactoListNew;
            direccion.setLibretacontactoList(libretacontactoListNew);
            List<Cliente> attachedClienteListNew = new ArrayList<Cliente>();
            for (Cliente clienteListNewClienteToAttach : clienteListNew) {
                clienteListNewClienteToAttach = em.getReference(clienteListNewClienteToAttach.getClass(), clienteListNewClienteToAttach.getCliId());
                attachedClienteListNew.add(clienteListNewClienteToAttach);
            }
            clienteListNew = attachedClienteListNew;
            direccion.setClienteList(clienteListNew);
            direccion = em.merge(direccion);
            if (empleadoNew != null && !empleadoNew.equals(empleadoOld)) {
                Direccion oldDirIdOfEmpleado = empleadoNew.getDirId();
                if (oldDirIdOfEmpleado != null) {
                    oldDirIdOfEmpleado.setEmpleado(null);
                    oldDirIdOfEmpleado = em.merge(oldDirIdOfEmpleado);
                }
                empleadoNew.setDirId(direccion);
                empleadoNew = em.merge(empleadoNew);
            }
            if (dirCpOld != null && !dirCpOld.equals(dirCpNew)) {
                dirCpOld.setDireccion(null);
                dirCpOld = em.merge(dirCpOld);
            }
            if (dirCpNew != null && !dirCpNew.equals(dirCpOld)) {
                dirCpNew.setDireccion(direccion);
                dirCpNew = em.merge(dirCpNew);
            }
            if (localidadIdOld != null && !localidadIdOld.equals(localidadIdNew)) {
                localidadIdOld.setDireccion(null);
                localidadIdOld = em.merge(localidadIdOld);
            }
            if (localidadIdNew != null && !localidadIdNew.equals(localidadIdOld)) {
                localidadIdNew.setDireccion(direccion);
                localidadIdNew = em.merge(localidadIdNew);
            }
            if (provinciaIdOld != null && !provinciaIdOld.equals(provinciaIdNew)) {
                provinciaIdOld.setDireccion(null);
                provinciaIdOld = em.merge(provinciaIdOld);
            }
            if (provinciaIdNew != null && !provinciaIdNew.equals(provinciaIdOld)) {
                provinciaIdNew.setDireccion(direccion);
                provinciaIdNew = em.merge(provinciaIdNew);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                Direccion oldDirIdOfProveedor = proveedorNew.getDirId();
                if (oldDirIdOfProveedor != null) {
                    oldDirIdOfProveedor.setProveedor(null);
                    oldDirIdOfProveedor = em.merge(oldDirIdOfProveedor);
                }
                proveedorNew.setDirId(direccion);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Libretacontacto libretacontactoListNewLibretacontacto : libretacontactoListNew) {
                if (!libretacontactoListOld.contains(libretacontactoListNewLibretacontacto)) {
                    Direccion oldDirIdOfLibretacontactoListNewLibretacontacto = libretacontactoListNewLibretacontacto.getDirId();
                    libretacontactoListNewLibretacontacto.setDirId(direccion);
                    libretacontactoListNewLibretacontacto = em.merge(libretacontactoListNewLibretacontacto);
                    if (oldDirIdOfLibretacontactoListNewLibretacontacto != null && !oldDirIdOfLibretacontactoListNewLibretacontacto.equals(direccion)) {
                        oldDirIdOfLibretacontactoListNewLibretacontacto.getLibretacontactoList().remove(libretacontactoListNewLibretacontacto);
                        oldDirIdOfLibretacontactoListNewLibretacontacto = em.merge(oldDirIdOfLibretacontactoListNewLibretacontacto);
                    }
                }
            }
            for (Cliente clienteListNewCliente : clienteListNew) {
                if (!clienteListOld.contains(clienteListNewCliente)) {
                    Direccion oldDirIdOfClienteListNewCliente = clienteListNewCliente.getDirId();
                    clienteListNewCliente.setDirId(direccion);
                    clienteListNewCliente = em.merge(clienteListNewCliente);
                    if (oldDirIdOfClienteListNewCliente != null && !oldDirIdOfClienteListNewCliente.equals(direccion)) {
                        oldDirIdOfClienteListNewCliente.getClienteList().remove(clienteListNewCliente);
                        oldDirIdOfClienteListNewCliente = em.merge(oldDirIdOfClienteListNewCliente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = direccion.getDirId();
                if (findDireccion(id) == null) {
                    throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.");
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
            Direccion direccion;
            try {
                direccion = em.getReference(Direccion.class, id);
                direccion.getDirId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direccion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Empleado empleadoOrphanCheck = direccion.getEmpleado();
            if (empleadoOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Empleado " + empleadoOrphanCheck + " in its empleado field has a non-nullable dirId field.");
            }
            Proveedor proveedorOrphanCheck = direccion.getProveedor();
            if (proveedorOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Proveedor " + proveedorOrphanCheck + " in its proveedor field has a non-nullable dirId field.");
            }
            List<Libretacontacto> libretacontactoListOrphanCheck = direccion.getLibretacontactoList();
            for (Libretacontacto libretacontactoListOrphanCheckLibretacontacto : libretacontactoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Libretacontacto " + libretacontactoListOrphanCheckLibretacontacto + " in its libretacontactoList field has a non-nullable dirId field.");
            }
            List<Cliente> clienteListOrphanCheck = direccion.getClienteList();
            for (Cliente clienteListOrphanCheckCliente : clienteListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direccion (" + direccion + ") cannot be destroyed since the Cliente " + clienteListOrphanCheckCliente + " in its clienteList field has a non-nullable dirId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Localidad dirCp = direccion.getDirCp();
            if (dirCp != null) {
                dirCp.setDireccion(null);
                dirCp = em.merge(dirCp);
            }
            Localidad localidadId = direccion.getLocalidadId();
            if (localidadId != null) {
                localidadId.setDireccion(null);
                localidadId = em.merge(localidadId);
            }
            Provincia provinciaId = direccion.getProvinciaId();
            if (provinciaId != null) {
                provinciaId.setDireccion(null);
                provinciaId = em.merge(provinciaId);
            }
            em.remove(direccion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Direccion> findDireccionEntities() {
        return findDireccionEntities(true, -1, -1);
    }

    public List<Direccion> findDireccionEntities(int maxResults, int firstResult) {
        return findDireccionEntities(false, maxResults, firstResult);
    }

    private List<Direccion> findDireccionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direccion.class));
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

    public Direccion findDireccion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direccion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direccion> rt = cq.from(Direccion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Direccion direccion){
        
        String query = "insert into direccion (dir_calle,dir_nro,dir_cruce1,dir_cruce2,dir_piso,dir_dpto,dir_cp,localidad_id,provincia_id)"
                        + "values(?,?,?,?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            //ps.setInt(0, direccion.getDirId());
            ps.setString(1, direccion.getDirCalle());
            ps.setInt(2, direccion.getDirNro());
            ps.setString(3, direccion.getDirCruce1());
            ps.setString(4, direccion.getDirCruce2());
            ps.setInt(5, direccion.getDirPiso());
            ps.setString(6, direccion.getDirDpto());
            ps.setShort(7, direccion.getDir_CpShort());
            ps.setInt(8, direccion.getLocalidad_Id());
            ps.setShort(9, direccion.getProvincia_IdShort());
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) direccion.setDirId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Direccion getById(int id){
        List<Direccion> lista = getByFiltro("dir_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Direccion> getAll(){
        return getByFiltro("1=1");
    }
    public List<Direccion> getByFiltro(String filtro){
        List<Direccion> lista = new ArrayList();
        String query = "select * from direccion where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Direccion direccion = new Direccion(
//                        rs.getInt("dir_id"),
                        rs.getString("dir_calle"),
                        rs.getInt("dir_nro"),
                        rs.getString("dir_cruce1"),
                        rs.getString("dir_cruce2"),
                        rs.getInt("dir_piso"),
                        rs.getString("dir_dpto"),
                        rs.getShort("dir_cp"),
                        rs.getInt("localidad_id"),
                        rs.getShort("provincia_id")
                ); 
                lista.add(direccion);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Direccion> getByCodPos(int codPos){
        return getByFiltro("dir_cp ='"+codPos+"'");
    }
    
    public List<Direccion> getByLocalidad(int localidad){
        return getByFiltro("localidad_id ='"+localidad+"'");
    }
    
    public List<Direccion> getByProvincia(int provincia){
        return getByFiltro("provincia_id ='"+provincia+"'");
    }
    
    public void remove(Direccion direccion){
        if(direccion != null ){
            String query = "delete from direccion where dir_id ="+direccion.getDirId();
        try {
            conn.createStatement().execute(query);
            direccion = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Direccion direccion, int id){
        String query = "update direccion set dir_calle = ?, dir_nro = ?, dir_cruce1 = ?, dir_cruce2 = ?, dir_piso = ?, dir_dpto = ?, dir_cp = ?, localidad_id = ?, provincia_id = ?"
                + " where dir_id = " + id;
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, direccion.getDirCalle());
            ps.setInt(2, direccion.getDirNro());
            ps.setString(3, direccion.getDirCruce1());
            ps.setString(4, direccion.getDirCruce2());
            ps.setInt(5, direccion.getDirPiso());
            ps.setString(6, direccion.getDirDpto());
            ps.setShort(7, direccion.getDir_CpShort());
            ps.setInt(8, direccion.getLocalidad_Id());
            ps.setShort(9, direccion.getProvincia_IdShort());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int getUltimoId() {
        int ultimoId = 0;
        String query = "SELECT dir_id, max(dir_id) FROM direccion";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                ultimoId = rs.getInt("dir_id");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ultimoId;
    }
    public int getDirIdByProve(int id) {
        int idDir = 0;
        String query = "select dir_id from proveedor where prove_id = " + id;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while (rs.next()) {
                idDir = rs.getInt("dir_id");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return idDir;
    }
}
