package com.svc.jogging.model.res;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData {

    protected Success success;

    protected Object data;

    protected Error error;

    @JsonIgnore
    protected Exception exception;

    @JsonIgnore
    public boolean isOk() {
        return exception == null;
    }

    @Builder
    private ResponseData(Success success, Object data) {
        if (isOk()) {
            this.success = success != null ? success : new Success();
        }
        this.data = data;
    }

    @Data
    protected static class Success {
        String message = "ok";
    }

    @Data
    @NoArgsConstructor
    public static class Error {
        String message = "error";
        int code = 400;

        public Error(String message) {
            this.message = message;
        }

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }

}
