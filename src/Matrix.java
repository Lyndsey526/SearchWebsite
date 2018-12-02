import java.util.*;

public class Matrix
{
    private double mat[][];
    private int rows, columns;
    private static Random randGen = new Random();

    public Matrix(int row, int col)
    {
        rows = row;
        columns = col;
        mat = new double[rows][columns];
    }

    public Matrix(double oldMat[][])
    {
        rows = oldMat.length;
        columns = oldMat[0].length;
        mat = new double[rows][columns];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                mat[i][j] = oldMat[i][j];
    }

    public Matrix add(Matrix other)
    {
        if(columns != other.columns || rows != other.rows) {
            return null;
        }
        else
        {
            Matrix sum = new Matrix(rows, columns);
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < other.columns; j++)
                    sum.mat[i][j] = mat[i][j] + other.mat[i][j];
            return sum;
        }
    }

    public Matrix subtract(Matrix other)
    {
        if(columns != other.columns || rows != other.rows) {
            return null;
        }
        else
        {
            Matrix diff = new Matrix(rows, columns);
            for(int i = 0; i < rows; i++)
                for(int j = 0; j < other.columns; j++)
                    diff.mat[i][j] = mat[i][j] - other.mat[i][j];
            return diff;
        }
    }

    public Matrix multiply(double scalar)
    {
        Matrix product = new Matrix(rows, columns);
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                product.mat[i][j] = mat[i][j] * scalar;
        return product;
    }

    public Matrix multiply(Matrix other)
    {
        if(columns != other.rows) {
            return null;
        }
        else
        {
            Matrix product = new Matrix(rows, other.columns);
            for(int i = 0; i < rows; i++)
            {
                for(int j = 0; j < other.columns; j++)
                {
                    double sum = 0;
                    for(int k = 0; k < columns; k++)
                        sum += mat[i][k] * other.mat[k][j];
                    product.mat[i][j] = sum;
                }
            }
            return product;
        }
    }

    public int size(int dim)
    {
        if(dim == 1) {
            return rows;
        }
        else if(dim == 2) {
            return columns;
        }
        else {
            return -1;
        }
    }

    public Matrix unitVector()
    {
        return multiply(1 / magnitude());
    }

    public double magnitude()
    {
        if(rows == 1)
        {
            double sum = 0.0;
            for(int i = 0; i < columns; i++)
                sum += (mat[0][i] * mat[0][i]);
            return Math.sqrt(sum);
        }
        else if(columns == 1)
        {
            double sum = 0.0;
            for(int i = 0; i < rows; i++)
                sum += (mat[i][0] * mat[i][0]);
            return Math.sqrt(sum);
        }
        else {
            return -1.0;
        }
    }

    public double[][] toVectorMatrix()
    {
        double thisMat[][] = new double[rows][columns];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < columns; j++)
                thisMat[i][j] = mat[i][j];
        return thisMat;
    }

    public static Matrix createOnesMatrix(int rows, int cols)
    {
        Matrix newMat = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                newMat.mat[i][j] = 1;
        return newMat;
    }

    public static Matrix createRandomMatrix(int rows, int cols)
    {
        Matrix newMat = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                newMat.mat[i][j] = randGen.nextDouble();
        return newMat;
    }
}