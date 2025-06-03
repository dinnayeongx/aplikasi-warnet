package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;

public class TransaksiPage extends JFrame {
    private JTextField idTransaksiField;
    private JTextField bookingIdField;
    private JTextField waktuMulaiField;
    private JTextField waktuSelesaiField;
    private JComboBox<String> metodeBayarCombo;
    private JTextField totalBayarField;
    private JComboBox<String> pegawaiCombo;
    private JList<String> layananList;

    public TransaksiPage(String bookingId) {
        setTitle("Transaksi");
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ID Transaksi (readonly)
        JPanel idPanel = new JPanel(new BorderLayout());
        JLabel idLabel = new JLabel("ID Transaksi:");
        idLabel.setPreferredSize(new Dimension(120, 25));
        idPanel.add(idLabel, BorderLayout.WEST);
        idTransaksiField = new JTextField();
        idTransaksiField.setEditable(false);
        idTransaksiField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        idPanel.add(idTransaksiField, BorderLayout.CENTER);
        formPanel.add(idPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Booking ID (readonly)
        JPanel bookingPanel = new JPanel(new BorderLayout());
        JLabel bookingLabel = new JLabel("Booking ID:");
        bookingLabel.setPreferredSize(new Dimension(120, 25));
        bookingPanel.add(bookingLabel, BorderLayout.WEST);
        bookingIdField = new JTextField(bookingId);
        bookingIdField.setEditable(false);
        bookingIdField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        bookingPanel.add(bookingIdField, BorderLayout.CENTER);
        formPanel.add(bookingPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Waktu Mulai
        JPanel waktuMulaiPanel = new JPanel(new BorderLayout());
        JLabel waktuMulaiLabel = new JLabel("Waktu Mulai (HH:mm):");
        waktuMulaiLabel.setPreferredSize(new Dimension(120, 25));
        waktuMulaiPanel.add(waktuMulaiLabel, BorderLayout.WEST);
        waktuMulaiField = new JTextField();
        waktuMulaiField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        waktuMulaiPanel.add(waktuMulaiField, BorderLayout.CENTER);
        formPanel.add(waktuMulaiPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Waktu Selesai
        JPanel waktuSelesaiPanel = new JPanel(new BorderLayout());
        JLabel waktuSelesaiLabel = new JLabel("Waktu Selesai (HH:mm):");
        waktuSelesaiLabel.setPreferredSize(new Dimension(120, 25));
        waktuSelesaiPanel.add(waktuSelesaiLabel, BorderLayout.WEST);
        waktuSelesaiField = new JTextField();
        waktuSelesaiField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        waktuSelesaiPanel.add(waktuSelesaiField, BorderLayout.CENTER);
        formPanel.add(waktuSelesaiPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Metode Bayar
        JPanel metodeBayarPanel = new JPanel(new BorderLayout());
        JLabel metodeBayarLabel = new JLabel("Metode Bayar:");
        metodeBayarLabel.setPreferredSize(new Dimension(120, 25));
        metodeBayarPanel.add(metodeBayarLabel, BorderLayout.WEST);
        metodeBayarCombo = new JComboBox<>(new String[]{"Tunai", "Kartu", "E-Wallet"});
        metodeBayarPanel.add(metodeBayarCombo, BorderLayout.CENTER);
        formPanel.add(metodeBayarPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Total Bayar
        JPanel totalBayarPanel = new JPanel(new BorderLayout());
        JLabel totalBayarLabel = new JLabel("Total Bayar:");
        totalBayarLabel.setPreferredSize(new Dimension(120, 25));
        totalBayarPanel.add(totalBayarLabel, BorderLayout.WEST);
        totalBayarField = new JTextField();
        totalBayarField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        totalBayarPanel.add(totalBayarField, BorderLayout.CENTER);
        formPanel.add(totalBayarPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Pegawai
        JPanel pegawaiPanel = new JPanel(new BorderLayout());
        JLabel pegawaiLabel = new JLabel("Pegawai:");
        pegawaiLabel.setPreferredSize(new Dimension(120, 25));
        pegawaiPanel.add(pegawaiLabel, BorderLayout.WEST);
        pegawaiCombo = new JComboBox<>(new String[]{"Pegawai 1", "Pegawai 2", "Pegawai 3"});
        pegawaiPanel.add(pegawaiCombo, BorderLayout.CENTER);
        formPanel.add(pegawaiPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Layanan (multi select list)
        JPanel layananPanel = new JPanel(new BorderLayout());
        JLabel layananLabel = new JLabel("Layanan:");
        layananLabel.setPreferredSize(new Dimension(120, 25));
        layananPanel.add(layananLabel, BorderLayout.WEST);
        layananList = new JList<>(new String[]{"Layanan 1", "Layanan 2", "Layanan 3"});
        layananList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane layananScrollPane = new JScrollPane(layananList);
        layananScrollPane.setPreferredSize(new Dimension(200, 80));
        layananPanel.add(layananScrollPane, BorderLayout.CENTER);
        formPanel.add(layananPanel);
        formPanel.add(Box.createVerticalStrut(5));

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton("Simpan");
        JButton batalBtn = new JButton("Batal");
        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        simpanBtn.addActionListener(e -> {
            if (validasiInput()) {
                String waktuMulai = waktuMulaiField.getText().trim();
                String waktuSelesai = waktuSelesaiField.getText().trim();
                String metodeBayar = (String) metodeBayarCombo.getSelectedItem();
                String totalBayar = totalBayarField.getText().trim();
                String pegawai = (String) pegawaiCombo.getSelectedItem();
                List<String> layananTerpilih = layananList.getSelectedValuesList();

                String pesan = "Booking ID: " + bookingId + "\n" +
                        "Waktu Mulai: " + waktuMulai + "\n" +
                        "Waktu Selesai: " + waktuSelesai + "\n" +
                        "Metode Bayar: " + metodeBayar + "\n" +
                        "Total Bayar: " + totalBayar + "\n" +
                        "Pegawai: " + pegawai + "\n" +
                        "Layanan: " + String.join(", ", layananTerpilih);

                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan:\n\n" + pesan);
                this.dispose();
            }
        });

        batalBtn.addActionListener(e -> this.dispose());

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private boolean validasiInput() {
        if (waktuMulaiField.getText().trim().isEmpty() ||
                waktuSelesaiField.getText().trim().isEmpty() ||
                totalBayarField.getText().trim().isEmpty() ||
                layananList.getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(this, "Harap isi semua field dan pilih minimal satu layanan.");
            return false;
        }
        try {
            Double.valueOf(totalBayarField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Total Bayar harus berupa angka.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransaksiPage("BK00123"));
    }
}
