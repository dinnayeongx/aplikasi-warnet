// gui/BookingPage.java
package gui;

import java.awt.*;
import javax.swing.*;

public class BookingPage extends JFrame {
    public BookingPage() {
        setTitle("Booking Komputer");
        setSize(500, 350);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] labels = {"ID:", "Pelanggan:", "Tanggal Booking:", "Waktu Mulai:", "Waktu Selesai:", "Status Booking:", "Komputer:"};
        for (String label : labels) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);
            JComponent field = label.contains("Status") ? new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"})
                                   : label.contains("Pelanggan") || label.contains("Komputer") ? new JComboBox<>()
                                   : new JTextField();
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

        // ActionListeners
        simpanBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Booking disimpan"));
        batalBtn.addActionListener(e -> this.dispose()); // tutup window
    }
}