# Solution
This design solves the catalog browsing problem by loading only essential metadata upfront. Users can quickly navigate thousands of books without incurring the cost of loading full content. Heavy resources are fetched lazily and cached, preventing wasted memory and unnecessary queries. The refresh mechanism adds flexibility, ensuring that updates can be incorporated without breaking immutability or performance. In short, the diagram shows how lazy loading plus refresh keeps browsing fast while still allowing access to up-to-date book content when required.

![uml diagram](/docs/img_2.png)

The UML diagram illustrates a Supplier-based Lazy Loading design. The Book class holds lightweight metadata (title, author, ISBN, description) and delegates heavy content access to BookContentProxy. The proxy uses a Supplier<BookContent> to defer creation of the immutable BookContent record until it’s actually needed. On the first call to getBookContent(), the proxy fetches the content (simulating a database read) and caches it. Subsequent calls return the cached instance directly, avoiding repeated expensive operations.

The proxy also provides a refresh() method, which resets the supplier so the next access rebuilds BookContent with updated data. This ensures that if the database changes, the cached content can be refreshed manually.
