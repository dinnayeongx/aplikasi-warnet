package gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class PelangganPage extends JFrame {
    private JTextField txtID, txtNama, txtEmail, txtNoHP, txtTotalKunjungan;

    public PelangganPage() {
        setTitle("Manajemen Pelanggan");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtID = createField(formPanel, "ID:");
        txtNama = createField(formPanel, "Nama:");
        txtEmail = createField(formPanel, "Email:");
        txtNoHP = createField(formPanel, "No HP:");
        txtTotalKunjungan = createField(formPanel, "Total Kunjungan:");

        JPanel buttonPanel = new JPanel();
        JButton tambahBtn = new JButton("Tambah");
        JButton editBtn = new JButton("Edit");
        JButton hapusBtn = new JButton("Hapus");
        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(hapusBtn);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        tambahBtn.addActionListener(e -> tambah());
        editBtn.addActionListener(e -> edit());
        hapusBtn.addActionListener(e -> hapus());

        setVisible(true);
    }

    private JTextField createField(JPanel parent, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(120, 25));
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        parent.add(panel);
        parent.add(Box.createVerticalStrut(5));
        return field;
    }

    private void tambah() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Pelanggan (nama, email, nomorHP, total_kunjungan) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setString(2, txtEmail.getText());
            stmt.setString(3, txtNoHP.getText());
            stmt.setInt(4, Integer.parseInt(txtTotalKunjungan.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pelanggan berhasil ditambahkan!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan pelanggan!");
        }
    }

    private void edit() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Pelanggan SET nama=?, email=?, nomorHP=?, total_kunjungan=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtNama.getText());
            stmt.setString(2, txtEmail.getText());
            stmt.setString(3, txtNoHP.getText());
            stmt.setInt(4, Integer.parseInt(txtTotalKunjungan.getText()));
            stmt.setInt(5, Integer.parseInt(txtID.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pelanggan berhasil diupdate!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengupdate pelanggan!");
        }
    }

    private void hapus() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Pelanggan WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(txtID.getText()));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Pelanggan berhasil dihapus!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus pelanggan!");
        }
    }
}