package edu.austral.ingsis.clifford;

public class Pwd implements Command {
  private final Cursor cursor;

  public Pwd(Cursor cursor) {
    this.cursor = cursor;
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    return new Result(currentDir.path(), cursor);
  }
}
