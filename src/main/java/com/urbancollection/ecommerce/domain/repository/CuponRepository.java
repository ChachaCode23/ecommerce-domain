package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;

/**
 * Esta interfaz extiende de IBaseRepository y se utiliza
 * para definir las operaciones CRUD que se pueden realizar
 * sobre la entidad Cupon.
 *
 * Si en el futuro se necesitan metodos adicionales
 * especificos para los cupones, pueden agregarse aqui.
 */
public interface CuponRepository extends IBaseRepository<Cupon, Long> {
    // Metodos personalizados de Cupon pueden definirse aqui
}
