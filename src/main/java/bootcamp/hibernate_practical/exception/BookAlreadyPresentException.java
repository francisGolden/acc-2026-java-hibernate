package bootcamp.hibernate_practical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookAlreadyPresentException extends RuntimeException {
    public BookAlreadyPresentException(String title){
        super("A book with title (" + title + ") is already present in the database");
    }
}
