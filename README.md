# GoNature

GoNature is a user-friendly park management app built using Java, CSS, JavaFX, and client-server architecture. It incorporates a SQL database for seamless reservation data management.

## Centralized Park Managing System

This project is built with a FullStack approach. We have a remote server managing a database called GoNature. We also have several types of clients that can connect: Clients, Park employees, Service employees, Guides, Park managers, and Department managers.

## Features

### General
- User authentication
- Real-time data synchronization between client and server
- SMS notifications for reservation approvals and reminders

### Clients
- Log in with ID
- View past reservations
- Make new reservations
- Join waiting list if no available space
- Update or cancel existing reservations

### Guides
- Initially not defined as guide users in the DB
- Need approval from a service employee
- Log in with user ID and password
- View past reservations
- Make new reservations
- Join waiting list if no available space
- Update or cancel existing reservations

### Park Employees
- Log in with user ID and password
- Check the number of visitors currently in the park
- Make new reservations for clients or guides (group reservations)
- Update the number of visitors that have arrived from a reservation

### Service Employees
- Log in with user ID and password
- Approve guide user permissions

### Park Managers
- Log in with user ID and password
- Request changes to dwell time or maximum capacity from the department manager
- Generate usage reports showing dates and hours the park wasn't at full capacity
- Generate total reports showing the number of group and individual visitors for a selected month

### Department Managers
- Log in with user ID and password
- View and approve/reject park manager requests for changes to dwell time or maximum capacity
- View visit reports for selected dates
- View cancellation reports showing the number of order cancellations and their distribution

## Technologies Used
- Java
- JavaFX
- CSS
- SQL
- Client-server architecture

## Installation

### Prerequisites
- Java Development Kit (JDK)
- JavaFX
- SQL Database (MySQL, PostgreSQL, etc.)
- Git

### Steps
1. **Clone the repository:**
   ```sh
   git clone https://github.com/ShadiAbureesh/GoNature.git
