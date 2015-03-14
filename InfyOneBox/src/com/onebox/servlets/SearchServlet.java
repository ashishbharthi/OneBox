package com.onebox.servlets;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.queryparser.classic.ParseException;

import com.google.gson.Gson;
import com.onebox.entity.OneInfyObject;
import com.onebox.entity.ResultsObject;
import com.onebox.entity.ShowObject;
import com.onebox.services.LuceneSearchService;
import com.onebox.services.SpellCheckService;

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
		
		Boolean voice = Boolean.parseBoolean(request.getParameter("voice"));
		String searchText = request.getParameter("srchTxt");
		LuceneSearchService lss = LuceneSearchService.getInstance();
		SpellCheckService scs = SpellCheckService.getInstance();
		
		String correctedText = scs.correctSpellings(searchText);
		
		System.out.println("isVoice:" + request.getParameter("voice"));
		List<OneInfyObject> results = null;
		try {
			
			ResultsObject ro = lss.getSearchResults(correctedText);
			
			ro.setOriginalSearchText(searchText);
			
			if(searchText.toLowerCase().trim().equals(correctedText)){
				ro.setSpellCorrect(false);
			}
			else{
				ro.setCorrectedSearchText(correctedText);
				ro.setSpellCorrect(true);
			}
			Gson g = new Gson();
			response.setContentType("application/json");
			Writer w = response.getWriter();
			w.write(g.toJson(ro));
			System.out.println(g.toJson(ro));
			response.getWriter().flush();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
