package gui;

import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;

public class BookingPage extends JFrame {
    private JTextField idField;
    private JComboBox<String> pelangganComboBox;
    private JComboBox<String> komputerComboBox;

    public BookingPage() {
        setTitle("Booking Komputer");
        setSize(500, 350);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] labels = {
            "ID:",
            "Pelanggan:",
            "Tanggal Booking:",
            "Waktu Mulai:",
            "Waktu Selesai:",
            "Status Booking:",
            "Komputer:"
        };

        for (String label : labels) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);

            JComponent field;

            if (label.equals("ID:")) {
                idField = new JTextField();
                field = idField;
            } else if (label.equals("Tanggal Booking:")) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                DateFormatter df = new DateFormatter(format);
                JFormattedTextField dateField = new JFormattedTextField(df);
                dateField.setValue(new Date());
                field = dateField;
            } else if (label.contains("Waktu")) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormatter tf = new DateFormatter(timeFormat);
                JFormattedTextField timeField = new JFormattedTextField(tf);
                timeField.setToolTipText("Format: HH:mm");
                timeField.setText("HH:mm");
                timeField.setForeground(Color.GRAY);
                field = timeField;
            } else if (label.contains("Status")) {
                field = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
            } else if (label.contains("Pelanggan")) {
                pelangganComboBox = new JComboBox<>(new String[]{"Pelanggan 1", "Pelanggan 2"});
                field = pelangganComboBox;
            } else if (label.contains("Komputer")) {
                komputerComboBox = new JComboBox<>(new String[]{"Komputer 1", "Komputer 2"});
                field = komputerComboBox;
            } else {
                field = new JTextField();
            }

            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            inputPanel.add(field, BorderLayout.CENTER);
            formPanel.add(inputPanel);
            formPanel.add(Box.createVerticalStrut(5));
        }

        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton("Simpan");
        JButton batalBtn = new JButton("Batal");
        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        simpanBtn.addActionListener(e -> {
            String bookingId = idField.getText().trim();
            if (bookingId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID Booking tidak boleh kosong!");
                return;
            }
            // TODO: Simpan data booking ke DB di sini

            JOptionPane.showMessageDialog(this, "Booking disimpan");

            // Buka TransaksiPage dengan bookingId
            new TransaksiPage(bookingId);

            // Tutup BookingPage
            this.dispose();
        });

        batalBtn.addActionListener(e -> this.dispose());
    }
}