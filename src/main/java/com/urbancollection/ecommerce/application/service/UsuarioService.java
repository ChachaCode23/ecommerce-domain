package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.logistica.Direccion;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.DireccionRepository;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;

import java.util.List;
import java.util.Optional;

// Servicio de aplicación para gestionar usuarios.
// Extiende BaseService para reutilizar el manejo de errores y el logger.
public class UsuarioService extends BaseService implements IUsuarioService {

    // Repositorio del dominio para acceder a los usuarios en la base de datos.
    private final UsuarioRepository usuarioRepository;
    // Repositorio para guardar y consultar direcciones asociadas a usuarios.
    private final DireccionRepository direccionRepository;

    // Constructor donde se inyectan los repositorios necesarios.
    public UsuarioService(UsuarioRepository usuarioRepository, DireccionRepository direccionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
    }

    @Override
    // Devuelve la lista de usuarios. Si hay error, lo captura y devuelve una lista vacía.
    public List<Usuario> listar() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            handleError(e, "Error al listar usuarios");
            return List.of();
        }
    }

    @Override
    // Busca un usuario por id y lo devuelve envuelto en Optional.
    public Optional<Usuario> buscarPorId(Long id) {
        try {
            return Optional.ofNullable(usuarioRepository.findById(id));
        } catch (Exception e) {
            handleError(e, "Error al buscar usuario por id");
            return Optional.empty();
        }
    }

    @Override
    // Crea un usuario sin dirección. Valida, normaliza y revisa que el correo no esté repetido.
    public OperationResult crear(Usuario nuevo) {
        try {
            if (nuevo == null) return OperationResult.failure("Usuario requerido");

            // Normalizo campos de texto antes de validar.
            normalizar(nuevo);

            // Uso la utilidad de validación genérica del proyecto.
            OperationResult v = ValidationUtil.validate(nuevo);
            if (!v.isSuccess()) return v;

            // Verifico si ya existe un usuario con el mismo correo (ignorando mayúsculas/minúsculas).
            if (nuevo.getCorreo() != null) {
                boolean existe = usuarioRepository.existsByCorreoIgnoreCase(nuevo.getCorreo());
                System.out.println("DEBUG: Verificando correo '" + nuevo.getCorreo() + "' - Existe: " + existe);
                
                if (existe) {
                    return OperationResult.failure("Ya existe un usuario con ese correo");
                }
            }

            // Si todo está bien, guardo el usuario.
            usuarioRepository.save(nuevo);
            if (logger != null) logger.info("Usuario creado id={} correo={}", nuevo.getId(), nuevo.getCorreo());
            return OperationResult.success("Usuario creado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al crear usuario");
            return OperationResult.failure("No se pudo crear el usuario");
        }
    }

    @Override
    // Crea un usuario y también una dirección asociada como principal.
    public OperationResult crearConDireccion(Usuario nuevo, Direccion direccion) {
        try {
            if (nuevo == null) return OperationResult.failure("Usuario requerido");
            if (direccion == null) return OperationResult.failure("Dirección requerida");

            // Normalizo campos del usuario.
            normalizar(nuevo);

            // Valido el usuario antes de guardar.
            OperationResult v = ValidationUtil.validate(nuevo);
            if (!v.isSuccess()) return v;

            // Reviso que el correo no esté repetido.
            if (nuevo.getCorreo() != null) {
                boolean existe = usuarioRepository.existsByCorreoIgnoreCase(nuevo.getCorreo());
                if (existe) {
                    return OperationResult.failure("Ya existe un usuario con ese correo");
                }
            }

            // Primero guardo el usuario.
            usuarioRepository.save(nuevo);

            // Luego creo la dirección asociada a ese usuario.
            direccion.setUsuarioId(nuevo.getId().intValue());
            direccion.setEsPrincipal(true);
            direccionRepository.save(direccion);

            return OperationResult.success("Usuario y dirección creados correctamente");
        } catch (Exception e) {
            handleError(e, "Error al crear usuario con dirección");
            return OperationResult.failure("No se pudo crear el usuario con dirección");
        }
    }

    @Override
    // Actualiza los datos de un usuario existente.
    public OperationResult actualizar(Long id, Usuario cambios) {
        try {
            Usuario existente = usuarioRepository.findById(id);
            if (existente == null) return OperationResult.failure("Usuario no encontrado");

            if (cambios == null) return OperationResult.failure("Datos de usuario requeridos");

            // Normalizo los datos nuevos antes de validar.
            normalizar(cambios);

            OperationResult v = ValidationUtil.validate(cambios);
            if (!v.isSuccess()) return v;

            // Valido que el nuevo correo no esté en uso por otro usuario.
            String correoNuevo = cambios.getCorreo();
            if (correoNuevo != null && !correoNuevo.equalsIgnoreCase(existente.getCorreo())) {
                Usuario conMismoCorreo = usuarioRepository.findByCorreoIgnoreCase(correoNuevo);
                if (conMismoCorreo != null && !conMismoCorreo.getId().equals(existente.getId())) {
                    return OperationResult.failure("Ya existe otro usuario con ese correo");
                }
            }

            // Solo actualizo los campos que no vienen nulos en "cambios".
            if (cambios.getNombre() != null) existente.setNombre(cambios.getNombre());
            if (cambios.getCorreo() != null) existente.setCorreo(cambios.getCorreo());
            if (cambios.getContrasena() != null) existente.setContrasena(cambios.getContrasena());
            if (cambios.getRol() != null) existente.setRol(cambios.getRol());

            usuarioRepository.save(existente);

            if (logger != null) logger.info("Usuario actualizado id={} correo={}", existente.getId(), existente.getCorreo());
            return OperationResult.success("Usuario actualizado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al actualizar usuario");
            return OperationResult.failure("No se pudo actualizar el usuario");
        }
    }

    @Override
    // Elimina un usuario por id si existe.
    public OperationResult eliminar(Long id) {
        try {
            Usuario existente = usuarioRepository.findById(id);
            if (existente == null) return OperationResult.failure("Usuario no encontrado");

            usuarioRepository.delete(id);
            if (logger != null) logger.info("Usuario eliminado id={}", id);
            return OperationResult.success("Usuario eliminado correctamente");
        } catch (Exception e) {
            handleError(e, "Error al eliminar usuario");
            return OperationResult.failure("No se pudo eliminar el usuario");
        }
    }

    // Método privado para limpiar espacios en blanco de los campos de texto del usuario.
    private void normalizar(Usuario u) {
        if (u.getNombre() != null) u.setNombre(u.getNombre().trim());
        if (u.getCorreo() != null) u.setCorreo(u.getCorreo().trim());
        if (u.getRol() != null) u.setRol(u.getRol().trim());
    }
}
