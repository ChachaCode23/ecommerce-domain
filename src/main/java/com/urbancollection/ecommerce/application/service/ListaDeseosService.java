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

/**
 * ListaDeseosService
 *
 * Servicio de la lógica de negocio para la lista de deseos (wishlist).
 *
 * Aquí controlo:
 *  - agregar un producto a la wishlist de un usuario
 *  - quitarlo
 *  - listar lo que ese usuario tiene guardado
 *
 * Uso OperationResult para devolver si la operación fue exitosa o no,
 * junto con un mensaje entendible.
 */
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

    /**
     * agregar:
     * Agrega un producto a la lista de deseos del usuario.
     * Pasos:
     * 1. Valido que el usuario exista.
     * 2. Valido que el producto exista.
     * 3. Verifico que ese producto no esté ya en la wishlist del usuario.
     * 4. Creo el registro ListaDeseos y lo guardo.
     */
    @Override
    public OperationResult agregar(Long usuarioId, Long productoId) {
        try {
            // validar usuario
            Usuario u = usuarioRepo.findById(usuarioId);
            if (u == null) return OperationResult.failure("Usuario no encontrado");

            // validar producto
            Producto p = productoRepo.findById(productoId);
            if (p == null) return OperationResult.failure("Producto no encontrado");

            // evitar duplicados
            if (listaRepo.existsByUsuarioIdAndProductoId(usuarioId, productoId)) {
                return OperationResult.failure("Ya esta en tu lista de deseos");
            }

            // crear relación usuario-producto en la wishlist
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

    /**
     * quitar:
     * Elimina un ítem de la wishlist por su id.
     * Si no existe, devuelvo error de negocio.
     */
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

    /**
     * listarDeUsuario:
     * Devuelve todos los productos en la lista de deseos de un usuario.
     * Si algo falla, devuelvo lista vacía y registro el error en el log.
     */
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
