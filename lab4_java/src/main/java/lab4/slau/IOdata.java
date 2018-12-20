package lab4.slau;

public interface IOdata {
  void writeToConsole(Object... args);

  void readFromConsole(Object... args);

  void writeToFile(String filename) throws Exception;

  void readFromFile(String filename) throws Exception;
}
