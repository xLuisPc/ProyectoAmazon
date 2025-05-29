package com.example.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class imprimirEtiqueta implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Extraer variables del modelo BPMN
        String nombre = (String) execution.getVariable("nombre");
        String pais = (String) execution.getVariable("pais");
        String estado = (String) execution.getVariable("estado");
        String ciudad = (String) execution.getVariable("ciudad");
        String direccion = (String) execution.getVariable("direccion");
        String tipoEntrega = (String) execution.getVariable("tipoEntrega"); // viene de un radio group

        // Determinar si el envío es internacional basado en el valor seleccionado
        boolean esInternacional = tipoEntrega.equalsIgnoreCase("internacional");

        // Guardar el tipo de envío como variable para decisiones posteriores en el modelo
        execution.setVariable("esInternacional", esInternacional);

        // Imprimir la etiqueta
        System.out.println("======= ETIQUETA DE ENVÍO =======");
        System.out.println("Nombre:        " + nombre);
        System.out.println("Dirección:     " + direccion);
        System.out.println("Ciudad:        " + ciudad);
        System.out.println("Estado:        " + estado);
        System.out.println("País:          " + pais);
        System.out.println("Tipo de Entrega: " + tipoEntrega);
        System.out.println("Tipo de Envío: " + (esInternacional ? "Internacional" : "Nacional"));
        System.out.println("=================================");
    }
}
