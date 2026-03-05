package org.example.exercises.problem1;

import java.util.function.Supplier;

// Demo
public class Problem1 {
    static void main(String[] args) {
        Book book = new Book("Design Patterns", "Gamma et al.", "12345", "Classic GoF book");

        // Access metadata (fast, no content loaded yet)
        System.out.println("Title: " + book.getTitle());
        System.out.println("Description: " + book.getShortDescription());

        // Access heavy content (lazy load happens here)
        System.out.println("Now loading content...");
        System.out.println(book.getBookContent());

        // Simulate an update in the database
        System.out.println("--- Database content updated externally ---");

        // Refresh the proxy to reload content
        book.refreshBookContent();

        // Access updated content
        System.out.println("After refresh:");
        System.out.println(book.getBookContent());
    }
}

// Represents the heavy resource (full book content)
record BookContent(String content) {
    BookContent(String content) {
        // Simulate expensive operation (e.g., DB or file read)
        System.out.println("Loading book content for ID: " + content + "...");
        try {
            Thread.sleep(2000); // simulate delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        this.content = "Full content of book with ID " + content;
    }
}

// Proxy that controls access to BookContent
//class BookContentProxy {
//    private BookContent realContent;
//    private final String bookId;
//
//    public BookContentProxy(String bookId) {
//        this.bookId = bookId;
//    }
//
//    // Lazy load or return cached content
//    public String getContent() {
//        if (realContent == null) {
//            realContent = fetchFromDatabase();
//        } return realContent.content();
//    }
//
//    // Force refresh (e.g., after an update elsewhere)
//    public void refresh() {
//        realContent = fetchFromDatabase(); // reconstruct with new data
//    }
//
//    // Simulated database fetch
//    private BookContent fetchFromDatabase() {
//        // In reality, query DB with bookId
//        return new BookContent(bookId);
//    }
//}

class BookContentProxy {
    private Supplier<BookContent> supplier;
    private final String bookId;

    public BookContentProxy(String bookId) {
        // Initially, supplier points to the factory method
        this.bookId = bookId;
        this.supplier = () -> createAndCacheBookContent(bookId);
    }

    public String getContent() {
        return supplier.get().content();
    }

    public synchronized void refresh() {
        this.supplier = () -> createAndCacheBookContent(bookId);
    }

    // Thread-safe creation and caching
    private synchronized BookContent createAndCacheBookContent(String bookId) {
        class BookContentFactory implements Supplier<BookContent> {
            private final BookContent instance = new BookContent(bookId);

            @Override
            public BookContent get() {
                return instance;
            }
        }

        // Swap supplier to cached factory after first creation
        if (!(supplier instanceof BookContentFactory)) {
            supplier = new BookContentFactory();
        }

        return supplier.get();
    }

}


// Lightweight Book class with metadata
class Book {
    private final String title;
    private final String author;
    private final String isbn;
    private final String shortDescription;
    private final BookContentProxy contentProxy;

    public Book(String title, String author, String isbn, String shortDescription) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.shortDescription = shortDescription;
        this.contentProxy = new BookContentProxy(isbn); // use ISBN as ID
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getIsbn() { return isbn; }
    public String getShortDescription() { return shortDescription; }

    // Lazy loading access
    public String getBookContent() {
        return contentProxy.getContent();
    }

    public void refreshBookContent() {
        contentProxy.refresh();
    }
}

