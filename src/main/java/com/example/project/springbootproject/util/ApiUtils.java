package com.example.project.springbootproject.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiUtils {

  public static <T> ApiResult<T> success() {
    return new ApiResult<>(true, null, null);
  }

  public static <T> ApiResult<T> success(T response) {
    return new ApiResult<>(true, response, null);
  }

  public static ApiResult<?> error(Throwable throwable, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(throwable, status));
  }

  public static ApiResult<?> error(String message, HttpStatus status) {
    return new ApiResult<>(false, null, new ApiError(message, status));
  }


  public static ResponseEntity<ApiResult<?>> newResponse(Throwable throwable, HttpStatus status) {
    return newResponse(throwable.getMessage(), status);
  }

  public static ResponseEntity<ApiResult<?>> newResponse(String message, HttpStatus status) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    return new ResponseEntity<>(error(message, status), headers, status);
  }

  @Builder
  @AllArgsConstructor
  @Data
  public static class ApiError {

    private final String message;
    private final int status;

    ApiError(Throwable throwable, HttpStatus status) {
      this(throwable.getMessage(), status.value());
    }

    ApiError(String message, HttpStatus status) {
      this(message, status.value());
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("message", message)
        .append("status", status)
        .toString();
    }
  }

  public static class ApiResult<T> {
    private final boolean success;
    private final T response;
    private final ApiError error;

    private ApiResult(boolean success, T response, ApiError error) {
      this.success = success;
      this.response = response;
      this.error = error;
    }

    public boolean isSuccess() {
      return success;
    }

    public ApiError getError() {
      return error;
    }

    public T getResponse() {
      return response;
    }

    @Override
    public String toString() {
      return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("success", success)
        .append("response", response)
        .append("error", error)
        .toString();
    }
  }

}