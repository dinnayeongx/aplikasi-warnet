package gui;

import java.awt.*;
import javax.swing.*;

public class TransaksiPage extends JFrame {
    public TransaksiPage() {
        setTitle("Transaksi");
        setSize(500, 350);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] labels = {"ID Transaksi:", "Waktu Mulai:", "Waktu Selesai:", "Metode Bayar:", "Total Bayar:", "Pegawai:", "Layanan:"};
        for (String label : labels) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);
            JComponent field;
            if(label.equals("Metode Bayar:")) {
                field = new JComboBox<>(new String[]{"Tunai", "Kartu", "E-Wallet"});
            } else if(label.equals("Pegawai:")) {
                field = new JComboBox<>();
            } else if(label.equals("Layanan:")) {
                field = new JList<>(new String[] {"Layanan 1", "Layanan 2", "Layanan 3"});
                JScrollPane scrollPane = new JScrollPane((JList<?>)field);
                scrollPane.setPreferredSize(new Dimension(200, 60));
                inputPanel.add(scrollPane, BorderLayout.CENTER);
                formPanel.add(inputPanel);
                formPanel.add(Box.createVerticalStrut(5));
                continue;
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

        simpanBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Transaksi disimpan"));
        batalBtn.addActionListener(e -> this.dispose());
    }
}
