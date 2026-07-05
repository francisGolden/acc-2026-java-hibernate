package bootcamp.hibernate_practical.repository;

public class InvalidPublicationYearException extends RuntimeException {
    public InvalidPublicationYearException(int publicationYear){
        super("The selected publication year (" + publicationYear + ") is not valid");
    }
}
