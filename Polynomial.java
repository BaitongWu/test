package comp2120.lab4;

import java.util.Scanner;

public class Polynomial
{
  private double[] coeffs;

  public Polynomial()
  {
    this(new double [0]);
  }

  public Polynomial(double coeffs[])
  {
    this.coeffs = new double[coeffs.length];
    for (int i=0; i != this.coeffs.length; ++i)
      this.coeffs[i] = coeffs[i];
  }

  public String toString()
  {
    StringBuilder buf = new StringBuilder();
    for (int i=this.coeffs.length; i != 0; --i)
    {
      if (this.coefficient(i-1) == 0 && i-1 != 0)
        continue;

      buf.append(this.coefficient(i-1));
      switch (i-1)
      {
        default:
          buf.append("*x^"+(i-1)+" + ");
          break;

        case 1:
          buf.append("*x + ");
          break;

        case 0:
          break;
      }
    }
    return buf.toString();
  }

  public boolean isNil()
  {
    return this.coeffs.length == 0;
  }

  public boolean isScalar()
  {
    return this.coeffs.length == 1;
  }

  public boolean isPolynomial()
  {
    return this.coeffs.length > 1;
  }

  public int degree()
  {
    for (int i=this.coeffs.length; i != 0; --i)
      if (this.coeffs[i-1] != 0)
        return i-1;
    return 0;
  }

  public double coefficient(int exponent) throws ArrayIndexOutOfBoundsException
  {
    if (exponent >= 0 && exponent < this.coeffs.length)
      return coeffs[exponent];
    else
      throw new ArrayIndexOutOfBoundsException(
        "Polynomial coefficient exponent must be < "+this.coeffs.length
        + " but was " + exponent
      );
  }

  public double[] roots() throws ArithmeticException
  {
    switch (this.degree())
    {
      default:
        throw new ArithmeticException(
          "Unable to find root of a Polynomial of degree "+this.degree()
        );

      // scalar case...
      case 1:
      {
        if (coeffs[1] != 0)
        {
          double[] retval = new double[1];
          retval[0] = this.coeffs[0] / this.coeffs[1] * -1.0;
          return retval;
        }
        else
          return new double[0]; // no roots
      }

      // quadratic case...
      case 2:
      {
        final double a = this.coeffs[2];
        final double b = this.coeffs[1];
        final double c = this.coeffs[0];
        final double discriminant = Math.sqrt(b*b-4*a*c);
        final double q = (b+Math.signum(b)*discriminant)/-2;
        final double root1 = q / a;
        final double root2 = c / q;

        if (Double.isNaN(root1) && Double.isNaN(root2))
          return new double[0]; // no roots
        else if (!Double.isNaN(root1) && Double.isNaN(root2))
        {
          double[] retval = new double[1];
          retval[0] = root1;
          return retval;
        }
        else if (Double.isNaN(root1) && !Double.isNaN(root2))
        {
          double[] retval = new double[1];
          retval[0] = root2;
          return retval;
        }
        else // neither root1 and root2 are NaNs
        {
          if (root1 == root2)
          {
            double[] retval = new double[1];
            retval[0] = root1;
            return retval;
          }
          else
          {
            double[] retval = new double[2];
            retval[0] = root1;
            retval[1] = root2;
            return retval;
          }
        }
      }
    }
  }

  public Polynomial derivative()
  {
    switch (this.coeffs.length)
    {
      // nil case...
      case 0:
        return this;

      default:
      {
        double[] new_coeffs = new double[this.coeffs.length-1];
        for (int i=1; i != this.coeffs.length; ++i)
          new_coeffs[i-1] = this.coeffs[i] * i;
        return new Polynomial(new_coeffs);
      }
    }
  }

  public Polynomial add(Polynomial p)
{
   int size1 = p.coeffs.length;
   int size2 = this.coeffs.length;
   int count = 0;
   if(size1 < size2) {
       double[] coeffs = new double[size1];
       while (count < size1) {
           coeffs[count] = p.coeffs[count] + this.coeffs[count];
           count++;
       }
       return new Polynomial(coeffs);
   }else {
       double[] coeffs = new double[size2];
       while (count < size2) {
           coeffs[count] = p.coeffs[count] + this.coeffs[count];
           count++;
       }
       return new Polynomial(coeffs);
   }
}

  public static Polynomial read(java.io.InputStream is)
  {
    Scanner in = new Scanner(is);
    String pline = in.nextLine();

    int count = 0;
    Scanner lineIn = new Scanner(pline);
    while (lineIn.hasNextDouble())
    {
      lineIn.nextDouble();
      ++count;
    }

    double[] coeffs = new double[count];
    lineIn = new Scanner(pline);
    while (lineIn.hasNextDouble())
      coeffs[--count] = lineIn.nextDouble();

    return new Polynomial(coeffs);
  }

  public static void main(String[] args)
  {
    System.out.print("Enter polynomial a's coefficients: ");
    Polynomial a = Polynomial.read(System.in);

    System.out.print("Enter polynomial a's coefficients: ");
    Polynomial b = Polynomial.read(System.in);

    Polynomial p = a.add(b);
    System.out.println("  Polynomial: " + p);
    System.out.println("       isNil: " + p.isNil());
    System.out.println("    isScalar: " + p.isScalar());
    System.out.println("isPolynomial: " + p.isPolynomial());
    System.out.println("      Degree: " + p.degree());
    System.out.println("  Derivative: " + p.derivative());

    try
    {
      final double[] roots = p.roots();
      if (roots.length == 0)
        System.out.println("       Roots: <None>");
      else
      {
        System.out.print("       Roots: { ");
        for (int i=0; i != roots.length; ++i)
        {
          System.out.print(""+roots[i]);
          if (i+1 != roots.length)
            System.out.print(", ");
          else
            System.out.println(" }");
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("Error occurred finding roots: "+e.getMessage());
    }
  }
}
