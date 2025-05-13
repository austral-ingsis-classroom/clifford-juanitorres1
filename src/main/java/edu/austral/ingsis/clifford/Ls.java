package edu.austral.ingsis.clifford;

import java.util.List;
import java.util.Optional;

public class Ls implements Command {
  private final Cursor cursor;
  private final Optional<Filter> filter;

  public Ls(Cursor cursor) {
    this.cursor = cursor;
    this.filter = Optional.empty();
  }

  public Ls(Cursor cursor, Filter filter) {
    this.cursor = cursor;
    this.filter = Optional.of(filter);
  }

  @Override
  public Result execute() {
    Element.Directory currentDir = cursor.getMark();
    String result;
    if (filter.isPresent()) {
      List<Element> sortedElements = currentDir.getSortedElements(filter.get() == Filter.ASC);
      result = currentDir.getElementsAsString(sortedElements);
    } else {
      result = currentDir.getElementsAsString();
    }
    return new Result(result, cursor);
  }
}
