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
            // Ranking aspect goes here.
            request.setAttribute("links_list", sites);
        }
        catch (Exception e) {

        }
        //put database in place of system.out.println
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/view/Search.jsp");
        dispatcher.forward(request, response);
    }
}
