package com.urbancollection.ecommerce.shared.tasks;

/**
 * Puerto para encolar tareas operativas / avisos.
 * Implementación dummy la damos en DependenciesConfig.
 */
public interface TaskListPort {

    /**
     * @param taskType etiqueta corta. Ej: "REVISAR_STOCK"
     * @param description detalle humano.
     */
    void enqueue(String taskType, String description);
}
