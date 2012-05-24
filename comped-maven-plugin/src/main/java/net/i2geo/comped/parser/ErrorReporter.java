package net.i2geo.comped.parser;

import java.util.HashSet;
import java.util.Set;

public class ErrorReporter {

	private Set<String> falseLocale = new HashSet<String>();
	private Set<String> noLocale    = new HashSet<String>();
	private Set<String> noId        = new HashSet<String>();
	private Set<String> duplicates  = new HashSet<String>();
	private Set<String> emptyName   = new HashSet<String>();

	public void addFalseLocale(String uri) {
		falseLocale.add(uri);
	}

	public void addNoLocale(String uri) {
		noLocale.add(uri);
	}

	public void addEmptyName(String uri) {
		emptyName.add(uri);
	}

	public void addNoId(String uri) {
		noId.add(uri);
	}

	public void addDuplicates(String uri) {
		duplicates.add(uri);
	}

	public void report() {
		System.out.println("\nName is empty in: ");
		for (String item : emptyName) {
			System.out.println("   " + item);
		}

		System.out.println("\nName has no locale in: ");
		for (String item : noLocale) {
			System.out.println("   " + item);
		}
		
		
		System.out.println("\nName has no correct locale in: ");
		for (String item : falseLocale) {
			System.out.println("   " + item);
		}

		System.out.println("\nItem has no id: ");
		for (String item : noId) {
			System.out.println("   " + item);
		}

		System.out.println("\nDuplicate uri: ");
		for (String item : duplicates) {
			System.out.println("   " + item);
		}
	}
}
