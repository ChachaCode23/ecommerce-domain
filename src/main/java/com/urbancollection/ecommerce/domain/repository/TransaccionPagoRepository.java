package com.urbancollection.ecommerce.domain.repository;

import com.urbancollection.ecommerce.domain.entity.ventas.TransaccionPago;

/**

 * Esta interfaz se encarga de definir las operaciones CRUD para la entidad TransaccionPago.
 * Extiende de IBaseRepository para heredar los métodos básicos de persistencia:
 * guardar, buscar, eliminar y listar.
 */
public interface TransaccionPagoRepository extends IBaseRepository<TransaccionPago, Long> {
    // Se pueden agregar metodos especificos de TransaccionPago si son necesarios
}
