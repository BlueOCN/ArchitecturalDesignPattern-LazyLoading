# 📚 Problem Scenario: Online Library System

You’re building an **Online Library System** where users can browse books. Each `Book` object contains:

- Title
- Author
- ISBN
- Short description

But each book also has a **large resource field**: the **full content of the book** (hundreds of pages stored in a database or external storage).

---

### ⚠️ The Problem

- When users browse the catalog, they only need **basic book info** (title, author, description).
- Loading the **entire book content** for every listed book would:
    - Waste memory and slow down performance.
    - Cause unnecessary database queries.
    - Make the system sluggish when browsing thousands of books.

---

### 🎯 Challenge for You

How would you apply **Lazy Loading** here so that:
- The `Book` object only loads its **full content** when a user explicitly opens it to read.
- The catalog browsing remains fast and lightweight.

---

👉 Your task: Design the **Java solution** (classes, methods, maybe a proxy or getter) that implements Lazy Loading for this scenario.