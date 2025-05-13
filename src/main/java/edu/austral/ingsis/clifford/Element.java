package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public sealed interface Element permits Element.Directory, Element.File {

  String name();

  String path();

  record File(String name, String path, Directory padre) implements Element {}

  record Directory(String name, String path, List<Element> source, Directory padre, Directory root)
      implements Element {
    public Directory {
      source = List.copyOf(source);
    }

    public List<Element> getSource() {
      return source;
    }

    public List<Element> getSortedElements(boolean ascending) {
      List<Element> sortedList = new ArrayList<>(source);
      if (ascending) {
        sortedList.sort(Comparator.comparing(Element::name));
      } else {
        sortedList.sort(Comparator.comparing(Element::name).reversed());
      }
      return sortedList;
    }

    public String getElementsAsString() {
      return getElementsAsString(source);
    }

    public String getElementsAsString(List<Element> elements) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < elements.size(); i++) {
        sb.append(elements.get(i).name().trim());
        if (i < elements.size() - 1) {
          sb.append(" ");
        }
      }
      return sb.toString();
    }

    public Element.Directory getDirByPath(String path) {
      if (path.equals("/") || path.isEmpty()) {
        return getRoot();
      }

      Element.Directory current = path.startsWith("/") ? root : this;
      String[] parts = path.split("/");
      for (String part : parts) {
        if (part.isEmpty() || part.equals(".")) continue;
        if (part.equals("..")) {
          if (current.padre() != null) {
            current = current.padre();
          }
          continue;
        }

        Optional<Element.Directory> nextDir = current.findDirectory(part);
        if (nextDir.isEmpty()) return null;
        current = nextDir.get();
      }
      return current;
    }

    public Directory getRoot() {
      Directory directorioActual = this;
      while (directorioActual.padre != null) {
        directorioActual = directorioActual.padre;
      }
      return directorioActual;
    }

    public Optional<Directory> findDirectory(String dirName) {
      return source.stream()
          .filter(e -> e instanceof Directory && e.name().equals(dirName))
          .map(e -> (Directory) e)
          .findFirst();
    }

    public Optional<Element> findElement(String name) {
      return source.stream().filter(e -> e.name().equals(name)).findFirst();
    }

    public Directory addElement(Element element) {
      List<Element> updatedSource = new ArrayList<>(source);
      updatedSource.add(element);
      return new Directory(name, path, updatedSource, padre, root);
    }

    public Directory removeElement(Element element) {
      List<Element> updatedSource = new ArrayList<>(source);
      updatedSource.remove(element);
      return new Directory(name, path, updatedSource, padre, root);
    }

    public Directory createDirectory(String dirName) {
      return new Directory(
          dirName, path + "/" + dirName, List.of(), this, root != null ? root : getRoot());
    }

    public File createFile(String fileName) {
      return new File(fileName, path + "/" + fileName, this);
    }

    public Directory updateInParent() {
      if (padre == null) {
        return this;
      }
      Directory updatedParent = padre.removeElement(this).addElement(this);
      Directory fullUpdatedParent = updatedParent.updateInParent();
      return new Directory(name, path, source, fullUpdatedParent, root);
    }
  }
}
