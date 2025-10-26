package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;

import java.util.List;
import java.util.Optional;

public class UsuarioService extends BaseService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> listar() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            handleError(e, "Error al listar usuarios");
            return List.of();
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        try {
            return Optional.ofNullable(usuarioRepository.findById(id));
        } catch (Exception e) {
            handleError(e, "Error al buscar usuario por id");
            return Optional.empty();
        }
    }

    @Override
    public OperationResult crear(Usuario nuevo) {
        try {
            if (nuevo == null) return OperationResult.failure("Usuario requerido");

            normalizar(nuevo);

            // Bean Validation (anotaciones en la entidad)
            OperationResult v = ValidationUtil.validate(nuevo);
            if (!v.isSuccess()) return v;

           // ===== Reglas de negocio =====
            // Email único (case-insensitive)
            if (nuevo.getCorreo() != null &&
                usuarioRepository.existsByCorreoIgnoreCase(nuevo.getCorreo())) {
                return OperationResult.failure("Ya existe un usuario con ese correo");
            }

            usuarioRepository.save(nuevo);
            logger.info("Usuario creado id={} correo={}", nuevo.getId(), nuevo.getCorreo());
            return OperationResult.success("Usuario creado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al crear usuario");
            return OperationResult.failure("No se pudo crear el usuario");
        }
    }

    @Override
    public OperationResult actualizar(Long id, Usuario cambios) {
        try {
            Usuario existente = usuarioRepository.findById(id);
            if (existente == null) return OperationResult.failure("Usuario no encontrado");

            if (cambios == null) return OperationResult.failure("Datos de usuario requeridos");

            normalizar(cambios);

            // Bean Validation
            OperationResult v = ValidationUtil.validate(cambios);
            if (!v.isSuccess()) return v;

            // ===== Reglas de negocio =====
            // Si el correo cambia, validar único (case-insensitive)
            String correoNuevo = cambios.getCorreo();
            if (correoNuevo != null && !correoNuevo.equalsIgnoreCase(existente.getCorreo())) {
                // preferible buscar el usuario con ese correo y chequear id distinto
                Usuario conMismoCorreo = usuarioRepository.findByCorreoIgnoreCase(correoNuevo);
                if (conMismoCorreo != null && !conMismoCorreo.getId().equals(existente.getId())) {
                    return OperationResult.failure("Ya existe otro usuario con ese correo");
                }
            }

            // Aplicar cambios simples (ajusta según tus campos)
            if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
            if (cambios.getCorreo() != null) existente.setCorreo(cambios.getCorreo());
            if (cambios.getContrasena() != null) existente.setContrasena(cambios.getContrasena());
            if (cambios.getRol() != null) existente.setRol(cambios.getRol());

            usuarioRepository.save(existente);
            logger.info("Usuario actualizado id={} correo={}", existente.getId(), existente.getCorreo());
            return OperationResult.success("Usuario actualizado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al actualizar usuario");
            return OperationResult.failure("No se pudo actualizar el usuario");
        }
    }

    @Override
    public OperationResult eliminar(Long id) {
        try {
            Usuario existente = usuarioRepository.findById(id);
            if (existente == null) return OperationResult.failure("Usuario no encontrado");

            usuarioRepository.delete(id);
            logger.info("Usuario eliminado id={}", id);
            return OperationResult.success("Usuario eliminado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al eliminar usuario");
            return OperationResult.failure("No se pudo eliminar el usuario");
        }
    }

    // -------- Helpers --------
    private void normalizar(Usuario u) {
        if (u.getNombre() != null) u.setNombre(u.getNombre().trim());
        if (u.getCorreo() != null) u.setCorreo(u.getCorreo().trim());
        if (u.getRol() != null) u.setRol(u.getRol().trim());
        // (No forzamos lower-case para guardar; la comparación ya es ignore-case)
    }
}
