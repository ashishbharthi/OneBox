package com.onebox.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.gauner.jSpellCorrect.spi.ToySpellingCorrector;

public class SpellCheckService {
	private static ToySpellingCorrector sc = new ToySpellingCorrector();
	public static ToySpellingCorrector getSc() {
		return sc;
	}

	public static void setSc(ToySpellingCorrector sc) {
		SpellCheckService.sc = sc;
	}

	private static SpellCheckService scs = null;
	
	private SpellCheckService() throws MalformedURLException, IOException{
		BufferedReader reader = null;
		InputStream in = getClass().getClassLoader().getResourceAsStream("dictionary.txt"); 
        reader = new BufferedReader(new InputStreamReader(in));
        
        String line = null;
        while ( (line = reader.readLine()) != null) {
            sc.trainSingle(line);
        }
		//sc.trainFile("dictionary.txt");
		sc.trainSingle("Performagic");
		sc.trainSingle("iTravel");
		sc.trainSingle("gls");
		sc.trainSingle("inbay");
		
	}
	
	public static SpellCheckService getInstance() throws MalformedURLException, IOException {
		if(scs == null){
			scs = new SpellCheckService();
			return scs;
		}
		else{
			return scs;
		}
	}
	
	public String correctSpellings(String searchText) {
		String[] tokens =  searchText.split(" ");
		String result = "";
		for (int i = 0; i < tokens.length; i++) {
			result += sc.correct(tokens[i]) + " ";
		}
		return result.trim();
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		SpellCheckService scs = SpellCheckService.getInstance();
		System.out.println(scs.getSc().correct("leav"));
		
	}

	
}
