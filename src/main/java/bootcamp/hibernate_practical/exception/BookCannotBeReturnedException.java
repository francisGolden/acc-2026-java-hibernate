package bootcamp.hibernate_practical.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookCannotBeReturnedException extends RuntimeException {
    public BookCannotBeReturnedException(long id){
        super("Book with id [" + id + "] is not available to be returned");
    }
}
