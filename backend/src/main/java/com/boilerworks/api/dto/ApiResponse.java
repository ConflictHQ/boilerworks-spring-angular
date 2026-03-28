package com.boilerworks.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private boolean ok;
  private T data;
  private List<FieldError> errors;

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(true, data, null);
  }

  public static <T> ApiResponse<T> error(List<FieldError> errors) {
    return new ApiResponse<>(false, null, errors);
  }

  public static <T> ApiResponse<T> error(String field, String message) {
    return new ApiResponse<>(false, null, List.of(new FieldError(field, List.of(message))));
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FieldError {
    private String field;
    private List<String> messages;
  }
}
