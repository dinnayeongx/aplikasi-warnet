package gui;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class KomputerPage extends JFrame {
    private JTextField txtID, txtSpesifikasi, txtLokasi;
    private JComboBox<String> cmbStatus;
    private JButton btnTambah, btnEdit, btnHapus;

    public KomputerPage() {
        setTitle("Manajemen Komputer");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblID = new JLabel("ID:");
        lblID.setBounds(30, 30, 80, 25);
        add(lblID);

        txtID = new JTextField();
        txtID.setBounds(120, 30, 200, 25);
        txtID.setEditable(false);
        add(txtID);

        JLabel lblSpesifikasi = new JLabel("Spesifikasi:");
        lblSpesifikasi.setBounds(30, 70, 80, 25);
        add(lblSpesifikasi);

        txtSpesifikasi = new JTextField();
        txtSpesifikasi.setBounds(120, 70, 200, 25);
        add(txtSpesifikasi);

        JLabel lblLokasi = new JLabel("Lokasi:");
        lblLokasi.setBounds(30, 110, 80, 25);
        add(lblLokasi);

        txtLokasi = new JTextField();
        txtLokasi.setBounds(120, 110, 200, 25);
        add(txtLokasi);

        JLabel lblStatus = new JLabel("Status:");
        lblStatus.setBounds(30, 150, 80, 25);
        add(lblStatus);

        cmbStatus = new JComboBox<>(new String[]{"Siap Pakai", "Sedang Dipakai"});
        cmbStatus.setBounds(120, 150, 200, 25);
        add(cmbStatus);

        btnTambah = new JButton("Tambah");
        btnTambah.setBounds(30, 190, 90, 25);
        add(btnTambah);

        btnEdit = new JButton("Edit");
        btnEdit.setBounds(130, 190, 90, 25);
        add(btnEdit);

        btnHapus = new JButton("Hapus");
        btnHapus.setBounds(230, 190, 90, 25);
        add(btnHapus);

        btnTambah.addActionListener(e -> tambah());
        btnEdit.addActionListener(e -> edit());
        btnHapus.addActionListener(e -> hapus());

        setVisible(true);
    }

    private void tambah() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Komputer (Spesifikasi, Lokasi, Status) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, txtSpesifikasi.getText());
            stmt.setString(2, txtLokasi.getText());
            stmt.setString(3, (String) cmbStatus.getSelectedItem());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    txtID.setText(String.valueOf(newId));
                }
                JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal tambah data!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal tambah data!");
        }
    }

    private void edit() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Komputer SET Spesifikasi=?, Lokasi=?, Status=? WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtSpesifikasi.getText());
            stmt.setString(2, txtLokasi.getText());
            stmt.setString(3, (String) cmbStatus.getSelectedItem());
            stmt.setString(4, txtID.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal update data!");
        }
    }

    private void hapus() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Komputer WHERE ID=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtID.getText());
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal hapus data!");
        }
    }
}