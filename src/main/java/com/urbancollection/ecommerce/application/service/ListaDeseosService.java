package com.urbancollection.ecommerce.application.service;

import com.urbancollection.ecommerce.domain.base.OperationResult;
import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.usuarios.Usuario;
import com.urbancollection.ecommerce.domain.entity.ventas.ListaDeseos;
import com.urbancollection.ecommerce.domain.repository.ListaDeseosRepository;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.domain.repository.UsuarioRepository;
import com.urbancollection.ecommerce.shared.BaseService;

import java.util.List;

public class ListaDeseosService extends BaseService implements IListaDeseosService {

    private final ListaDeseosRepository listaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;

    public ListaDeseosService(ListaDeseosRepository listaRepo,
                              UsuarioRepository usuarioRepo,
                              ProductoRepository productoRepo) {
        this.listaRepo = listaRepo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
    }

    @Override
    public OperationResult agregar(Long usuarioId, Long productoId) {
        try {
            // validar usuario
            Usuario u = usuarioRepo.findById(usuarioId);
            if (u == null) return OperationResult.failure("Usuario no encontrado");

            // validar producto (existencia; si manejas 'activo', valida aquí p.isActivo())
            Producto p = productoRepo.findById(productoId);
            if (p == null) return OperationResult.failure("Producto no encontrado");

            // evitar duplicados
            if (listaRepo.existsByUsuarioIdAndProductoId(usuarioId, productoId)) {
                return OperationResult.failure("Ya esta en tu lista de deseos");
            }

            // crear registro
            ListaDeseos ld = new ListaDeseos();
            ld.setUsuario(u);
            ld.setProducto(p);
            listaRepo.save(ld);

            logger.info("Wishlist agregado: user={} product={}", usuarioId, productoId);
            return OperationResult.success("Agregado a tu lista de deseos");
        } catch (Exception e) {
            handleError(e, "Error al agregar a lista de deseos");
            return OperationResult.failure("No se pudo agregar a la lista de deseos");
        }
    }

    @Override
    public OperationResult quitar(Long listaDeseosId) {
        try {
            ListaDeseos ld = listaRepo.findById(listaDeseosId);
            if (ld == null) return OperationResult.failure("Elemento no encontrado");
            listaRepo.delete(listaDeseosId);
            logger.info("Wishlist eliminado id={}", listaDeseosId);
            return OperationResult.success("Eliminado de tu lista de deseos");
        } catch (Exception e) {
            handleError(e, "Error al quitar de lista de deseos");
            return OperationResult.failure("No se pudo eliminar de la lista de deseos");
        }
    }

    @Override
    public List<ListaDeseos> listarDeUsuario(Long usuarioId) {
        try {
            return listaRepo.findByUsuarioId(usuarioId);
        } catch (Exception e) {
            handleError(e, "Error al listar wishlist del usuario");
            return java.util.List.of();
        }
    }
}
