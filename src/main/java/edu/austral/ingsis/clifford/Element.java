package edu.austral.ingsis.clifford;

import java.util.List;

public sealed interface Element permits Element.Directory, Element.File {

  String name();

  String path();

  record File(String name, String path, Directory padre) implements Element {}

  record Directory(String name, String path, List<Element> source, Directory padre, Directory root)
      implements Element {

    public void setSource(Element element) {
      if (!source.contains(element)) source.add(element);
    }

    public List<Element> getSource() {
      return source;
    }
  }
}
