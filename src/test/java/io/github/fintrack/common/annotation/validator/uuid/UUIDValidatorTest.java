package io.github.fintrack.common.annotation.validator.uuid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UUIDValidatorTest {

    private UUIDValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UUIDValidator();
    }

    @Test
    void shouldReturnTrueForValidUUID() {
        String validUuid = UUID.randomUUID().toString();

        assertTrue(validator.isValid(validUuid, null));
    }

    @Test
    void shouldReturnFalseForInvalidUUID() {
        String invalidUuid = "1234-invalid";

        assertFalse(validator.isValid(invalidUuid, null));
    }

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void shouldReturnFalseWhenValueIsBlank() {
        assertFalse(validator.isValid("   ", null));
    }
}