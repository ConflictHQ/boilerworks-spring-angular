package com.boilerworks.api.forms;

import com.boilerworks.api.forms.service.FormValidationService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FormServiceTest {

    private final FormValidationService validationService = new FormValidationService();

    @Test
    void validateRequiredFieldPresent() {
        Map<String, Object> schema = Map.of("fields", List.of(
            Map.of("name", "email", "type", "email", "required", true)
        ));
        Map<String, Object> data = Map.of("email", "test@example.com");

        Map<String, List<String>> errors = validationService.validate(schema, data);
        assertThat(errors).isEmpty();
    }

    @Test
    void validateRequiredFieldMissing() {
        Map<String, Object> schema = Map.of("fields", List.of(
            Map.of("name", "email", "type", "email", "required", true)
        ));
        Map<String, Object> data = Map.of();

        Map<String, List<String>> errors = validationService.validate(schema, data);
        assertThat(errors).containsKey("email");
        assertThat(errors.get("email")).contains("This field is required");
    }

    @Test
    void validateInvalidEmail() {
        Map<String, Object> schema = Map.of("fields", List.of(
            Map.of("name", "email", "type", "email", "required", false)
        ));
        Map<String, Object> data = Map.of("email", "not-an-email");

        Map<String, List<String>> errors = validationService.validate(schema, data);
        assertThat(errors).containsKey("email");
        assertThat(errors.get("email")).contains("Invalid email address");
    }

    @Test
    void validateSelectWithInvalidOption() {
        Map<String, Object> schema = Map.of("fields", List.of(
            Map.of("name", "color", "type", "select", "required", false,
                    "options", List.of("red", "blue", "green"))
        ));
        Map<String, Object> data = Map.of("color", "purple");

        Map<String, List<String>> errors = validationService.validate(schema, data);
        assertThat(errors).containsKey("color");
        assertThat(errors.get("color")).contains("Invalid selection");
    }

    @Test
    void validateNumberRange() {
        Map<String, Object> schema = Map.of("fields", List.of(
            Map.of("name", "age", "type", "number", "required", false, "min", 0, "max", 150)
        ));
        Map<String, Object> data = Map.of("age", 200);

        Map<String, List<String>> errors = validationService.validate(schema, data);
        assertThat(errors).containsKey("age");
        assertThat(errors.get("age")).contains("Must be at most 150.0");
    }
}
