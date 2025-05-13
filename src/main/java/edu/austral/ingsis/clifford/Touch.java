package edu.austral.ingsis.clifford;

public class Touch implements Command {
  private final Cursor cursor;
  private final String fileName;

  public Touch(Cursor cursor, String fileName) {
    this.cursor = cursor;
    this.fileName = fileName;
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    if (currentDir.findElement(fileName).isPresent()) {
      return new Result("'" + fileName + "' file already exists", cursor);
    }
    Element.File newFile = currentDir.createFile(fileName);
    Element.Directory updatedDir = currentDir.addElement(newFile);
    Element.Directory finalDir = updatedDir.updateInParent();
    Cursor updatedCursor = cursor.NewCli(finalDir);
    return new Result("'" + fileName + "' file created", updatedCursor);
  }
}
