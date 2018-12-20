package lab4.slau;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class TriDiagonalMatrix implements IOdata {
  private double[][] _matix;

  private int _size;

  public TriDiagonalMatrix() { }

  public TriDiagonalMatrix(Vector a, Vector b, Vector c) throws Exception {
    if (a.getSize() != b.getSize() || a.getSize() != c.getSize() || b.getSize() != c.getSize()) {
      throw new Exception("Невозможно составить техдиагональную матрицу из векторов разных размеров");
    }
    _size = b.getSize();
    _matix = new double[_size][_size];
    _matix[0][0] = b.getComponent(0);
    _matix[0][1] = c.getComponent(0);
    for (int i = 1; i < _size - 1; ++i) {
      _matix[i][i - 1] = a.getComponent(i);
      _matix[i][i] = b.getComponent(i);
      _matix[i][i + 1] = c.getComponent(i);
    }
    _matix[_size - 1][_size - 1] = b.getComponent(_size - 1);
    _matix[_size - 1][_size - 2] = a.getComponent(_size - 1);
  }

  public TriDiagonalMatrix(String fileName) throws Exception {
    readFromFile(fileName);
  }

  protected TriDiagonalMatrix(double[][] matr) {
    _matix = matr;
    _size = matr.length;
  }

  public int getSize() {
    return _size;
  }

  public double getA(int ind) {
    if (ind == 0) {
      return 0;
    }
    return _matix[ind][ind - 1];
  }

  public double getB(int ind) {
    return _matix[ind][ind];
  }

  public double getC(int ind) {
    if (ind == getSize() - 1) {
      return 0;
    }
    return _matix[ind][ind + 1];
  }

  private double[][] getEmptyMatrix() {
    double[][] m = new double[_size][_size];
    for (int i = 0; i < _size; ++i) {
      Arrays.fill(m[i], 0);
    }
    return m;
  }

  public TriDiagonalMatrix add(TriDiagonalMatrix matr) throws Exception {
    if (_size != matr._size) {
      throw new Exception("Невозможно сложить матрицы разных размерностей");
    }
    double[][] m = getEmptyMatrix();
    m[0][0] = _matix[0][0] + matr._matix[0][0];
    m[0][1] = _matix[0][1] + matr._matix[0][1];
    for (int i = 1; i < _size - 1; ++i) {
      for (int j = i - 1; j <= i + 1; ++j) {
        m[i][j] = _matix[i][j] + matr._matix[i][j];
      }
    }
    m[_size - 1][_size - 2] = _matix[_size - 1][_size - 2] + matr._matix[_size - 1][_size - 2];
    m[_size - 1][_size - 1] = _matix[_size - 1][_size - 1] + matr._matix[_size - 1][_size - 1];
    return new TriDiagonalMatrix(m);
  }

  public TriDiagonalMatrix subtract(TriDiagonalMatrix matr) throws Exception {
    if (_size != matr._size) {
      throw new Exception("Невозможно вычесть матрицы разных размерностей");
    }
    double[][] m = getEmptyMatrix();
    m[0][0] = _matix[0][0] - matr._matix[0][0];
    m[0][1] = _matix[0][1] - matr._matix[0][1];
    for (int i = 1; i < _size - 1; ++i) {
      for (int j = i - 1; j <= i + 1; ++j) {
        m[i][j] = _matix[i][j] - matr._matix[i][j];
      }
    }
    m[_size - 1][_size - 2] = _matix[_size - 1][_size - 2] - matr._matix[_size - 1][_size - 2];
    m[_size - 1][_size - 1] = _matix[_size - 1][_size - 1] - matr._matix[_size - 1][_size - 1];
    return new TriDiagonalMatrix(m);
  }

  public TriDiagonalMatrix multiply(TriDiagonalMatrix matr) throws Exception {
    if (_size != matr._size) {
      throw new Exception("Невозможно уможить матрицы разных размерностей");
    }
    double[][] m = getEmptyMatrix();
    for (int i = 0; i < _size; ++i) {
      for (int j = 0; j < _size; ++j) {
        m[i][j] = 0;
        for (int k = 0; k < _size; ++k) {
          m[i][j] += _matix[i][k] * matr._matix[k][j];
        }
      }
    }
    return new TriDiagonalMatrix(m);
  }

  public Vector multiply(Vector vec) throws Exception {
    if (_size != vec.getSize()) {
      throw new Exception("Невозможно уможить на вектор");
    }
    double[] newVec = new double[_size];
    for (int i = 0; i < _size; ++i) {
      for (int j = i - 1 < 0 ? i : i - 1; j <= (i + 1 >= _size ? i : i + 1); ++j) {
        newVec[i] = 0;
        for (int k = 0; k < _size; ++k) {
          newVec[i] += _matix[i][k] * vec.getComponent(k);
        }
      }
    }
    return new Vector(newVec);
  }

  @Override
  public void writeToConsole(Object ...args)  {
    for (double[] row : _matix) {
      for (double elem : row) {
        System.out.print(elem + "  ");
      }
      System.out.println();
    }
  }

  @Override
  public void readFromConsole(Object ...args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Введите размер матрицы: ");
    _size = scanner.nextInt();
    System.out.println("Введите элементы строки матрицы: ");
    double[][] matr = getEmptyMatrix();
    matr[0][0] = scanner.nextDouble();
    matr[0][1] = scanner.nextDouble();
    for (int i = 1; i < _size - 1; ++i) {
      for (int k = 0; k < i - 1; ++k) {
        System.out.print("0 ");
      }
      for (int j = i - 1; j <= i + 1; ++j) {
        matr[i][j] = scanner.nextDouble();
      }
    }
    for (int k = 0; k < _size - 2; ++k) {
      System.out.print("0 ");
    }
    matr[_size - 1][_size - 2] = scanner.nextDouble();
    matr[_size - 1][_size - 1] = scanner.nextDouble();
    _matix = matr;
  }

  @Override
  public void writeToFile(String filename) throws Exception {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(getSize() + System.lineSeparator());
      for (double[] row : _matix) {
        for (double el : row) {
          writer.write(el + " ");
        }
      }
    }
  }

  @Override
  public void readFromFile(String filename) throws Exception {

  }
}
