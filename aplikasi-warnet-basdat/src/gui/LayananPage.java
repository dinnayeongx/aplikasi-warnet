package gui;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;

public class LayananPage extends JFrame {
    private JTextField txtId, txtNama, txtHarga, txtDurasi;

    public LayananPage() {
        setTitle("Manajemen Layanan");
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Inisialisasi field
        txtId = new JTextField();
        txtNama = new JTextField();
        txtHarga = new JTextField();
        txtDurasi = new JTextField();

        String[] labels = {"ID:", "Nama Layanan:", "Harga:", "Durasi:"};
        JTextField[] fields = {txtId, txtNama, txtHarga, txtDurasi};

        for (int i = 0; i < labels.length; i++) {
            JPanel inputPanel = new JPanel(new BorderLayout());
            JLabel jLabel = new JLabel(labels[i]);
            jLabel.setPreferredSize(new Dimension(120, 25));
            inputPanel.add(jLabel, BorderLayout.WEST);
            fields[i].setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            inputPanel.add(fields[i], BorderLayout.CENTER);
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

        tambahBtn.addActionListener(e -> tambahLayanan());
        editBtn.addActionListener(e -> editLayanan());
        hapusBtn.addActionListener(e -> hapusLayanan());
    }

    private void tambahLayanan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Layanan (id, nama, harga, durasi) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            stmt.setString(2, txtNama.getText());
            stmt.setDouble(3, Double.parseDouble(txtHarga.getText()));
            stmt.setInt(4, Integer.parseInt(txtDurasi.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Layanan berhasil ditambahkan!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan layanan: " + ex.getMessage());
        }
    }

    private void editLayanan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Layanan SET nama = ?, harga = ?, durasi = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setDouble(2, Double.parseDouble(txtHarga.getText()));
            stmt.setInt(3, Integer.parseInt(txtDurasi.getText()));
            stmt.setInt(4, Integer.parseInt(txtId.getText()));
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Layanan berhasil diupdate!");
            } else {
                JOptionPane.showMessageDialog(this, "ID layanan tidak ditemukan.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengedit layanan: " + ex.getMessage());
        }
    }

    private void hapusLayanan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Layanan WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtId.getText()));
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Layanan berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "ID layanan tidak ditemukan.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus layanan: " + ex.getMessage());
        }
    }
} 