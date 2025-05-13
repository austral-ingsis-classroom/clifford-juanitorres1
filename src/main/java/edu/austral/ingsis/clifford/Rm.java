package edu.austral.ingsis.clifford;

import java.util.Optional;

public class Rm implements Command {
  private final Cursor cursor;
  private final String name;
  private final boolean recursive;

  public Rm(Cursor cursor, String name, boolean recursive) {
    this.cursor = cursor;
    this.name = name;
    this.recursive = recursive;
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    Optional<Element> optionalElement = currentDir.findElement(name);
    if (optionalElement.isEmpty()) {
      return new Result("'" + name + "' does not exist", cursor);
    }
    Element elementToRemove = optionalElement.get();
    if (elementToRemove instanceof Element.Directory dirToRemove) {
      if (!recursive) {
        return new Result("cannot remove '" + name + "', is a directory", cursor);
      }
      Cursor newCursor = cursor.NewCli(dirToRemove);
      for (Element child : dirToRemove.getSource()) {
        Command rmChild = new Rm(newCursor, child.name(), true);
        Result result = rmChild.execute();
        newCursor = result.cursor();
      }
    }
    Element.Directory updatedDir = currentDir.removeElement(elementToRemove);
    Element.Directory finalDir = updatedDir.updateInParent();
    Cursor updatedCursor = cursor.NewCli(finalDir);
    return new Result("'" + name + "' removed", updatedCursor);
  }
}
