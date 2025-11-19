package com.urbancollection.ecommerce.domain.entity.usuarios;

import com.urbancollection.ecommerce.domain.base.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void constructorSinArgumentos_deberiaCrearInstanciaYExtenderBaseEntity() {
        Usuario usuario = new Usuario();
        assertNotNull(usuario);
        assertTrue(usuario instanceof BaseEntity);
    }

    @Test
    void gettersYSettersBasicos_debenGuardarYRetornarValores() {
        Usuario u = new Usuario();

        String nombre = "Juan Pérez";
        String correo = "juan@example.com";
        String contrasena = "secreta123";
        String rol = "ADMIN";

        u.setNombre(nombre);
        u.setCorreo(correo);
        u.setContrasena(contrasena);
        u.setRol(rol);

        assertEquals(nombre, u.getNombre());
        assertEquals(correo, u.getCorreo());
        assertEquals(contrasena, u.getContrasena());
        assertEquals(rol, u.getRol());
    }

    @Test
    void clase_deberiaEstarMapeadaAUsuarioCoreYOverrideDeId() {
        Entity entity = Usuario.class.getAnnotation(Entity.class);
        assertNotNull(entity, "Usuario debe tener @Entity");

        Table table = Usuario.class.getAnnotation(Table.class);
        assertNotNull(table, "Usuario debe tener @Table");
        assertEquals("Usuario", table.name());
        assertEquals("core", table.schema());

        // hay constraint e índice sobre email
        UniqueConstraint[] uniques = table.uniqueConstraints();
        assertTrue(uniques.length > 0);
        assertEquals("email", uniques[0].columnNames()[0]);

        Index[] indexes = table.indexes();
        assertTrue(indexes.length > 0);
        assertTrue(indexes[0].columnList().contains("email"));

        AttributeOverride override = Usuario.class.getAnnotation(AttributeOverride.class);
        assertNotNull(override, "Usuario debe tener @AttributeOverride para id");
        assertEquals("id", override.name());
        assertEquals("usuario_id", override.column().name());
    }

    @Test
    void campoCorreo_deberiaTenerEmailNotBlankSizeYColumnEmail() throws NoSuchFieldException {
        Field field = Usuario.class.getDeclaredField("correo");

        assertNotNull(field.getAnnotation(NotBlank.class), "correo debe tener @NotBlank");
        assertNotNull(field.getAnnotation(Email.class), "correo debe tener @Email");
        assertNotNull(field.getAnnotation(Size.class), "correo debe tener @Size");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "correo debe tener @Column");
        assertEquals("email", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void campoNombre_deberiaTenerNotBlankYSizeYColumnCorrecta() throws NoSuchFieldException {
        Field field = Usuario.class.getDeclaredField("nombre");

        assertNotNull(field.getAnnotation(NotBlank.class), "nombre debe tener @NotBlank");
        assertNotNull(field.getAnnotation(Size.class), "nombre debe tener @Size");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "nombre debe tener @Column");
        assertEquals("nombre", column.name());
        assertFalse(column.nullable());
    }

    @Test
    void campoContrasena_deberiaTenerSizeYColumnHashPassword() throws NoSuchFieldException {
        Field field = Usuario.class.getDeclaredField("contrasena");

        Size size = field.getAnnotation(Size.class);
        assertNotNull(size, "contrasena debe tener @Size");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "contrasena debe tener @Column");
        assertEquals("hash_password", column.name());
        assertEquals(255, column.length());
    }

    @Test
    void campoRol_deberiaTenerNotBlankSizeYColumnCorrecta() throws NoSuchFieldException {
        Field field = Usuario.class.getDeclaredField("rol");

        assertNotNull(field.getAnnotation(NotBlank.class), "rol debe tener @NotBlank");
        assertNotNull(field.getAnnotation(Size.class), "rol debe tener @Size");

        Column column = field.getAnnotation(Column.class);
        assertNotNull(column, "rol debe tener @Column");
        assertEquals("rol", column.name());
        assertEquals(20, column.length());
        assertFalse(column.nullable());
    }
}
