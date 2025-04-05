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
- **Database Storage**: Store booking information securely in a MySQL database.

## Prerequisites

- **Java**: JDK 8 or later
- **MySQL Database**: To store booking records.
- **JDBC Driver**: MySQL Connector/J (ensure it’s added to the project classpath).

## Installation

1. **Clone the Repository**:
    ```bash
    git clone https://github.com/ShashwatTulsyan/Online-Parking-System.git
    cd Online-Parking-System
    ```

2. **Set Up the MySQL Database**:
    - Create a database named `parkingsystemdb`.
    - Create the `bookings` table using the following SQL:
      ```sql
      CREATE TABLE bookings (
          id INT PRIMARY KEY AUTO_INCREMENT,
          username VARCHAR(50) NOT NULL,
          mobile VARCHAR(15) NOT NULL,
          car_number VARCHAR(15) NOT NULL,
          card_number VARCHAR(64) NOT NULL,
          cvv VARCHAR(64) NOT NULL,
          expiry_date VARCHAR(5) NOT NULL,
          duration INT NOT NULL CHECK (duration > 0),
          slot_id VARCHAR(20) NOT NULL,
          status ENUM('booked', 'cancelled', 'expired') DEFAULT 'booked',
          booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

          CONSTRAINT unique_slot UNIQUE (slot_id, status)
      );
      ```

3. **Update the Database Credentials**:
    - Update the `insertBookingData` method in `ParkingSystemGUIProject.java` with your MySQL username and password:
      ```java
      Connection conn = DriverManager.getConnection(
          "jdbc:mysql://localhost:3306/parkingsystemdb", "your_username", "your_password");
      ```

## Usage

1. **Compile and Run**:
    ```bash
    javac -cp ".;path_to_mysql_connector.jar" ParkingSystemGUIProject.java
    java -cp ".;path_to_mysql_connector.jar" ParkingSystemGUIProject
    ```
    Replace `path_to_mysql_connector.jar` with the actual path to your MySQL Connector JAR file.

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

- **GUI Components**: Built using Java Swing (`JFrame`, `JPanel`, `JComboBox`, `JButton`, etc.).
- **Database Integration**: Utilizes JDBC to connect and store booking data securely in MySQL.
- **Custom Validation**:
   - Validates mobile numbers (must start with 6, 7, 8, or 9 and be exactly 10 digits).
   - Validates credit card number, CVV, and expiry format.

## Potential Improvements

- Add cancellation or modification functionality for bookings.
- Encrypt sensitive data like card number and CVV before saving to the database.
- Enhance error handling for more detailed feedback.
- Support for multiple parking locations or lots.
- Include an admin panel for booking overview and analytics.

## Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue if you have suggestions for improvements.

> **Note**: Don't forget to replace `"your_username"` and `"your_password"` in the JDBC connection string with your actual MySQL credentials.

