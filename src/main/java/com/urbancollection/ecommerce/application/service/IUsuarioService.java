package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(Long id);

    OperationResult crear(Usuario nuevo);

    OperationResult actualizar(Long id, Usuario cambios);

    OperationResult eliminar(Long id);
}
