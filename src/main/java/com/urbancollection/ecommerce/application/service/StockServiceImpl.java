package com.urbancollection.ecommerce.application.service;

import java.util.List;

import com.urbancollection.ecommerce.domain.entity.catalogo.Producto;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;
import com.urbancollection.ecommerce.domain.repository.ProductoRepository;
import com.urbancollection.ecommerce.domain.service.StockService;

/**
 * Servicio de dominio para validar y descontar stock.
 */
public class StockServiceImpl implements StockService {

    private final ProductoRepository productoRepository;

    public StockServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void validarDisponibilidad(List<ItemPedido> items) {
        if (items == null) return;
        for (ItemPedido it : items) {
            if (it == null || it.getProducto() == null || it.getProducto().getId() == null) {
                throw new IllegalArgumentException("Item inválido para validar stock.");
            }
            Producto p = productoRepository.findById(it.getProducto().getId());
            if (p == null) throw new IllegalStateException("Producto no encontrado: " + it.getProducto().getId());

            Integer stock = p.getStock(); // ✅ evita comparar int con null
            if (stock == null || stock < it.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para producto " + p.getId());
            }
        }
    }

    @Override
    public void descontar(List<ItemPedido> items) {
        if (items == null) return;
        for (ItemPedido it : items) {
            Producto p = productoRepository.findById(it.getProducto().getId());
            Integer stock = p.getStock();
            int nuevo = (stock != null ? stock : 0) - it.getCantidad();
            if (nuevo < 0) throw new IllegalStateException("Stock negativo para producto " + p.getId());
            p.setStock(nuevo);
            productoRepository.save(p);
        }
    }
}
