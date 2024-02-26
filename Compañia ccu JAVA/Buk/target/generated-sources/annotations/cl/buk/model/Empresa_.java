package cl.buk.model;

import cl.buk.model.Departamento;
import cl.buk.model.Ubicacion;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-12-06T09:28:23", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Empresa.class)
public class Empresa_ { 

    public static volatile SingularAttribute<Empresa, Integer> rut;
    public static volatile ListAttribute<Empresa, Departamento> departamentoList;
    public static volatile SingularAttribute<Empresa, String> dv;
    public static volatile SingularAttribute<Empresa, String> razonSocial;
    public static volatile SingularAttribute<Empresa, Ubicacion> idUbicacion;

}