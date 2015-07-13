
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Prepared {

    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost:3306/casesevents";
        String user = "kerrynic";
        String password = "thomas51";

        try {

            con = DriverManager.getConnection(url, user, password);
            //pst = con.prepareStatement("SELECT SUM(CASE WHEN Location=1 THEN 1 ELSE 0 END) FROM Cases");
            pst = con.prepareStatement("SELECT LastName, FirstName FROM Cases WHERE Location=1");
            //pst = con.prepareStatement("SELECT Id, Name FROM Authors WHERE Id=1;");
            rs = pst.executeQuery();
            
            while (rs.next()) {
                System.out.print(rs.getString(1));
                System.out.print(": ");
                System.out.println(rs.getString(2));
            }
            
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Prepared.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Prepared.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }
}
