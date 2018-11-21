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
import ar.com.gestion.entities.Condtrib;
import ar.com.gestion.entities.Direccion;
import ar.com.gestion.entities.Tdoc;
import ar.com.gestion.entities.Compra;
import java.util.ArrayList;
import java.util.List;
import ar.com.gestion.entities.Producto;
import ar.com.gestion.entities.Pago;
import ar.com.gestion.entities.Ctapagar;
import ar.com.gestion.entities.DetalleProveedor;
import ar.com.gestion.entities.Proveedor;
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
public class ProveedorJpaController implements Serializable {
    
    Connection conn;
    
    public ProveedorJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public ProveedorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Proveedor proveedor) throws IllegalOrphanException {
        if (proveedor.getCompraList() == null) {
            proveedor.setCompraList(new ArrayList<Compra>());
        }
        if (proveedor.getProductoList() == null) {
            proveedor.setProductoList(new ArrayList<Producto>());
        }
        if (proveedor.getPagoList() == null) {
            proveedor.setPagoList(new ArrayList<Pago>());
        }
        if (proveedor.getCtapagarList() == null) {
            proveedor.setCtapagarList(new ArrayList<Ctapagar>());
        }
        List<String> illegalOrphanMessages = null;
        Condtrib condtribIdOrphanCheck = proveedor.getCondtribId();
        if (condtribIdOrphanCheck != null) {
            Proveedor oldProveedorOfCondtribId = condtribIdOrphanCheck.getProveedor();
            if (oldProveedorOfCondtribId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Condtrib " + condtribIdOrphanCheck + " already has an item of type Proveedor whose condtribId column cannot be null. Please make another selection for the condtribId field.");
            }
        }
        Direccion dirIdOrphanCheck = proveedor.getDirId();
        if (dirIdOrphanCheck != null) {
            Proveedor oldProveedorOfDirId = dirIdOrphanCheck.getProveedor();
            if (oldProveedorOfDirId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Direccion " + dirIdOrphanCheck + " already has an item of type Proveedor whose dirId column cannot be null. Please make another selection for the dirId field.");
            }
        }
        Tdoc tdocIdOrphanCheck = proveedor.getTdocId();
        if (tdocIdOrphanCheck != null) {
            Proveedor oldProveedorOfTdocId = tdocIdOrphanCheck.getProveedor();
            if (oldProveedorOfTdocId != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Tdoc " + tdocIdOrphanCheck + " already has an item of type Proveedor whose tdocId column cannot be null. Please make another selection for the tdocId field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Condtrib condtribId = proveedor.getCondtribId();
            if (condtribId != null) {
                condtribId = em.getReference(condtribId.getClass(), condtribId.getCondtribId());
                proveedor.setCondtribId(condtribId);
            }
            Direccion dirId = proveedor.getDirId();
            if (dirId != null) {
                dirId = em.getReference(dirId.getClass(), dirId.getDirId());
                proveedor.setDirId(dirId);
            }
            Tdoc tdocId = proveedor.getTdocId();
            if (tdocId != null) {
                tdocId = em.getReference(tdocId.getClass(), tdocId.getTdocId());
                proveedor.setTdocId(tdocId);
            }
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : proveedor.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getCompId());
                attachedCompraList.add(compraListCompraToAttach);
            }
            proveedor.setCompraList(attachedCompraList);
            List<Producto> attachedProductoList = new ArrayList<Producto>();
            for (Producto productoListProductoToAttach : proveedor.getProductoList()) {
                productoListProductoToAttach = em.getReference(productoListProductoToAttach.getClass(), productoListProductoToAttach.getProdId());
                attachedProductoList.add(productoListProductoToAttach);
            }
            proveedor.setProductoList(attachedProductoList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : proveedor.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getPagoId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            proveedor.setPagoList(attachedPagoList);
            List<Ctapagar> attachedCtapagarList = new ArrayList<Ctapagar>();
            for (Ctapagar ctapagarListCtapagarToAttach : proveedor.getCtapagarList()) {
                ctapagarListCtapagarToAttach = em.getReference(ctapagarListCtapagarToAttach.getClass(), ctapagarListCtapagarToAttach.getCpId());
                attachedCtapagarList.add(ctapagarListCtapagarToAttach);
            }
            proveedor.setCtapagarList(attachedCtapagarList);
            em.persist(proveedor);
            if (condtribId != null) {
                condtribId.setProveedor(proveedor);
                condtribId = em.merge(condtribId);
            }
            if (dirId != null) {
                dirId.setProveedor(proveedor);
                dirId = em.merge(dirId);
            }
            if (tdocId != null) {
                tdocId.setProveedor(proveedor);
                tdocId = em.merge(tdocId);
            }
            for (Compra compraListCompra : proveedor.getCompraList()) {
                Proveedor oldProveeIdOfCompraListCompra = compraListCompra.getProveeId();
                compraListCompra.setProveeId(proveedor);
                compraListCompra = em.merge(compraListCompra);
                if (oldProveeIdOfCompraListCompra != null) {
                    oldProveeIdOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldProveeIdOfCompraListCompra = em.merge(oldProveeIdOfCompraListCompra);
                }
            }
            for (Producto productoListProducto : proveedor.getProductoList()) {
                Proveedor oldProveIdOfProductoListProducto = productoListProducto.getProveId();
                productoListProducto.setProveId(proveedor);
                productoListProducto = em.merge(productoListProducto);
                if (oldProveIdOfProductoListProducto != null) {
                    oldProveIdOfProductoListProducto.getProductoList().remove(productoListProducto);
                    oldProveIdOfProductoListProducto = em.merge(oldProveIdOfProductoListProducto);
                }
            }
            for (Pago pagoListPago : proveedor.getPagoList()) {
                Proveedor oldProveeIdOfPagoListPago = pagoListPago.getProveeId();
                pagoListPago.setProveeId(proveedor);
                pagoListPago = em.merge(pagoListPago);
                if (oldProveeIdOfPagoListPago != null) {
                    oldProveeIdOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldProveeIdOfPagoListPago = em.merge(oldProveeIdOfPagoListPago);
                }
            }
            for (Ctapagar ctapagarListCtapagar : proveedor.getCtapagarList()) {
                Proveedor oldProveIdOfCtapagarListCtapagar = ctapagarListCtapagar.getProveId();
                ctapagarListCtapagar.setProveId(proveedor);
                ctapagarListCtapagar = em.merge(ctapagarListCtapagar);
                if (oldProveIdOfCtapagarListCtapagar != null) {
                    oldProveIdOfCtapagarListCtapagar.getCtapagarList().remove(ctapagarListCtapagar);
                    oldProveIdOfCtapagarListCtapagar = em.merge(oldProveIdOfCtapagarListCtapagar);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Proveedor proveedor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor persistentProveedor = em.find(Proveedor.class, proveedor.getProveId());
            Condtrib condtribIdOld = persistentProveedor.getCondtribId();
            Condtrib condtribIdNew = proveedor.getCondtribId();
            Direccion dirIdOld = persistentProveedor.getDirId();
            Direccion dirIdNew = proveedor.getDirId();
            Tdoc tdocIdOld = persistentProveedor.getTdocId();
            Tdoc tdocIdNew = proveedor.getTdocId();
            List<Compra> compraListOld = persistentProveedor.getCompraList();
            List<Compra> compraListNew = proveedor.getCompraList();
            List<Producto> productoListOld = persistentProveedor.getProductoList();
            List<Producto> productoListNew = proveedor.getProductoList();
            List<Pago> pagoListOld = persistentProveedor.getPagoList();
            List<Pago> pagoListNew = proveedor.getPagoList();
            List<Ctapagar> ctapagarListOld = persistentProveedor.getCtapagarList();
            List<Ctapagar> ctapagarListNew = proveedor.getCtapagarList();
            List<String> illegalOrphanMessages = null;
            if (condtribIdNew != null && !condtribIdNew.equals(condtribIdOld)) {
                Proveedor oldProveedorOfCondtribId = condtribIdNew.getProveedor();
                if (oldProveedorOfCondtribId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Condtrib " + condtribIdNew + " already has an item of type Proveedor whose condtribId column cannot be null. Please make another selection for the condtribId field.");
                }
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                Proveedor oldProveedorOfDirId = dirIdNew.getProveedor();
                if (oldProveedorOfDirId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Direccion " + dirIdNew + " already has an item of type Proveedor whose dirId column cannot be null. Please make another selection for the dirId field.");
                }
            }
            if (tdocIdNew != null && !tdocIdNew.equals(tdocIdOld)) {
                Proveedor oldProveedorOfTdocId = tdocIdNew.getProveedor();
                if (oldProveedorOfTdocId != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Tdoc " + tdocIdNew + " already has an item of type Proveedor whose tdocId column cannot be null. Please make another selection for the tdocId field.");
                }
            }
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its proveeId field is not nullable.");
                }
            }
            for (Producto productoListOldProducto : productoListOld) {
                if (!productoListNew.contains(productoListOldProducto)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Producto " + productoListOldProducto + " since its proveId field is not nullable.");
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoListOldPago + " since its proveeId field is not nullable.");
                }
            }
            for (Ctapagar ctapagarListOldCtapagar : ctapagarListOld) {
                if (!ctapagarListNew.contains(ctapagarListOldCtapagar)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Ctapagar " + ctapagarListOldCtapagar + " since its proveId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (condtribIdNew != null) {
                condtribIdNew = em.getReference(condtribIdNew.getClass(), condtribIdNew.getCondtribId());
                proveedor.setCondtribId(condtribIdNew);
            }
            if (dirIdNew != null) {
                dirIdNew = em.getReference(dirIdNew.getClass(), dirIdNew.getDirId());
                proveedor.setDirId(dirIdNew);
            }
            if (tdocIdNew != null) {
                tdocIdNew = em.getReference(tdocIdNew.getClass(), tdocIdNew.getTdocId());
                proveedor.setTdocId(tdocIdNew);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getCompId());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            proveedor.setCompraList(compraListNew);
            List<Producto> attachedProductoListNew = new ArrayList<Producto>();
            for (Producto productoListNewProductoToAttach : productoListNew) {
                productoListNewProductoToAttach = em.getReference(productoListNewProductoToAttach.getClass(), productoListNewProductoToAttach.getProdId());
                attachedProductoListNew.add(productoListNewProductoToAttach);
            }
            productoListNew = attachedProductoListNew;
            proveedor.setProductoList(productoListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getPagoId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            proveedor.setPagoList(pagoListNew);
            List<Ctapagar> attachedCtapagarListNew = new ArrayList<Ctapagar>();
            for (Ctapagar ctapagarListNewCtapagarToAttach : ctapagarListNew) {
                ctapagarListNewCtapagarToAttach = em.getReference(ctapagarListNewCtapagarToAttach.getClass(), ctapagarListNewCtapagarToAttach.getCpId());
                attachedCtapagarListNew.add(ctapagarListNewCtapagarToAttach);
            }
            ctapagarListNew = attachedCtapagarListNew;
            proveedor.setCtapagarList(ctapagarListNew);
            proveedor = em.merge(proveedor);
            if (condtribIdOld != null && !condtribIdOld.equals(condtribIdNew)) {
                condtribIdOld.setProveedor(null);
                condtribIdOld = em.merge(condtribIdOld);
            }
            if (condtribIdNew != null && !condtribIdNew.equals(condtribIdOld)) {
                condtribIdNew.setProveedor(proveedor);
                condtribIdNew = em.merge(condtribIdNew);
            }
            if (dirIdOld != null && !dirIdOld.equals(dirIdNew)) {
                dirIdOld.setProveedor(null);
                dirIdOld = em.merge(dirIdOld);
            }
            if (dirIdNew != null && !dirIdNew.equals(dirIdOld)) {
                dirIdNew.setProveedor(proveedor);
                dirIdNew = em.merge(dirIdNew);
            }
            if (tdocIdOld != null && !tdocIdOld.equals(tdocIdNew)) {
                tdocIdOld.setProveedor(null);
                tdocIdOld = em.merge(tdocIdOld);
            }
            if (tdocIdNew != null && !tdocIdNew.equals(tdocIdOld)) {
                tdocIdNew.setProveedor(proveedor);
                tdocIdNew = em.merge(tdocIdNew);
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Proveedor oldProveeIdOfCompraListNewCompra = compraListNewCompra.getProveeId();
                    compraListNewCompra.setProveeId(proveedor);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldProveeIdOfCompraListNewCompra != null && !oldProveeIdOfCompraListNewCompra.equals(proveedor)) {
                        oldProveeIdOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldProveeIdOfCompraListNewCompra = em.merge(oldProveeIdOfCompraListNewCompra);
                    }
                }
            }
            for (Producto productoListNewProducto : productoListNew) {
                if (!productoListOld.contains(productoListNewProducto)) {
                    Proveedor oldProveIdOfProductoListNewProducto = productoListNewProducto.getProveId();
                    productoListNewProducto.setProveId(proveedor);
                    productoListNewProducto = em.merge(productoListNewProducto);
                    if (oldProveIdOfProductoListNewProducto != null && !oldProveIdOfProductoListNewProducto.equals(proveedor)) {
                        oldProveIdOfProductoListNewProducto.getProductoList().remove(productoListNewProducto);
                        oldProveIdOfProductoListNewProducto = em.merge(oldProveIdOfProductoListNewProducto);
                    }
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Proveedor oldProveeIdOfPagoListNewPago = pagoListNewPago.getProveeId();
                    pagoListNewPago.setProveeId(proveedor);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldProveeIdOfPagoListNewPago != null && !oldProveeIdOfPagoListNewPago.equals(proveedor)) {
                        oldProveeIdOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldProveeIdOfPagoListNewPago = em.merge(oldProveeIdOfPagoListNewPago);
                    }
                }
            }
            for (Ctapagar ctapagarListNewCtapagar : ctapagarListNew) {
                if (!ctapagarListOld.contains(ctapagarListNewCtapagar)) {
                    Proveedor oldProveIdOfCtapagarListNewCtapagar = ctapagarListNewCtapagar.getProveId();
                    ctapagarListNewCtapagar.setProveId(proveedor);
                    ctapagarListNewCtapagar = em.merge(ctapagarListNewCtapagar);
                    if (oldProveIdOfCtapagarListNewCtapagar != null && !oldProveIdOfCtapagarListNewCtapagar.equals(proveedor)) {
                        oldProveIdOfCtapagarListNewCtapagar.getCtapagarList().remove(ctapagarListNewCtapagar);
                        oldProveIdOfCtapagarListNewCtapagar = em.merge(oldProveIdOfCtapagarListNewCtapagar);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = proveedor.getProveId();
                if (findProveedor(id) == null) {
                    throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.");
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
            Proveedor proveedor;
            try {
                proveedor = em.getReference(Proveedor.class, id);
                proveedor.getProveId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The proveedor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = proveedor.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable proveeId field.");
            }
            List<Producto> productoListOrphanCheck = proveedor.getProductoList();
            for (Producto productoListOrphanCheckProducto : productoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Producto " + productoListOrphanCheckProducto + " in its productoList field has a non-nullable proveId field.");
            }
            List<Pago> pagoListOrphanCheck = proveedor.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Pago " + pagoListOrphanCheckPago + " in its pagoList field has a non-nullable proveeId field.");
            }
            List<Ctapagar> ctapagarListOrphanCheck = proveedor.getCtapagarList();
            for (Ctapagar ctapagarListOrphanCheckCtapagar : ctapagarListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Proveedor (" + proveedor + ") cannot be destroyed since the Ctapagar " + ctapagarListOrphanCheckCtapagar + " in its ctapagarList field has a non-nullable proveId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Condtrib condtribId = proveedor.getCondtribId();
            if (condtribId != null) {
                condtribId.setProveedor(null);
                condtribId = em.merge(condtribId);
            }
            Direccion dirId = proveedor.getDirId();
            if (dirId != null) {
                dirId.setProveedor(null);
                dirId = em.merge(dirId);
            }
            Tdoc tdocId = proveedor.getTdocId();
            if (tdocId != null) {
                tdocId.setProveedor(null);
                tdocId = em.merge(tdocId);
            }
            em.remove(proveedor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Proveedor> findProveedorEntities() {
        return findProveedorEntities(true, -1, -1);
    }

    public List<Proveedor> findProveedorEntities(int maxResults, int firstResult) {
        return findProveedorEntities(false, maxResults, firstResult);
    }

    private List<Proveedor> findProveedorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Proveedor.class));
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

    public Proveedor findProveedor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Proveedor.class, id);
        } finally {
            em.close();
        }
    }

    public int getProveedorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Proveedor> rt = cq.from(Proveedor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Proveedor proveedor){
        String query = "insert into proveedor (prove_razon,tdoc_id,prove_ndoc,condtrib_id,dir_id)"
                        + "values(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            //ps.setInt(0, proveedor.getProveId());
            ps.setString(1, proveedor.getProveRazon());
            ps.setInt(2, proveedor.getTdoc_Id());
            ps.setLong(3, proveedor.getProveNdoc());
            ps.setInt(4, proveedor.getCondtrib_Id());
            ps.setInt(5, proveedor.getDir_Id());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) proveedor.setProveId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public DetalleProveedor getById(int id){
        List<DetalleProveedor> lista = getByFiltro("codigo="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Proveedor> getAll(){
//        return getByFiltro("1=1");
        List<Proveedor> lista = new ArrayList();
        String query = "select prove_id,prove_razon,tdoc_id,prove_ndoc,condtrib_id,dir_id from proveedor";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Proveedor proveedor = new Proveedor(
                        rs.getInt("prove_id"),
                        rs.getString("prove_razon"),
                        rs.getInt("tdoc_id"),
                        rs.getLong("prove_ndoc"),
                        rs.getInt("condtrib_id"),
                        rs.getInt("dir_id")
                ); 
                lista.add(proveedor);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    public List<DetalleProveedor> getByFiltro(String filtro){
        List<DetalleProveedor> lista = new ArrayList();
        String query
                = "     SELECT p.prove_id as codigo, p.prove_razon as razon, t.tdoc_desc as tipoDoc, p.prove_ndoc as nDoc, "
                + "            c.condtrib_desc as condTrib, d.dir_calle as calle, d.dir_nro as nro, d.dir_cruce1 as calle1,"
                + "            d.dir_cruce2 as calle2, d.dir_piso as piso, d.dir_dpto as dpto, l.nombre as Localidad, "
                + "            d.dir_cp as CP, pcia.nombre as Provincia \n"
                + "     FROM   proveedor p, direccion d, localidad l, provincia pcia, tdoc t, condtrib c \n"
                + "     WHERE  p.tdoc_id = t.tdoc_id and p.condtrib_id = c.condtrib_id and p.dir_id = d.dir_id and "
                + "            d.localidad_id = l.id and d.provincia_id = pcia.id and " + filtro
                + "     ORDER BY p.prove_id ";
        //"select * from proveedor where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                DetalleProveedor proveedor = new DetalleProveedor(
                        rs.getInt("codigo"),
                        rs.getString("razon"),
                        rs.getString("tipoDoc"),
                        rs.getLong("nDoc"),
                        rs.getString("condTrib"),
                        rs.getString("calle"),
                        rs.getInt("nro"),
                        rs.getString("calle1"),
                        rs.getString("calle2"),
                        rs.getInt("piso"),
                        rs.getString("dpto"),
                        rs.getString("Localidad"),
                        rs.getShort("CP"),
                        rs.getString("Provincia")
                ); 
                lista.add(proveedor);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<DetalleProveedor> getByRazon(String razon){
        return getByFiltro("razon ='"+razon+"'");
    }
    public List<DetalleProveedor> getLikeRazon(String razon){
        return getByFiltro("p.prove_razon like'%"+razon+"%'");
    }
    public List<DetalleProveedor> getByDirId(Direccion dirId){
        return getByFiltro("codigo='"+dirId+"'");
    }
    public void remove(Proveedor proveedor){
        if(proveedor != null ){
            String query = "delete from proveedor where prove_id ="+proveedor.getProveId();
        try {
            conn.createStatement().execute(query);
            proveedor = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Proveedor proveedor, int id){
        String query = "update proveedor set prove_razon = ?, tdoc_id = ?, prove_ndoc = ?, condtrib_id = ?"
                + " where prove_id = " + id;
        try {
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, proveedor.getProveRazon());
            ps.setInt(2, proveedor.getTdoc_Id());
            ps.setLong(3, proveedor.getProveNdoc());
            ps.setInt(4, proveedor.getCondtrib_Id());
            
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public int getUltimoId(){
        int ultimoId=0;
        String query = "SELECT max(prove_id) as uId FROM proveedor";
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                ultimoId = rs.getInt("uId");
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return ultimoId;
        
        
    }
    
    public List<DetalleProveedor> getDetalleProveedor(){
        
        List<DetalleProveedor> lista = new ArrayList();
        String query
                = "SELECT p.prove_id as codigo, p.prove_razon as razon, t.tdoc_desc as tipoDoc, p.prove_ndoc as nDoc, "
                + "            c.condtrib_desc as condTrib, d.dir_calle as calle, d.dir_nro as nro, d.dir_cruce1 as calle1,"
                + "            d.dir_cruce2 as calle2, d.dir_piso as piso, d.dir_dpto as dpto, l.nombre as Localidad, "
                + "            d.dir_cp as CP, pcia.nombre as Provincia \n"
                + "FROM   proveedor p, direccion d, localidad l, provincia pcia, tdoc t, condtrib c \n"
                + "WHERE  p.tdoc_id = t.tdoc_id and p.condtrib_id = c.condtrib_id and p.dir_id = d.dir_id and "
                + "       d.localidad_id = l.id and d.provincia_id = pcia.id\n"
                + "ORDER BY p.prove_id";

        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                DetalleProveedor detalle = new DetalleProveedor(
                        rs.getInt("codigo"),
                        rs.getString("razon"),
                        rs.getString("tipoDoc"),
                        rs.getLong("nDoc"),
                        rs.getString("condTrib"),
                        rs.getString("calle"),
                        rs.getInt("nro"),
                        rs.getString("calle1"),
                        rs.getString("calle2"),
                        rs.getInt("piso"),
                        rs.getString("dpto"),
                        rs.getString("Localidad"),
                        rs.getShort("CP"),
                        rs.getString("Provincia")
                ); 
                lista.add(detalle);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    
    
}
