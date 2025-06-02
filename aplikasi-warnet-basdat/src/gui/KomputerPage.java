package gui;

import java.awt.*;
import javax.swing.*;

public class KomputerPage extends JFrame {
    public KomputerPage() {
        setTitle("Manajemen Komputer");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] labels = {"ID:", "Spesifikasi:", "Lokasi:", "Status:"};
        for (String label : labels) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);
            JComponent field = label.equals("Status:") ? new JComboBox<>(new String[]{"Siap Pakai", "Sedang Dipakai"}) : new JTextField();
            field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            inputPanel.add(field, BorderLayout.CENTER);
            formPanel.add(inputPanel);
            formPanel.add(Box.createVerticalStrut(5));
        }

        JPanel buttonPanel = new JPanel();
        JButton tambahBtn = new JButton("Tambah");
        JButton editBtn = new JButton("Edit");
        JButton hapusBtn = new JButton("Hapus");
        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(hapusBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        tambahBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Tambah Komputer clicked"));
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Edit Komputer clicked"));
        hapusBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Hapus Komputer clicked"));
    }
}