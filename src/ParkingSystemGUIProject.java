import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class ParkingSystemGUIProject extends JFrame {

    private JComboBox<String> vehicleTypeComboBox;
    private JComboBox<Integer> durationComboBox;
    private JPanel twoWheelerPanel, fourWheelerPanel;
    private JButton paymentButton;

    private JPanel[][] twoWheelerSlots = new JPanel[4][10];
    private JPanel[][] fourWheelerSlots = new JPanel[4][10];
    private JLabel[][] twoWheelerLabels = new JLabel[4][10];
    private JLabel[][] fourWheelerLabels = new JLabel[4][10];

    private Color availableColor = new Color(76, 175, 80); // Green
    private Color bookedColor = new Color(244, 67, 54); // Red
    private Color selectedColor = new Color(255, 235, 59); // Yellow

    private static final int PRICE_PER_HOUR = 100;

    private JPanel selectedSlot = null;

    public ParkingSystemGUIProject() {
        setTitle("Somaiya Online Parking System");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(230, 230, 250)); // Light lavender background

        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel vehicleLabel = new JLabel("Select Vehicle Type:");
        vehicleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(vehicleLabel, gbc);
        String[] vehicleTypes = {"2-Wheeler", "4-Wheeler"};
        vehicleTypeComboBox = new JComboBox<>(vehicleTypes);
        vehicleTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 1;
        contentPanel.add(vehicleTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel durationLabel = new JLabel("Select Time Duration (Hours):");
        durationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        contentPanel.add(durationLabel, gbc);
        Integer[] durations = {1, 2, 3, 4, 5};
        durationComboBox = new JComboBox<>(durations);
        durationComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        gbc.gridx = 1;
        contentPanel.add(durationComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel twoWheelerLabel = new JLabel("2-Wheeler Parking Slots:");
        twoWheelerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        contentPanel.add(twoWheelerLabel, gbc);
        twoWheelerPanel = createParkingSlotsPanel(twoWheelerSlots, twoWheelerLabels, "2W-");
        gbc.gridy = 3;
        contentPanel.add(twoWheelerPanel, gbc);

        gbc.gridy = 4;
        JLabel fourWheelerLabel = new JLabel("4-Wheeler Parking Slots:");
        fourWheelerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        contentPanel.add(fourWheelerLabel, gbc);
        fourWheelerPanel = createParkingSlotsPanel(fourWheelerSlots, fourWheelerLabels, "4W-");
        gbc.gridy = 5;
        contentPanel.add(fourWheelerPanel, gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        paymentButton = new JButton("Proceed to Payment");
        paymentButton.setFont(new Font("Segoe UI", Font.BOLD, 22));
        paymentButton.setBackground(new Color(0, 153, 51)); // Dark green button
        paymentButton.setForeground(Color.WHITE);
        paymentButton.setPreferredSize(new Dimension(300, 50));
        paymentButton.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        contentPanel.add(paymentButton, gbc);

        paymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selectedSlot == null) {
                    JOptionPane.showMessageDialog(ParkingSystemGUIProject.this, "Please select a parking slot.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    showPaymentForm();
                }
            }
        });

        return contentPanel;
    }

    private JPanel createParkingSlotsPanel(JPanel[][] slots, JLabel[][] labels, String labelPrefix) {
        JPanel panel = new JPanel(new GridLayout(4, 10, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Available Slots"));
        panel.setBackground(Color.WHITE);
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 10; col++) {
                slots[row][col] = new JPanel();
                slots[row][col].setBackground(availableColor);
                slots[row][col].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                labels[row][col] = new JLabel(labelPrefix + (row * 10 + col + 1));
                labels[row][col].setFont(new Font("Segoe UI", Font.BOLD, 14));
                slots[row][col].add(labels[row][col]);

                final JPanel currentSlot = slots[row][col];
                currentSlot.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        String selectedVehicleType = (String) vehicleTypeComboBox.getSelectedItem();
                        boolean isTwoWheeler = selectedVehicleType.equals("2-Wheeler");

                        if (isTwoWheeler && !labelPrefix.equals("2W-")) {
                            JOptionPane.showMessageDialog(ParkingSystemGUIProject.this,
                                    "You have selected a 2-Wheeler parking slot.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (!isTwoWheeler && labelPrefix.equals("2W-")) {
                            JOptionPane.showMessageDialog(ParkingSystemGUIProject.this,
                                    "You have selected a 4-Wheeler parking slot.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            if (selectedSlot != null && selectedSlot != currentSlot) {
                                selectedSlot.setBackground(availableColor); // Deselect previous slot
                            }
                            selectedSlot = currentSlot;
                            selectedSlot.setBackground(selectedColor);
                        }
                    }
                });
                panel.add(slots[row][col]);
            }
        }
        return panel;
    }

    private JTextField usernameField;
    private JTextField mobileField;
    private JTextField carNumberField;
    private JTextField cardNumberField;
    private JTextField cvvField;
    private JTextField expiryDateField;




    // Custom exception class for invalid mobile number
