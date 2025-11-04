package com.urbancollection.ecommerce.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

@ExtendWith(MockitoExtension.class)
class CuponServiceTest {

    @Mock private CuponRepository repo;

    private CuponService service;

    @BeforeEach
    void setUp() {
        service = new CuponService(repo);
    }

    @Test
    void crear_valido_success_y_guarda() {
        when(repo.save(any(Cupon.class))).thenAnswer(inv -> {
            Cupon x = inv.getArgument(0);
            x.setId(1L);
            return x;
        });

        OperationResult r = service.crear(new Cupon());

        assertNotNull(r);
        assertTrue(r.isSuccess());
        verify(repo, times(1)).save(any(Cupon.class));
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
        when(repo.findById(id)).thenReturn(new Cupon());
        when(repo.save(any(Cupon.class))).thenAnswer(inv -> inv.getArgument(0));

        OperationResult r = service.actualizar(id, new Cupon());

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(any(Cupon.class));
    }

    @Test
    void actualizar_inexistente_failure() {
        Long id = 999L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.actualizar(id, new Cupon());

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any());
    }

    @Test
    void eliminar_existente_success_y_deleteById() {
        Long id = 7L;
        Cupon c = new Cupon(); c.setId(id);
        when(repo.findById(id)).thenReturn(c);

        OperationResult r = service.eliminar(id);

        assertTrue(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).deleteById(id); // tu repo usa deleteById
    }

    @Test
    void eliminar_inexistente_failure() {
        Long id = 7L;
        when(repo.findById(id)).thenReturn(null);

        OperationResult r = service.eliminar(id);

        assertFalse(r.isSuccess());
        verify(repo, times(1)).findById(id);
        verify(repo, never()).deleteById(anyLong());
    }

    @Test
    void listar_y_buscarPorId_delegan_al_repo() {
        Cupon c = new Cupon(); c.setId(1L);
        when(repo.findAll()).thenReturn(List.of(c));
        when(repo.findById(1L)).thenReturn(c);

        var all = service.listar();
        Optional<Cupon> uno = service.buscarPorId(1L);

        assertEquals(1, all.size());
        assertTrue(uno.isPresent());
        assertEquals(1L, uno.get().getId());
        verify(repo, times(1)).findAll();
        verify(repo, times(1)).findById(1L);
    }
}
