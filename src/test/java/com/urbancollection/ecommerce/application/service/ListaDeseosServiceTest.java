package com.urbancollection.ecommerce.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.urbancollection.ecommerce.domain.repository.ListaDeseosRepository;
// importa otros puertos si tu service los usa:
// import com.urbancollection.ecommerce.shared.logging.LoggerPort;
// import com.urbancollection.ecommerce.shared.tasks.TaskListPort;

@ExtendWith(MockitoExtension.class)
class ListaDeseosServiceTest {

    @Mock private ListaDeseosRepository listaDeseosRepository;
    // @Mock private LoggerPort logger;
    // @Mock private TaskListPort taskListPort;

    @BeforeEach
    void setUp() {
        new ListaDeseosService(listaDeseosRepository, null, null);
    }

    @Test
    void crearLista_para_usuario_valido_guarda_y_retorna_DTO() { /* TODO */ }

    @Test
    void agregarProducto_evita_duplicados_en_la_misma_lista() { /* TODO */ }

    @Test
    void eliminarProducto_quita_item_de_la_lista() { /* TODO */ }

    @Test
    void obtenerLista_por_usuario_devuelve_DTO_correcto() { /* TODO */ }
}
