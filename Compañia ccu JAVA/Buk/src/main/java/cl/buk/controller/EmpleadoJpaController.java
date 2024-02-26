/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.controller;

import cl.buk.controller.exceptions.IllegalOrphanException;
import cl.buk.controller.exceptions.NonexistentEntityException;
import cl.buk.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.buk.model.Departamento;
import cl.buk.model.Empleado;
import cl.buk.model.Honorario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joan.toro
 */
public class EmpleadoJpaController implements Serializable {
    public EmpleadoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Buk_JPA");
    }

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) throws PreexistingEntityException, Exception {
        if (empleado.getHonorarioList() == null) {
            empleado.setHonorarioList(new ArrayList<Honorario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento idDepartamento = empleado.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento = em.getReference(idDepartamento.getClass(), idDepartamento.getId());
                empleado.setIdDepartamento(idDepartamento);
            }
            List<Honorario> attachedHonorarioList = new ArrayList<Honorario>();
            for (Honorario honorarioListHonorarioToAttach : empleado.getHonorarioList()) {
                honorarioListHonorarioToAttach = em.getReference(honorarioListHonorarioToAttach.getClass(), honorarioListHonorarioToAttach.getId());
                attachedHonorarioList.add(honorarioListHonorarioToAttach);
            }
            empleado.setHonorarioList(attachedHonorarioList);
            em.persist(empleado);
            if (idDepartamento != null) {
                idDepartamento.getEmpleadoList().add(empleado);
                idDepartamento = em.merge(idDepartamento);
            }
            for (Honorario honorarioListHonorario : empleado.getHonorarioList()) {
                Empleado oldRutEmpleadoOfHonorarioListHonorario = honorarioListHonorario.getRutEmpleado();
                honorarioListHonorario.setRutEmpleado(empleado);
                honorarioListHonorario = em.merge(honorarioListHonorario);
                if (oldRutEmpleadoOfHonorarioListHonorario != null) {
                    oldRutEmpleadoOfHonorarioListHonorario.getHonorarioList().remove(honorarioListHonorario);
                    oldRutEmpleadoOfHonorarioListHonorario = em.merge(oldRutEmpleadoOfHonorarioListHonorario);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpleado(empleado.getRut()) != null) {
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
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getRut());
            Departamento idDepartamentoOld = persistentEmpleado.getIdDepartamento();
            Departamento idDepartamentoNew = empleado.getIdDepartamento();
            List<Honorario> honorarioListOld = persistentEmpleado.getHonorarioList();
            List<Honorario> honorarioListNew = empleado.getHonorarioList();
            List<String> illegalOrphanMessages = null;
            for (Honorario honorarioListOldHonorario : honorarioListOld) {
                if (!honorarioListNew.contains(honorarioListOldHonorario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Honorario " + honorarioListOldHonorario + " since its rutEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDepartamentoNew != null) {
                idDepartamentoNew = em.getReference(idDepartamentoNew.getClass(), idDepartamentoNew.getId());
                empleado.setIdDepartamento(idDepartamentoNew);
            }
            List<Honorario> attachedHonorarioListNew = new ArrayList<Honorario>();
            for (Honorario honorarioListNewHonorarioToAttach : honorarioListNew) {
                honorarioListNewHonorarioToAttach = em.getReference(honorarioListNewHonorarioToAttach.getClass(), honorarioListNewHonorarioToAttach.getId());
                attachedHonorarioListNew.add(honorarioListNewHonorarioToAttach);
            }
            honorarioListNew = attachedHonorarioListNew;
            empleado.setHonorarioList(honorarioListNew);
            empleado = em.merge(empleado);
            if (idDepartamentoOld != null && !idDepartamentoOld.equals(idDepartamentoNew)) {
                idDepartamentoOld.getEmpleadoList().remove(empleado);
                idDepartamentoOld = em.merge(idDepartamentoOld);
            }
            if (idDepartamentoNew != null && !idDepartamentoNew.equals(idDepartamentoOld)) {
                idDepartamentoNew.getEmpleadoList().add(empleado);
                idDepartamentoNew = em.merge(idDepartamentoNew);
            }
            for (Honorario honorarioListNewHonorario : honorarioListNew) {
                if (!honorarioListOld.contains(honorarioListNewHonorario)) {
                    Empleado oldRutEmpleadoOfHonorarioListNewHonorario = honorarioListNewHonorario.getRutEmpleado();
                    honorarioListNewHonorario.setRutEmpleado(empleado);
                    honorarioListNewHonorario = em.merge(honorarioListNewHonorario);
                    if (oldRutEmpleadoOfHonorarioListNewHonorario != null && !oldRutEmpleadoOfHonorarioListNewHonorario.equals(empleado)) {
                        oldRutEmpleadoOfHonorarioListNewHonorario.getHonorarioList().remove(honorarioListNewHonorario);
                        oldRutEmpleadoOfHonorarioListNewHonorario = em.merge(oldRutEmpleadoOfHonorarioListNewHonorario);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getRut();
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
                empleado.getRut();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Honorario> honorarioListOrphanCheck = empleado.getHonorarioList();
            for (Honorario honorarioListOrphanCheckHonorario : honorarioListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleado (" + empleado + ") cannot be destroyed since the Honorario " + honorarioListOrphanCheckHonorario + " in its honorarioList field has a non-nullable rutEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento idDepartamento = empleado.getIdDepartamento();
            if (idDepartamento != null) {
                idDepartamento.getEmpleadoList().remove(empleado);
                idDepartamento = em.merge(idDepartamento);
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
    
}
