package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.BookAlreadyPresentException;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
import bootcamp.hibernate_practical.exception.InvalidCreateOrUpdateBookRequest;
import bootcamp.hibernate_practical.repository.BookRepository;
import bootcamp.hibernate_practical.exception.InvalidPublicationYearException;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.List;
import java.time.Year;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
        if (request.getTitle().isBlank() || request.getAuthor().isBlank() ||
                request.getGenre().isBlank() || request.getPublicationYear() == null) {
            throw new InvalidCreateOrUpdateBookRequest();
        }

        if (Integer.toString(request.getPublicationYear()).length() < 4 ||
                request.getPublicationYear() < 0 ||
                request.getPublicationYear() > Year.now(ZoneId.systemDefault()).getValue()) {
            throw new InvalidPublicationYearException(request.getPublicationYear());
        }

        if (!bookRepository.findByTitleIgnoreCase(request.getTitle()).isEmpty()){
            throw new BookAlreadyPresentException(request.getTitle());
        }

        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getGenre(),
                request.getPublicationYear(),
                true
        );

        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public List<BookResponse> getAllBooks() {
        List<Book> allBooks = bookRepository.findAll();
        return allBooks.stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> getBooksByTitle(String title){
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase(title);
        return books.stream().map(this::mapToResponse).toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapToResponse(book);
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        if (request.getTitle().isBlank() && request.getAuthor().isBlank() &&
                request.getGenre().isBlank() && request.getPublicationYear() == null &&
                request.getAvailable() == null) {
            throw new InvalidCreateOrUpdateBookRequest();
        }

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (!request.getTitle().isBlank()){
            book.setTitle(request.getTitle());
        }

        if (!request.getAuthor().isBlank()){
            book.setAuthor(request.getAuthor());
        }

        if (!request.getGenre().isBlank()){
            book.setGenre(request.getGenre());
        }

        if (!Integer.toString(request.getPublicationYear()).isBlank() &&
                Integer.toString(request.getPublicationYear()).length() < 4 ||
                request.getPublicationYear() < 0 ||
                request.getPublicationYear() > Year.now(ZoneId.systemDefault()).getValue()) {
            throw new InvalidPublicationYearException(request.getPublicationYear());
        }

        if (request.getPublicationYear() != null){
            book.setPublicationYear(request.getPublicationYear());
        }

        if (request.getAvailable() != null) {
            book.setAvailable(request.getAvailable());
        }

        bookRepository.save(book);

        return mapToResponse(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    public List<BookResponse> findByAuthor(String author) {
        List<Book> books = bookRepository.findBooksByAuthorIgnoreCase(author);
        return books.stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findAvailableBooks(){
        List<Book> books = bookRepository.findByAvailableTrue();
        return books.stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> findBooksByPublicationYearAfter(int year){
        List<Book> books = bookRepository.findBooksByPublicationYearAfter(year);
        return books.stream().map(this::mapToResponse).toList();
    }

    public long getLibraryBooksCount(){
        return bookRepository.count();
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable());
    }
}
