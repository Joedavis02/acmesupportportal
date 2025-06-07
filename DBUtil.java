import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBUtil {

    public static Connection getConnection() {
        try {
            // Secure DB credentials
            String url = "https://supportportal.com/";
            String user = System.getenv("DB_USER");
            String pass = System.getenv("DB_PASS");

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}