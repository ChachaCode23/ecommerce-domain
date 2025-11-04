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
import com.urbancollection.ecommerce.domain.entity.logistica.Envio;
import com.urbancollection.ecommerce.domain.repository.EnvioRepository;

@ExtendWith(MockitoExtension.class)
class EnvioServiceTest {

    @Mock private EnvioRepository repo;
    private EnvioService service;

    @BeforeEach
    void setUp() {
        service = new EnvioService(repo);
    }

    @Test
    void crear_valido_guarda_y_success() {
        Envio e = new Envio();
        when(repo.save(any(Envio.class))).thenAnswer(inv -> {
            Envio x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        OperationResult r = service.crear(e);

        assertNotNull(r);
        assertTrue(r.isSuccess());
        verify(repo, times(1)).save(any(Envio.class));
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
        Envio existente = new Envio(); existente.setId(id);
        Envio cambios = new Envio(); // merge simple en el service

        when(repo.findById(id)).thenReturn(existente);
        when(repo.save(any(Envio.class))).thenAnswer(inv -> inv.getArgument(0));

        OperationResult r = service.actualizar(id, cambios);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(Envio.class));
    }

    @Test
    void actualizar_inexistente_failure_y_no_guarda() {
        Long id = 99L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.actualizar(id, new Envio());

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_cambios_null_failure_y_no_guarda() {
        Long id = 11L;
        when(repo.findById(id)).thenReturn(new Envio());

        OperationResult r = service.actualizar(id, null);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_existente_success() {
        Long id = 7L;
        Envio existente = new Envio(); existente.setId(id);
        when(repo.findById(id)).thenReturn(existente);

        OperationResult r = service.eliminar(id);

        assertTrue(r.isSuccess());
        // No verifico delete/deleteById porque tu repo puede exponer cualquiera
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
        Envio e = new Envio(); e.setId(1L);
        when(repo.findAll()).thenReturn(List.of(e));
        when(repo.findById(1L)).thenReturn(e);

        var all = service.listar();
        Optional<Envio> uno = service.buscarPorId(1L);

        assertEquals(1, all.size());
        assertTrue(uno.isPresent());
        assertEquals(1L, uno.get().getId());
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).findById(1L);
    }
}
