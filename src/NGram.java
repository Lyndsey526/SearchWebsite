import java.io.*;
import java.util.*;

public class NGram 
{
	private String text;
	private ArrayList<String> gramSets;
	
	public NGram(String dbText)
	{
		text = dbText.toLowerCase();
		gramSets = new ArrayList<String>();
	}
	//get possible number of NGrams in a search term
	public int getNumNGrams(int n)
	{
		return (text.length() - n + 1);
	}
	
	public int countNearMatch(String searchString, double percMatch)
	{
		int length = searchString.length();
		int gramCount = 0;
		for(int i = 0; i < text.length() - length + 1; i++)
		{
			double count = 0;
			for(int j = 0; j < length; j++)
			{
				if(text.charAt(i + j) == searchString.charAt(j))
					count++;
			}
			if(length != 0 && count / length >= percMatch)
			{
				//System.out.println(text.substring(i, i + searchString.length()));
				gramCount++;
			}
		}
		return gramCount;
	}

	public int countMismatch(String searchString, double percMismatch) // NOT Case with % = 1.0
	{
		int length = searchString.length();
		int gramCount = 0;
		for(int i = 0; i < text.length() - length + 1; i++)
		{
			double count = 0;
			for(int j = 0; j < length; j++)
			{
				if(text.charAt(i + j) != searchString.charAt(j))
					count++;
			}
			if(length != 0 && count / length >= percMismatch)
				gramCount++;
		}
		return gramCount;
	}

	public int countExactNGram(String searchString, int n) // AND with n = 1
	{
		gramSets.clear();
		listGrams(searchString.split(" "), n);
		System.out.println(gramSets.toArray());
		Iterator<String> iter = gramSets.iterator();
		
		int matches = 0;
		while(iter.hasNext())
		{
			String ngram = iter.next();
			matches += countNearMatch(ngram, 1.0);
		}
		
		return matches;
	}
	
	public int countAnyNGram(String searchString, int n, double percMatch) // OR with n = 1
	{
		gramSets.clear();
		listGrams(searchString.split(" "), "", n);
		Iterator<String> iter = gramSets.iterator();
		
		int matches = 0;
		while(iter.hasNext())
		{
			String ngram = iter.next();
			matches += countNearMatch(ngram, percMatch);
		}
		
		return matches;
	}
	//get pairs of triplets ie college of (1) of engineering (2) engineering school (3)
	private void listGrams(String set[], int n)
	{
		for(int i = 0; i < set.length - n + 1; i++)
		{
			String gram = "";
			for(int j = 0; j < n; j++)
				gram += set[i + j];
			gramSets.add(gram);
		}
	}
	//To list every possible combination with N number of grams
	private void listGrams(String set[], String thisGram, int n)
	{
		if(n == 0)
			gramSets.add(thisGram);
		else
		{
			for(int i = 0; i < set.length; i++)
				listGrams(set, thisGram + " " + set[i], n - 1);
		}
	}
	//when does not find anything (can delete later used for testing)
	public static void main(String args[]) throws FileNotFoundException
	{
		//
		Scanner input = new Scanner(new File("test.txt"));
		String text = "";
		while(input.hasNextLine())
			text += input.nextLine();
		input.close();

		//Ranks the one that comes out the most match for whatever you are looking for
		//The green text is what is being entered in the searchkey
		NGram ranker = new NGram(text);
		System.out.println(ranker.countAnyNGram("at", 1, 0.5));
		System.out.println(ranker.countAnyNGram("at all", 2, 0.6));
		System.out.println(ranker.countExactNGram("of the", 1));
		System.out.println(ranker.countExactNGram("at the", 2));
		System.out.println(ranker.countExactNGram("out", 1));
		System.out.println(ranker.countNearMatch("cordial", 1.0));
		System.out.println(ranker.countNearMatch("cardial", 0.85));
		System.out.println(ranker.countNearMatch("cord al", 0.85));
		System.out.println(ranker.countMismatch("zzxyz", 0.85));
		
	}
}
