package com.urbancollection.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ItemPedidoRepository;

@ExtendWith(MockitoExtension.class)
class ItemPedidoServiceTest {

    @Mock private ItemPedidoRepository repo;

    private ItemPedidoService service;

    @BeforeEach
    void setUp() {
        service = new ItemPedidoService(repo);
    }

    @Test
    void crear_valido_guarda_y_success() {
        ItemPedido it = new ItemPedido();
        when(repo.save(any(ItemPedido.class))).thenAnswer(inv -> {
            ItemPedido x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        OperationResult r = service.crear(it);

        assertNotNull(r);
        assertTrue(r.isSuccess());
        verify(repo, times(1)).save(any(ItemPedido.class));
    }

    @Test
    void crear_null_failure_y_no_guarda() {
        OperationResult r = service.crear(null);

        assertNotNull(r);
        assertFalse(r.isSuccess());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_existente_con_cambios_success() {
        Long id = 10L;
        ItemPedido existente = new ItemPedido(); existente.setId(id);
        ItemPedido cambios = new ItemPedido();

        when(repo.findById(id)).thenReturn(existente);
        when(repo.save(any(ItemPedido.class))).thenAnswer(inv -> inv.getArgument(0));

        OperationResult r = service.actualizar(id, cambios);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(ItemPedido.class));
    }

    @Test
    void actualizar_inexistente_failure_y_no_guarda() {
        Long id = 99L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.actualizar(id, new ItemPedido());

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_cambios_null_failure_y_no_guarda() {
        Long id = 11L;
        when(repo.findById(id)).thenReturn(new ItemPedido());

        OperationResult r = service.actualizar(id, null);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_existente_success() {
        Long id = 7L;
        ItemPedido existente = new ItemPedido(); existente.setId(id);
        when(repo.findById(id)).thenReturn(existente);

        OperationResult r = service.eliminar(id);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void eliminar_inexistente_failure() {
        Long id = 777L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.eliminar(id);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void listar_y_buscarPorId_delegan_al_repo() {
        ItemPedido it = new ItemPedido(); it.setId(1L);
        when(repo.findAll()).thenReturn(List.of(it));
        when(repo.findById(1L)).thenReturn(it);

        var all = service.listar();
        Optional<ItemPedido> uno = service.buscarPorId(1L);

        assertEquals(1, all.size());
        assertTrue(uno.isPresent());
        assertEquals(1L, uno.get().getId());
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).findById(1L);
    }
}
