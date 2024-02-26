/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.controller;

import cl.buk.controller.exceptions.IllegalOrphanException;
import cl.buk.controller.exceptions.NonexistentEntityException;
import cl.buk.model.Departamento;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.buk.model.Empresa;
import cl.buk.model.Ubicacion;
import cl.buk.model.Empleado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joan.toro
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Buk_JPA");
    }
    public DepartamentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) {
        if (departamento.getEmpleadoList() == null) {
            departamento.setEmpleadoList(new ArrayList<Empleado>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa rutEmpresa = departamento.getRutEmpresa();
            if (rutEmpresa != null) {
                rutEmpresa = em.getReference(rutEmpresa.getClass(), rutEmpresa.getRut());
                departamento.setRutEmpresa(rutEmpresa);
            }
            Ubicacion idUbicacion = departamento.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion = em.getReference(idUbicacion.getClass(), idUbicacion.getId());
                departamento.setIdUbicacion(idUbicacion);
            }
            List<Empleado> attachedEmpleadoList = new ArrayList<Empleado>();
            for (Empleado empleadoListEmpleadoToAttach : departamento.getEmpleadoList()) {
                empleadoListEmpleadoToAttach = em.getReference(empleadoListEmpleadoToAttach.getClass(), empleadoListEmpleadoToAttach.getRut());
                attachedEmpleadoList.add(empleadoListEmpleadoToAttach);
            }
            departamento.setEmpleadoList(attachedEmpleadoList);
            em.persist(departamento);
            if (rutEmpresa != null) {
                rutEmpresa.getDepartamentoList().add(departamento);
                rutEmpresa = em.merge(rutEmpresa);
            }
            if (idUbicacion != null) {
                idUbicacion.getDepartamentoList().add(departamento);
                idUbicacion = em.merge(idUbicacion);
            }
            for (Empleado empleadoListEmpleado : departamento.getEmpleadoList()) {
                Departamento oldIdDepartamentoOfEmpleadoListEmpleado = empleadoListEmpleado.getIdDepartamento();
                empleadoListEmpleado.setIdDepartamento(departamento);
                empleadoListEmpleado = em.merge(empleadoListEmpleado);
                if (oldIdDepartamentoOfEmpleadoListEmpleado != null) {
                    oldIdDepartamentoOfEmpleadoListEmpleado.getEmpleadoList().remove(empleadoListEmpleado);
                    oldIdDepartamentoOfEmpleadoListEmpleado = em.merge(oldIdDepartamentoOfEmpleadoListEmpleado);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getId());
            Empresa rutEmpresaOld = persistentDepartamento.getRutEmpresa();
            Empresa rutEmpresaNew = departamento.getRutEmpresa();
            Ubicacion idUbicacionOld = persistentDepartamento.getIdUbicacion();
            Ubicacion idUbicacionNew = departamento.getIdUbicacion();
            List<Empleado> empleadoListOld = persistentDepartamento.getEmpleadoList();
            List<Empleado> empleadoListNew = departamento.getEmpleadoList();
            List<String> illegalOrphanMessages = null;
            for (Empleado empleadoListOldEmpleado : empleadoListOld) {
                if (!empleadoListNew.contains(empleadoListOldEmpleado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleado " + empleadoListOldEmpleado + " since its idDepartamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (rutEmpresaNew != null) {
                rutEmpresaNew = em.getReference(rutEmpresaNew.getClass(), rutEmpresaNew.getRut());
                departamento.setRutEmpresa(rutEmpresaNew);
            }
            if (idUbicacionNew != null) {
                idUbicacionNew = em.getReference(idUbicacionNew.getClass(), idUbicacionNew.getId());
                departamento.setIdUbicacion(idUbicacionNew);
            }
            List<Empleado> attachedEmpleadoListNew = new ArrayList<Empleado>();
            for (Empleado empleadoListNewEmpleadoToAttach : empleadoListNew) {
                empleadoListNewEmpleadoToAttach = em.getReference(empleadoListNewEmpleadoToAttach.getClass(), empleadoListNewEmpleadoToAttach.getRut());
                attachedEmpleadoListNew.add(empleadoListNewEmpleadoToAttach);
            }
            empleadoListNew = attachedEmpleadoListNew;
            departamento.setEmpleadoList(empleadoListNew);
            departamento = em.merge(departamento);
            if (rutEmpresaOld != null && !rutEmpresaOld.equals(rutEmpresaNew)) {
                rutEmpresaOld.getDepartamentoList().remove(departamento);
                rutEmpresaOld = em.merge(rutEmpresaOld);
            }
            if (rutEmpresaNew != null && !rutEmpresaNew.equals(rutEmpresaOld)) {
                rutEmpresaNew.getDepartamentoList().add(departamento);
                rutEmpresaNew = em.merge(rutEmpresaNew);
            }
            if (idUbicacionOld != null && !idUbicacionOld.equals(idUbicacionNew)) {
                idUbicacionOld.getDepartamentoList().remove(departamento);
                idUbicacionOld = em.merge(idUbicacionOld);
            }
            if (idUbicacionNew != null && !idUbicacionNew.equals(idUbicacionOld)) {
                idUbicacionNew.getDepartamentoList().add(departamento);
                idUbicacionNew = em.merge(idUbicacionNew);
            }
            for (Empleado empleadoListNewEmpleado : empleadoListNew) {
                if (!empleadoListOld.contains(empleadoListNewEmpleado)) {
                    Departamento oldIdDepartamentoOfEmpleadoListNewEmpleado = empleadoListNewEmpleado.getIdDepartamento();
                    empleadoListNewEmpleado.setIdDepartamento(departamento);
                    empleadoListNewEmpleado = em.merge(empleadoListNewEmpleado);
                    if (oldIdDepartamentoOfEmpleadoListNewEmpleado != null && !oldIdDepartamentoOfEmpleadoListNewEmpleado.equals(departamento)) {
                        oldIdDepartamentoOfEmpleadoListNewEmpleado.getEmpleadoList().remove(empleadoListNewEmpleado);
                        oldIdDepartamentoOfEmpleadoListNewEmpleado = em.merge(oldIdDepartamentoOfEmpleadoListNewEmpleado);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = departamento.getId();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleado> empleadoListOrphanCheck = departamento.getEmpleadoList();
            for (Empleado empleadoListOrphanCheckEmpleado : empleadoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Empleado " + empleadoListOrphanCheckEmpleado + " in its empleadoList field has a non-nullable idDepartamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Empresa rutEmpresa = departamento.getRutEmpresa();
            if (rutEmpresa != null) {
                rutEmpresa.getDepartamentoList().remove(departamento);
                rutEmpresa = em.merge(rutEmpresa);
            }
            Ubicacion idUbicacion = departamento.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion.getDepartamentoList().remove(departamento);
                idUbicacion = em.merge(idUbicacion);
            }
            em.remove(departamento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
