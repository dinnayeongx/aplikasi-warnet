package gui;

import java.awt.*;
import javax.swing.*;
import java.sql.*;
import java.util.HashMap;

public class BookingPage extends JFrame {
    private JTextField txtTanggalBooking, txtWaktuMulai, txtWaktuSelesai;
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

        String[] labels = {"Pelanggan:", "Tanggal Booking:", "Waktu Mulai:", "Waktu Selesai:", "Status Booking:", "Komputer:"};
        for (String label : labels) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(label);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);

            JComponent field;
            switch (label) {
                case "Status Booking:":
                    cmbStatusBooking = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
                    field = cmbStatusBooking;
                    break;
                case "Pelanggan:":
                    cmbPelanggan = new JComboBox<>();
                    field = cmbPelanggan;
                    break;
                case "Komputer:":
                    cmbKomputer = new JComboBox<>();
                    field = cmbKomputer;
                    break;
                case "Tanggal Booking:":
                    txtTanggalBooking = new JTextField();
                    field = txtTanggalBooking;
                    break;
                case "Waktu Mulai:":
                    txtWaktuMulai = new JTextField();
                    field = txtWaktuMulai;
                    break;
                case "Waktu Selesai:":
                    txtWaktuSelesai = new JTextField();
                    field = txtWaktuSelesai;
                    break;
                default:
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

        simpanBtn.addActionListener(e -> simpan());
        batalBtn.addActionListener(e -> this.dispose());

        loadPelanggan();
        loadKomputer();
    }

    private void loadPelanggan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama FROM Pelanggan";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nama = rs.getString("nama");
                pelangganMap.put(nama, id);
                cmbPelanggan.addItem(nama);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadKomputer() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, spesifikasi FROM Komputer WHERE status = 'Siap Pakai'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String spesifikasi = rs.getString("spesifikasi");
                komputerMap.put(spesifikasi, id);
                cmbKomputer.addItem(spesifikasi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simpan() {
        String tanggalBooking = txtTanggalBooking.getText();
        String waktuMulai = txtWaktuMulai.getText();
        String waktuSelesai = txtWaktuSelesai.getText();
        String status = (String) cmbStatusBooking.getSelectedItem();

        String pelangganNama = (String) cmbPelanggan.getSelectedItem();
        String komputerSpesifikasi = (String) cmbKomputer.getSelectedItem();

        Integer pelangganId = pelangganMap.get(pelangganNama);
        Integer komputerId = komputerMap.get(komputerSpesifikasi);

        if (tanggalBooking.isEmpty() || waktuMulai.isEmpty() || waktuSelesai.isEmpty() || pelangganId == null || komputerId == null) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Booking (status_booking, tanggal_booking, waktu_mulai, waktu_selesai, pelanggan_id, komputer_id) " + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setDate(2, Date.valueOf(tanggalBooking));
            stmt.setTimestamp(3, Timestamp.valueOf(waktuMulai));
            stmt.setTimestamp(4, Timestamp.valueOf(waktuSelesai));
            stmt.setInt(5, pelangganId);
            stmt.setInt(6, komputerId);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Booking berhasil disimpan.");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan booking.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saat simpan: " + ex.getMessage());
        }
    }
}