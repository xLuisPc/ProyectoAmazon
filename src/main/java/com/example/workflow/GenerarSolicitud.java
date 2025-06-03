package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;
import java.util.Map;

public class GenerarSolicitud implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // Variables del formulario de inicio
        String producto = (String) execution.getVariable("producto");
        int cantidadInventario = (int) execution.getVariable("cantidadInventario");
        String almacen = "Almacen Sur"; // fijo por ahora, como en tus pruebas

        // Crear o actualizar inventario
        Map<String, Integer> inventario = (Map<String, Integer>) execution.getVariable("inventario");
        if (inventario == null) {
            inventario = new HashMap<>();
        }

        // Guardar cantidad inicial en el inventario
        String clave = almacen + ":" + producto;
        inventario.put(clave, cantidadInventario);

        // Variables necesarias para el resto del proceso
        execution.setVariable("producto", producto);
        execution.setVariable("cantidadSolicitada", 4); // valor fijo como dijiste
        execution.setVariable("almacenSolicitante", almacen);
        execution.setVariable("inventario", inventario);
    }
}