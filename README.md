# 🎬 Movie Ticket Booking System

A clean and structured **Java-based Movie Ticket Booking System** that simulates  
real-world ticket booking workflows using **file-based (CSV) persistence**.

The project emphasizes **clear architecture, realistic data flow, and maintainability**,  
without relying on external databases or complex UI frameworks.

---

## ✨ Key Features

- 🎥 View available movies and show schedules  
- 💺 Seat selection with status tracking (AVAILABLE / LOCKED / BOOKED)  
- 🎟️ Ticket booking with payment simulation  
- 📜 Booking history tracking  
- 📁 File-based persistence using CSV files  
- 🖥️ Clean and simple console-based interaction  

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

The system uses **structured CSV files** to simulate a realistic backend environment:

- 🎬 `movies.csv`   → movie catalog  
- 🕒 `shows.csv`    → show schedules  
- 💺 `seats.csv`    → seat availability & pricing  
- 👤 `users.csv`    → registered users  
- 🎟️ `bookings.csv` → confirmed & pending bookings  
- 📜 `logs.csv`     → system activity logs  

This approach keeps the data **transparent, easy to inspect, and simple to extend**.

---

## ▶️ How to Run

1. Clone the repository  
2. Open the project in any Java IDE  
   *(VS Code / IntelliJ / Eclipse)*  
3. Run `Main.java`  
4. Follow the on-screen console menu  

---

## 🔮 Future Enhancements

- 🗄️ Database integration (MySQL / PostgreSQL)  
- 🌐 REST API using Spring Boot  
- 📱 Web or mobile frontend  
- ⏱️ Advanced seat locking with timeout  

---

## 👤 Author

**Souvik Dhar**  
🔗 GitHub: https://github.com/2002Souvik
