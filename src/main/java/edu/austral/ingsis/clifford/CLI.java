package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class CLI implements FileSystem {
  public Element.Directory root;
  public Element.Directory mark;

  // me muevo entre C:juani/downloads/.....
  public CLI(String user) {
    this.root = new Element.Directory(user, "C:" + user, new ArrayList<>(), null, null);
    this.mark = root;
  }

  @Override
  public List<Element> ls() {
    return mark.getSource();
  }

  @Override
  public List<Element> lsOrd(Filter arg) {
    if (Objects.equals(arg, Filter.ASC)) {
      List<Element> ascendente = new ArrayList<>(mark.getSource());
      ascendente.sort(Comparator.comparing(Element::name));
      return ascendente;
    } else if (Objects.equals(arg, Filter.DESC)) {
      List<Element> descendente = new ArrayList<>(mark.getSource());
      descendente.sort(Comparator.comparing(Element::name).reversed());
      return descendente;
    }
    return null;
  }

  @Override
  public String mkdir(Element.Directory dir) {
    if (!mark.getSource().contains(dir)) {
      mark.setSource(dir);
      return dir.name() + " directory created";
    }
    return dir.name() + " directory not created";
  }

  @Override
  public String cd(Element.Directory dir) {
    if (dir.root() == root && dir.padre().getSource().contains(dir)) {
      mark = dir;
      return "moved to directory " + mark.name();
    }
    return "no hay tal dir";
  }

  @Override
  public String cd(String arg) {
    if (Objects.equals(arg, ".")) {
      return "moved to directory " + mark.name();
    } else if (Objects.equals(arg, "..")) {
      if (mark.padre() != null) {
        mark = mark.padre();
        return "moved to directory " + mark.name();
      }
      return "no hay padre";
    }
    return "arg no valido";
  }

  @Override
  public String pwd() {
    return root.path();
  }

  @Override
  public String touch(Element.File newFile) {
    if (!mark.getSource().contains(newFile)) {
      mark.setSource(newFile);
      return newFile.name() + " file created";
    }
    return "existe ya";
  }

  @Override
  public String rm(Element file) {
    if (mark.getSource().contains(file)) {
      mark.getSource().remove(file);
      return file.name() + " removed";
    }
    return "no eliminado";
  }

  @Override
  public String rmRecursive(Element.Directory directory) {
    if (directory.equals(mark) || mark.getSource().contains(directory)) {
      List<Element> copySource = new ArrayList<>(directory.getSource());
      for (Element e : copySource) {
        if (e instanceof Element.Directory subDir) {
          rmRecursive(subDir);
        } else {
          directory.getSource().remove(e);
        }
      }
      Element.Directory parentDir = directory.padre();
      parentDir.getSource().remove(directory);
      if (mark == directory) {
        mark = parentDir;
      }
      return directory.name() + " removed";
    }
    return "no eliminado";
  }
}
