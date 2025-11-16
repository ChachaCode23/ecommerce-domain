package com.urbancollection.ecommerce.domain.service;

import java.util.List;
import com.urbancollection.ecommerce.domain.entity.ventas.ItemPedido;

public interface StockService {
    void validarDisponibilidad(List<ItemPedido> items); // lanza excepción si falta stock
    void descontar(List<ItemPedido> items);             // persiste nuevos stocks
}
