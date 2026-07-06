package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.*;
import bootcamp.hibernate_practical.repository.BookRepository;
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
        return bookRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    public List<BookResponse> getBooksByTitle(String title){
        return bookRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse).toList();
    }

    public BookResponse getBookById(Long id) {
        return mapToResponse(bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id)));
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        if (request.getTitle().isBlank() && request.getAuthor().isBlank() &&
                request.getGenre().isBlank() && request.getPublicationYear() == null &&
                request.getAvailable() == null && request.getBorrowStatus() == null) {
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

        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public void deleteBook(Long id) {
        bookRepository.delete(bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id)));
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

    public BookResponse borrowBook(long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        if (book.isBorrowStatus()){
            throw new BookAlreadyBorrowedException(id);
        }
        book.setBorrowStatus(true);
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    public BookResponse returnBook(long id){
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        if (!book.isBorrowStatus()){
            throw new BookCannotBeReturnedException(id);
        }
        book.setBorrowStatus(false);
        Book savedBook = bookRepository.save(book);
        return mapToResponse(savedBook);
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable(), book.isBorrowStatus());
    }
}
