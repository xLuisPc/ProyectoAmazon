package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CrearOrdenCompra implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String producto = (String) execution.getVariable("producto");
        int cantidad = ((Number) execution.getVariable("cantidadSolicitada")).intValue();
        String proveedor = "Proveedor Prueba";

        // Generar una ID de orden de forma simple
        String ordenCompraId = "OC-" + System.currentTimeMillis();

        execution.setVariable("ordenCompraId", ordenCompraId);
        execution.setVariable("proveedor", proveedor);
        execution.setVariable("productoOrdenado", producto);
        execution.setVariable("cantidadOrdenada", cantidad);

        System.out.println("Orden de compra creada:");
        System.out.println("ID: " + ordenCompraId);
        System.out.println("Proveedor: " + proveedor);
        System.out.println("Producto: " + producto);
        System.out.println("Cantidad: " + cantidad);
    }
}
