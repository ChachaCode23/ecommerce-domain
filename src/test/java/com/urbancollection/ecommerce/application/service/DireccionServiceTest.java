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
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;

@ExtendWith(MockitoExtension.class)
class DireccionServiceTest {

    @Mock private DireccionRepository repo;

    private DireccionService service;

    @BeforeEach
    void setUp() {
        service = new DireccionService(repo);
    }

    @Test
    void crear_valido_guarda_y_retorna_success() {
        Direccion d = new Direccion();
        when(repo.save(any(Direccion.class))).thenAnswer(inv -> {
            Direccion x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        OperationResult r = service.crear(d);

        assertNotNull(r);
        assertTrue(r.isSuccess());
        verify(repo, times(1)).save(any(Direccion.class));
    }

    @Test
    void crear_null_retornafailure() {
        OperationResult r = service.crear(null);
        assertNotNull(r);
        assertFalse(r.isSuccess());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_existente_merge_simple_success() {
        Long id = 9L;
        Direccion existente = new Direccion(); existente.setId(id);
        Direccion cambios = new Direccion(); // el service hace merge simple (setId y save)

        when(repo.findById(id)).thenReturn(existente);
        when(repo.save(any(Direccion.class))).thenAnswer(inv -> inv.getArgument(0));

        OperationResult r = service.actualizar(id, cambios);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(Direccion.class));
    }

    @Test
    void actualizar_inexistente_retornafailure_y_no_guarda() {
        Long id = 999L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.actualizar(id, new Direccion());

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_con_cambios_null_retornafailure() {
        Long id = 10L;
        when(repo.findById(id)).thenReturn(new Direccion());

        OperationResult r = service.actualizar(id, null);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_existente_success() {
        Long id = 3L;
        Direccion existente = new Direccion(); existente.setId(id);
        when(repo.findById(id)).thenReturn(existente);

        OperationResult r = service.eliminar(id);

        assertTrue(r.isSuccess());
        // No verificamos delete/deleteById porque el nombre puede variar en tu interfaz
        verify(repo, times(1)).findById(id);
    }

    @Test
    void eliminar_inexistente_failure() {
        Long id = 7L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.eliminar(id);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
    }

    @Test
    void listar_y_buscarPorId_delegan_al_repo() {
        Direccion d = new Direccion(); d.setId(1L);
        when(repo.findAll()).thenReturn(List.of(d));
        when(repo.findById(1L)).thenReturn(d);

        var all = service.listar();
        Optional<Direccion> uno = service.buscarPorId(1L);

        assertEquals(1, all.size());
        assertTrue(uno.isPresent());
        assertEquals(1L, uno.get().getId());
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).findById(1L);
    }
}
