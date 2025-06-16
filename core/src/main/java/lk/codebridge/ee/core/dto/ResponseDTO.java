package lk.codebridge.ee.core.dto;

import java.io.Serializable;

public class ResponseDTO implements Serializable {

    private Object message;
    private boolean success;

    public ResponseDTO(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ResponseDTO() {
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
