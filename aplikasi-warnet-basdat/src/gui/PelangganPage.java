package gui;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class PelangganPage extends JFrame {

    private JTextField idField, namaField, emailField, noHpField, totalKunjunganField;
    private JTable pelangganTable;
    private DefaultTableModel tableModel;

    public PelangganPage() {
        setTitle("Manajemen Pelanggan");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        idField = createFieldWithLabel(formPanel, "ID:");
        idField.setEditable(false);

        namaField = createFieldWithLabel(formPanel, "Nama:");
        emailField = createFieldWithLabel(formPanel, "Email:");
        noHpField = createFieldWithLabel(formPanel, "No HP:");
        totalKunjunganField = createFieldWithLabel(formPanel, "Total Kunjungan:");
        totalKunjunganField.setEditable(false);

        String[] columns = {"ID", "Nama", "Email", "No HP", "Total Kunjungan"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pelangganTable = new JTable(tableModel);
        pelangganTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        pelangganTable.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            int selectedRow = pelangganTable.getSelectedRow();
            if (selectedRow >= 0) {
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                namaField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                emailField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                noHpField.setText(tableModel.getValueAt(selectedRow, 3).toString());
                totalKunjunganField.setText(tableModel.getValueAt(selectedRow, 4).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(pelangganTable);
        scrollPane.setPreferredSize(new Dimension(650, 150));

        JPanel buttonPanel = new JPanel();
        JButton tambahBtn = new JButton("Tambah");
        JButton editBtn = new JButton("Edit");
        JButton hapusBtn = new JButton("Hapus");

        buttonPanel.add(tambahBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(hapusBtn);

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTableFromDB();

        tambahBtn.addActionListener(e -> {
            String nama = namaField.getText().trim();
            String email = emailField.getText().trim();
            String noHp = noHpField.getText().trim();

            if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lengkapi data pelanggan!");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO Pelanggan (nama, email, nomorHP, total_kunjungan) VALUES (?, ?, ?, 0)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, email);
                ps.setString(3, noHp);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Data pelanggan berhasil ditambahkan.");
                clearForm();
                refreshTableFromDB();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menambah pelanggan: " + ex.getMessage());
            }
        });

        editBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih pelanggan yang ingin diedit!");
                return;
            }
            String nama = namaField.getText().trim();
            String email = emailField.getText().trim();
            String noHp = noHpField.getText().trim();

            if (nama.isEmpty() || email.isEmpty() || noHp.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Lengkapi data pelanggan!");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "UPDATE Pelanggan SET nama = ?, email = ?, nomorHP = ? WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nama);
                ps.setString(2, email);
                ps.setString(3, noHp);
                ps.setString(4, id);
                int updated = ps.executeUpdate();
                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Data pelanggan berhasil diupdate.");
                    clearForm();
                    refreshTableFromDB();
                } else {
                    JOptionPane.showMessageDialog(this, "Data pelanggan tidak ditemukan.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal mengupdate pelanggan: " + ex.getMessage());
            }
        });

        hapusBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pilih pelanggan yang ingin dihapus!");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus pelanggan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "DELETE FROM Pelanggan WHERE ID = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, id);
                int deleted = ps.executeUpdate();
                if (deleted > 0) {
                    JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus.");
                    clearForm();
                    refreshTableFromDB();
                } else {
                    JOptionPane.showMessageDialog(this, "Data pelanggan tidak ditemukan.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal menghapus pelanggan: " + ex.getMessage());
            }
        });
    }

    private JTextField createFieldWithLabel(JPanel panel, String label) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(120, 25));
        inputPanel.add(jLabel, BorderLayout.WEST);
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        inputPanel.add(field, BorderLayout.CENTER);
        panel.add(inputPanel);
        panel.add(Box.createVerticalStrut(5));
        return field;
    }

    private void refreshTableFromDB() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT ID, nama, email, nomorHP, total_kunjungan FROM Pelanggan ORDER BY ID";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            tableModel.setRowCount(0);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("nomorHP"),
                    rs.getInt("total_kunjungan")
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengambil data pelanggan: " + ex.getMessage());
        }
    }

    private void clearForm() {
        idField.setText("");
        namaField.setText("");
        emailField.setText("");
        noHpField.setText("");
        totalKunjunganField.setText("");
        pelangganTable.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PelangganPage().setVisible(true));
    }
}
