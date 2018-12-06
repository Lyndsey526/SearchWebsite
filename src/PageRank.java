/*
		https://en.wikipedia.org/wiki/PageRank
*/
public class PageRank
{
	private Matrix M = null;
	
	public PageRank(int counts[][])
	{
		double prob[][] = makeMMatrix(counts);
		M = new Matrix(prob);
	}
	
	public double[][] getRank(double damp_factor, double tol)
	{
		int N = M.size(2);
		Matrix vect = Matrix.createRandomMatrix(N, 1), last_v = Matrix.createOnesMatrix(N, 1).multiply(100);
		vect = vect.unitVector();
		Matrix M_hat = M.multiply(damp_factor).add(Matrix.createOnesMatrix(N, N).multiply((1 - damp_factor) / N));
		while(vect.subtract(last_v).magnitude() > tol)
		{
			last_v = vect;
			vect = M_hat.multiply(vect);
		}
		return vect.toVectorMatrix();
	}
	
	private double[][] makeMMatrix(int counts[][])
	{
		int n = counts.length;
		double prob[][] = new double[n][n];
		for(int j = 0; j < n; j++)
		{
			int sum = 0;
			for(int i = 0; i < n; i++)
				sum += counts[i][j];
			for(int i = 0; i < n; i++)
				prob[i][j] = counts[i][j] / ((double)sum);
		}
		return prob;
	}
	
	private static void print(double arr[][])
	{
		for(int i = 0; i < arr.length; i++)
		{
			for(int j = 0; j < arr[0].length; j++)
				System.out.print(arr[i][j] + " ");
			System.out.println();
		}
		System.out.println();
	}
	
	public static void main(String args[])
	{
		int M[][] = {{0, 0, 0, 0, 1},
					 {1, 0, 0, 0, 0},
					 {1, 0, 0, 0, 0},
					 {0, 1, 1, 0, 0},
					 {0, 0, 1, 1, 0}};

		PageRank ranker = new PageRank(M);
		PageRank.print(ranker.getRank(0.80, 0.00001));
		
	}
}
/*
Output Code
/Library/Java/JavaVirtualMachines/jdk-10.0.2.jdk/Contents/Home/bin/java "-javaagent:/Applications/IntelliJ IDEA.app/Contents/lib/idea_rt.jar=64079:/Applications/IntelliJ IDEA.app/Contents/bin" -Dfile.encoding=UTF-8 -classpath "/Users/turtle/Desktop/Search 2 - works/out/production/Search:/Users/turtle/Desktop/Search 2 - works/web/WEB-INF/lib/jstl-1.2.jar:/Users/turtle/Desktop/Search 2 - works/web/WEB-INF/lib/mysql-connector-java-8.0.13.jar" PageRank
0.4419260489883389
0.24751715902638516
0.24751715902638516
0.3677713224113276
0.463976919783892


Process finished with exit code 0
*/