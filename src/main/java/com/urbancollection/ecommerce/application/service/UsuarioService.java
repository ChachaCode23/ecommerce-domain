package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.shared.BaseService;
import com.urbancollection.ecommerce.shared.ValidationUtil;

import java.util.List;
import java.util.Optional;

/**
 * UsuarioService
 *
 * Servicio de la lógica de negocio para usuarios.
 * Aquí manejo:
 *  - crear usuario
 *  - actualizar usuario
 *  - eliminar usuario
 *  - listar / buscar usuarios
 *
 * También validaciones de negocio como:
 *  - correo único
 *  - normalizar datos antes de guardar
 */
public class UsuarioService extends BaseService implements IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * listar:
     * Devuelve todos los usuarios.
     * Si algo falla, devuelvo lista vacía y lo dejo logueado.
     */
    @Override
    public List<Usuario> listar() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            handleError(e, "Error al listar usuarios");
            return List.of();
        }
    }

    /**
     * buscarPorId:
     * Busca un usuario por id.
     * Uso Optional porque puede que no exista.
     */
    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        try {
            return Optional.ofNullable(usuarioRepository.findById(id));
        } catch (Exception e) {
            handleError(e, "Error al buscar usuario por id");
            return Optional.empty();
        }
    }

    /**
     * crear:
     * Crea un usuario nuevo.
     *
     * Reglas:
     * - El body no puede ser null.
     * - Corro Bean Validation (anotaciones en la entidad Usuario).
     * - El correo no puede estar repetido (ignora mayúsculas/minúsculas).
     *
     * Si todo está bien:
     * - guardo el usuario
     * - hago log
     * - devuelvo OperationResult.success con mensaje
     */
    @Override
    public OperationResult crear(Usuario nuevo) {
        try {
            if (nuevo == null) return OperationResult.failure("Usuario requerido");

            normalizar(nuevo);

            // Validaciones por anotaciones (@NotBlank, etc.)
            OperationResult v = ValidationUtil.validate(nuevo);
            if (!v.isSuccess()) return v;

            // Regla de negocio: correo único
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

    /**
     * actualizar:
     * Edita un usuario existente.
     *
     * Flujo:
     * - Busco el usuario por id. Si no existe → error.
     * - Normalizo y valido los datos nuevos.
     * - Si cambia el correo, vuelvo a chequear que no esté usado por otro usuario.
     * - Aplico los cambios campo por campo.
     * - Guardo y retorno éxito.
     */
    @Override
    public OperationResult actualizar(Long id, Usuario cambios) {
        try {
            Usuario existente = usuarioRepository.findById(id);
            if (existente == null) return OperationResult.failure("Usuario no encontrado");

            if (cambios == null) return OperationResult.failure("Datos de usuario requeridos");

            normalizar(cambios);

            // Validación Bean Validation en el objeto cambios
            OperationResult v = ValidationUtil.validate(cambios);
            if (!v.isSuccess()) return v;

            // Regla de negocio: si se cambia el correo, debe seguir siendo único
            String correoNuevo = cambios.getCorreo();
            if (correoNuevo != null && !correoNuevo.equalsIgnoreCase(existente.getCorreo())) {
                Usuario conMismoCorreo = usuarioRepository.findByCorreoIgnoreCase(correoNuevo);
                if (conMismoCorreo != null && !conMismoCorreo.getId().equals(existente.getId())) {
                    return OperationResult.failure("Ya existe otro usuario con ese correo");
                }
            }

            // Aplico solo los campos que vienen en "cambios"
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

    /**
     * eliminar:
     * Borra un usuario por id.
     * Si no existe, devuelvo error de negocio.
     */
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

    /**
     * normalizar:
     * Limpia espacios y deja los strings en una forma consistente
     * antes de validar o guardar.
     */
    private void normalizar(Usuario u) {
        if (u.getNombre() != null) u.setNombre(u.getNombre().trim());
        if (u.getCorreo() != null) u.setCorreo(u.getCorreo().trim());
        if (u.getRol() != null) u.setRol(u.getRol().trim());
        // No forzamos lower-case aquí porque hacemos comparación ignore-case.
    }
}
