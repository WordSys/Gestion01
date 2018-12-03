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
import ar.com.gestion.entities.Detallecomp;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Detallevta;
import ar.com.gestion.entities.Producto;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import ar.com.gestion.repositories.exceptions.PreexistingEntityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class ProductoJpaController implements Serializable {
    
    Connection conn;
    
    public ProductoJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) throws PreexistingEntityException, Exception {
        if (producto.getDetallecompList() == null) {
            producto.setDetallecompList(new ArrayList<Detallecomp>());
        }
        if (producto.getDetallevtaList() == null) {
            producto.setDetallevtaList(new ArrayList<Detallevta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveId = producto.getProveId();
            if (proveId != null) {
                proveId = em.getReference(proveId.getClass(), proveId.getProveId());
                producto.setProveId(proveId);
            }
            List<Detallecomp> attachedDetallecompList = new ArrayList<Detallecomp>();
            for (Detallecomp detallecompListDetallecompToAttach : producto.getDetallecompList()) {
                detallecompListDetallecompToAttach = em.getReference(detallecompListDetallecompToAttach.getClass(), detallecompListDetallecompToAttach.getDetcompId());
                attachedDetallecompList.add(detallecompListDetallecompToAttach);
            }
            producto.setDetallecompList(attachedDetallecompList);
            List<Detallevta> attachedDetallevtaList = new ArrayList<Detallevta>();
            for (Detallevta detallevtaListDetallevtaToAttach : producto.getDetallevtaList()) {
                detallevtaListDetallevtaToAttach = em.getReference(detallevtaListDetallevtaToAttach.getClass(), detallevtaListDetallevtaToAttach.getDetvtaId());
                attachedDetallevtaList.add(detallevtaListDetallevtaToAttach);
            }
            producto.setDetallevtaList(attachedDetallevtaList);
            em.persist(producto);
            if (proveId != null) {
                proveId.getProductoList().add(producto);
                proveId = em.merge(proveId);
            }
            for (Detallecomp detallecompListDetallecomp : producto.getDetallecompList()) {
                Producto oldProdIdOfDetallecompListDetallecomp = detallecompListDetallecomp.getProdId();
                detallecompListDetallecomp.setProdId(producto);
                detallecompListDetallecomp = em.merge(detallecompListDetallecomp);
                if (oldProdIdOfDetallecompListDetallecomp != null) {
                    oldProdIdOfDetallecompListDetallecomp.getDetallecompList().remove(detallecompListDetallecomp);
                    oldProdIdOfDetallecompListDetallecomp = em.merge(oldProdIdOfDetallecompListDetallecomp);
                }
            }
            for (Detallevta detallevtaListDetallevta : producto.getDetallevtaList()) {
                Producto oldProdIdOfDetallevtaListDetallevta = detallevtaListDetallevta.getProdId();
                detallevtaListDetallevta.setProdId(producto);
                detallevtaListDetallevta = em.merge(detallevtaListDetallevta);
                if (oldProdIdOfDetallevtaListDetallevta != null) {
                    oldProdIdOfDetallevtaListDetallevta.getDetallevtaList().remove(detallevtaListDetallevta);
                    oldProdIdOfDetallevtaListDetallevta = em.merge(oldProdIdOfDetallevtaListDetallevta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProducto(producto.getProdId()) != null) {
                throw new PreexistingEntityException("Producto " + producto + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getProdId());
            Proveedor proveIdOld = persistentProducto.getProveId();
            Proveedor proveIdNew = producto.getProveId();
            List<Detallecomp> detallecompListOld = persistentProducto.getDetallecompList();
            List<Detallecomp> detallecompListNew = producto.getDetallecompList();
            List<Detallevta> detallevtaListOld = persistentProducto.getDetallevtaList();
            List<Detallevta> detallevtaListNew = producto.getDetallevtaList();
            List<String> illegalOrphanMessages = null;
            for (Detallecomp detallecompListOldDetallecomp : detallecompListOld) {
                if (!detallecompListNew.contains(detallecompListOldDetallecomp)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallecomp " + detallecompListOldDetallecomp + " since its prodId field is not nullable.");
                }
            }
            for (Detallevta detallevtaListOldDetallevta : detallevtaListOld) {
                if (!detallevtaListNew.contains(detallevtaListOldDetallevta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Detallevta " + detallevtaListOldDetallevta + " since its prodId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveIdNew != null) {
                proveIdNew = em.getReference(proveIdNew.getClass(), proveIdNew.getProveId());
                producto.setProveId(proveIdNew);
            }
            List<Detallecomp> attachedDetallecompListNew = new ArrayList<Detallecomp>();
            for (Detallecomp detallecompListNewDetallecompToAttach : detallecompListNew) {
                detallecompListNewDetallecompToAttach = em.getReference(detallecompListNewDetallecompToAttach.getClass(), detallecompListNewDetallecompToAttach.getDetcompId());
                attachedDetallecompListNew.add(detallecompListNewDetallecompToAttach);
            }
            detallecompListNew = attachedDetallecompListNew;
            producto.setDetallecompList(detallecompListNew);
            List<Detallevta> attachedDetallevtaListNew = new ArrayList<Detallevta>();
            for (Detallevta detallevtaListNewDetallevtaToAttach : detallevtaListNew) {
                detallevtaListNewDetallevtaToAttach = em.getReference(detallevtaListNewDetallevtaToAttach.getClass(), detallevtaListNewDetallevtaToAttach.getDetvtaId());
                attachedDetallevtaListNew.add(detallevtaListNewDetallevtaToAttach);
            }
            detallevtaListNew = attachedDetallevtaListNew;
            producto.setDetallevtaList(detallevtaListNew);
            producto = em.merge(producto);
            if (proveIdOld != null && !proveIdOld.equals(proveIdNew)) {
                proveIdOld.getProductoList().remove(producto);
                proveIdOld = em.merge(proveIdOld);
            }
            if (proveIdNew != null && !proveIdNew.equals(proveIdOld)) {
                proveIdNew.getProductoList().add(producto);
                proveIdNew = em.merge(proveIdNew);
            }
            for (Detallecomp detallecompListNewDetallecomp : detallecompListNew) {
                if (!detallecompListOld.contains(detallecompListNewDetallecomp)) {
                    Producto oldProdIdOfDetallecompListNewDetallecomp = detallecompListNewDetallecomp.getProdId();
                    detallecompListNewDetallecomp.setProdId(producto);
                    detallecompListNewDetallecomp = em.merge(detallecompListNewDetallecomp);
                    if (oldProdIdOfDetallecompListNewDetallecomp != null && !oldProdIdOfDetallecompListNewDetallecomp.equals(producto)) {
                        oldProdIdOfDetallecompListNewDetallecomp.getDetallecompList().remove(detallecompListNewDetallecomp);
                        oldProdIdOfDetallecompListNewDetallecomp = em.merge(oldProdIdOfDetallecompListNewDetallecomp);
                    }
                }
            }
            for (Detallevta detallevtaListNewDetallevta : detallevtaListNew) {
                if (!detallevtaListOld.contains(detallevtaListNewDetallevta)) {
                    Producto oldProdIdOfDetallevtaListNewDetallevta = detallevtaListNewDetallevta.getProdId();
                    detallevtaListNewDetallevta.setProdId(producto);
                    detallevtaListNewDetallevta = em.merge(detallevtaListNewDetallevta);
                    if (oldProdIdOfDetallevtaListNewDetallevta != null && !oldProdIdOfDetallevtaListNewDetallevta.equals(producto)) {
                        oldProdIdOfDetallevtaListNewDetallevta.getDetallevtaList().remove(detallevtaListNewDetallevta);
                        oldProdIdOfDetallevtaListNewDetallevta = em.merge(oldProdIdOfDetallevtaListNewDetallevta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getProdId();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getProdId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Detallecomp> detallecompListOrphanCheck = producto.getDetallecompList();
            for (Detallecomp detallecompListOrphanCheckDetallecomp : detallecompListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Detallecomp " + detallecompListOrphanCheckDetallecomp + " in its detallecompList field has a non-nullable prodId field.");
            }
            List<Detallevta> detallevtaListOrphanCheck = producto.getDetallevtaList();
            for (Detallevta detallevtaListOrphanCheckDetallevta : detallevtaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Producto (" + producto + ") cannot be destroyed since the Detallevta " + detallevtaListOrphanCheckDetallevta + " in its detallevtaList field has a non-nullable prodId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedor proveId = producto.getProveId();
            if (proveId != null) {
                proveId.getProductoList().remove(producto);
                proveId = em.merge(proveId);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Producto producto){
        
        String query = "insert into producto (prod_id,prod_desc,prod_can,prod_pu,preve_id)"
                        + "values(?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, producto.getProdId());
            ps.setString(1, producto.getProdDesc());
            ps.setDouble(2, Double.valueOf(producto.getProdCan().toString()));
            ps.setDouble(3, Double.valueOf(producto.getProdPu().toString()));
            ps.setInt(4, producto.getProveId().getProveId());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) producto.setProdId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Producto getById(int id){
        List<Producto> lista = getByFiltro("Prod_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Producto> getAll(){
        return getByFiltro("1=1");
    }
    public List<Producto> getByFiltro(String filtro){
        List<Producto> lista = new ArrayList();
        String query = "select * from producto where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Producto producto = new Producto(
                        rs.getInt("prod_id"),
                        rs.getString("prod_desc"),
                        rs.getDouble("prod_can"),
                        rs.getDouble("prod_pu"),
                        rs.getInt("prove_id")
                ); 
                lista.add(producto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Producto> getDetalle(){
        List<Producto> lista = new ArrayList();
        String query = "select p.prod_id as Codigo, p.prod_desc as Descripcion, p.prod_can as Cantidad, p.prod_pu as PU, "
                + "pp.prove_id as Codigo_ Prove, pp.prove_desc as Razon from producto p, proveedor pp";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Producto producto = new Producto(
                        rs.getInt("prod_id"),
                        rs.getString("prod_desc"),
                        rs.getDouble("prod_can"),
                        rs.getDouble("prod_pu"),
                        rs.getInt("prove_id")
                ); 
                lista.add(producto);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<Producto> getByDescripcion(String descripcion){
        return getByFiltro("prod_desc ='"+descripcion+"'");
    }
    public List<Producto> getLikeDescripcion(String descripcion){
        return getByFiltro("prod_desc like'%"+descripcion+"%'");
    }
    public List<Producto> getByProveedor(Proveedor proveedor){
        return getByFiltro("prove_id ='"+proveedor+"'");
    }
    public void remove(Producto producto){
        if(producto != null ){
            String query = "delete from producto where prod_id ="+producto.getProdId();
        try {
            conn.createStatement().execute(query);
            producto = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Producto producto){
        String query = "update producto set prod_desc = ?, prod_can = ?, prod_pu = ?, prove_id = ? "
                + "where prod_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, producto.getProdDesc() );
            ps.setDouble(2, producto.getProdCan());
            ps.setDouble(3, producto.getProdPu());
            ps.setInt(4, Integer.valueOf(producto.getProveId().toString()));
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
