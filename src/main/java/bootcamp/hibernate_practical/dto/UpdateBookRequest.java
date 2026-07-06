package bootcamp.hibernate_practical.dto;

import lombok.Getter;

@Getter
public class UpdateBookRequest {
    private String title;
    private String author;
    private String genre;
    private Integer publicationYear;
    private Boolean available;
    private Boolean borrowStatus;
}
