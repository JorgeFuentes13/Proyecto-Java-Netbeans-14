package cl.buk.model;

import cl.buk.model.Empleado;
import cl.buk.model.Empresa;
import cl.buk.model.Ubicacion;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-12-06T09:28:23", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Departamento.class)
public class Departamento_ { 

    public static volatile SingularAttribute<Departamento, String> sigla;
    public static volatile ListAttribute<Departamento, Empleado> empleadoList;
    public static volatile SingularAttribute<Departamento, Empresa> rutEmpresa;
    public static volatile SingularAttribute<Departamento, Ubicacion> idUbicacion;
    public static volatile SingularAttribute<Departamento, Integer> id;
    public static volatile SingularAttribute<Departamento, String> nombre;

}