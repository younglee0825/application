package carrot.app.Controller;

import carrot.app.Exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse errorResponse(Exception e){
        e.printStackTrace();
        return new ErrorResponse("501","비밀번호가 일치하지 않습니다.");
    }

    @Data
    @AllArgsConstructor
    static class ErrorResponse{
        private String code;
        private String msg;
    }
}
