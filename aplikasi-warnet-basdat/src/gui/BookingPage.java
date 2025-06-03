package gui;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.text.DateFormatter;

public class BookingPage extends JFrame {
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

            if (label.equals("Tanggal Booking:")) {
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                DateFormatter df = new DateFormatter(format);
                JFormattedTextField dateField = new JFormattedTextField(df);
                dateField.setValue(new Date()); // default ke hari ini
                field = dateField;
            } else if (label.contains("Waktu")) {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormatter tf = new DateFormatter(timeFormat);
                JFormattedTextField timeField = new JFormattedTextField(tf);
                timeField.setToolTipText("Format: HH:mm"); // optional
                timeField.setText("HH:mm"); // placeholder (tidak akan hilang otomatis)
                timeField.setForeground(Color.GRAY);
                field = timeField;
            } else if (label.contains("Status")) {
                field = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
            } else if (label.contains("Pelanggan") || label.contains("Komputer")) {
                field = new JComboBox<>();
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

        simpanBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Booking disimpan"));
        batalBtn.addActionListener(e -> this.dispose()); // tutup window
    }
}
