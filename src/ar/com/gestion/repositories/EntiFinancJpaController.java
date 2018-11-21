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
import ar.com.gestion.entities.Cheques;
import ar.com.gestion.entities.EntiFinanc;
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
public class EntiFinancJpaController implements Serializable {

    Connection conn;
    
    public EntiFinancJpaController() {
        conn = ConnectorMySql.getConnection();
    }
    
    public EntiFinancJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(EntiFinanc entiFinanc) throws PreexistingEntityException, Exception {
        if (entiFinanc.getChequesList() == null) {
            entiFinanc.setChequesList(new ArrayList<Cheques>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Cheques> attachedChequesList = new ArrayList<Cheques>();
            for (Cheques chequesListChequesToAttach : entiFinanc.getChequesList()) {
                chequesListChequesToAttach = em.getReference(chequesListChequesToAttach.getClass(), chequesListChequesToAttach.getChId());
                attachedChequesList.add(chequesListChequesToAttach);
            }
            entiFinanc.setChequesList(attachedChequesList);
            em.persist(entiFinanc);
            for (Cheques chequesListCheques : entiFinanc.getChequesList()) {
                EntiFinanc oldEntiIdOfChequesListCheques = chequesListCheques.getEntiId();
                chequesListCheques.setEntiId(entiFinanc);
                chequesListCheques = em.merge(chequesListCheques);
                if (oldEntiIdOfChequesListCheques != null) {
                    oldEntiIdOfChequesListCheques.getChequesList().remove(chequesListCheques);
                    oldEntiIdOfChequesListCheques = em.merge(oldEntiIdOfChequesListCheques);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEntiFinanc(entiFinanc.getEntfId()) != null) {
                throw new PreexistingEntityException("EntiFinanc " + entiFinanc + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(EntiFinanc entiFinanc) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            EntiFinanc persistentEntiFinanc = em.find(EntiFinanc.class, entiFinanc.getEntfId());
            List<Cheques> chequesListOld = persistentEntiFinanc.getChequesList();
            List<Cheques> chequesListNew = entiFinanc.getChequesList();
            List<String> illegalOrphanMessages = null;
            for (Cheques chequesListOldCheques : chequesListOld) {
                if (!chequesListNew.contains(chequesListOldCheques)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cheques " + chequesListOldCheques + " since its entiId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Cheques> attachedChequesListNew = new ArrayList<Cheques>();
            for (Cheques chequesListNewChequesToAttach : chequesListNew) {
                chequesListNewChequesToAttach = em.getReference(chequesListNewChequesToAttach.getClass(), chequesListNewChequesToAttach.getChId());
                attachedChequesListNew.add(chequesListNewChequesToAttach);
            }
            chequesListNew = attachedChequesListNew;
            entiFinanc.setChequesList(chequesListNew);
            entiFinanc = em.merge(entiFinanc);
            for (Cheques chequesListNewCheques : chequesListNew) {
                if (!chequesListOld.contains(chequesListNewCheques)) {
                    EntiFinanc oldEntiIdOfChequesListNewCheques = chequesListNewCheques.getEntiId();
                    chequesListNewCheques.setEntiId(entiFinanc);
                    chequesListNewCheques = em.merge(chequesListNewCheques);
                    if (oldEntiIdOfChequesListNewCheques != null && !oldEntiIdOfChequesListNewCheques.equals(entiFinanc)) {
                        oldEntiIdOfChequesListNewCheques.getChequesList().remove(chequesListNewCheques);
                        oldEntiIdOfChequesListNewCheques = em.merge(oldEntiIdOfChequesListNewCheques);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = entiFinanc.getEntfId();
                if (findEntiFinanc(id) == null) {
                    throw new NonexistentEntityException("The entiFinanc with id " + id + " no longer exists.");
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
            EntiFinanc entiFinanc;
            try {
                entiFinanc = em.getReference(EntiFinanc.class, id);
                entiFinanc.getEntfId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The entiFinanc with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Cheques> chequesListOrphanCheck = entiFinanc.getChequesList();
            for (Cheques chequesListOrphanCheckCheques : chequesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This EntiFinanc (" + entiFinanc + ") cannot be destroyed since the Cheques " + chequesListOrphanCheckCheques + " in its chequesList field has a non-nullable entiId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(entiFinanc);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<EntiFinanc> findEntiFinancEntities() {
        return findEntiFinancEntities(true, -1, -1);
    }

    public List<EntiFinanc> findEntiFinancEntities(int maxResults, int firstResult) {
        return findEntiFinancEntities(false, maxResults, firstResult);
    }

    private List<EntiFinanc> findEntiFinancEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(EntiFinanc.class));
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

    public EntiFinanc findEntiFinanc(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(EntiFinanc.class, id);
        } finally {
            em.close();
        }
    }

    public int getEntiFinancCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<EntiFinanc> rt = cq.from(EntiFinanc.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (EntiFinanc entiFinanc){
        
        String query = "insert into entiFinanc (entf_id,entf_desc)"
                        + "values(?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, entiFinanc.getEntfId());
            ps.setString(1, entiFinanc.getEntfDesc());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) entiFinanc.setEntfId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public EntiFinanc getById(int id){
        List<EntiFinanc> lista = getByFiltro("entf_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<EntiFinanc> getAll(){
        return getByFiltro("1=1");
    }
    public List<EntiFinanc> getByFiltro(String filtro){
        List<EntiFinanc> lista = new ArrayList();
        String query = "select * from enti_financ where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                EntiFinanc entiFinanc = new EntiFinanc(
                        rs.getInt("entf_id"),
                        rs.getString("entf_desc")
                ); 
                lista.add(entiFinanc);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<EntiFinanc> getByDescripcion(String descripcion){
        return getByFiltro("entf_desc ='"+descripcion+"'");
    }
    public List<EntiFinanc> getLikeDescripcion(String descripcion){
        return getByFiltro("entf_desc like'%"+descripcion+"%'");
    }
    public void remove(EntiFinanc entiFinanc){
        if(entiFinanc != null ){
            String query = "delete from enti_financ where entf_id ="+entiFinanc.getEntfId();
        try {
            conn.createStatement().execute(query);
            entiFinanc = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(EntiFinanc entiFinanc){
        String query = "update enti_financ set entf_desc = ? "
                + "where entf_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, entiFinanc.getEntfDesc());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
