package net.i2geo.comped.parser;

import java.util.ArrayList;
import java.util.List;

public class StatisticsReporter {
	
	private List<String> messages = new ArrayList<String>();

	public void addMessage(String m) {
		messages.add(m);
	}
	
	public void report() {
		System.out.println();
		for (String message : messages) {
			System.out.println(message);
		}
		System.out.println();
	}
}
