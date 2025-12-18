# 🎬 Movie Ticket Booking System

A simple and structured **Java-based Movie Ticket Booking System** that simulates
real-world ticket booking workflows using **file-based (CSV) persistence**.

The project focuses on **clarity, clean structure, and realistic data flow**
rather than UI or external databases.

---

## ✨ Features

- View available movies and shows
- Seat selection with status tracking (AVAILABLE / LOCKED / BOOKED)
- Ticket booking with payment simulation
- Booking history tracking
- File-based persistence using CSV files
- Clean console-based interaction

---

## 🗂️ Project Structure

MovieTicketBookingSystem
│
├─ src
│ ├─ controller → application flow & menus
│ ├─ model → core entities (Movie, Show, Seat, Booking)
│ ├─ service → business logic
│ ├─ repository → CSV file handling
│ └─ util → helper utilities
│
├─ data
│ ├─ movies.csv
│ ├─ shows.csv
│ ├─ seats.csv
│ ├─ users.csv
│ ├─ bookings.csv
│ └─ logs.csv
│
└─ README.md

---

## 📊 Sample Data (CSV-Based)

This project uses **structured CSV files** to simulate a real backend system:

- `movies.csv`   → movie catalog  
- `shows.csv`    → show schedules  
- `seats.csv`    → seat availability & pricing  
- `users.csv`    → registered users  
- `bookings.csv` → confirmed & pending bookings  
- `logs.csv`     → system activity logs  

These files make the system **easy to inspect, debug, and extend**.

---

## ▶️ How to Run

1. Clone the repository  
2. Open in any Java IDE (VS Code / IntelliJ / Eclipse)  
3. Run `Main.java`  
4. Follow the console menu  

---

## 🔮 Future Improvements

- Database integration (MySQL / PostgreSQL)
- REST API using Spring Boot
- Web or mobile frontend
- Advanced seat locking with timeout

---

## 👤 Author

**Souvik Dhar**  
GitHub: https://github.com/2002Souvik
