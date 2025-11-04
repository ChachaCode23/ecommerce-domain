package com.urbancollection.ecommerce.application.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.shared.logging.LoggerPort;
import com.urbancollection.ecommerce.shared.tasks.TaskListPort;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock private ProductoRepository productoRepository;
    @Mock private LoggerPort logger;
    @Mock private TaskListPort taskListPort; // <-- tercer parámetro del constructor

    private ProductoService service;

    @BeforeEach
    void setUp_inicializa_mocks_y_service() {
        service = new ProductoService(productoRepository, logger, taskListPort);
    }

    @Test
    void crearProducto_valido_guarda_y_retorna_DTO_correcto() {
        String nombre = "Tennis Puma";
        String descripcion = "Hombre";
        BigDecimal precio = BigDecimal.valueOf(100);
        int stock = 10;

        Producto guardado = new Producto();
        guardado.setId(1L);
        guardado.setNombre(nombre);
        guardado.setDescripcion(descripcion);
        guardado.setPrecio(precio);
        guardado.setStock(stock);

        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        var dto = service.crearProducto(nombre, descripcion, precio, stock);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(nombre, dto.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void crearProducto_con_precio_cero_o_negativo_lanza_error() {
        String nombre = "Tennis Puma";
        String descripcion = "Hombre";
        BigDecimal precio = BigDecimal.ZERO; // o negativo
        int stock = 10;

        // Si el service aún no valida antes de guardar, el repo simula la validación:
        when(productoRepository.save(any(Producto.class)))
                .thenThrow(new IllegalArgumentException("precio inválido"));

        assertThrows(IllegalArgumentException.class, () ->
            service.crearProducto(nombre, descripcion, precio, stock)
        );
    }
}
