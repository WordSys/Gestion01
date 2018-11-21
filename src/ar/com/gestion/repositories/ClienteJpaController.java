/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Cliente;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Condtrib;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Tdoc;
import ar.com.gestion.entities.Ctacobrar;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.entities.Cobranzas;
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
public class ClienteJpaController implements Serializable {
    
    Connection conn;
    
    public ClienteJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public ClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cliente cliente) throws PreexistingEntityException, Exception {
        if (cliente.getCtacobrarList() == null) {
            cliente.setCtacobrarList(new ArrayList<Ctacobrar>());
        }
        if (cliente.getVentaList() == null) {
            cliente.setVentaList(new ArrayList<Venta>());
        }
        if (cliente.getCobranzasList() == null) {
            cliente.setCobranzasList(new ArrayList<Cobranzas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Condtrib condtribId = cliente.getCondtribId();
            if (condtribId != null) {
                condtribId = em.getReference(condtribId.getClass(), condtribId.getCondtribId());
                cliente.setCondtribId(condtribId);
            }
            Direccion dirId = cliente.getDirId();
            if (dirId != null) {
                dirId = em.getReference(dirId.getClass(), dirId.getDirId());
                cliente.setDirId(dirId);
            }
            Tdoc tdocId = cliente.getTdocId();
            if (tdocId != null) {
                tdocId = em.getReference(tdocId.getClass(), tdocId.getTdocId());
                cliente.setTdocId(tdocId);
            }
            List<Ctacobrar> attachedCtacobrarList = new ArrayList<Ctacobrar>();
            for (Ctacobrar ctacobrarListCtacobrarToAttach : cliente.getCtacobrarList()) {
                ctacobrarListCtacobrarToAttach = em.getReference(ctacobrarListCtacobrarToAttach.getClass(), ctacobrarListCtacobrarToAttach.getCcId());
                attachedCtacobrarList.add(ctacobrarListCtacobrarToAttach);
            }
            cliente.setCtacobrarList(attachedCtacobrarList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : cliente.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getVtaId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            cliente.setVentaList(attachedVentaList);
            List<Cobranzas> attachedCobranzasList = new ArrayList<Cobranzas>();
            for (Cobranzas cobranzasListCobranzasToAttach : cliente.getCobranzasList()) {
                cobranzasListCobranzasToAttach = em.getReference(cobranzasListCobranzasToAttach.getClass(), cobranzasListCobranzasToAttach.getCobId());
                attachedCobranzasList.add(cobranzasListCobranzasToAttach);
            }
            cliente.setCobranzasList(attachedCobranzasList);
            em.persist(cliente);
            if (condtribId != null) {
                condtribId.getClienteList().add(cliente);
                condtribId = em.merge(condtribId);
            }
            if (dirId != null) {
                dirId.getClienteList().add(cliente);
                dirId = em.merge(dirId);
            }
            if (tdocId != null) {
                tdocId.getClienteList().add(cliente);
                tdocId = em.merge(tdocId);
            }
            for (Ctacobrar ctacobrarListCtacobrar : cliente.getCtacobrarList()) {
                Cliente oldCliIdOfCtacobrarListCtacobrar = ctacobrarListCtacobrar.getCliId();
                ctacobrarListCtacobrar.setCliId(cliente);
                ctacobrarListCtacobrar = em.merge(ctacobrarListCtacobrar);
                if (oldCliIdOfCtacobrarListCtacobrar != null) {
                    oldCliIdOfCtacobrarListCtacobrar.getCtacobrarList().remove(ctacobrarListCtacobrar);
                    oldCliIdOfCtacobrarListCtacobrar = em.merge(oldCliIdOfCtacobrarListCtacobrar);
                }
            }
            for (Venta ventaListVenta : cliente.getVentaList()) {
                Cliente oldCliIdOfVentaListVenta = ventaListVenta.getCliId();
                ventaListVenta.setCliId(cliente);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldCliIdOfVentaListVenta != null) {
                    oldCliIdOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldCliIdOfVentaListVenta = em.merge(oldCliIdOfVentaListVenta);
                }
            }
            for (Cobranzas cobranzasListCobranzas : cliente.getCobranzasList()) {
                Cliente oldCliIdOfCobranzasListCobranzas = cobranzasListCobranzas.getCliId();
                cobranzasListCobranzas.setCliId(cliente);
                cobranzasListCobranzas = em.merge(cobranzasListCobranzas);
                if (oldCliIdOfCobranzasListCobranzas != null) {
                    oldCliIdOfCobranzasListCobranzas.getCobranzasList().remove(cobranzasListCobranzas);
                    oldCliIdOfCobranzasListCobranzas = em.merge(oldCliIdOfCobranzasListCobranzas);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCliente(cliente.getCliId()) != null) {
                throw new PreexistingEntityException("Cliente " + cliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cliente cliente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente persistentCliente = em.find(Cliente.class, cliente.getCliId());
            Condtrib condtribIdOld = persistentCliente.getCondtribId();
            Condtrib condtribIdNew = cliente.getCondtribId();
            Direccion dirIdOld = persistentCliente.getDirId();
            Direccion dirIdNew = cliente.getDirId();
            Tdoc tdocIdOld = persistentCliente.getTdocId();
            Tdoc tdocIdNew = cliente.getTdocId();
            List<Ctacobrar> ctacobrarListOld = persistentCliente.getCtacobrarList();
            List<Ctacobrar> ctacobrarListNew = cliente.getCtacobrarList();
            List<Venta> ventaListOld = persistentCliente.getVentaList();
            List<Venta> ventaListNew = cliente.getVentaList();
            List<Cobranzas> cobranzasListOld = persistentCliente.getCobranzasList();
            List<Cobranzas> cobranzasListNew = cliente.getCobranzasList();
            List<String> illegalOrphanMessages = null;
            for (Ctacobrar ctacobrarListOldCtacobrar : ctacobrarListOld) {
                if (!ctacobrarListNew.contains(ctacobrarListOldCtacobrar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ctacobrar " + ctacobrarListOldCtacobrar + " since its cliId field is not nullable.");
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Venta " + ventaListOldVenta + " since its cliId field is not nullable.");
                }
            }
            for (Cobranzas cobranzasListOldCobranzas : cobranzasListOld) {
                if (!cobranzasListNew.contains(cobranzasListOldCobranzas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cobranzas " + cobranzasListOldCobranzas + " since its cliId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (condtribIdNew != null) {
                condtribIdNew = em.getReference(condtribIdNew.getClass(), condtribIdNew.getCondtribId());
                cliente.setCondtribId(condtribIdNew);
            }
            if (dirIdNew != null) {
                dirIdNew = em.getReference(dirIdNew.getClass(), dirIdNew.getDirId());
                cliente.setDirId(dirIdNew);
            }
            if (tdocIdNew != null) {
                tdocIdNew = em.getReference(tdocIdNew.getClass(), tdocIdNew.getTdocId());
                cliente.setTdocId(tdocIdNew);
            }
            List<Ctacobrar> attachedCtacobrarListNew = new ArrayList<Ctacobrar>();
            for (Ctacobrar ctacobrarListNewCtacobrarToAttach : ctacobrarListNew) {
                ctacobrarListNewCtacobrarToAttach = em.getReference(ctacobrarListNewCtacobrarToAttach.getClass(), ctacobrarListNewCtacobrarToAttach.getCcId());
                attachedCtacobrarListNew.add(ctacobrarListNewCtacobrarToAttach);
            }
            ctacobrarListNew = attachedCtacobrarListNew;
            cliente.setCtacobrarList(ctacobrarListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getVtaId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            cliente.setVentaList(ventaListNew);
            List<Cobranzas> attachedCobranzasListNew = new ArrayList<Cobranzas>();
            for (Cobranzas cobranzasListNewCobranzasToAttach : cobranzasListNew) {
                cobranzasListNewCobranzasToAttach = em.getReference(cobranzasListNewCobranzasToAttach.getClass(), cobranzasListNewCobranzasToAttach.getCobId());
                attachedCobranzasListNew.add(cobranzasListNewCobranzasToAttach);
            }
            cobranzasListNew = attachedCobranzasListNew;
            cliente.setCobranzasList(cobranzasListNew);
            cliente = em.merge(cliente);
            if (condtribIdOld != null && !condtribIdOld.equals(condtribIdNew)) {
                condtribIdOld.getClienteList().remove(cliente);
                condtribIdOld = em.merge(condtribIdOld);
            }
            if (condtribIdNew != null && !condtribIdNew.equals(condtribIdOld)) {
                condtribIdNew.getClienteList().add(cliente);
                condtribIdNew = em.merge(condtribIdNew);
            }
            if (dirIdOld != null && !dirIdOld.equals(dirIdNew)) {
                dirIdOld.getClienteList().remove(cliente);
                dirIdOld = em.merge(dirIdOld);
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                dirIdNew.getClienteList().add(cliente);
                dirIdNew = em.merge(dirIdNew);
            }
            if (tdocIdOld != null && !tdocIdOld.equals(tdocIdNew)) {
                tdocIdOld.getClienteList().remove(cliente);
                tdocIdOld = em.merge(tdocIdOld);
            }
            if (tdocIdNew != null && !tdocIdNew.equals(tdocIdOld)) {
                tdocIdNew.getClienteList().add(cliente);
                tdocIdNew = em.merge(tdocIdNew);
            }
            for (Ctacobrar ctacobrarListNewCtacobrar : ctacobrarListNew) {
                if (!ctacobrarListOld.contains(ctacobrarListNewCtacobrar)) {
                    Cliente oldCliIdOfCtacobrarListNewCtacobrar = ctacobrarListNewCtacobrar.getCliId();
                    ctacobrarListNewCtacobrar.setCliId(cliente);
                    ctacobrarListNewCtacobrar = em.merge(ctacobrarListNewCtacobrar);
                    if (oldCliIdOfCtacobrarListNewCtacobrar != null && !oldCliIdOfCtacobrarListNewCtacobrar.equals(cliente)) {
                        oldCliIdOfCtacobrarListNewCtacobrar.getCtacobrarList().remove(ctacobrarListNewCtacobrar);
                        oldCliIdOfCtacobrarListNewCtacobrar = em.merge(oldCliIdOfCtacobrarListNewCtacobrar);
                    }
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Cliente oldCliIdOfVentaListNewVenta = ventaListNewVenta.getCliId();
                    ventaListNewVenta.setCliId(cliente);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldCliIdOfVentaListNewVenta != null && !oldCliIdOfVentaListNewVenta.equals(cliente)) {
                        oldCliIdOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldCliIdOfVentaListNewVenta = em.merge(oldCliIdOfVentaListNewVenta);
                    }
                }
            }
            for (Cobranzas cobranzasListNewCobranzas : cobranzasListNew) {
                if (!cobranzasListOld.contains(cobranzasListNewCobranzas)) {
                    Cliente oldCliIdOfCobranzasListNewCobranzas = cobranzasListNewCobranzas.getCliId();
                    cobranzasListNewCobranzas.setCliId(cliente);
                    cobranzasListNewCobranzas = em.merge(cobranzasListNewCobranzas);
                    if (oldCliIdOfCobranzasListNewCobranzas != null && !oldCliIdOfCobranzasListNewCobranzas.equals(cliente)) {
                        oldCliIdOfCobranzasListNewCobranzas.getCobranzasList().remove(cobranzasListNewCobranzas);
                        oldCliIdOfCobranzasListNewCobranzas = em.merge(oldCliIdOfCobranzasListNewCobranzas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cliente.getCliId();
                if (findCliente(id) == null) {
                    throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.");
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
            Cliente cliente;
            try {
                cliente = em.getReference(Cliente.class, id);
                cliente.getCliId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cliente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Ctacobrar> ctacobrarListOrphanCheck = cliente.getCtacobrarList();
            for (Ctacobrar ctacobrarListOrphanCheckCtacobrar : ctacobrarListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Ctacobrar " + ctacobrarListOrphanCheckCtacobrar + " in its ctacobrarList field has a non-nullable cliId field.");
            }
            List<Venta> ventaListOrphanCheck = cliente.getVentaList();
            for (Venta ventaListOrphanCheckVenta : ventaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Venta " + ventaListOrphanCheckVenta + " in its ventaList field has a non-nullable cliId field.");
            }
            List<Cobranzas> cobranzasListOrphanCheck = cliente.getCobranzasList();
            for (Cobranzas cobranzasListOrphanCheckCobranzas : cobranzasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cliente (" + cliente + ") cannot be destroyed since the Cobranzas " + cobranzasListOrphanCheckCobranzas + " in its cobranzasList field has a non-nullable cliId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Condtrib condtribId = cliente.getCondtribId();
            if (condtribId != null) {
                condtribId.getClienteList().remove(cliente);
                condtribId = em.merge(condtribId);
            }
            Direccion dirId = cliente.getDirId();
            if (dirId != null) {
                dirId.getClienteList().remove(cliente);
                dirId = em.merge(dirId);
            }
            Tdoc tdocId = cliente.getTdocId();
            if (tdocId != null) {
                tdocId.getClienteList().remove(cliente);
                tdocId = em.merge(tdocId);
            }
            em.remove(cliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cliente> findClienteEntities() {
        return findClienteEntities(true, -1, -1);
    }

    public List<Cliente> findClienteEntities(int maxResults, int firstResult) {
        return findClienteEntities(false, maxResults, firstResult);
    }

    private List<Cliente> findClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cliente.class));
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

    public Cliente findCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cliente> rt = cq.from(Cliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Cliente cliente){
        String query = "insert into cliente (Cli_id,Cli_razon,tdoc_id,cli_ndoc,condtrib_id,cli_falta,dir_id)"
                        + "values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, cliente.getCliId());
            ps.setString(1, cliente.getCliRazon() );
            ps.setInt(2, cliente.getTdocId().getTdocId());
            ps.setInt(3, cliente.getCliNdoc());
            ps.setInt(4, cliente.getCondtribId().getCondtribId());
            ps.setDate(5, Date.valueOf(cliente.getCliFalta().toString()));
            ps.setInt(6, cliente.getDirId().getDirId());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) cliente.setCliId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Cliente getById(int id){
        List<Cliente> lista = getByFiltro("Cli_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Cliente> getAll(){
        return getByFiltro("1=1");
    }
    public List<Cliente> getByFiltro(String filtro){
        List<Cliente> lista = new ArrayList();
        String query = "select * from cliente where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Cliente cliente = new Cliente(
                        rs.getInt("Cli_id"),
                        rs.getString("cli_razon"),
                        rs.getInt("cli_ndoc"),
                        rs.getDate("cli_falta")
                ); 
                lista.add(cliente);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Cliente> getByRazon(String razon){
        return getByFiltro("cli_razon ='"+razon+"'");
    }
    public List<Cliente> getLikeRazon(String razon){
        return getByFiltro("cli_razon like'%"+razon+"%'");
    }
    public List<Cliente> getByDirId(Direccion dirId){
        return getByFiltro("dir_id ='"+dirId+"'");
    }
    public void remove(Cliente cliente){
        if(cliente != null ){
            String query = "delete from cliente where cli_id ="+cliente.getCliId();
        try {
            conn.createStatement().execute(query);
            cliente = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Cliente cliente){
        String query = "update cliente set cli_razon = ?, tdoc_id = ?, cli_ndoc = ?, contrib_id = ?, cli_falta = ?, dir_id = ? "
                + "where cli_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
           ps.setString(1, cliente.getCliRazon() );
            ps.setInt(2, cliente.getTdocId().getTdocId());
            ps.setInt(3, cliente.getCliNdoc());
            ps.setInt(4, cliente.getCondtribId().getCondtribId());
            ps.setDate(5, Date.valueOf(cliente.getCliFalta().toString()));
            ps.setInt(6, cliente.getDirId().getDirId());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
