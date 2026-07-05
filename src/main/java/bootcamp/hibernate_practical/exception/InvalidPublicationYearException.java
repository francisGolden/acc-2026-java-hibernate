package bootcamp.hibernate_practical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPublicationYearException extends RuntimeException {
    public InvalidPublicationYearException(int publicationYear){
        super("The selected publication year (" + publicationYear + ") is not valid");
    }
}
