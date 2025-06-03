package gui;

import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;

public class BookingPage extends JFrame {
    private JFormattedTextField txtTanggalBooking, txtWaktuMulai, txtWaktuSelesai;
    private JComboBox<String> cmbStatusBooking;
    private JComboBox<String> cmbPelanggan, cmbKomputer;

    private HashMap<String, Integer> pelangganMap = new HashMap<>();
    private HashMap<String, Integer> komputerMap = new HashMap<>();

    public BookingPage() {
        setTitle("Booking Komputer");
        setSize(500, 350);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtTanggalBooking = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd")));
        txtTanggalBooking.setValue(new java.util.Date());

        txtWaktuMulai = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("HH:mm")));
        txtWaktuSelesai = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("HH:mm")));

        cmbStatusBooking = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
        cmbPelanggan = new JComboBox<>();
        cmbKomputer = new JComboBox<>();

        addFormField(formPanel, "Pelanggan:", cmbPelanggan);
        addFormField(formPanel, "Tanggal Booking:", txtTanggalBooking);
        addFormField(formPanel, "Waktu Mulai:", txtWaktuMulai);
        addFormField(formPanel, "Waktu Selesai:", txtWaktuSelesai);
        addFormField(formPanel, "Status Booking:", cmbStatusBooking);
        addFormField(formPanel, "Komputer:", cmbKomputer);

        JPanel buttonPanel = new JPanel();
        JButton simpanBtn = new JButton("Simpan");
        JButton batalBtn = new JButton("Batal");
        buttonPanel.add(simpanBtn);
        buttonPanel.add(batalBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        simpanBtn.addActionListener(e -> simpan());
        batalBtn.addActionListener(e -> this.dispose());

        loadPelanggan();
        loadKomputer();
    }

    private void addFormField(JPanel panel, String label, JComponent field) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(jLabel, BorderLayout.WEST);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        inputPanel.add(field, BorderLayout.CENTER);
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(5));
    }

    private void loadPelanggan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama FROM Pelanggan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nama = rs.getString("nama");
                pelangganMap.put(nama, id);
                cmbPelanggan.addItem(nama);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pelanggan");
        }
    }

    private void loadKomputer() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, spesifikasi FROM Komputer WHERE status = 'Siap Pakai'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String spesifikasi = rs.getString("spesifikasi");
                komputerMap.put(spesifikasi, id);
                cmbKomputer.addItem(spesifikasi);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat komputer");
        }
    }

    private void simpan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Booking (status_booking, tanggal_booking, waktu_mulai, waktu_selesai, pelanggan_id, komputer_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            String tanggalStr = txtTanggalBooking.getText();
            String mulaiStr = txtWaktuMulai.getText();
            String selesaiStr = txtWaktuSelesai.getText();

            SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Timestamp waktuMulai = new Timestamp(fullFormat.parse(tanggalStr + " " + mulaiStr).getTime());
            Timestamp waktuSelesai = new Timestamp(fullFormat.parse(tanggalStr + " " + selesaiStr).getTime());

            stmt.setString(1, (String) cmbStatusBooking.getSelectedItem());
            stmt.setDate(2, java.sql.Date.valueOf(tanggalStr));
            stmt.setTimestamp(3, waktuMulai);
            stmt.setTimestamp(4, waktuSelesai);
            stmt.setInt(5, pelangganMap.get((String) cmbPelanggan.getSelectedItem()));
            stmt.setInt(6, komputerMap.get((String) cmbKomputer.getSelectedItem()));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Booking berhasil disimpan!");
            this.dispose();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan booking!\n" + e.getMessage());
        }
    }
}