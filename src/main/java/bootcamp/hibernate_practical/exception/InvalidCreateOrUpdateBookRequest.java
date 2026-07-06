package bootcamp.hibernate_practical.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCreateOrUpdateBookRequest extends RuntimeException {
    public InvalidCreateOrUpdateBookRequest(){
        super("One or more fields of the create book request were empty or invalid");
    }
}
