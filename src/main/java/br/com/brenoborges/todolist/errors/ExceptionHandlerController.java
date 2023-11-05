package br.com.brenoborges.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Toda exceção passa por essa anotação.
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Metodo que retorna a tratativa especifica desse erro.
     * 
     * @param e
     * @return Mensagem ao usuário referente ao erro.
     */
    // Anotation da exceção especifica que estou lançando nesse método.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMostSpecificCause().getMessage());
    }
}
