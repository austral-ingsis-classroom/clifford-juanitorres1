package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.austral.ingsis.clifford.Element;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ElementTest {

  @Test
  void testDirectoryNamePathGetSetSource() {
    // Arrange
    List<Element> source = new ArrayList<>();
    Element.Directory directory =
        new Element.Directory("Downloads", "C:/juani", source, null, null);
    assertEquals("Downloads", directory.name());
    assertEquals("C:/juani", directory.path());
    assertTrue(directory.getSource().isEmpty());
    Element.File file = new Element.File("file", "C:/juani/file", directory);
    directory.setSource(file);
    assertEquals(1, directory.getSource().size());
    assertEquals(file, directory.getSource().get(0));
  }

  @Test
  void testFileNamePath() {
    List<Element> source = new ArrayList<>();
    Element.Directory parent = new Element.Directory("juani", "C:/juani", source, null, null);
    Element.File file = new Element.File("juani.txt", "C:/juani/juani.txt", parent);
    assertEquals("juani.txt", file.name());
    assertEquals("C:/juani/juani.txt", file.path());
  }

  @Test
  void testDirectorySetSourceNoDuplicates() {
    List<Element> source = new ArrayList<>();
    Element.Directory directory = new Element.Directory("juani", "C:/juani", source, null, null);
    Element.File file = new Element.File("juani.txt", "C:/juani/juani.txt", directory);
    directory.setSource(file);
    directory.setSource(file);
    assertEquals(1, directory.getSource().size(), "");
  }
}
