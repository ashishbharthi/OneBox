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

import java.io.IOException;
import java.util.ArrayList;
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
		addDoc(w, "harmony", "HarmonyURL");
		addDoc(w, "view passport Details", "Passport URL");
		addDoc(w, "show me my passport", "Passport URL");
		addDoc(w, "Global Leave", "Leave System URL");
		addDoc(w, "GLS", "Leave System URL");
		addDoc(w, "Global Immigration", "GI system Url");
		addDoc(w, "Apply Leave", "ApplyLeaveWorkflow");
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
	
	public static List<String> getSearchResults(String s) throws ParseException, IOException{
		
		List<String> result = new ArrayList<String>();
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
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			result.add(d.get("isbn"));
		}

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
		return result;
	}
	public static void main(String[] args) throws IOException, ParseException {
		// 0. Specify the analyzer for tokenizing text.
		// The same analyzer should be used for indexing and searching
		StandardAnalyzer analyzer = new StandardAnalyzer();

		// 1. create the index
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		IndexWriter w = new IndexWriter(index, config);
		addDoc(w, "harmony", "HarmonyURL");
		addDoc(w, "view passport Details", "Passport URL");
		addDoc(w, "show me my passport", "Passport URL");
		addDoc(w, "Global Leave", "Leave System URL");
		addDoc(w, "GLS", "Leave System URL");
		addDoc(w, "Global Immigration", "GI system Url");
		addDoc(w, "Apply Leave", "ApplyLeaveWorkflow");
		addDoc(w, "Lunch Menu Cafeteria", "Cafeteria Menu");

		w.close();

		// 2. query
		String querystr = args.length > 0 ? args[0] : "Leave";

		// the "title" arg specifies the default field to use
		// when no field is explicitly specified in the query.
		Query q = new QueryParser("title", analyzer).parse(querystr);

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
		System.out.println("Found " + hits.length + " hits.");
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			System.out.println((i + 1) + ". " + d.get("isbn") + "\t"
					+ d.get("title"));
		}

		// reader can only be closed when there
		// is no need to access the documents any more.
		reader.close();
	}

	private static void addDoc(IndexWriter w, String title, String isbn)
			throws IOException {
		Document doc = new Document();
		doc.add(new TextField("title", title, Field.Store.YES));

		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
}