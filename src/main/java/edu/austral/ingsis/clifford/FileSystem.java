package edu.austral.ingsis.clifford;

import java.util.List;

public interface FileSystem {
  List<Element> ls();

  List<Element> lsOrd(Filter arg);

  String mkdir(Element.Directory dir);

  String cd(Element.Directory dir);

  String cd(String arg);

  String pwd();

  String touch(Element.File newFile);

  String rm(Element file);

  String rmRecursive(Element.Directory directory);
}
