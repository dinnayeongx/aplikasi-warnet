package gui;
import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame {
    public Dashboard() {
         setTitle("Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel summaryPanel = new JPanel(new GridLayout(1, 3));
        summaryPanel.add(new JLabel("Total Pelanggan: -"));
        summaryPanel.add(new JLabel("Komputer Aktif: -"));
        summaryPanel.add(new JLabel("Transaksi Hari Ini: -"));
        add(summaryPanel, BorderLayout.NORTH);

        JPanel navPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        JButton pelangganBtn = new JButton("Pelanggan");
        JButton bookingBtn = new JButton("Booking");
        JButton komputerBtn = new JButton("Komputer");
        JButton layananBtn = new JButton("Layanan");
        JButton laporanBtn = new JButton("Laporan");

        navPanel.add(pelangganBtn);
        navPanel.add(bookingBtn);
        navPanel.add(komputerBtn);
        navPanel.add(layananBtn);
        navPanel.add(laporanBtn);
        add(navPanel, BorderLayout.CENTER);
        pelangganBtn.addActionListener(e -> {
            new PelangganPage().setVisible(true);
        });

        bookingBtn.addActionListener(e -> {
            new BookingPage().setVisible(true);
        });

        komputerBtn.addActionListener(e -> {
            new KomputerPage().setVisible(true);
        });

        layananBtn.addActionListener(e -> {
            new LayananPage().setVisible(true);
        });

        laporanBtn.addActionListener(e -> {
            new LaporanPage().setVisible(true);
        });
    }
}