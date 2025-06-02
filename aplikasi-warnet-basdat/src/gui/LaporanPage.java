package gui;

import java.awt.*;
import javax.swing.*;

public class LaporanPage extends JFrame {
    public LaporanPage() {
        setTitle("Laporan");
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.add(new JLabel("Periode:"));
        JComboBox<String> periodeCombo = new JComboBox<>(new String[]{"Harian", "Mingguan", "Bulanan"});
        filterPanel.add(periodeCombo);
        JButton exportPDF = new JButton("Export PDF");
        JButton exportExcel = new JButton("Export Excel");
        filterPanel.add(exportPDF);
        filterPanel.add(exportExcel);

        String[] columnNames = {"Tanggal", "Pendapatan", "Komputer Digunakan", "Layanan Terlaris"};
        Object[][] data = {};
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ActionListeners
        exportPDF.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export PDF clicked untuk periode: " + periodeCombo.getSelectedItem()));
        exportExcel.addActionListener(e -> JOptionPane.showMessageDialog(this, "Export Excel clicked untuk periode: " + periodeCombo.getSelectedItem()));

    }
}