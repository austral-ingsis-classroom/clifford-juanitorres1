package edu.austral.ingsis.clifford;

public interface Command {

  record Result(String output, Cursor cursor) {}

  Result execute();
}
