# Online Parking System

A Java Swing-based GUI application designed to manage parking spaces for 2-wheelers and 4-wheelers. This system provides an interactive interface for selecting parking slots, payment processing, and booking confirmation, leveraging JDBC to store booking data in a MySQL database.

## Features

- **Vehicle Selection**: Choose between 2-Wheeler and 4-Wheeler parking options.
- **Time Duration**: Specify the parking duration in hours.
- **Parking Slot Visualization**: Display available slots for 2-wheelers and 4-wheelers with interactive slot selection.
- **Color-Coded Status**:
  - Green: Available slot
  - Yellow: Selected slot
  - Red: Booked slot
- **User Validation**: Validate mobile number, card number, CVV, and expiry date during payment.
- **Database Storage**: Store booking information in a MySQL database.

## Prerequisites

- **Java**: JDK 8 or later
- **MySQL Database**: To store booking records.
- **JDBC Driver**: MySQL Connector/J (ensure it’s added to the project classpath).

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/ShashwatTulsyan/Online-Parking-System.git
    cd ParkingSystemGUIProject
    ```

2. Set up the MySQL database:
   - Create a database named `parkingsystemdb`.
   - Create a `bookings` table using the following SQL:
     ```sql
     CREATE TABLE bookings (
         id INT PRIMARY KEY AUTO_INCREMENT,
         username VARCHAR(50),
         mobile VARCHAR(15),
         car_number VARCHAR(15),
         card_number VARCHAR(16),
         cvv VARCHAR(3),
         expiry_date VARCHAR(5),
         duration INT
     );
     ```

3. Update the database credentials:
   - Update the `insertBookingData` method in `ParkingSystemGUIProject.java` with your MySQL username and password:
     ```java
     Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/parkingsystemdb", "your_username", "your_password");
     ```

## Usage

1. **Compile and Run**:
    ```bash
    javac ParkingSystemGUIProject.java
    java ParkingSystemGUIProject
    ```

2. **Select Vehicle and Duration**:
   - Choose between 2-Wheeler and 4-Wheeler options.
   - Select a time duration in hours.

3. **Slot Selection**:
   - Click on an available slot to select it.

4. **Proceed to Payment**:
   - Click "Proceed to Payment" after selecting a slot.
   - Enter your details, including a valid credit card number, CVV, and expiry date.

5. **Booking Confirmation**:
   - On successful payment, the selected slot turns red, indicating a booked status.

## Code Highlights

- **GUI Components**: Created using Java Swing (`JFrame`, `JPanel`, `JComboBox`, `JButton`, etc.).
- **Database Integration**: Uses JDBC to connect and insert booking data into MySQL.
- **Custom Validation**:
   - Ensures mobile numbers start with 6, 7, 8, or 9 and have exactly 10 digits.
   - Validates card details, including length, format, and expiry date check.

## Potential Improvements

- Add cancellation or modification functionality for bookings.
- Improve error handling, especially for database-related issues.
- Expand the system to handle multiple parking lots.

## Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue if you have suggestions for improvements.

**Note**: Replace `"your_username"` and `"your_password"` in the JDBC connection string with your actual MySQL credentials.
