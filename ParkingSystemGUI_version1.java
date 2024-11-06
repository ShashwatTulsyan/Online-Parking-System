/*In this version we have not taken all the information from the user to store the details */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ParkingSystemGUI_version1 extends JFrame {

    private JComboBox<String> vehicleTypeComboBox;
    private JComboBox<String> locationComboBox;
    private JComboBox<Integer> durationComboBox;
    private JPanel twoWheelerPanel, fourWheelerPanel;
    private JButton paymentButton;

    private JPanel[][] twoWheelerSlots = new JPanel[4][10];
    private JPanel[][] fourWheelerSlots = new JPanel[4][10];
    private JLabel[][] twoWheelerLabels = new JLabel[4][10];
    private JLabel[][] fourWheelerLabels = new JLabel[4][10];

    private Color availableColor = Color.GREEN;
    private Color bookedColor = Color.RED;

    // Database connection variables
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public ParkingSystemGUI_version1() {
        try {
            // Establish MySQL connection
            connectToDatabase();

            setTitle("Online Parking Management System");
            setSize(1000, 700); // Set size to cover more of the screen
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10); // Increased spacing

            // Heading label
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            JLabel headingLabel = new JLabel("Online Parking System", JLabel.CENTER);
            headingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 28)); // Increased font size and changed font
            add(headingLabel, gbc);

            // Vehicle type selection
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 1;
            JLabel vehicleLabel = new JLabel("Select Vehicle Type:");
            vehicleLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18)); // Larger font
            add(vehicleLabel, gbc);
            String[] vehicleTypes = {"2-Wheeler", "4-Wheeler"};
            vehicleTypeComboBox = new JComboBox<>(vehicleTypes);
            vehicleTypeComboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 18)); // Match font size
            gbc.gridx = 1;
            add(vehicleTypeComboBox, gbc);

            // Parking location selection
            gbc.gridx = 0;
            gbc.gridy = 2;
            JLabel locationLabel = new JLabel("Select Parking Location:");
            locationLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            add(locationLabel, gbc);
            String[] locations = {"Location A", "Location B", "Location C", "Location D"};
            locationComboBox = new JComboBox<>(locations);
            locationComboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            gbc.gridx = 1;
            add(locationComboBox, gbc);

            // Time duration selection
            gbc.gridx = 0;
            gbc.gridy = 3;
            JLabel durationLabel = new JLabel("Select Time Duration (Hours):");
            durationLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            add(durationLabel, gbc);
            Integer[] durations = {1, 2, 3, 4, 5};
            durationComboBox = new JComboBox<>(durations);
            durationComboBox.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            gbc.gridx = 1;
            add(durationComboBox, gbc);

            // Parking slots display for 2-Wheelers
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            JLabel twoWheelerLabel = new JLabel("2-Wheeler Parking Slots:");
            twoWheelerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22)); // Larger font for section headers
            add(twoWheelerLabel, gbc);
            twoWheelerPanel = createParkingSlotsPanel(twoWheelerSlots, twoWheelerLabels, availableColor, "2W-");
            gbc.gridy = 5;
            add(twoWheelerPanel, gbc);

            // Parking slots display for 4-Wheelers
            gbc.gridy = 6;
            JLabel fourWheelerLabel = new JLabel("4-Wheeler Parking Slots:");
            fourWheelerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
            add(fourWheelerLabel, gbc);

            gbc.gridy = 7;
            fourWheelerPanel = createParkingSlotsPanel(fourWheelerSlots, fourWheelerLabels, availableColor, "4W-");
            add(fourWheelerPanel, gbc);

            // Payment button
            gbc.gridy = 8;
            gbc.gridwidth = 1;
            paymentButton = new JButton("Make Payment");
            paymentButton.setFont(new Font("Comic Sans MS", Font.BOLD, 20)); // Increased button font
            paymentButton.setPreferredSize(new Dimension(250, 50)); // Enlarged button
            gbc.gridx = 0;
            gbc.gridwidth = 2; // Center the button below the options
            add(paymentButton, gbc);

            // ActionListener for payment button
            paymentButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleBooking();
                }
            });

            setVisible(true);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel createParkingSlotsPanel(JPanel[][] slots, JLabel[][] labels, Color initialColor, String prefix) {
        JPanel panel = new JPanel(new GridLayout(4, 10, 10, 10)); // Increased spacing
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 10; j++) {
                JPanel slot = new JPanel(new BorderLayout());
                slot.setPreferredSize(new Dimension(80, 80)); // Increased slot size
                slot.setBackground(initialColor);
                slot.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                JLabel label = new JLabel(prefix + (i + 1) + "-" + (j + 1), SwingConstants.CENTER);
                label.setFont(new Font("Comic Sans MS", Font.PLAIN, 16)); // Increased label font
                labels[i][j] = label;
                slot.add(label, BorderLayout.CENTER);

                slots[i][j] = slot;
                panel.add(slot);
            }
        }
        return panel;
    }

    private void handleBooking() {
        String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
        String location = (String) locationComboBox.getSelectedItem();
        int duration = (int) durationComboBox.getSelectedItem();

        try {
            if (isSlotAvailable(vehicleType, location)) {
                int option = JOptionPane.showConfirmDialog(null,
                        "Vehicle Type: " + vehicleType +
                                "\nParking Location: " + location +
                                "\nDuration: " + duration + " hour(s)" +
                                "\nProceed to Payment?",
                        "Confirm Parking Details",
                        JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    showPaymentDialog(vehicleType, location, duration);
                    markSlotAsBooked(vehicleType, location);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No available slots at the selected location.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error while checking slot availability: " + e.getMessage());
        }
    }

    private boolean isSlotAvailable(String vehicleType, String location) throws SQLException {
        String query = "SELECT * FROM ParkingSlots WHERE vehicle_type = ? AND location = ? AND is_booked = false";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, vehicleType);
        preparedStatement.setString(2, location);
        resultSet = preparedStatement.executeQuery();

        return resultSet.next(); // Returns true if there's at least one available slot
    }

    private void markSlotAsBooked(String vehicleType, String location) throws SQLException {
        String updateQuery = "UPDATE ParkingSlots SET is_booked = true WHERE vehicle_type = ? AND location = ? AND is_booked = false LIMIT 1";
        preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, vehicleType);
        preparedStatement.setString(2, location);
        preparedStatement.executeUpdate();
    }

    private void showPaymentDialog(String vehicleType, String location, int duration) {
        JTextField cardNumberField = new JTextField(16);
        JTextField cvvField = new JTextField(3);
        JTextField expiryDateField = new JTextField(5);

        JPanel paymentPanel = new JPanel(new GridLayout(3, 2));
        paymentPanel.add(new JLabel("Card Number:"));
        paymentPanel.add(cardNumberField);
        paymentPanel.add(new JLabel("CVV:"));
        paymentPanel.add(cvvField);
        paymentPanel.add(new JLabel("Expiry Date (MM/YY):"));
        paymentPanel.add(expiryDateField);

        int paymentOption = JOptionPane.showConfirmDialog(null, paymentPanel, "Payment Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (paymentOption == JOptionPane.OK_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment Successful!\n" +
                    "Vehicle Type: " + vehicleType +
                    "\nParking Location: " + location +
                    "\nDuration: " + duration + " hour(s)");
        }
    }

    // Establish connection to MySQL database
    private void connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/parkingsystemdb"; // Database URL
        String user = "root"; // Database username
        String password = "Shashwat@22"; // Database password

        connection = DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        new ParkingSystemGUI_version1();
    }
}
