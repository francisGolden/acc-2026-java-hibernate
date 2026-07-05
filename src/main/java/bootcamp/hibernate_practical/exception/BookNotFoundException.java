package bootcamp.hibernate_practical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException (Long id) {
        super("Book with id [" + id + "] was not found");
    }
}
