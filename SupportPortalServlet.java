import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.commons.text.StringEscapeUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class SupportPortalServlet {

    private static final Logger logger = Logger.getLogger(SupportPortalServlet.class.getName());

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            String name = request.getParameter("name");
            name = StringEscapeUtils.escapeHtml4(name);
            response.getWriter().println("<h1>Hello " + name + "</h1>");

            String email = request.getParameter("email");
            if (email != null) {
                response.getWriter().println("<h3>Search Results for Email: " + email + "</h3>");
                // Secure SQL query
                try (Connection conn = DBUtil.getConnection()) {
                    String query = "SELECT * FROM customers WHERE email = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, email);
                    ResultSet rs = pstmt.executeQuery();
                    while (rs.next()) {
                        response.getWriter().println("<p>Customer Name: " + rs.getString("name") + "</p>");
                        response.getWriter().println("<p>Email: " + rs.getString("email") + "</p>");
                    }
                } catch (Exception e) {
                    response.getWriter().println("<p>Error retrieving customer data.</p>");
                }
            }

            String fileParam = request.getParameter("file");
            if (fileParam != null) {
                String filename = Paths.get(fileParam).getFileName().toString();
                File file = new File("/uploads/" + filename);
                try (Scanner scanner = new Scanner(file)) {
                    response.getWriter().println("<h3>Attachment Content:</h3><pre>");
                    while (scanner.hasNextLine()) {
                        response.getWriter().println(scanner.nextLine());
                    }
                    response.getWriter().println("</pre>");
                } catch (Exception e) {
                    response.getWriter().println("<p>Attachment not found.</p>");
                }
            }
        } catch (Exception e) {
            logger.severe("Error processing request: " + e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            logger.info("User login attempted for user: " + username);
            response.setContentType("text/html");
            response.getWriter().println("<p>Login submitted. Check logs for debug info.</p>");
        } catch (Exception e) {
            logger.severe("Error processing POST request: " + e.getMessage());
        }
    }
}