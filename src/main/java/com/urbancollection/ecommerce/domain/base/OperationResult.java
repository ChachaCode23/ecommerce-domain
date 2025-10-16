package com.urbancollection.ecommerce.domain.base;


public class OperationResult {

    /** true si la operación fue exitosa; false si falla. */
    private boolean success;

    /** Mensaje explicando el resultado (éxito o error). */
    private String message;

    /**
     * Constructor principal.
     * @param success indica éxito o fallo
     * @param message detalle del resultado
     */
    public OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // @return true si fue éxito; false si fue fallo.
    public boolean isSuccess() {
        return success;
    }

    // @return mensaje asociado al resultado
    public String getMessage() {
        return message;
    }

    /**
     * crea un resultado exitoso.
     * @param message mensaje de confirmación/información
     * @return OperationResult con success=true
     */
    public static OperationResult success(String message) {
        return new OperationResult(true, message);
    }

    /**
     * crea un resultado fallido.
     * @param message mensaje de error/causa
     * @return OperationResult con success=false
     */
    public static OperationResult failure(String message) {
        return new OperationResult(false, message);
    }
}
