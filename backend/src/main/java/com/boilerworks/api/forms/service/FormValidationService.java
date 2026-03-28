package com.boilerworks.api.forms.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Validates form submission data against a form definition schema.
 * Schema format:
 * {
 *   "fields": [
 *     { "name": "field_name", "type": "text|number|email|select|checkbox|textarea|date",
 *       "label": "Field Label", "required": true/false,
 *       "options": [...], "min": ..., "max": ..., "pattern": "..." }
 *   ]
 * }
 */
@Service
public class FormValidationService {

    @SuppressWarnings("unchecked")
    public Map<String, List<String>> validate(Map<String, Object> schema, Map<String, Object> data) {
        Map<String, List<String>> errors = new LinkedHashMap<>();

        Object fieldsObj = schema.get("fields");
        if (!(fieldsObj instanceof List<?> fields)) {
            errors.put("__all__", List.of("Invalid schema: missing fields array"));
            return errors;
        }

        for (Object fieldObj : fields) {
            if (!(fieldObj instanceof Map<?, ?> field)) continue;

            String name = (String) field.get("name");
            String type = (String) field.get("type");
            Boolean required = (Boolean) field.get("required");

            if (name == null) continue;

            Object value = data.get(name);
            List<String> fieldErrors = new ArrayList<>();

            // Required check
            if (Boolean.TRUE.equals(required)) {
                if (value == null || (value instanceof String s && s.isBlank())) {
                    fieldErrors.add("This field is required");
                }
            }

            // Type-specific validation
            if (value != null && !fieldErrors.isEmpty() == false) {
                switch (type != null ? type : "text") {
                    case "number" -> {
                        try {
                            double num = value instanceof Number n ? n.doubleValue() :
                                Double.parseDouble(value.toString());
                            if (field.get("min") != null) {
                                double min = ((Number) field.get("min")).doubleValue();
                                if (num < min) fieldErrors.add("Must be at least " + min);
                            }
                            if (field.get("max") != null) {
                                double max = ((Number) field.get("max")).doubleValue();
                                if (num > max) fieldErrors.add("Must be at most " + max);
                            }
                        } catch (NumberFormatException e) {
                            fieldErrors.add("Must be a number");
                        }
                    }
                    case "email" -> {
                        if (value instanceof String s && !s.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                            fieldErrors.add("Invalid email address");
                        }
                    }
                    case "select" -> {
                        Object options = field.get("options");
                        if (options instanceof List<?> optList && !optList.contains(value)) {
                            fieldErrors.add("Invalid selection");
                        }
                    }
                    default -> {
                        // text, textarea, date, checkbox — basic string validation
                        if (value instanceof String s) {
                            Object maxLength = field.get("maxLength");
                            if (maxLength instanceof Number n && s.length() > n.intValue()) {
                                fieldErrors.add("Must be at most " + n.intValue() + " characters");
                            }
                        }
                    }
                }
            }

            if (!fieldErrors.isEmpty()) {
                errors.put(name, fieldErrors);
            }
        }

        return errors;
    }
}
