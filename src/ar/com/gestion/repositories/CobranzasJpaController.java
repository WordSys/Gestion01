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
import ar.com.gestion.entities.Cliente;
import ar.com.gestion.entities.Venta;
import ar.com.gestion.entities.Cheques;
import ar.com.gestion.entities.Cobranzas;
import ar.com.gestion.repositories.exceptions.IllegalOrphanException;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author walter
 */
public class CobranzasJpaController implements Serializable {
    
    Connection conn;

    public CobranzasJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public CobranzasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cobranzas cobranzas) {
        if (cobranzas.getChequesList() == null) {
            cobranzas.setChequesList(new ArrayList<Cheques>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cliente cliId = cobranzas.getCliId();
            if (cliId != null) {
                cliId = em.getReference(cliId.getClass(), cliId.getCliId());
                cobranzas.setCliId(cliId);
            }
            Venta cobNfact = cobranzas.getCobNfact();
            if (cobNfact != null) {
                cobNfact = em.getReference(cobNfact.getClass(), cobNfact.getVtaId());
                cobranzas.setCobNfact(cobNfact);
            }
            List<Cheques> attachedChequesList = new ArrayList<Cheques>();
            for (Cheques chequesListChequesToAttach : cobranzas.getChequesList()) {
                chequesListChequesToAttach = em.getReference(chequesListChequesToAttach.getClass(), chequesListChequesToAttach.getChId());
                attachedChequesList.add(chequesListChequesToAttach);
            }
            cobranzas.setChequesList(attachedChequesList);
            em.persist(cobranzas);
            if (cliId != null) {
                cliId.getCobranzasList().add(cobranzas);
                cliId = em.merge(cliId);
            }
            if (cobNfact != null) {
                cobNfact.getCobranzasList().add(cobranzas);
                cobNfact = em.merge(cobNfact);
            }
            for (Cheques chequesListCheques : cobranzas.getChequesList()) {
                Cobranzas oldCobNreciboOfChequesListCheques = chequesListCheques.getCobNrecibo();
                chequesListCheques.setCobNrecibo(cobranzas);
                chequesListCheques = em.merge(chequesListCheques);
                if (oldCobNreciboOfChequesListCheques != null) {
                    oldCobNreciboOfChequesListCheques.getChequesList().remove(chequesListCheques);
                    oldCobNreciboOfChequesListCheques = em.merge(oldCobNreciboOfChequesListCheques);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cobranzas cobranzas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cobranzas persistentCobranzas = em.find(Cobranzas.class, cobranzas.getCobId());
            Cliente cliIdOld = persistentCobranzas.getCliId();
            Cliente cliIdNew = cobranzas.getCliId();
            Venta cobNfactOld = persistentCobranzas.getCobNfact();
            Venta cobNfactNew = cobranzas.getCobNfact();
            List<Cheques> chequesListOld = persistentCobranzas.getChequesList();
            List<Cheques> chequesListNew = cobranzas.getChequesList();
            List<String> illegalOrphanMessages = null;
            for (Cheques chequesListOldCheques : chequesListOld) {
                if (!chequesListNew.contains(chequesListOldCheques)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cheques " + chequesListOldCheques + " since its cobNrecibo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (cliIdNew != null) {
                cliIdNew = em.getReference(cliIdNew.getClass(), cliIdNew.getCliId());
                cobranzas.setCliId(cliIdNew);
            }
            if (cobNfactNew != null) {
                cobNfactNew = em.getReference(cobNfactNew.getClass(), cobNfactNew.getVtaId());
                cobranzas.setCobNfact(cobNfactNew);
            }
            List<Cheques> attachedChequesListNew = new ArrayList<Cheques>();
            for (Cheques chequesListNewChequesToAttach : chequesListNew) {
                chequesListNewChequesToAttach = em.getReference(chequesListNewChequesToAttach.getClass(), chequesListNewChequesToAttach.getChId());
                attachedChequesListNew.add(chequesListNewChequesToAttach);
            }
            chequesListNew = attachedChequesListNew;
            cobranzas.setChequesList(chequesListNew);
            cobranzas = em.merge(cobranzas);
            if (cliIdOld != null && !cliIdOld.equals(cliIdNew)) {
                cliIdOld.getCobranzasList().remove(cobranzas);
                cliIdOld = em.merge(cliIdOld);
            }
            if (cliIdNew != null && !cliIdNew.equals(cliIdOld)) {
                cliIdNew.getCobranzasList().add(cobranzas);
                cliIdNew = em.merge(cliIdNew);
            }
            if (cobNfactOld != null && !cobNfactOld.equals(cobNfactNew)) {
                cobNfactOld.getCobranzasList().remove(cobranzas);
                cobNfactOld = em.merge(cobNfactOld);
            }
            if (cobNfactNew != null && !cobNfactNew.equals(cobNfactOld)) {
                cobNfactNew.getCobranzasList().add(cobranzas);
                cobNfactNew = em.merge(cobNfactNew);
            }
            for (Cheques chequesListNewCheques : chequesListNew) {
                if (!chequesListOld.contains(chequesListNewCheques)) {
                    Cobranzas oldCobNreciboOfChequesListNewCheques = chequesListNewCheques.getCobNrecibo();
                    chequesListNewCheques.setCobNrecibo(cobranzas);
                    chequesListNewCheques = em.merge(chequesListNewCheques);
                    if (oldCobNreciboOfChequesListNewCheques != null && !oldCobNreciboOfChequesListNewCheques.equals(cobranzas)) {
                        oldCobNreciboOfChequesListNewCheques.getChequesList().remove(chequesListNewCheques);
                        oldCobNreciboOfChequesListNewCheques = em.merge(oldCobNreciboOfChequesListNewCheques);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cobranzas.getCobId();
                if (findCobranzas(id) == null) {
                    throw new NonexistentEntityException("The cobranzas with id " + id + " no longer exists.");
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
            Cobranzas cobranzas;
            try {
                cobranzas = em.getReference(Cobranzas.class, id);
                cobranzas.getCobId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cobranzas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cheques> chequesListOrphanCheck = cobranzas.getChequesList();
            for (Cheques chequesListOrphanCheckCheques : chequesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cobranzas (" + cobranzas + ") cannot be destroyed since the Cheques " + chequesListOrphanCheckCheques + " in its chequesList field has a non-nullable cobNrecibo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cliente cliId = cobranzas.getCliId();
            if (cliId != null) {
                cliId.getCobranzasList().remove(cobranzas);
                cliId = em.merge(cliId);
            }
            Venta cobNfact = cobranzas.getCobNfact();
            if (cobNfact != null) {
                cobNfact.getCobranzasList().remove(cobranzas);
                cobNfact = em.merge(cobNfact);
            }
            em.remove(cobranzas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cobranzas> findCobranzasEntities() {
        return findCobranzasEntities(true, -1, -1);
    }

    public List<Cobranzas> findCobranzasEntities(int maxResults, int firstResult) {
        return findCobranzasEntities(false, maxResults, firstResult);
    }

    private List<Cobranzas> findCobranzasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cobranzas.class));
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

    public Cobranzas findCobranzas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cobranzas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCobranzasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cobranzas> rt = cq.from(Cobranzas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public void save (Cobranzas cobranzas){
        
        String query = "insert into cobranzas (cob_id,cob_fecha,cob_nrecibo,cob_nfact,cli_id,cob_monto)"
                        + "values(?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, cobranzas.getCobId());
            ps.setDate(1, Date.valueOf(cobranzas.getCobFecha().toString()));
            ps.setInt(2, cobranzas.getCobNrecibo());
            ps.setInt(3, cobranzas.getCobNfact().getComprobNro().getComprobNro());
            ps.setInt(4, cobranzas.getCliId().getCliId());
            ps.setDouble(5, cobranzas.getCobMonto());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) cobranzas.setCobId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Cobranzas getById(int id){
        List<Cobranzas> lista = getByFiltro("cob_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Cobranzas> getAll(){
        return getByFiltro("1=1");
    }
    public List<Cobranzas> getByFiltro(String filtro){
        List<Cobranzas> lista = new ArrayList();
        String query = "select * from cobranzas where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Cobranzas cobranzas = new Cobranzas(
                        rs.getInt("cob_id"),
                        rs.getDate("cob_fecha"),
                        rs.getInt("cob_nrecibo"),
                        rs.getInt("cob_nfact"),
                        rs.getInt("cli_id"),
                        rs.getDouble("cob_monto")
                ); 
                lista.add(cobranzas);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Cobranzas> getByNroRecibo(Integer nrecibo){
        return getByFiltro("cob_nrecibo ='"+nrecibo+"'");
    }
    public List<Cobranzas> getByNroFac(Integer nfac){
        return getByFiltro("cob_nfact ='"+nfac+"'");
    }
    public List<Cobranzas> getByCliente(Cliente cliente){
        return getByFiltro("cli_id ='"+cliente+"'");
    }
    public void remove(Cobranzas cobranzas){
        if(cobranzas != null ){
            String query = "delete from cobranzas where cob_id ="+cobranzas.getCobId();
        try {
            conn.createStatement().execute(query);
            cobranzas = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Cobranzas cobranzas){
        String query = "update cobranzas set cob_fecha = ?, cob_nrecibo = ?, cob_nfact = ? , cli_id = ?, cob_monto = ?"
                + "where cob_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(cobranzas.getCobFecha().toString()));
            ps.setInt(2, cobranzas.getCobNrecibo());
            ps.setInt(3, cobranzas.getCobNfact().getComprobNro().getComprobNro());
            ps.setInt(4, cobranzas.getCliId().getCliId());
            ps.setDouble(5, cobranzas.getCobMonto());
            
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    
}
