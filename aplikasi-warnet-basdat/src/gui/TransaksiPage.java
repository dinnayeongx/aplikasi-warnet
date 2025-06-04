package gui;

import db.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;

public class TransaksiPage extends JFrame {
    private JTextField waktuMulaiField;
    private JTextField waktuSelesaiField;
    private JComboBox<String> metodeBayarCombo;
    private JTextField totalBayarField;
    private JComboBox<String> pegawaiCombo;
    private JComboBox<String> layananCombo;

    private HashMap<String, Integer> pegawaiMap = new HashMap<>();
    private HashMap<String, Integer> layananMap = new HashMap<>();

    private String bookingId;

    public TransaksiPage(String bookingId) {
        this.bookingId = bookingId;
        setTitle("Transaksi");
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(createFieldPanel("Waktu Mulai (yyyy-MM-dd HH:mm):", waktuMulaiField = new JTextField()));
        formPanel.add(createFieldPanel("Waktu Selesai (yyyy-MM-dd HH:mm):", waktuSelesaiField = new JTextField()));
        metodeBayarCombo = new JComboBox<>(new String[]{"Tunai", "Kartu", "E-Wallet"});
        formPanel.add(createFieldPanel("Metode Bayar:", metodeBayarCombo));
        formPanel.add(createFieldPanel("Total Bayar:", totalBayarField = new JTextField()));
        pegawaiCombo = new JComboBox<>();
        formPanel.add(createFieldPanel("Pegawai:", pegawaiCombo));
        layananCombo = new JComboBox<>();
        formPanel.add(createFieldPanel("Layanan:", layananCombo));

        loadPegawai();
        loadLayanan();

        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton("Simpan");
        JButton batalBtn = new JButton("Batal");
        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);

        simpanBtn.addActionListener(e -> simpanTransaksi());
        batalBtn.addActionListener(e -> dispose());

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private JPanel createFieldPanel(String label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(150, 25));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        panel.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
        return panel;
    }

    private void loadPegawai() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama FROM Pegawai";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nama = rs.getString("nama");
                String label = id + " - " + nama;
                pegawaiCombo.addItem(label);
                pegawaiMap.put(label, id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pegawai: " + e.getMessage());
        }
    }

    private void loadLayanan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama_layanan FROM Layanan";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nama = rs.getString("nama_layanan");
                String label = id + " - " + nama;
                layananCombo.addItem(label);
                layananMap.put(label, id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat layanan: " + e.getMessage());
        }
    }

    private void simpanTransaksi() {
        if (!validasiInput()) return;

        String mulai = waktuMulaiField.getText().trim();
        String selesai = waktuSelesaiField.getText().trim();
        String metode = (String) metodeBayarCombo.getSelectedItem();
        double total = Double.parseDouble(totalBayarField.getText().trim());

        int pegawaiId = pegawaiMap.get((String) pegawaiCombo.getSelectedItem());
        int layananId = layananMap.get((String) layananCombo.getSelectedItem());

        try (Connection conn = DBConnection.getConnection()) {
            // Ambil pelanggan_id dan komputer_id dari booking
            String queryBooking = "SELECT pelanggan_id, komputer_id FROM Booking WHERE ID = ?";
            PreparedStatement psBooking = conn.prepareStatement(queryBooking);
            psBooking.setString(1, bookingId);
            ResultSet rs = psBooking.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Data booking tidak ditemukan.");
                return;
            }

            int pelangganId = rs.getInt("pelanggan_id");
            int komputerId = rs.getInt("komputer_id");

            // Simpan transaksi lengkap
            String sql = """
                INSERT INTO Transaksi (
                    waktu_mulai, waktu_selesai, metode_bayar, total_bayar,
                    pelanggan_id, pegawai_id, komputer_id, layanan_id
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setTimestamp(1, Timestamp.valueOf(mulai));
            stmt.setTimestamp(2, Timestamp.valueOf(selesai));
            stmt.setString(3, metode);
            stmt.setDouble(4, total);
            stmt.setInt(5, pelangganId);
            stmt.setInt(6, pegawaiId);
            stmt.setInt(7, komputerId);
            stmt.setInt(8, layananId);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan transaksi: " + e.getMessage());
        }
    }

    private boolean validasiInput() {
        try {
            if (waktuMulaiField.getText().trim().isEmpty() ||
                waktuSelesaiField.getText().trim().isEmpty() ||
                totalBayarField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi.");
                return false;
            }
            Double.parseDouble(totalBayarField.getText().trim());
            Timestamp.valueOf(waktuMulaiField.getText().trim());
            Timestamp.valueOf(waktuSelesaiField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format waktu atau total bayar tidak valid.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransaksiPage("BK00123"));
    }
}