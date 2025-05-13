package edu.austral.ingsis.clifford;

public class Cd implements Command {
  private final Cursor cursor;
  private final String dirPath;

  public Cd(Cursor cursor, String dirPath) {
    this.cursor = cursor;
    this.dirPath = dirPath;
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    Element.Directory targetDir = currentDir.getDirByPath(dirPath);
    if (targetDir != null) {
      Cursor newCursor = cursor.withMark(targetDir);
      return new Result("moved to directory '" + targetDir.name() + "'", newCursor);
    }
    return new Result("'" + dirPath + "' directory does not exist", cursor);
  }
}
