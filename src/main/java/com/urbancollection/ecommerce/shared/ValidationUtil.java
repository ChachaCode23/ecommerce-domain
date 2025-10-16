package com.urbancollection.ecommerce.shared;

import java.util.Set;
import java.util.stream.Collectors;

import com.urbancollection.ecommerce.domain.base.OperationResult;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 *  -Aque se recibe un objeto (con anotaciones como @NotNull, @Size, etc.),
 *    validarlo con el Validator y devolver un OperationResult
 *    con el detalle de errores o "OK" si todo está bien.
 */
public final class ValidationUtil {

    /**
     * Validator compartido, obtenido desde la fábrica por defecto.
     * - Es estatico y final para reutilizarlo sin crear nuevas instancias.
     */
    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    //Constructor privado para evitar que se creen instancias

    private ValidationUtil() {}

    /**
     * Metodo genérico para validar cualquier tipo de objeto (T).
     *
     * Flujo:
     * 1) Si el bean es null, devuelve failure con mensaje claro.
     * 2) Ejecuta VALIDATOR.validate(bean) para obtener las violaciones.
     * 3) Si hay violaciones, construye un mensaje unificado "propiedad: mensaje"
     *    separado por "; " y devuelve failure.
     * 4) Si no hay violaciones, devuelve success("OK").
     *
     * @param bean objeto a validar (debe tener anotaciones de Bean Validation)
     * @param <T>  tipo genérico del objeto
     * @return OperationResult con éxito o con los errores de validación
     */
    public static <T> OperationResult validate(T bean) {
        // Validación básica: no aceptar null.
        if (bean == null) return OperationResult.failure("Objeto requerido para validacion");

        // Ejecuta las validaciones declarativas (anotaciones) sobre el bean.
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(bean);

        // Si hay errores, los mapeamos a "propiedad: mensaje" y los unimos en un solo string.
        if (!violations.isEmpty()) {
            String msg = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining("; "));
            return OperationResult.failure(msg);
        }

        // Si no hubo violaciones, todo OK.
        return OperationResult.success("OK");
    }
}
