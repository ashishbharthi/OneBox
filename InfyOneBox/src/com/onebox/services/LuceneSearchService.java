package com.onebox.services;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import com.onebox.entity.OneInfyObject;
import com.onebox.entity.ResultsObject;
import com.onebox.entity.ShowObject;
import com.onebox.entity.WorkflowObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class LuceneSearchService {

	private static Directory index = null;
	private static StandardAnalyzer analyzer = new StandardAnalyzer();
	public static LuceneSearchService lss;
	private LuceneSearchService() throws IOException {
		// 1. create the index
		index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, "harmony", "Harmony");
		addDoc(w, "view passport Details", "Passport");
		addDoc(w, "passport", "Passport");
		addDoc(w, "Global Leave", "Leave System");
		addDoc(w, "GLS", "Leave System");
		addDoc(w, "Global Immigration", "Global Immigration System");
		addDoc(w, "Apply Leave from tomorrow", "W100");
		addDoc(w, "Apply Leave", "W100");
		addDoc(w, "Apply Leave from tomorrow for 1 day", "W100");
		
		addDoc(w, "Lunch Menu Cafeteria", "Cafeteria Menu");

		w.close();
	}
	
	public static LuceneSearchService getInstance() throws IOException{
		if(lss == null){
			lss = new LuceneSearchService();
			return lss;
		}
		else{
			return lss;
		}
	}
	
	
	
	public static ResultsObject getSearchResults(String s) throws ParseException, IOException{
		
		List<OneInfyObject> result = new ArrayList<OneInfyObject>();
		Query q = new QueryParser("title", analyzer).parse(s);

		// 3. search
		int hitsPerPage = 10;
		IndexReader reader = DirectoryReader.open(index);
		IndexSearcher searcher = new IndexSearcher(reader);

		ScoreDoc sd = new ScoreDoc(0, 100);
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				hitsPerPage, sd);
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		// 4. display results
		//System.out.println("Found " + hits.length + " hits.");
		
		ResultsObject ro = null;
		
		if(isApply(s)){
			if(hits.length > 1){
				if(s.equalsIgnoreCase("Apply Leave")){
					
					result.add(new WorkflowObject(searcher.doc(hits[0].doc).get("isbn"), "apply", "From Date", "date", "incomplete", ""));
					ro = new ResultsObject(result, "Please select from date", false);
					
				}else if(s.equalsIgnoreCase("Apply Leave from tomorrow")){
					
					result.add(new WorkflowObject(searcher.doc(hits[0].doc).get("isbn"), "apply", "To Date", "date", "incomplete", ""));
					ro = new ResultsObject(result, "Please select start date", false);
					
				}else if(s.toLowerCase().indexOf("apply leave from tomorrow to") >= 0){
					
					Calendar c = Calendar.getInstance();
					c.setTime(new Date()); // Now use today date.
					c.add(Calendar.DATE, 1); // Adding 5 days
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					
					result.add(new WorkflowObject(searcher.doc(hits[0].doc).get("isbn"), "apply", "To Date", "date", "confirm", s.replaceFirst("tomorrow", sdf.format(c.getTime())) + "to my manager."));
					ro = new ResultsObject(result, "Please confirm.", false);
					
				}else if(s.toLowerCase().indexOf("to my manager") >= 0){
					
					Calendar c = Calendar.getInstance();
					c.setTime(new Date()); // Now use today date.
					c.add(Calendar.DATE, 1); // Adding 5 days
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
					
					result.add(new WorkflowObject(searcher.doc(hits[0].doc).get("isbn"), "apply", "To Date", "date", "confirm", s.replaceFirst("tomorrow", sdf.format(c.getTime()))));
					ro = new ResultsObject(result, "Leave successfully applied. Thank you for using our application.", false);
					
				}
			}
		}else{
			
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				if(hits[i].score > 0.5f && !d.get("isbn").equals("W100"))
					result.add(new ShowObject("show", d.get("isbn")));
			}
			
			String msg = "Showing results for " + s;
			if (result.size() == 1) {
				ShowObject so = (ShowObject)result.get(0);
				msg = "Opening "+ so.getName() +" application";
			}
			
			ro = new ResultsObject(result, msg, false);
			
		}
		
		
		

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
		return ro;
	}
	
	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
	private static boolean isShow(String str){
		String[] arr = {"show", "view", "open"};
		
		for (int i = 0; i < arr.length; i++) {
			if(str.toLowerCase().indexOf(arr[i]) > 0){
				return true;
			}
		}
		
		return false;
	}
	private static boolean isApply(String str){
		String[] arr = {"apply"};
		
		for (int i = 0; i < arr.length; i++) {
			if(str.toLowerCase().indexOf(arr[i]) >= 0){
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
//	public static void main(String[] args) throws IOException, ParseException {
//		// 0. Specify the analyzer for tokenizing text.
//		// The same analyzer should be used for indexing and searching
//		StandardAnalyzer analyzer = new StandardAnalyzer();
//
//		// 1. create the index
//		Directory index = new RAMDirectory();
//
//		IndexWriterConfig config = new IndexWriterConfig(analyzer);
//
//		IndexWriter w = new IndexWriter(index, config);
//		addDoc(w, "harmony", "Harmony");
//		addDoc(w, "view passport Details", "Passport");
//		addDoc(w, "show me my passport", "Passport");
//		addDoc(w, "Global Leave", "Leave System");
//		addDoc(w, "GLS", "Leave System");
//		addDoc(w, "Global Immigration", "Global Immigration System");
//		addDoc(w, "Apply Leave from tomorrow", "ApplyLeaveWorkflow");
//		addDoc(w, "Apply Leave", "ApplyLeaveWorkflow");
//		addDoc(w, "Apply Leave from tomorrow for 1 day", "ApplyLeaveWorkflow");
//		
//		addDoc(w, "Lunch Menu Cafeteria", "Cafeteria Menu");
//
//		w.close();
//
//		// 2. query
//		String querystr = args.length > 0 ? args[0] : "Cafeteria";
//
//		// the "title" arg specifies the default field to use
//		// when no field is explicitly specified in the query.
//		Query q = new QueryParser("title", analyzer).parse(querystr);
//
//		// 3. search
//		int hitsPerPage = 10;
//		IndexReader reader = DirectoryReader.open(index);
//		IndexSearcher searcher = new IndexSearcher(reader);
//
//		ScoreDoc sd = new ScoreDoc(0, 100);
//		TopScoreDocCollector collector = TopScoreDocCollector.create(
//				hitsPerPage, sd);
//		searcher.search(q, collector);
//		ScoreDoc[] hits = collector.topDocs().scoreDocs;
//
//		// 4. display results
//		System.out.println("Found " + hits.length + " hits.");
//		for (int i = 0; i < hits.length; ++i) {
//			int docId = hits[i].doc;
//			
//			Document d = searcher.doc(docId);
//			System.out.println((i + 1) + ". " + d.get("isbn") + "\t"
//					+ d.get("title") + " -->" + hits[i].score);
//		}
//
//		// reader can only be closed when there
//		// is no need to access the documents any more.
//		reader.close();
//	}
}