package bootcamp.hibernate_practical.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(long id){
        super("Book with id [" + id + "] is not available to be borrowed");
    }
}
