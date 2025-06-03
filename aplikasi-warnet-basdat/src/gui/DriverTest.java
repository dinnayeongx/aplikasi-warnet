package gui;
public class DriverTest {
    public static void main(String[] args) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver ditemukan!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver TIDAK ditemukan!");
            e.printStackTrace();
        }
    }
}