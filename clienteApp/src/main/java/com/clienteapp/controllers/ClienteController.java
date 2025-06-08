package com.clienteapp.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.clienteapp.services.CamundaService;

@Controller
public class ClienteController {

    @Autowired
    private CamundaService camundaService;

    private final String clienteUserId = "cliente1"; // para pruebas

    @GetMapping("/")
    public String mostrarInicio() {
        return "index"; // Carga el index.html de Thymeleaf
    }    

    @GetMapping("/tareas")
    public String verTareas(Model model) {
        CamundaService.Task[] tasks = camundaService.getTasksByAssignee(clienteUserId);
        model.addAttribute("tasks", tasks);
        return "tareas"; // plantilla Thymeleaf tareas.html
    }

    @PostMapping("/completar")
    public String completarTarea(@RequestParam String taskId) {
        camundaService.completeTask(taskId);
        return "redirect:/tareas";
    }

    @GetMapping("/formulario-inicio")
    public String mostrarFormulario(Model model) {
        String formKey = camundaService.getStartFormKey("Devoluciones_Amazon");
        model.addAttribute("formKey", formKey);
        return "solicitud-cliente"; 
    }

    @PostMapping("/start-process")
    public String iniciarProceso(@RequestParam Map<String, String> allParams, Model model) {
        try {
            Map<String, Object> variables = new HashMap<>();

            // Mapea nombre, fecha, motivo (puedes hacer esto dinámico si quieres)
            variables.put("nombre", allParams.get("nombreComprador"));
            String fechaStr = allParams.get("fechaCompra");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                Date fecha = new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
                variables.put("fechaCompra", fecha);
            }
            variables.put("motivo", allParams.get("motivo"));

            String proceso = allParams.get("proceso"); // viene oculto desde el formulario
            camundaService.startProcessWithVariables(proceso, variables);

            model.addAttribute("mensaje", "Proceso iniciado correctamente.");
            return "proceso-iniciado";

        } catch (Exception e) {
            model.addAttribute("mensaje", "Error: " + e.getMessage());
            return "error";
        }
    }

}
