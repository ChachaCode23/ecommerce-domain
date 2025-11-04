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
import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;
import com.urbancollection.ecommerce.domain.repository.TransaccionPagoRepository;

@ExtendWith(MockitoExtension.class)
class TransaccionPagoServiceTest {

    @Mock private TransaccionPagoRepository repo;

    private TransaccionPagoService service;

    @BeforeEach
    void setUp() {
        service = new TransaccionPagoService(repo);
    }

    @Test
    void crear_valido_success_y_guarda() {
        when(repo.save(any(TransaccionPago.class))).thenAnswer(inv -> {
            TransaccionPago x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        OperationResult r = service.crear(new TransaccionPago());

        assertNotNull(r);
        assertTrue(r.isSuccess());
        verify(repo, times(1)).save(any(TransaccionPago.class));
    }

    @Test
    void crear_null_failure_y_no_guarda() {
        OperationResult r = service.crear(null);

        assertNotNull(r);
        assertFalse(r.isSuccess());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_existente_success_y_save() {
        Long id = 10L;
        when(repo.findById(id)).thenReturn(new TransaccionPago());
        when(repo.save(any(TransaccionPago.class))).thenAnswer(inv -> inv.getArgument(0));

        OperationResult r = service.actualizar(id, new TransaccionPago());

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(TransaccionPago.class));
    }

    @Test
    void actualizar_inexistente_failure() {
        Long id = 999L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.actualizar(id, new TransaccionPago());

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_existente_success() {
        Long id = 7L;
        TransaccionPago t = new TransaccionPago(); t.setId(id);
        when(repo.findById(id)).thenReturn(t);

        OperationResult r = service.eliminar(id);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        // el service usa repository.delete(id) (nota en tu código) :contentReference[oaicite:2]{index=2}
    }

    @Test
    void eliminar_inexistente_failure() {
        Long id = 7L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.eliminar(id);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void listar_y_buscarPorId_delegan_al_repo() {
        TransaccionPago t = new TransaccionPago(); t.setId(1L);
        when(repo.findAll()).thenReturn(List.of(t));
        when(repo.findById(1L)).thenReturn(t);

        var all = service.listar();
        Optional<TransaccionPago> uno = service.buscarPorId(1L);

        assertNotNull(all);
        assertEquals(1, all.size());
        assertTrue(uno.isPresent());
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).findById(1L);
    }
}
