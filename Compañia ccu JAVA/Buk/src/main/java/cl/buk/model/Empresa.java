/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author joan.toro
 */
@Entity
@Table(name = "Empresa")
@NamedQueries({
    @NamedQuery(name = "Empresa.findAll", query = "SELECT e FROM Empresa e"),
    @NamedQuery(name = "Empresa.findByRut", query = "SELECT e FROM Empresa e WHERE e.rut = :rut"),
    @NamedQuery(name = "Empresa.findByDv", query = "SELECT e FROM Empresa e WHERE e.dv = :dv"),
    @NamedQuery(name = "Empresa.findByRazonSocial", query = "SELECT e FROM Empresa e WHERE e.razonSocial = :razonSocial")})
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Rut")
    private Integer rut;
    @Basic(optional = false)
    @Column(name = "Dv")
    private String dv;
    @Basic(optional = false)
    @Column(name = "RazonSocial")
    private String razonSocial;
    @JoinColumn(name = "IdUbicacion", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Ubicacion idUbicacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "rutEmpresa")
    private List<Departamento> departamentoList;

    public Empresa() {
    }

    public Empresa(Integer rut) {
        this.rut = rut;
    }

    public Empresa(Integer rut, String dv, String razonSocial) {
        this.rut = rut;
        this.dv = dv;
        this.razonSocial = razonSocial;
    }

    public Integer getRut() {
        return rut;
    }

    public void setRut(Integer rut) {
        this.rut = rut;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Ubicacion getIdUbicacion() {
        return idUbicacion;
    }
    
    public void setIdUbicacion(Ubicacion idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

    public void setDepartamentoList(List<Departamento> departamentoList) {
        this.departamentoList = departamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rut != null ? rut.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empresa)) {
            return false;
        }
        Empresa other = (Empresa) object;
        if ((this.rut == null && other.rut != null) || (this.rut != null && !this.rut.equals(other.rut))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.buk.model.Empresa[ rut=" + rut + " ]";
    }
    
}
