package airlinemanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DummyPayment extends JFrame implements ActionListener {

    JTextField tfBookingRef, tfAmount, tfCardNumber, tfName;
    JPasswordField tfCVV;
    JButton payButton, cancelButton;

    public DummyPayment(String bookingRef, double amount) {
        setTitle("Dummy Payment");
        setSize(400, 350);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        setLocationRelativeTo(null);

        JLabel heading = new JLabel("Dummy Payment Gateway");
        heading.setBounds(50, 10, 300, 30);
        heading.setFont(new Font("Tahoma", Font.BOLD, 16));
        add(heading);

        JLabel lblBooking = new JLabel("Booking Reference:");
        lblBooking.setBounds(30, 60, 130, 25);
        add(lblBooking);

        tfBookingRef = new JTextField(bookingRef);
        tfBookingRef.setBounds(170, 60, 180, 25);
        add(tfBookingRef);

        JLabel lblAmount = new JLabel("Amount (INR):");
        lblAmount.setBounds(30, 100, 130, 25);
        add(lblAmount);

        tfAmount = new JTextField(amount > 0 ? String.valueOf(amount) : "");
        tfAmount.setBounds(170, 100, 180, 25);
        add(tfAmount);

        JLabel lblCard = new JLabel("Card Number:");
        lblCard.setBounds(30, 140, 130, 25);
        add(lblCard);

        tfCardNumber = new JTextField();
        tfCardNumber.setBounds(170, 140, 180, 25);
        add(tfCardNumber);

        JLabel lblName = new JLabel("Name on Card:");
        lblName.setBounds(30, 180, 130, 25);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(170, 180, 180, 25);
        add(tfName);

        JLabel lblCVV = new JLabel("CVV:");
        lblCVV.setBounds(30, 220, 130, 25);
        add(lblCVV);

        tfCVV = new JPasswordField();
        tfCVV.setBounds(170, 220, 80, 25);
        add(tfCVV);

        payButton = new JButton("Pay Now");
        payButton.setBounds(90, 260, 100, 30);
        payButton.addActionListener(this);
        add(payButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(210, 260, 100, 30);
        cancelButton.addActionListener(this);
        add(cancelButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancelButton) {
            dispose();
            return;
        }

        if (ae.getSource() == payButton) {
            String bookingRef = tfBookingRef.getText().trim();
            String amountStr = tfAmount.getText().trim();

            if (bookingRef.isEmpty() || amountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Booking Reference and Amount.", "Input required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double amount = 0.0;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Invalid input", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String card = tfCardNumber.getText().trim();
            String name = tfName.getText().trim();
            String cvv = new String(tfCVV.getPassword()).trim();

            if (card.length() < 12 || cvv.length() < 3 || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter valid dummy card details.", "Invalid card", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Conn conn = new Conn();
                String txnId = "TXN" + System.currentTimeMillis();
                String insert = "INSERT INTO payments (booking_ref, transaction_id, amount, status) VALUES ('" 
                                + bookingRef.replace("'", "") + "', '" + txnId + "', " + amount + ", 'SUCCESS')";
                conn.s.executeUpdate(insert);

                JOptionPane.showMessageDialog(this, "Payment Successful!\nTransaction ID: " + txnId, "Success", JOptionPane.INFORMATION_MESSAGE);
                JOptionPane.showMessageDialog(null, "Ticket Booked Successfully!\nBooking Reference: " + bookingRef);

                dispose();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
