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
		
		Boolean voice = Boolean.parseBoolean(request.getParameter("voice"));
		String searchText = request.getParameter("srchTxt");
		LuceneSearchService lss = LuceneSearchService.getInstance();
		
		System.out.println("isVoice:" + request.getParameter("voice"));
		try {
			List<OneInfyObject> results = lss.getSearchResults(searchText);
			
			ResultsObject ro = new ResultsObject(results, "Showing results for " + searchText, false);
			
			Gson g = new Gson();
			
			
			response.setContentType("application/json");
			
			Writer w = response.getWriter();
			
			w.write(g.toJson(ro));
			
			response.getWriter().flush();
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private boolean isShow(String str){
		String[] arr = {"show", "view", "open"};
		
		for (int i = 0; i < arr.length; i++) {
			if(str.toLowerCase().indexOf(arr[i]) > 0){
				return true;
			}
		}
		
		return false;
	}
	private boolean isApply(String str){
		String[] arr = {"apply"};
		
		for (int i = 0; i < arr.length; i++) {
			if(str.toLowerCase().indexOf(arr[i]) > 0){
				return true;
			}
		}
		
		return false;
	}
	private boolean isApprove(String str){
		String[] arr = {"approve"};
		
		for (int i = 0; i < arr.length; i++) {
			if(str.toLowerCase().indexOf(arr[i]) > 0){
				return true;
			}
		}
		
		return false;
	}
}
