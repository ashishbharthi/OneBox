package com.onebox.services;

import java.io.IOException;
import java.io.InputStream;
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
		//sc.trainFile("dictionary.txt");
		sc.trainSingle("Performagic");
		sc.trainSingle("iTravel");
		
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
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		SpellCheckService scs = SpellCheckService.getInstance();
		System.out.println(scs.getSc().correct("travel"));
	}
}
