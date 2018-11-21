/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.com.gestion.repositories;

import ar.com.gestion.connectors.ConnectorMySql;
import ar.com.gestion.entities.Cheques;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.com.gestion.entities.Cobranzas;
import ar.com.gestion.entities.EntiFinanc;
import ar.com.gestion.repositories.exceptions.NonexistentEntityException;
import java.sql.Connection;
import java.sql.Date;
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
public class ChequesJpaController implements Serializable {
    
    Connection conn;
    
    public ChequesJpaController() {
        conn = ConnectorMySql.getConnection();
    }

    public ChequesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cheques cheques) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cobranzas cobNrecibo = cheques.getCobNrecibo();
            if (cobNrecibo != null) {
                cobNrecibo = em.getReference(cobNrecibo.getClass(), cobNrecibo.getCobId());
                cheques.setCobNrecibo(cobNrecibo);
            }
            EntiFinanc entiId = cheques.getEntiId();
            if (entiId != null) {
                entiId = em.getReference(entiId.getClass(), entiId.getEntfId());
                cheques.setEntiId(entiId);
            }
            em.persist(cheques);
            if (cobNrecibo != null) {
                cobNrecibo.getChequesList().add(cheques);
                cobNrecibo = em.merge(cobNrecibo);
            }
            if (entiId != null) {
                entiId.getChequesList().add(cheques);
                entiId = em.merge(entiId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cheques cheques) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cheques persistentCheques = em.find(Cheques.class, cheques.getChId());
            Cobranzas cobNreciboOld = persistentCheques.getCobNrecibo();
            Cobranzas cobNreciboNew = cheques.getCobNrecibo();
            EntiFinanc entiIdOld = persistentCheques.getEntiId();
            EntiFinanc entiIdNew = cheques.getEntiId();
            if (cobNreciboNew != null) {
                cobNreciboNew = em.getReference(cobNreciboNew.getClass(), cobNreciboNew.getCobId());
                cheques.setCobNrecibo(cobNreciboNew);
            }
            if (entiIdNew != null) {
                entiIdNew = em.getReference(entiIdNew.getClass(), entiIdNew.getEntfId());
                cheques.setEntiId(entiIdNew);
            }
            cheques = em.merge(cheques);
            if (cobNreciboOld != null && !cobNreciboOld.equals(cobNreciboNew)) {
                cobNreciboOld.getChequesList().remove(cheques);
                cobNreciboOld = em.merge(cobNreciboOld);
            }
            if (cobNreciboNew != null && !cobNreciboNew.equals(cobNreciboOld)) {
                cobNreciboNew.getChequesList().add(cheques);
                cobNreciboNew = em.merge(cobNreciboNew);
            }
            if (entiIdOld != null && !entiIdOld.equals(entiIdNew)) {
                entiIdOld.getChequesList().remove(cheques);
                entiIdOld = em.merge(entiIdOld);
            }
            if (entiIdNew != null && !entiIdNew.equals(entiIdOld)) {
                entiIdNew.getChequesList().add(cheques);
                entiIdNew = em.merge(entiIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cheques.getChId();
                if (findCheques(id) == null) {
                    throw new NonexistentEntityException("The cheques with id " + id + " no longer exists.");
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
            Cheques cheques;
            try {
                cheques = em.getReference(Cheques.class, id);
                cheques.getChId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cheques with id " + id + " no longer exists.", enfe);
            }
            Cobranzas cobNrecibo = cheques.getCobNrecibo();
            if (cobNrecibo != null) {
                cobNrecibo.getChequesList().remove(cheques);
                cobNrecibo = em.merge(cobNrecibo);
            }
            EntiFinanc entiId = cheques.getEntiId();
            if (entiId != null) {
                entiId.getChequesList().remove(cheques);
                entiId = em.merge(entiId);
            }
            em.remove(cheques);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cheques> findChequesEntities() {
        return findChequesEntities(true, -1, -1);
    }

    public List<Cheques> findChequesEntities(int maxResults, int firstResult) {
        return findChequesEntities(false, maxResults, firstResult);
    }

    private List<Cheques> findChequesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cheques.class));
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

    public Cheques findCheques(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cheques.class, id);
        } finally {
            em.close();
        }
    }

    public int getChequesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cheques> rt = cq.from(Cheques.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public void save (Cheques cheques){
        
        String query = "insert into cheques (ch_id, ch_fecha, ch_fechavto, ch_suc, "
                + "enti_id, ch_nro, tdoc_id, ch_ndoc, cli_id, ch_monto, cob_recibo)"
                        + "values(?,?,?,?,?,?,?,?,?,?,?)";
       
        try {
            PreparedStatement ps = conn.prepareStatement(query,1 );// 1 = PreparedStatement.RETURN_GENERATED_KEYS)
            ps.setInt(0, cheques.getChId());
            ps.setDate(1, Date.valueOf(cheques.getChFecha().toString()));
            ps.setDate(2, Date.valueOf(cheques.getChFechavto().toString()));
            ps.setInt(3, cheques.getChSuc());
            ps.setInt(4, cheques.getEntiId().getEntfId());
            ps.setInt(5, cheques.getChNro());
            ps.setInt(6, cheques.getTdocId());
            ps.setInt(7, cheques.getChNdoc());
            ps.setInt(8, cheques.getCliId());
            ps.setDouble(9, cheques.getChMonto());
            ps.setInt(10, cheques.getCobNrecibo().getCobNrecibo());
            
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) cheques.setChId(rs.getInt(1));
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public Cheques getById(int id){
        List<Cheques> lista = getByFiltro("ch_id="+id);
        if(lista.size()==0) return null;
        else return lista.get(0);
    }
    public List<Cheques> getAll(){
        return getByFiltro("1=1");
    }
    public List<Cheques> getByFiltro(String filtro){
        List<Cheques> lista = new ArrayList();
        String query = "select * from cheques where "+filtro;
        try {
            ResultSet rs = conn.createStatement().executeQuery(query);
            while(rs.next()){
                Cheques cheques = new Cheques(
                        rs.getInt("ch_id"),
                        rs.getDate("ch_fecha"),
                        rs.getDate("ch_fechavto"),
                        rs.getInt("ch_suc"),
                        rs.getInt("enti_id"),
                        rs.getInt("ch_nro"),
                        rs.getInt("tdoc_id"),
                        rs.getInt("ch_ndoc"),
                        rs.getInt("cli_id"),
                        rs.getDouble("ch_monto"),
                        rs.getInt("cob_nrecibo")
                ); 
                lista.add(cheques);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return lista;
    }
    public List<Cheques> getByEntFi(int entfi){
        return getByFiltro("enti_id ='"+entfi+"'");
    }
    public List<Cheques> getByCliente(int cliente){
        return getByFiltro("cli_id ='"+cliente+"'");
    }
    public void remove(Cheques cheques){
        if(cheques != null ){
            String query = "delete from cheques where ch_id ="+cheques.getChId();
        try {
            conn.createStatement().execute(query);
            cheques = null;
        } catch (Exception e) {
            System.out.println(e);
        }   
        }
    }
    public void update(Cheques cheques){
        String query = "update cheques set ch_fecha = ?, ch_fechavto = ?, ch_suc = ?, enti_id = ?,"
                + " ch_nro = ?, tdoc_id = ?, ch_ndoc = ?, cli_id = ?, ch_monto = ?, cob_recibo = ? "
                + "where ch_id = ? ";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setDate(1, Date.valueOf(cheques.getChFecha().toString()));
            ps.setDate(2, Date.valueOf(cheques.getChFechavto().toString()));
            ps.setInt(3, cheques.getChSuc());
            ps.setInt(4, cheques.getEntiId().getEntfId());
            ps.setInt(5, cheques.getChNro());
            ps.setInt(6, cheques.getTdocId());
            ps.setInt(7, cheques.getChNdoc());
            ps.setInt(8, cheques.getCliId());
            ps.setDouble(9, cheques.getChMonto());
            ps.setInt(10, cheques.getCobNrecibo().getCobNrecibo());
            ps.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
}
