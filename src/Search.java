import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/Search")
public class Search extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    public Search()
    {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/view/Search.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Are you clearing, if not continue.
        String key = request.getParameter("searchKey");
        System.out.println("KEY: " + key);
        DBReader reader = new DBReader();
        try {
            reader.query("%" + key + "%");
            ArrayList<Sites> sites = reader.getAllResults();

            // Boolean stuff goes here.

            // Ranking aspect goes here.
            KeyCounts counts[] = new KeyCounts[sites.size()];
            for(int i = 0; i < sites.size(); i++)
            {
                Sites s = sites.get(i);
               // s.getsitename();
                // if title flag is true do s.getsitename() else s.getsourcecode()
                String text = s.getsourcecode();
                NGram ranker = new NGram(text);
                //line below is the line to chagne depedning on what type of match you are expecting to return.
                int matches = ranker.countNearMatch(key, 0.50);
                System.out.println("MATCHES: " + matches);
                counts[i] = new KeyCounts(s, matches);
            }

            Arrays.sort(counts, new KeyCountComparer());
            sites.clear();
            for(int i = 0; i < counts.length; i++) {
                sites.add(counts[i].getSite());
                System.out.println(counts[i].getCount());
            }

            request.setAttribute("links_list", sites);
        }
        catch (Exception e) {

        }
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/view/Search.jsp");
        dispatcher.forward(request, response);
    }
}
