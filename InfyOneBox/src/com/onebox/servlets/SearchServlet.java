package com.onebox.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import com.onebox.services.LuceneSearchService;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getWriter().write("Hi from Servlet!");
		super.doGet(req, resp);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		LuceneSearchService lss = LuceneSearchService.getInstance();
		
		try {
			List<String> results = lss.getSearchResults(request.getParameter("srchTxt"));
			response.setContentType("application/json");
			response.getWriter().println("[");
			for (Iterator iterator = results.iterator(); iterator.hasNext();) {
				String string = (String) iterator.next();
				response.getWriter().println("{");
				response.getWriter().println("type:show,");
				response.getWriter().println("url:\"" + string + "\"");
				response.getWriter().println("}");
			}
			response.getWriter().println("]");
			response.getWriter().flush();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
