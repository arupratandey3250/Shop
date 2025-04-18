# Cafe Management System

A Java-based desktop application for managing cafe operations including menu viewing, order processing, customer reviews, and sales analytics.

## Features

### Authentication
- Dual login system (Admin & User)
- Admin credentials: username="arup", password="3250"

### User Features
- **View Menu**: Displays the cafe's menu card
- **Place Orders**: Interactive order interface with:
  - Sandwich selection
  - Coffee selection
  - Order modification capabilities
- **Submit Reviews**: 
  - Star ratings (1-5)
  - Text comments
  - Image uploads
  - Review display section

### Admin Features
- **Sales Reports**: View all transaction records
- **Sales Analytics**: Visual pie chart of sales distribution
- **All User Features**

## Technical Details

### Technologies Used
- Java Swing for GUI
- JFreeChart for data visualization
- File I/O for data persistence

### File Structure
- `sales.txt`: Stores order transactions
- `reviews.txt`: Stores customer reviews
- `reviews_images/`: Directory for review images

### Design Patterns
- MVC-like architecture
- Singleton pattern for main application window
- Observer pattern for UI updates

## Installation & Usage

### Requirements
- Java 8 or higher
- JFreeChart library (included in most Java IDEs)

### Running the Application
1. Compile all Java files:
   ```bash
   javac Shop.java
   java Shop
## Using the System
- Select login type (User/Admin)
- For admin, enter credentials
- Navigate through the intuitive GUI:
- Menu buttons for different functionalities
- Interactive order interface
- Review submission form


## Role Select
![logo](https://github.com/arupratandey3250/Shop/blob/main/Screenshot%202025-04-18%20193026.png)
