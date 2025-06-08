package com.clienteapp.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CamundaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String camundaUrl = "http://localhost:8080/engine-rest";

    // Obtener tareas asignadas a un usuario
    public Task[] getTasksByAssignee(String assignee) {
        String url = camundaUrl + "/task?assignee=" + assignee;
        return restTemplate.getForObject(url, Task[].class);
    }

    // Completar tarea
    public void completeTask(String taskId) {
        String url = camundaUrl + "/task/" + taskId + "/complete";
        restTemplate.postForObject(url, null, Void.class);
    }

    // Nueva clase para mapear la respuesta del startForm
    public static class FormResponse {
        private String key;
        private String contextPath;

        public String getKey() { return key; }
        public void setKey(String key) { this.key = key; }
        public String getContextPath() { return contextPath; }
        public void setContextPath(String contextPath) { this.contextPath = contextPath; }
    }

    // Nuevo método para obtener el formKey del evento de inicio
    public String getStartFormKey(String processKey) {
        String url = camundaUrl + "/process-definition/key/" + processKey + "/startForm";
        FormResponse response = restTemplate.getForObject(url, FormResponse.class);
        return response != null ? response.getKey() : null;
    }

    // Modela clase Task según el JSON de Camunda o usa Map
    public static class Task {
        private String id;
        private String name;
        // getters y setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public void startProcessWithVariables(String processKey, Map<String, Object> variables) {
        String url = camundaUrl + "/process-definition/key/" + processKey + "/start";

        Map<String, Object> body = new HashMap<>();
        Map<String, Object> vars = new HashMap<>();

    for (Map.Entry<String, Object> entry : variables.entrySet()) {
        Map<String, Object> varInfo = new HashMap<>();
        Object val = entry.getValue();

        if (val instanceof Date) {
            // Formatear la fecha a ISO 8601 con zona horaria
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String isoDate = isoFormat.format((Date) val);

            varInfo.put("value", isoDate);
            varInfo.put("type", "Date");
        } else if (val instanceof String) {
            varInfo.put("value", val);
            varInfo.put("type", "String");
        } else if (val instanceof Integer) {
            varInfo.put("value", val);
            varInfo.put("type", "Integer");
        } else {
            varInfo.put("value", val);
            varInfo.put("type", "Object"); // fallback
        }

        vars.put(entry.getKey(), varInfo);
    }
        body.put("variables", vars);

        System.out.println("Enviando a Camunda: " + body); // LOG IMPORTANTE

        restTemplate.postForObject(url, body, String.class);
    }

}
