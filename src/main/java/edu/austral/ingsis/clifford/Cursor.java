package edu.austral.ingsis.clifford;

import java.util.List;

public class Cursor {
  private final Element.Directory root;
  private final Element.Directory mark;

  public Cursor(String user) {
    this.root = new Element.Directory(user, "", List.of(), null, null);
    this.mark = root;
  }

  private Cursor(Element.Directory mark, Element.Directory root) {
    this.mark = mark;
    this.root = root;
  }

  public Element.Directory getMark() {
    return mark;
  }

  public Cursor NewCli(Element.Directory newMark) {
    return new Cursor(newMark, newMark.root());
  }

  public Cursor withMark(Element.Directory dir) {
    return new Cursor(dir, this.root);
  }
}
