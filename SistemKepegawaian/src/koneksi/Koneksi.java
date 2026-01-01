package koneksi;

import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    private static Connection conn;

    public static Connection getKoneksi() {
        try {
            if (conn == null) {
                String url = "jdbc:mysql://localhost:3306/kepegawaian";
                String user = "root";
                String pass = "";
                conn = DriverManager.getConnection(url, user, pass);
            }
        } catch (Exception e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return conn;
    }
}
