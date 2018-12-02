/*
 * http://eecs.ceas.uc.edu/~annexsfs/Courses/cs690/Indexing%20and%20Representation.ppt
 */

import java.io.File;
import java.util.*;

public class VectorSpace 
{
	private TreeMap<String, Integer> totalCounts;
	private ArrayList<String> documents;
	private ArrayList<TreeMap<String, Integer>> documentCounts;
	
	public VectorSpace()
	{
		totalCounts = new TreeMap<String, Integer>();
		documents = new ArrayList<String>();
		documentCounts = new ArrayList<TreeMap<String, Integer>>();
	}
	
	public void addDocument(String doc)
	{
		documents.add(doc);
	}
	
	public void compile(String term)
	{
		String terms[] = term.split(" ");
		Iterator<String> iter = documents.iterator();
		while(iter.hasNext())
		{
			TreeMap<String, Integer> counts = new TreeMap<String, Integer>();
			for(int i = 0; i < terms.length; i++)
				counts.put(terms[i], 0);
			
			String textTerms[] = iter.next().replaceAll("[^a-zA-Z ]", " ").toLowerCase().split(" ");
			for(int i = 0; i < textTerms.length; i++)
			{
				if(!textTerms[i].equals(""))
				{
					if(counts.containsKey(textTerms[i])) {
						counts.put(textTerms[i], counts.get(textTerms[i]) + 1);
					}
					else {
						counts.put(textTerms[i], 1);
					}
						
					if(totalCounts.containsKey(textTerms[i])) {
						totalCounts.put(textTerms[i], totalCounts.get(textTerms[i]) + 1);
					}
					else {
						totalCounts.put(textTerms[i], 1);
					}
				}
			}
			documentCounts.add(counts);
		}
	}
	
	private void print(double arr[])
	{
		for(int i = 0; i < arr.length; i++)
			System.out.print(arr[i] + " ");
		System.out.println();
	}
	
	public double[] scoreSimilarity(String term)
	{
		String terms[] = term.toLowerCase().split(" ");
		double weightsQ[] = new double[terms.length];
		double weightsD[] = new double[terms.length];
		double logNN[] = new double[terms.length];
		double tf[] = new double[terms.length];
		double simScores[] = new double[documents.size()];
		
		int N = getNumDocuments();
		for(int j = 0; j < terms.length; j++)
		{
			int matches = 0;
			weightsQ[j] = 1.0 / terms.length;
			for(int i = 0; i < documentCounts.size(); i++)
			{
				TreeMap<String, Integer> doc = documentCounts.get(i);
				if(doc.containsKey(terms[j]) && doc.get(terms[j]) > 0) {
					matches++;
				}
			}
			if(matches != 0) {
				logNN[j] = Math.log(N / matches);
			}
			else {
				logNN[j] = 0.0;
			}
		}
		
		for(int k = 0; k < documents.size(); k++)
		{
			double sum = 0.0;
			TreeMap<String, Integer> doc = documentCounts.get(k);
			for(int j = 0; j < terms.length; j++)
			{
				tf[j] = doc.containsKey(terms[j]) ? doc.get(terms[j]) : 0;
				sum += (tf[j] * tf[j] * logNN[j] * logNN[j]);
			}
			
			for(int j = 0; j < terms.length; j++)
				weightsD[j] = (sum != 0) ? (tf[j] * logNN[j]) / Math.sqrt(sum) : 0.0;
			
			double sumWDQ = 0.0, sumWD = 0.0, sumWQ = 0.0;
			for(int j = 0; j < weightsD.length; j++)
			{
				sumWDQ += weightsQ[j] * weightsD[j];
				sumWD += weightsD[j] * weightsD[j];
				sumWQ += weightsQ[j] * weightsQ[j];
			}
			
			simScores[k] = (sumWD * sumWQ != 0) ? sumWDQ / Math.sqrt(sumWD * sumWQ) : 0.0;
		}
		
		return simScores;
	}
	
	public int getNumDocuments()
	{
		return documents.size();
	}
	
	public static void main(String args[]) throws Exception
	{
		VectorSpace space = new VectorSpace();
		String names[] = {"test.txt", "test2.txt", "test3.txt", "test4.txt"};
		for(int i = 0; i < 4; i++)
		{
			Scanner input = new Scanner(new File(names[i]));
			String text = "";
			while(input.hasNextLine())
				text += (input.nextLine() + "\n");
			space.addDocument(text);
		}
		
		String searchString = "aid four days school gay yet lover now axe figure";
		space.compile(searchString);
		double simScores[] = space.scoreSimilarity(searchString);
		for(int i = 0; i < simScores.length; i++)
			System.out.println(simScores[i]);
	}
}
