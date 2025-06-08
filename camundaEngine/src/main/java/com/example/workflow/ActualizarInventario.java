package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class ActualizarInventario implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String producto = (String) execution.getVariable("producto");
        int cantidadRecibida = ((Number) execution.getVariable("cantidadSolicitada")).intValue();
        String almacen = (String) execution.getVariable("almacenSolicitante");

        // Obtener o crear el inventario global (por proceso)
        Map<String, Integer> inventario = (Map<String, Integer>) execution.getVariable("inventario");
        if (inventario == null) {
            inventario = new HashMap<>();
        }

        // Clave del inventario combinando almacén y producto
        String clave = almacen + ":" + producto;

        // Obtener cantidad actual (si existe)
        int cantidadActual = inventario.getOrDefault(clave, 0);

        // Actualizar con la nueva cantidad
        int nuevaCantidad = cantidadActual + cantidadRecibida;
        inventario.put(clave, nuevaCantidad);

        // Guardar el inventario actualizado como variable de proceso
        execution.setVariable("inventario", inventario);

        System.out.println("Inventario actualizado:");
        System.out.println("Almacén: " + almacen + ", Producto: " + producto);
        System.out.println("Cantidad anterior: " + cantidadActual + ", Recibida: " + cantidadRecibida + ", Total: " + nuevaCantidad);
    }
}