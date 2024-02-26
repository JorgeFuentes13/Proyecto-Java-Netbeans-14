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
import cl.buk.model.Ubicacion;
import cl.buk.model.Departamento;
import cl.buk.model.Empresa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author joan.toro
 */
public class EmpresaJpaController implements Serializable {
    public EmpresaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("Buk_JPA");
    }

    public EmpresaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empresa empresa) throws PreexistingEntityException, Exception {
        if (empresa.getDepartamentoList() == null) {
            empresa.setDepartamentoList(new ArrayList<Departamento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ubicacion idUbicacion = empresa.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion = em.getReference(idUbicacion.getClass(), idUbicacion.getId());
                empresa.setIdUbicacion(idUbicacion);
            }
            List<Departamento> attachedDepartamentoList = new ArrayList<Departamento>();
            for (Departamento departamentoListDepartamentoToAttach : empresa.getDepartamentoList()) {
                departamentoListDepartamentoToAttach = em.getReference(departamentoListDepartamentoToAttach.getClass(), departamentoListDepartamentoToAttach.getId());
                attachedDepartamentoList.add(departamentoListDepartamentoToAttach);
            }
            empresa.setDepartamentoList(attachedDepartamentoList);
            em.persist(empresa);
            if (idUbicacion != null) {
                idUbicacion.getEmpresaList().add(empresa);
                idUbicacion = em.merge(idUbicacion);
            }
            for (Departamento departamentoListDepartamento : empresa.getDepartamentoList()) {
                Empresa oldRutEmpresaOfDepartamentoListDepartamento = departamentoListDepartamento.getRutEmpresa();
                departamentoListDepartamento.setRutEmpresa(empresa);
                departamentoListDepartamento = em.merge(departamentoListDepartamento);
                if (oldRutEmpresaOfDepartamentoListDepartamento != null) {
                    oldRutEmpresaOfDepartamentoListDepartamento.getDepartamentoList().remove(departamentoListDepartamento);
                    oldRutEmpresaOfDepartamentoListDepartamento = em.merge(oldRutEmpresaOfDepartamentoListDepartamento);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEmpresa(empresa.getRut()) != null) {
                throw new PreexistingEntityException("Empresa " + empresa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empresa empresa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empresa persistentEmpresa = em.find(Empresa.class, empresa.getRut());
            Ubicacion idUbicacionOld = persistentEmpresa.getIdUbicacion();
            Ubicacion idUbicacionNew = empresa.getIdUbicacion();
            List<Departamento> departamentoListOld = persistentEmpresa.getDepartamentoList();
            List<Departamento> departamentoListNew = empresa.getDepartamentoList();
            List<String> illegalOrphanMessages = null;
            for (Departamento departamentoListOldDepartamento : departamentoListOld) {
                if (!departamentoListNew.contains(departamentoListOldDepartamento)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Departamento " + departamentoListOldDepartamento + " since its rutEmpresa field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUbicacionNew != null) {
                idUbicacionNew = em.getReference(idUbicacionNew.getClass(), idUbicacionNew.getId());
                empresa.setIdUbicacion(idUbicacionNew);
            }
            List<Departamento> attachedDepartamentoListNew = new ArrayList<Departamento>();
            for (Departamento departamentoListNewDepartamentoToAttach : departamentoListNew) {
                departamentoListNewDepartamentoToAttach = em.getReference(departamentoListNewDepartamentoToAttach.getClass(), departamentoListNewDepartamentoToAttach.getId());
                attachedDepartamentoListNew.add(departamentoListNewDepartamentoToAttach);
            }
            departamentoListNew = attachedDepartamentoListNew;
            empresa.setDepartamentoList(departamentoListNew);
            empresa = em.merge(empresa);
            if (idUbicacionOld != null && !idUbicacionOld.equals(idUbicacionNew)) {
                idUbicacionOld.getEmpresaList().remove(empresa);
                idUbicacionOld = em.merge(idUbicacionOld);
            }
            if (idUbicacionNew != null && !idUbicacionNew.equals(idUbicacionOld)) {
                idUbicacionNew.getEmpresaList().add(empresa);
                idUbicacionNew = em.merge(idUbicacionNew);
            }
            for (Departamento departamentoListNewDepartamento : departamentoListNew) {
                if (!departamentoListOld.contains(departamentoListNewDepartamento)) {
                    Empresa oldRutEmpresaOfDepartamentoListNewDepartamento = departamentoListNewDepartamento.getRutEmpresa();
                    departamentoListNewDepartamento.setRutEmpresa(empresa);
                    departamentoListNewDepartamento = em.merge(departamentoListNewDepartamento);
                    if (oldRutEmpresaOfDepartamentoListNewDepartamento != null && !oldRutEmpresaOfDepartamentoListNewDepartamento.equals(empresa)) {
                        oldRutEmpresaOfDepartamentoListNewDepartamento.getDepartamentoList().remove(departamentoListNewDepartamento);
                        oldRutEmpresaOfDepartamentoListNewDepartamento = em.merge(oldRutEmpresaOfDepartamentoListNewDepartamento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empresa.getRut();
                if (findEmpresa(id) == null) {
                    throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.");
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
            Empresa empresa;
            try {
                empresa = em.getReference(Empresa.class, id);
                empresa.getRut();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empresa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Departamento> departamentoListOrphanCheck = empresa.getDepartamentoList();
            for (Departamento departamentoListOrphanCheckDepartamento : departamentoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empresa (" + empresa + ") cannot be destroyed since the Departamento " + departamentoListOrphanCheckDepartamento + " in its departamentoList field has a non-nullable rutEmpresa field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Ubicacion idUbicacion = empresa.getIdUbicacion();
            if (idUbicacion != null) {
                idUbicacion.getEmpresaList().remove(empresa);
                idUbicacion = em.merge(idUbicacion);
            }
            em.remove(empresa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empresa> findEmpresaEntities() {
        return findEmpresaEntities(true, -1, -1);
    }

    public List<Empresa> findEmpresaEntities(int maxResults, int firstResult) {
        return findEmpresaEntities(false, maxResults, firstResult);
    }

    private List<Empresa> findEmpresaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empresa.class));
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

    public Empresa findEmpresa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empresa.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpresaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empresa> rt = cq.from(Empresa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
