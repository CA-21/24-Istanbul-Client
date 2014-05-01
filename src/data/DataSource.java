package data;

import java.util.ArrayList;

import killerapp.istanbul24.db.Venue;

public class DataSource {
	
	private static DataSource instance;
	private ArrayList<Integer> questions;
	private ArrayList<Venue> venues;
	private int questionCount;
	public boolean nothingFound;
	
	public int getQuestionCount() {
		return questionCount;
	}

	public void setQuestionCount(int questionCount) {
		this.questionCount = questionCount;
	}

	public ArrayList<Venue> getVenues() {
		return venues;
	}

	public void setVenues(ArrayList<Venue> venues) {
		this.venues = venues;
	}

	private DataSource(){
		questions= new ArrayList<Integer>();
		venues = new ArrayList<Venue>();
	}
	
	public static DataSource getInstance(){
		if(instance==null){
			instance = new DataSource();
		}
		return instance;
	}

	public void setQuestions(ArrayList<Integer> questions) {
		this.questions = questions;
	}

	public ArrayList<Integer> getQuestions() {
		return questions;
	}
	
	
}
