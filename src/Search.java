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
        ArrayList<Sites> sites;
		try {
			String filterKey =  key.replace("title:", "").replace("http:", "").replace("AND", "").replace("OR", "").trim();
			if(key.contains("NOT"))
			{
			    String thisFilterKey = filterKey.substring(0, filterKey.indexOf("NOT")).trim();
			    String notFilterKey = filterKey.substring(filterKey.indexOf("NOT") + 3).trim();
			    System.out.println("|" + thisFilterKey + "|" + notFilterKey + "|");

				if(key.contains("title:"))
				    reader.queryNot("%" + thisFilterKey + "%", "%" + notFilterKey + "%", "w.SiteName");
				else if(key.contains("http:"))
				    reader.queryNot("%" + thisFilterKey + "%", "%" + notFilterKey + "%", "w.URL");
				else
				    reader.queryNot("%" + thisFilterKey + "%", "%" + notFilterKey + "%", "s.SourceCode");
			    sites = reader.getAllResults();
			}
			else {
                if (key.contains("title:"))
                    reader.queryTitle("%" + filterKey.replace(" ", "%") + "%");
                else if (key.contains("http:"))
                    reader.queryHTTP("%" + filterKey.replace(" ", "%") + "%");
                else
                    reader.query("%" + filterKey.replace(" ", "%") + "%");

                sites = reader.getAllResults();

                // Ranking aspect goes here.
                KeyCounts counts[] = new KeyCounts[sites.size()];
                boolean notFlag = false;
                for (int i = 0; i < sites.size(); i++) {
                    Sites s = sites.get(i);

                    // if title flag is true do s.getsitename() else s.getsourcecode()
                    String text;
                    if (key.contains("title:")) {
                        text = s.getsitename();
                        key = key.replace("title:", "");
                    } else if (key.contains("http:")) {
                        text = s.geturl();
                        key = key.replace("http:", "");
                    } else
                        text = s.getsourcecode();

                    boolean andFlag = false, orFlag = false;
                    if (key.contains("AND")) {
                        andFlag = true;
                        key = key.replace("AND", "");
                    } else if (key.contains("OR")) {
                        orFlag = true;
                        key = key.replace("OR", "");
                    } else if (key.contains("NOT")) {
                        notFlag = true;
                        key = key.replace("NOT", "");
                    }

                    NGram ranker = new NGram(text);
                    //line below is the line to change depending on what type of match you are expecting to return.
                    int matches = 0;
                    if (andFlag)
                        matches = ranker.countExactNGram(key, 1);
                    else if (orFlag)
                        matches = ranker.countAnyNGram(key, 1, 0.9);
                    else if (notFlag)
                        matches = ranker.countExactNGram(key, 1);
                    else
                        matches = ranker.countNearMatch(key, 0.8);
                    System.out.println("MATCHES: " + matches);
                    counts[i] = new KeyCounts(s, matches);
                }

                Arrays.sort(counts, new KeyCountComparer());
                sites.clear();
                for (int i = 0; i < counts.length; i++) {
                    if (!notFlag || counts[i].getCount() == 0)
                        sites.add(counts[i].getSite());
                }
            }
			request.setAttribute("links_list", sites);
		}
		catch (Exception e) {

		}
		RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/view/Search.jsp");
		dispatcher.forward(request, response);
	}
}
