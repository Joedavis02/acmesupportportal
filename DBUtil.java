import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

    public static Connection getConnection() {
        try {
            // ❌ Hardcoded DB credentials
            String url = "jdbc:mysql://localhost:3306/acme_support";
            String user = "support_user";
            String pass = "supersecret";

            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
