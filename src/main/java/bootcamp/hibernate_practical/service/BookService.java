package bootcamp.hibernate_practical.service;

import bootcamp.hibernate_practical.dto.BookResponse;
import bootcamp.hibernate_practical.dto.CreateBookRequest;
import bootcamp.hibernate_practical.dto.UpdateBookRequest;
import bootcamp.hibernate_practical.entity.Book;
import bootcamp.hibernate_practical.exception.BookNotFoundException;
import bootcamp.hibernate_practical.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public BookResponse createBook(CreateBookRequest request) {
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
        return allBooks.stream().map(book -> new BookResponse(
                book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(),
                book.getPublicationYear(), book.isAvailable()))
                .toList();
    }

    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return new BookResponse(
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable()
        );
    }

    public BookResponse updateBook(Long id, UpdateBookRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setGenre(request.getGenre());
        book.setPublicationYear(request.getPublicationYear());

        if (request.getAvailable() != null) {
            book.setAvailable(request.getAvailable());
        }

        bookRepository.save(book);

        return new BookResponse(
                book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable()
        );
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    public List<BookResponse> findByAuthor(String author) {
        List<Book> books = bookRepository.findBooksByAuthor(author);
        return books.stream()
                .map(book -> new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable()))
                .toList();
    }

    public List<BookResponse> findAvailableBooks(){
        List<Book> books = bookRepository.findByAvailableTrue();
        return books.stream()
                .map(book -> new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                        book.getGenre(), book.getPublicationYear(), book.isAvailable())
                )
                .toList();
    }

    private BookResponse mapToResponse(Book book) {
        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(),
                book.getGenre(), book.getPublicationYear(), book.isAvailable());
    }
}
