package bootcamp.hibernate_practical.exception;

public class InvalidCreateOrUpdateBookRequest extends RuntimeException {
    public InvalidCreateOrUpdateBookRequest(){
        super("One or more fields of the create book request were empty or invalid");
    }
}
