/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.controller;

import cl.buk.controller.exceptions.IllegalOrphanException;
import cl.buk.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import cl.buk.model.Empresa;
import java.util.ArrayList;
import java.util.List;
import cl.buk.model.Departamento;
import cl.buk.model.Ubicacion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joan.toro
 */
public class UbicacionJpaController implements Serializable {

    public UbicacionJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Buk_JPA");
    }

    public UbicacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ubicacion ubicacion) {
        if (ubicacion.getEmpresaList() == null) {
            ubicacion.setEmpresaList(new ArrayList<Empresa>());
        }
        if (ubicacion.getDepartamentoList() == null) {
            ubicacion.setDepartamentoList(new ArrayList<Departamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Empresa> attachedEmpresaList = new ArrayList<Empresa>();
            for (Empresa empresaListEmpresaToAttach : ubicacion.getEmpresaList()) {
                empresaListEmpresaToAttach = em.getReference(empresaListEmpresaToAttach.getClass(), empresaListEmpresaToAttach.getRut());
                attachedEmpresaList.add(empresaListEmpresaToAttach);
            }
            ubicacion.setEmpresaList(attachedEmpresaList);
            List<Departamento> attachedDepartamentoList = new ArrayList<Departamento>();
            for (Departamento departamentoListDepartamentoToAttach : ubicacion.getDepartamentoList()) {
                departamentoListDepartamentoToAttach = em.getReference(departamentoListDepartamentoToAttach.getClass(), departamentoListDepartamentoToAttach.getId());
                attachedDepartamentoList.add(departamentoListDepartamentoToAttach);
            }
            ubicacion.setDepartamentoList(attachedDepartamentoList);
            em.persist(ubicacion);
            for (Empresa empresaListEmpresa : ubicacion.getEmpresaList()) {
                Ubicacion oldIdUbicacionOfEmpresaListEmpresa = empresaListEmpresa.getIdUbicacion();
                empresaListEmpresa.setIdUbicacion(ubicacion);
                empresaListEmpresa = em.merge(empresaListEmpresa);
                if (oldIdUbicacionOfEmpresaListEmpresa != null) {
                    oldIdUbicacionOfEmpresaListEmpresa.getEmpresaList().remove(empresaListEmpresa);
                    oldIdUbicacionOfEmpresaListEmpresa = em.merge(oldIdUbicacionOfEmpresaListEmpresa);
                }
            }
            for (Departamento departamentoListDepartamento : ubicacion.getDepartamentoList()) {
                Ubicacion oldIdUbicacionOfDepartamentoListDepartamento = departamentoListDepartamento.getIdUbicacion();
                departamentoListDepartamento.setIdUbicacion(ubicacion);
                departamentoListDepartamento = em.merge(departamentoListDepartamento);
                if (oldIdUbicacionOfDepartamentoListDepartamento != null) {
                    oldIdUbicacionOfDepartamentoListDepartamento.getDepartamentoList().remove(departamentoListDepartamento);
                    oldIdUbicacionOfDepartamentoListDepartamento = em.merge(oldIdUbicacionOfDepartamentoListDepartamento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ubicacion ubicacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion persistentUbicacion = em.find(Ubicacion.class, ubicacion.getId());
            List<Empresa> empresaListOld = persistentUbicacion.getEmpresaList();
            List<Empresa> empresaListNew = ubicacion.getEmpresaList();
            List<Departamento> departamentoListOld = persistentUbicacion.getDepartamentoList();
            List<Departamento> departamentoListNew = ubicacion.getDepartamentoList();
            List<String> illegalOrphanMessages = null;
            for (Empresa empresaListOldEmpresa : empresaListOld) {
                if (!empresaListNew.contains(empresaListOldEmpresa)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empresa " + empresaListOldEmpresa + " since its idUbicacion field is not nullable.");
                }
            }
            for (Departamento departamentoListOldDepartamento : departamentoListOld) {
                if (!departamentoListNew.contains(departamentoListOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentoListOldDepartamento + " since its idUbicacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empresa> attachedEmpresaListNew = new ArrayList<Empresa>();
            for (Empresa empresaListNewEmpresaToAttach : empresaListNew) {
                empresaListNewEmpresaToAttach = em.getReference(empresaListNewEmpresaToAttach.getClass(), empresaListNewEmpresaToAttach.getRut());
                attachedEmpresaListNew.add(empresaListNewEmpresaToAttach);
            }
            empresaListNew = attachedEmpresaListNew;
            ubicacion.setEmpresaList(empresaListNew);
            List<Departamento> attachedDepartamentoListNew = new ArrayList<Departamento>();
            for (Departamento departamentoListNewDepartamentoToAttach : departamentoListNew) {
                departamentoListNewDepartamentoToAttach = em.getReference(departamentoListNewDepartamentoToAttach.getClass(), departamentoListNewDepartamentoToAttach.getId());
                attachedDepartamentoListNew.add(departamentoListNewDepartamentoToAttach);
            }
            departamentoListNew = attachedDepartamentoListNew;
            ubicacion.setDepartamentoList(departamentoListNew);
            ubicacion = em.merge(ubicacion);
            for (Empresa empresaListNewEmpresa : empresaListNew) {
                if (!empresaListOld.contains(empresaListNewEmpresa)) {
                    Ubicacion oldIdUbicacionOfEmpresaListNewEmpresa = empresaListNewEmpresa.getIdUbicacion();
                    empresaListNewEmpresa.setIdUbicacion(ubicacion);
                    empresaListNewEmpresa = em.merge(empresaListNewEmpresa);
                    if (oldIdUbicacionOfEmpresaListNewEmpresa != null && !oldIdUbicacionOfEmpresaListNewEmpresa.equals(ubicacion)) {
                        oldIdUbicacionOfEmpresaListNewEmpresa.getEmpresaList().remove(empresaListNewEmpresa);
                        oldIdUbicacionOfEmpresaListNewEmpresa = em.merge(oldIdUbicacionOfEmpresaListNewEmpresa);
                    }
                }
            }
            for (Departamento departamentoListNewDepartamento : departamentoListNew) {
                if (!departamentoListOld.contains(departamentoListNewDepartamento)) {
                    Ubicacion oldIdUbicacionOfDepartamentoListNewDepartamento = departamentoListNewDepartamento.getIdUbicacion();
                    departamentoListNewDepartamento.setIdUbicacion(ubicacion);
                    departamentoListNewDepartamento = em.merge(departamentoListNewDepartamento);
                    if (oldIdUbicacionOfDepartamentoListNewDepartamento != null && !oldIdUbicacionOfDepartamentoListNewDepartamento.equals(ubicacion)) {
                        oldIdUbicacionOfDepartamentoListNewDepartamento.getDepartamentoList().remove(departamentoListNewDepartamento);
                        oldIdUbicacionOfDepartamentoListNewDepartamento = em.merge(oldIdUbicacionOfDepartamentoListNewDepartamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ubicacion.getId();
                if (findUbicacion(id) == null) {
                    throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.");
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
            Ubicacion ubicacion;
            try {
                ubicacion = em.getReference(Ubicacion.class, id);
                ubicacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ubicacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empresa> empresaListOrphanCheck = ubicacion.getEmpresaList();
            for (Empresa empresaListOrphanCheckEmpresa : empresaListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubicacion (" + ubicacion + ") cannot be destroyed since the Empresa " + empresaListOrphanCheckEmpresa + " in its empresaList field has a non-nullable idUbicacion field.");
            }
            List<Departamento> departamentoListOrphanCheck = ubicacion.getDepartamentoList();
            for (Departamento departamentoListOrphanCheckDepartamento : departamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Ubicacion (" + ubicacion + ") cannot be destroyed since the Departamento " + departamentoListOrphanCheckDepartamento + " in its departamentoList field has a non-nullable idUbicacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(ubicacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ubicacion> findUbicacionEntities() {
        return findUbicacionEntities(true, -1, -1);
    }

    public List<Ubicacion> findUbicacionEntities(int maxResults, int firstResult) {
        return findUbicacionEntities(false, maxResults, firstResult);
    }

    private List<Ubicacion> findUbicacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ubicacion.class));
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

    public Ubicacion findUbicacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ubicacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getUbicacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ubicacion> rt = cq.from(Ubicacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
