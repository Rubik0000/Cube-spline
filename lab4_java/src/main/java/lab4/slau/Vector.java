package lab4.slau;

import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Vector implements IOdata {
  static private Random random = new Random();

  private double[] _vector;

  public double getComponent(int ind) {
    return _vector[ind];
  }

  public int getSize() {
    return _vector.length;
  }

  public Vector(double ...values) {
    _vector = values;
  }

  public Vector(String filename) throws Exception {
    readFromFile(filename);
  }

  static public Vector getRandomVector(int size) {
    double[] vec = new double[size];
    double min = -100;
    double max = 100;
    for (int i = 0; i < size; ++i) {
      vec[i] = min + random.nextDouble() * (max - min);
    }
    return new Vector(vec);
  }

  public Vector add(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно сложить вектора разных размеров");
    }
    double[] newVec = new double[getSize()];
    for (int i = 0; i < getSize(); ++i) {
      newVec[i] = vec._vector[i] + _vector[i];
    }
    return new Vector(newVec);
  }

  public Vector subtract(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно вычесть вектора разных размеров");
    }
    double[] newVec = new double[getSize()];
    for (int i = 0; i < getSize(); ++i) {
      newVec[i] = _vector[i] - vec._vector[i];
    }
    return new Vector(newVec);
  }

  public double scalarMultiply(Vector vec) throws Exception {
    if (getSize() != vec.getSize()) {
      throw new Exception("Невозможно вычислить скалярное произведение векторов разных размеров");
    }
    double result = 0;
    for (int i = 0; i < getSize(); ++i) {
      result += _vector[i] * vec._vector[i];
    }
    return result;
  }

  public double getNorm() {
    double max = Math.abs(_vector[0]);
    for (int i = 1; i < getSize(); ++i) {
      if (Math.abs(_vector[i]) > max) {
        max = Math.abs(_vector[i]);
      }
    }
    return max;
  }

  @Override
  public void writeToConsole(Object ...args) {
    for (double el : _vector) {
      System.out.print(el + "  ");
    }
    System.out.println();
  }

  @Override
  public void readFromConsole(Object ...args) {
      Scanner scanner = new Scanner(System.in);
      int size = (Integer) args[0];
      System.out.println("Введите " + size + " компонент вектора: ");
      _vector = new double[size];
      for (int i = 0; i < size; ++i) {
        _vector[i] = scanner.nextDouble();
      }
  }

  @Override
  public void writeToFile(String filename) throws Exception {
    try (FileWriter writer = new FileWriter(filename)) {
      writer.write(getSize() + System.lineSeparator());
      for (double component : _vector) {
        writer.write(component + " ");
      }
      writer.write(System.lineSeparator());
    }
  }

  @Override
  public void readFromFile(String filename) throws Exception {
    try (Scanner scanner = new Scanner(new FileReader(filename))) {
      int size = scanner.nextInt();
      double[] newVec = new double[size];
      for (int i = 0; i < size; ++i) {
        newVec[i] = scanner.nextDouble();
      }
      _vector = newVec;
    }
  }

  public double[] toArray() {
    return _vector;
  }
}
