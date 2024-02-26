/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cl.buk.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author joan.toro
 */
@Entity
@Table(name = "Honorario")
@NamedQueries({
    @NamedQuery(name = "Honorario.findAll", query = "SELECT h FROM Honorario h"),
    @NamedQuery(name = "Honorario.findById", query = "SELECT h FROM Honorario h WHERE h.id = :id"),
    @NamedQuery(name = "Honorario.findByValorHora", query = "SELECT h FROM Honorario h WHERE h.valorHora = :valorHora")})
public class Honorario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ValorHora")
    private int valorHora;
    @JoinColumn(name = "RutEmpleado", referencedColumnName = "Rut")
    @ManyToOne(optional = false)
    private Empleado rutEmpleado;

    public Honorario() {
    }

    public Honorario(Integer id) {
        this.id = id;
    }

    public Honorario(Integer id, int valorHora) {
        this.id = id;
        this.valorHora = valorHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getValorHora() {
        return valorHora;
    }

    public void setValorHora(int valorHora) {
        this.valorHora = valorHora;
    }

    public Empleado getRutEmpleado() {
        return rutEmpleado;
    }

    public void setRutEmpleado(Empleado rutEmpleado) {
        this.rutEmpleado = rutEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Honorario)) {
            return false;
        }
        Honorario other = (Honorario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cl.buk.model.Honorario[ id=" + id + " ]";
    }
    
}
