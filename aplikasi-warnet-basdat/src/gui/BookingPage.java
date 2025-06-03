package gui;

import db.DBConnection;

import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.util.Date;

public class BookingPage extends JFrame {
    private JTextField idField;
    private JFormattedTextField tanggalField, waktuMulaiField, waktuSelesaiField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> pelangganComboBox, komputerComboBox;

    private HashMap<String, Integer> pelangganMap = new HashMap<>();
    private HashMap<String, Integer> komputerMap = new HashMap<>();

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
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                tanggalField = new JFormattedTextField(new DateFormatter(format));
                tanggalField.setValue(new Date());
                field = tanggalField;
            } else if (label.equals("Waktu Mulai:")) {
                waktuMulaiField = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("HH:mm")));
                waktuMulaiField.setText("HH:mm");
                waktuMulaiField.setForeground(Color.GRAY);
                field = waktuMulaiField;
            } else if (label.equals("Waktu Selesai:")) {
                waktuSelesaiField = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("HH:mm")));
                waktuSelesaiField.setText("HH:mm");
                waktuSelesaiField.setForeground(Color.GRAY);
                field = waktuSelesaiField;
            } else if (label.equals("Status Booking:")) {
                statusComboBox = new JComboBox<>(new String[]{"Pending", "Confirmed", "Cancelled"});
                field = statusComboBox;
            } else if (label.equals("Pelanggan:")) {
                pelangganComboBox = new JComboBox<>();
                field = pelangganComboBox;
            } else if (label.equals("Komputer:")) {
                komputerComboBox = new JComboBox<>();
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

        simpanBtn.addActionListener(e -> simpanBooking());
        batalBtn.addActionListener(e -> this.dispose());

        loadPelanggan();
        loadKomputer();
    }

    private void loadPelanggan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama FROM Pelanggan";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String nama = rs.getString("nama");
                String label = id + " - " + nama;
                pelangganComboBox.addItem(label);
                pelangganMap.put(label, id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pelanggan: " + e.getMessage());
        }
    }

    private void loadKomputer() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, spesifikasi FROM Komputer WHERE status = 'Siap Pakai'";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("ID");
                String spec = rs.getString("spesifikasi");
                String label = id + " - " + spec;
                komputerComboBox.addItem(label);
                komputerMap.put(label, id);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat komputer: " + e.getMessage());
        }
    }

    private void simpanBooking() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Booking (ID, status_booking, tanggal_booking, waktu_mulai, waktu_selesai, pelanggan_id, komputer_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            String bookingId = idField.getText().trim();
            String tanggal = tanggalField.getText();
            String mulai = waktuMulaiField.getText();
            String selesai = waktuSelesaiField.getText();

            // Format menjadi Timestamp
            SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Timestamp waktuMulai = new Timestamp(fullFormat.parse(tanggal + " " + mulai).getTime());
            Timestamp waktuSelesai = new Timestamp(fullFormat.parse(tanggal + " " + selesai).getTime());

            String selectedPelanggan = (String) pelangganComboBox.getSelectedItem();
            String selectedKomputer = (String) komputerComboBox.getSelectedItem();

            if (selectedPelanggan == null || selectedKomputer == null) {
                JOptionPane.showMessageDialog(this, "Pelanggan atau Komputer belum dipilih!");
                return;
            }

            stmt.setString(1, bookingId);
            stmt.setString(2, (String) statusComboBox.getSelectedItem());
            stmt.setDate(3, java.sql.Date.valueOf(tanggal));
            stmt.setTimestamp(4, waktuMulai);
            stmt.setTimestamp(5, waktuSelesai);
            stmt.setInt(6, pelangganMap.get(selectedPelanggan));
            stmt.setInt(7, komputerMap.get(selectedKomputer));

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Booking berhasil disimpan!");

            new TransaksiPage(bookingId);
            this.dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan booking:\n" + e.getMessage());
        }
    }
}
