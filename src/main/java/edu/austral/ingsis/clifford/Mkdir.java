package edu.austral.ingsis.clifford;

public class Mkdir implements Command {
  private final Cursor cursor;
  private final String dirName;

  public Mkdir(Cursor cursor, String dirName) {
    this.cursor = cursor;
    this.dirName = dirName;
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    if (currentDir.findDirectory(dirName).isPresent()) {
      return new Result("'" + dirName + "' directory already exists", cursor);
    }
    Element.Directory newDir = currentDir.createDirectory(dirName);
    Element.Directory updatedDir = currentDir.addElement(newDir);
    Element.Directory finalDir = updatedDir.updateInParent();
    Cursor updatedCursor = cursor.NewCli(finalDir);
    return new Result("'" + dirName + "' directory created", updatedCursor);
  }
}