private class InvalidMobileNumberException extends Exception {
    public InvalidMobileNumberException(String message) {
        super(message);
    }
}

    private void showPaymentForm() {
        while (true) {
            // Initialize fields if null, or retain existing input
            if (usernameField == null) {
                usernameField = new JTextField();
                mobileField = new JTextField();
                carNumberField = new JTextField();
                cardNumberField = new JTextField();
                cvvField = new JTextField();
                expiryDateField = new JTextField();
            }
    
            Object[] message = {
                    "Username:", usernameField,
                    "Mobile Number:", mobileField,
                    "Car Number:", carNumberField,
                    "Credit Card Number:", cardNumberField,
                    "CVV:", cvvField,
                    "Expiry Date (MM/YY):", expiryDateField
            };
    
            int option = JOptionPane.showConfirmDialog(this, message, "Payment", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.CANCEL_OPTION) {
                return; // Exit if the user cancels
            }
    
            try {
                // Validate mobile number
                validateMobileNumber(mobileField.getText());
    
                // Validate card details
                validateCardDetails(cardNumberField.getText(), cvvField.getText(), expiryDateField.getText());
    
                int duration = (Integer) durationComboBox.getSelectedItem();
                int amountPaid = duration * PRICE_PER_HOUR;
    
                // Insert booking information into the database
                if (insertBookingData(usernameField.getText(), mobileField.getText(), carNumberField.getText(),
                        cardNumberField.getText(), cvvField.getText(), expiryDateField.getText(), duration, selectedSlot)) {
                    JOptionPane.showMessageDialog(this, "Payment of " + amountPaid + " INR successful!", "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
                    if (selectedSlot != null) {
                        selectedSlot.setBackground(bookedColor);
                        selectedSlot = null; // Reset selected slot after booking
                    }
                    clearPaymentFormFields(); // Clear fields on successful payment
                    break; // Exit the loop on successful payment
                } else {
                    JOptionPane.showMessageDialog(this, "Error saving booking data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (InvalidMobileNumberException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Invalid Mobile Number", JOptionPane.ERROR_MESSAGE);
                // The loop will continue, showing the form again for the user to correct their input.
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Validation Error", JOptionPane.ERROR_MESSAGE);
                // The loop will continue, showing the form again for the user to correct their input.
            }
        }
    }
    
    // Method to validate mobile number
private void validateMobileNumber(String mobileNumber) throws InvalidMobileNumberException {
    if (mobileNumber.length() != 10 || !mobileNumber.matches("^[6789]\\d{9}$")) {
        throw new InvalidMobileNumberException("Mobile number must start with 6, 7, 8, or 9 and be exactly 10 digits.");
    }
}

    
    private boolean insertBookingData(String username, String mobile, String carNumber, String cardNumber, String cvv, String expiryDate, int duration, JPanel selectedSlot) {
        // Database connection and insertion logic
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/parkingsystemdb", "root", "Shashwat@22");
            String query = "INSERT INTO bookings (username, mobile, car_number, card_number, cvv, expiry_date, duration) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, mobile);
            pst.setString(3, carNumber);
            pst.setString(4, cardNumber);
            pst.setString(5, cvv);
            pst.setString(6, expiryDate);
            pst.setInt(7, duration);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("SQL Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            System.out.println("Error Message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        
    }

    // Method to validate card details
private void validateCardDetails(String cardNumber, String cvv, String expiryDate) throws IllegalArgumentException {
    if (cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
        throw new IllegalArgumentException("Credit Card Number must be exactly 16 digits.");
    }
    if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
        throw new IllegalArgumentException("CVV must be exactly 3 digits.");
    }
    if (!expiryDate.matches("(0[1-9]|1[0-2])/\\d{2}")) {
        throw new IllegalArgumentException("Expiry Date must be in MM/YY format.");
    }
    // Validate expiry date
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
    sdf.setLenient(false);
    try {
        Date expiry = sdf.parse(expiryDate);
        Calendar expiryCal = Calendar.getInstance();
        expiryCal.setTime(expiry);
        // Check if the expiry date is at least 30 days from now
        Calendar currentCal = Calendar.getInstance();
        currentCal.add(Calendar.DAY_OF_MONTH, 30);
        
        if (expiryCal.before(currentCal)) {
            throw new IllegalArgumentException("Expiry date must be at least 30 days from today.");
        }
    } catch (ParseException e) {
        throw new IllegalArgumentException("Invalid expiry date format.");
    }
}

    private void clearPaymentFormFields() {
        usernameField.setText("");
        mobileField.setText("");
        carNumberField.setText("");
        cardNumberField.setText("");
        cvvField.setText("");
        expiryDateField.setText("");
    }

    public static void main(String[] args) {
        new ParkingSystemGUIProject();
    }
}
