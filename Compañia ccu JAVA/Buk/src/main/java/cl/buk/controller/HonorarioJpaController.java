/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.controller;

import cl.buk.controller.exceptions.NonexistentEntityException;
import cl.buk.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.buk.model.Empleado;
import cl.buk.model.Honorario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joan.toro
 */
public class HonorarioJpaController implements Serializable {

    public HonorarioJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Buk_JPA");
    }

    public HonorarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Honorario honorario) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado rutEmpleado = honorario.getRutEmpleado();
            if (rutEmpleado != null) {
                rutEmpleado = em.getReference(rutEmpleado.getClass(), rutEmpleado.getRut());
                honorario.setRutEmpleado(rutEmpleado);
            }
            em.persist(honorario);
            if (rutEmpleado != null) {
                rutEmpleado.getHonorarioList().add(honorario);
                rutEmpleado = em.merge(rutEmpleado);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHonorario(honorario.getId()) != null) {
                throw new PreexistingEntityException("Honorario " + honorario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Honorario honorario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Honorario persistentHonorario = em.find(Honorario.class, honorario.getId());
            Empleado rutEmpleadoOld = persistentHonorario.getRutEmpleado();
            Empleado rutEmpleadoNew = honorario.getRutEmpleado();
            if (rutEmpleadoNew != null) {
                rutEmpleadoNew = em.getReference(rutEmpleadoNew.getClass(), rutEmpleadoNew.getRut());
                honorario.setRutEmpleado(rutEmpleadoNew);
            }
            honorario = em.merge(honorario);
            if (rutEmpleadoOld != null && !rutEmpleadoOld.equals(rutEmpleadoNew)) {
                rutEmpleadoOld.getHonorarioList().remove(honorario);
                rutEmpleadoOld = em.merge(rutEmpleadoOld);
            }
            if (rutEmpleadoNew != null && !rutEmpleadoNew.equals(rutEmpleadoOld)) {
                rutEmpleadoNew.getHonorarioList().add(honorario);
                rutEmpleadoNew = em.merge(rutEmpleadoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = honorario.getId();
                if (findHonorario(id) == null) {
                    throw new NonexistentEntityException("The honorario with id " + id + " no longer exists.");
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
            Honorario honorario;
            try {
                honorario = em.getReference(Honorario.class, id);
                honorario.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The honorario with id " + id + " no longer exists.", enfe);
            }
            Empleado rutEmpleado = honorario.getRutEmpleado();
            if (rutEmpleado != null) {
                rutEmpleado.getHonorarioList().remove(honorario);
                rutEmpleado = em.merge(rutEmpleado);
            }
            em.remove(honorario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Honorario> findHonorarioEntities() {
        return findHonorarioEntities(true, -1, -1);
    }

    public List<Honorario> findHonorarioEntities(int maxResults, int firstResult) {
        return findHonorarioEntities(false, maxResults, firstResult);
    }

    private List<Honorario> findHonorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Honorario.class));
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

    public Honorario findHonorario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Honorario.class, id);
        } finally {
            em.close();
        }
    }

    public int getHonorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Honorario> rt = cq.from(Honorario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
