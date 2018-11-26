import java.sql.*;
import java.util.ArrayList;

public class DBReader
{
    // MySQL: "jdbc:mysql://hostname:port/databaseName", "username", "password"
    private final String myDrive = "com.mysql.cj.jdbc.Driver";
    private final String dbUserName = "root";
    private final String dbPassword = "";
    private final String ConnectionString = "jdbc:mysql://localhost/SearchEngineDB";
    private ArrayList<Sites> sites;

    public Connection openConnection() throws Exception
    {
        Class.forName(myDrive);
        return DriverManager.getConnection(ConnectionString, dbUserName, dbPassword);
    }

    private Connection conn;

    public DBReader()
    {
        try {
            conn = openConnection();
            sites = new ArrayList<>();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void query(String key) throws SQLException
    {
        String sql = "SELECT w.LinkID, w.SiteName, w.URL, s.SourceCode " +
                     "FROM Websites w, SourceCodes s " +
                     "WHERE s.LinkID = w.LinkID AND s.SourceCode LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();

        int i = 0;
        while(rs.next())
        {
            Integer lid = rs.getInt("LinkID");
            String sname = rs.getString("SiteName");
            String url = rs.getString("URL");
            String scode = rs.getString("SourceCode");
            int len = scode.length();
            scode = (len <= 600 ? scode.substring(0, len) : scode.substring(0, 600));
            scode = scode.replaceAll("(?i)" + key.substring(1, key.length()-1), "<b>" + key.substring(1, key.length()-1).toUpperCase() + "</b>");

            if(i == 0)
                System.out.println(scode);
            i++;
            sites.add(new Sites(lid, sname, url, scode));
        }
    }

    public ArrayList<Sites> getAllResults()
    {
        return sites;
    }

    public static void main(String args[])
    {
        try {
            (new DBReader()).query("%engineering%");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
