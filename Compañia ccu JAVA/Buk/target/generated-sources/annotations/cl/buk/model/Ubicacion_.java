package cl.buk.model;

import cl.buk.model.Departamento;
import cl.buk.model.Empresa;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-12-06T09:28:23", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Ubicacion.class)
public class Ubicacion_ { 

    public static volatile ListAttribute<Ubicacion, Departamento> departamentoList;
    public static volatile SingularAttribute<Ubicacion, Integer> numero;
    public static volatile ListAttribute<Ubicacion, Empresa> empresaList;
    public static volatile SingularAttribute<Ubicacion, String> calle;
    public static volatile SingularAttribute<Ubicacion, Integer> id;
    public static volatile SingularAttribute<Ubicacion, String> provincia;
    public static volatile SingularAttribute<Ubicacion, String> region;

}