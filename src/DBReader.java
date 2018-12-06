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
           // conn = openConnection();
            sites = new ArrayList<>();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queryNot(String key, String notKey, String type) throws Exception {
        conn = openConnection();

        String sql = "SELECT w.LinkID, w.SiteName, w.URL, s.SourceCode " +
                "FROM Websites w, SourceCodes s " +
                "WHERE s.LinkID = w.LinkID AND (" + type + " LIKE ?) AND (NOT " + type + " LIKE ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, key);
        stmt.setString(2, notKey);
        ResultSet rs = stmt.executeQuery();

        addSites(rs, 0, key);
        closeDB();
    }

    public void query(String key) throws Exception
    {
        conn = openConnection();
        String sql = "SELECT w.LinkID, w.SiteName, w.URL, s.SourceCode " +
                "FROM Websites w, SourceCodes s " +
                "WHERE s.LinkID = w.LinkID AND s.SourceCode LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        //stmt.setString(1, flag);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();

        addSites(rs, 0 , key);
        closeDB();
    }

    public void queryTitle(String key) throws Exception
    {
        conn = openConnection();
        String sql = "SELECT w.LinkID, w.SiteName, w.URL, s.SourceCode " +
                "FROM Websites w, SourceCodes s " +
                "WHERE s.LinkID = w.LinkID AND w.SiteName LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        //stmt.setString(1, flag);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();

        int i = 0;
        addSites(rs, i, key);
        closeDB();
    }

    public void queryHTTP(String key) throws Exception
    {
        conn = openConnection();
        String sql = "SELECT w.LinkID, w.SiteName, w.URL, s.SourceCode " +
                     "FROM Websites w, SourceCodes s " +
                     "WHERE s.LinkID = w.LinkID AND w.URL LIKE ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, key);
        ResultSet rs = stmt.executeQuery();

        int i = 0;
        addSites(rs, i, key);
        closeDB();
    }

    public void addSites(ResultSet rs, int i, String key) throws Exception {
        conn = openConnection();
        while(rs.next())
        {
            Integer lid = rs.getInt("LinkID");
            String sname = rs.getString("SiteName");
            String url = rs.getString("URL");
            String scode = rs.getString("SourceCode");
            int len = scode.length();
            scode = (len <= 600 ? scode.substring(0, len) : scode.substring(0, 600));
            scode = scode.replaceAll("(?i)" + key.substring(1, key.length()-1), "<b>" + key.substring(1, key.length()-1).toUpperCase() + "</b>");

            if(i == 0) {
                System.out.println(scode);
            }
            i++;
            sites.add(new Sites(lid, sname, url, scode));
        }
        closeDB();
    }

    public ArrayList<Sites> getAllResults()
    {
        return sites;
    }

    public void closeDB(){
        try{
            if(conn!=null&&!conn.isClosed()){
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
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

/*
Output Code
/Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64093:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Users/turtle/Desktop/Search 2 - works/out/production/Search:/Users/turtle/Desktop/Search 2 - works/web/WEB-INF/lib/jstl-1.2.jar:/Users/turtle/Desktop/Search 2 - works/web/WEB-INF/lib/mysql-connector-java-8.0.13.jar" DBReader
California Baptist University Apply Now| Visit Campus| Give Admissions Undergraduate Graduate International Online Academics Schools and Colleges College of Architecture, Visual Arts and Design College of Arts and Sciences College of Behavioral and Social Sciences College of <b>ENGINEERING</b> College of Health Science College of Nursing School of Business School of Christian Ministries School of Education School of Music Online and Professional Studies Programs Academic Calendar Academic Catalogs Library Faculty Student Services Academic Advising Admissions Athletics Career Services Commencement Com

Process finished with exit code 0

*/