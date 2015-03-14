package com.onebox.entity;

import java.util.List;

public class ResultsObject {
	private List<OneInfyObject> results;
	private String say;
	private Boolean spellCorrect;
	private String originalSearchText;
	private String correctedSearchText;
	
	
	public ResultsObject(List<OneInfyObject> results, String say,
			Boolean spellCorrect) {
		super();
		this.results = results;
		this.say = say;
		this.spellCorrect = spellCorrect;
	}
	
	public String getOriginalSearchText() {
		return originalSearchText;
	}

	public void setOriginalSearchText(String originalSearchText) {
		this.originalSearchText = originalSearchText;
	}

	public String getCorrectedSearchText() {
		return correctedSearchText;
	}

	public void setCorrectedSearchText(String correctedSearchText) {
		this.correctedSearchText = correctedSearchText;
	}

	public List<OneInfyObject> getResults() {
		return results;
	}
	public void setResults(List<OneInfyObject> results) {
		this.results = results;
	}
	public String getSay() {
		return say;
	}
	public void setSay(String say) {
		this.say = say;
	}
	public Boolean getSpellCorrect() {
		return spellCorrect;
	}
	public void setSpellCorrect(Boolean spellCorrect) {
		this.spellCorrect = spellCorrect;
	}
	
	
}
