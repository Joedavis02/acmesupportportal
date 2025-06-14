import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class SupportPortalServlet extends HttpServlet {

    // ❌ Hardcoded secret API token (e.g., for third-party services)
    private static final String SUPPORT_BOT_API_KEY = "sk_live_support_789xyz";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<h1>Acme Customer Support Portal</h1>");

        // ❌ XSS Vulnerability: Displaying the user-supplied 'agent' name
        String agent = request.getParameter("agent");
        if (agent != null) {
            out.println("<p>Logged in as: <strong>" + agent + "</strong></p>");
        }

        // ❌ SQL Injection Vulnerability: Search customer by email
        String email = request.getParameter("email");
        if (email != null) {
            out.println("<h3>Search Results for Email: " + email + "</h3>");
            Connection conn = DBUtil.getConnection();
            try {
                String query = "SELECT * FROM customers WHERE email = '" + email + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    out.println("<p>Customer Name: " + rs.getString("name") + "</p>");
                    out.println("<p>Email: " + rs.getString("email") + "</p>");
                }
            } catch (Exception e) {
                out.println("<p>Error retrieving customer data.</p>");
            }
        }

        // ❌ Directory Traversal Vulnerability: File viewer
        String fileParam = request.getParameter("attachment");
        if (fileParam != null) {
            File file = new File("/var/acme/uploads/" + fileParam);
            try {
                Scanner scanner = new Scanner(file);
                out.println("<h3>Attachment Content:</h3><pre>");
                while (scanner.hasNextLine()) {
                    out.println(scanner.nextLine());
                }
                out.println("</pre>");
                scanner.close();
            } catch (Exception e) {
                out.println("<p>Attachment not found.</p>");
            }
        }

        out.println("<hr>");
        out.println("<form method='POST'><h3>Agent Login</h3>");
        out.println("Username: <input name='username'><br>");
        out.println("Password: <input type='password' name='password'><br>");
        out.println("<button type='submit'>Login</button></form>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // ❌ Logs sensitive user credentials
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("Agent login attempt: Username = " + username + ", Password = " + password);

        response.setContentType("text/html");
        response.getWriter().println("<p>Login submitted. Check logs for debug info.</p>");
    }
}
