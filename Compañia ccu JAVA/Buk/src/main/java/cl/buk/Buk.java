package cl.buk;

import cl.buk.controller.EmpresaJpaController;
import cl.buk.model.Empresa;

public class Buk {

    public static void main(String[] args) throws Exception {
        
        try {
            
            Empresa e = new Empresa(11111111, "1", "Empresa Test SpA");
            EmpresaJpaController controller = new EmpresaJpaController();
            controller.create(e);   
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
