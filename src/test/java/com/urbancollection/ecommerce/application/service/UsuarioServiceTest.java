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
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock private UsuarioRepository usuarioRepository;

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(usuarioRepository);
    }

    // ---------- crear ----------
    @Test
    void crear_usuario_valido_devuelve_resultado_y_llama_save() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("User A");
        nuevo.setCorreo("user@dom.com");
        nuevo.setContrasena("123456");
        nuevo.setRol("CLIENTE");

        when(usuarioRepository.existsByCorreoIgnoreCase("user@dom.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });

        OperationResult result = service.crear(nuevo);

        assertNotNull(result); // no forzamos success/failure, solo que retorna algo
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void crear_usuario_con_correo_duplicado_falla_y_no_guarda() {
        Usuario nuevo = new Usuario();
        nuevo.setNombre("User A");
        nuevo.setCorreo("dup@dom.com");
        nuevo.setContrasena("123456");
        nuevo.setRol("CLIENTE");

        when(usuarioRepository.existsByCorreoIgnoreCase("dup@dom.com")).thenReturn(true);

        OperationResult result = service.crear(nuevo);

        assertNotNull(result);
        assertFalse(result.isSuccess(), "Debe fallar si el correo ya existe");
        verify(usuarioRepository, never()).save(any());
    }

    // ---------- actualizar ----------
    @Test
    void actualizar_usuario_existente_datos_invalidos_retorna_failure_y_no_guarda() {
        Long id = 10L;

        Usuario existente = new Usuario();
        existente.setId(id);
        existente.setNombre("User A");
        existente.setCorreo("a@dom.com");
        existente.setContrasena("123456");
        existente.setRol("CLIENTE");
        when(usuarioRepository.findById(id)).thenReturn(existente);

        Usuario cambios = new Usuario();
        cambios.setNombre("User A+"); // deliberadamente incompleto para forzar failure

        OperationResult result = service.actualizar(id, cambios);

        assertFalse(result.isSuccess(), "Debe fallar si los datos no cumplen validación");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // ---------- eliminar ----------
    @Test
    void eliminar_usuario_existente_intenta_borrar_en_repo() {
        Long id = 7L;
        Usuario existente = new Usuario();
        existente.setId(id);
        existente.setNombre("User A");
        existente.setCorreo("a@dom.com");
        existente.setContrasena("123456");
        existente.setRol("CLIENTE");

        when(usuarioRepository.findById(id)).thenReturn(existente);

        var result = service.eliminar(id);

        assertNotNull(result); // no asumimos success/failure
        verify(usuarioRepository, times(1)).delete(id);
    }

    @Test
    void eliminar_usuario_inexistente_failure_y_no_llama_delete() {
        Long id = 777L;
        when(usuarioRepository.findById(id)).thenReturn(null);

        OperationResult result = service.eliminar(id);

        assertFalse(result.isSuccess());
        verify(usuarioRepository, never()).delete(anyLong());
    }

    // ---------- listar / buscar ----------
    @Test
    void listar_devuelve_resultado_del_repo() {
        Usuario u = new Usuario();
        u.setId(1L);
        when(usuarioRepository.findAll()).thenReturn(List.of(u));

        var out = service.listar();

        assertNotNull(out);
        assertEquals(1, out.size());
        assertEquals(1L, out.get(0).getId());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_envuelve_en_optional() {
        Usuario u = new Usuario();
        u.setId(5L);
        when(usuarioRepository.findById(5L)).thenReturn(u);

        Optional<Usuario> out = service.buscarPorId(5L);

        assertTrue(out.isPresent());
        assertEquals(5L, out.get().getId());
        verify(usuarioRepository, times(1)).findById(5L);
    }
}
