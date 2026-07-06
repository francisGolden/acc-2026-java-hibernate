package bootcamp.hibernate_practical.controller;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.service.BookService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponse createBook(@RequestBody CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/title/{title}")
    public List<BookResponse> getBooksByTitle(@PathVariable String title){
        return bookService.getBooksByTitle(title);
    }

    @GetMapping("/{id}")
    public BookResponse getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @PutMapping("/{id}")
    public BookResponse updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest updateBookRequest) {
        return bookService.updateBook(id, updateBookRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    @GetMapping("/author/{author}")
    public List<BookResponse> getBooksByAuthor(@PathVariable String author){
        return bookService.findByAuthor(author);
    }

    @GetMapping("/available")
    public List<BookResponse> getAvailableBooks() {
        return bookService.findAvailableBooks();
    }

    @GetMapping("/publicationYear_after/{publicationYear}")
    public List<BookResponse> getBooksByPublicationYearAfter(@PathVariable int publicationYear) {
        return bookService.findBooksByPublicationYearAfter(publicationYear);
    }

    @GetMapping("/get_library_books_count")
    public long getLibraryBooksCount(){
        return bookService.getLibraryBooksCount();
    }

    @PutMapping("/borrow/{id}")
    public BookResponse borrowBook(@PathVariable long id){
        return bookService.borrowBook(id);
    }

    @PutMapping("/return/{id}")
    public BookResponse returnBook(@PathVariable long id){
        return bookService.returnBook(id);
    }
}
