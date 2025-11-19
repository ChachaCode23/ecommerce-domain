package com.urbancollection.ecommerce.persistence.repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.urbancollection.ecommerce.domain.entity.catalogo.Cupon;
import com.urbancollection.ecommerce.domain.repository.CuponRepository;

// Implemento en memoria de CuponRepository.
// Uso un mapa para simular la base de datos y un contador para ir generando los ids.
public class CuponRepositoryImpl implements CuponRepository {

    // Mapa que guarda los cupones en memoria, usando el id como clave.
    private final Map<Long, Cupon> data = new HashMap<>();
    // Secuencia para generar ids auto incrementales cuando el cupón no tiene id.
    private final AtomicLong seq = new AtomicLong(0);

    @Override
    // Guarda un cupón en el "repositorio".
    // Si el cupón no tiene id, se le asigna uno nuevo.
    public Cupon save(Cupon cupon) {
        if (cupon.getId() == null) {
            cupon.setId(seq.incrementAndGet());
        }
        data.put(cupon.getId(), cupon);
        return cupon;
    }

    @Override
    // Busca un cupón por su id en el mapa.
    // Si no existe, devuelve null.
    public Cupon findById(Long id) {
        return data.get(id);
    }

    @Override
    // Devuelve una lista con todos los cupones almacenados en memoria.
    public List<Cupon> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    // Elimina un cupón del mapa usando su id.
    public void deleteById(Long id) {
        data.remove(id);
    }
}
