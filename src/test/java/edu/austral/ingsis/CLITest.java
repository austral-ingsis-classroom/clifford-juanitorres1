package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.CLI;
import edu.austral.ingsis.clifford.Element;
import edu.austral.ingsis.clifford.Filter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CLITest {

  private CLI cli;
  private Element.Directory dir1;
  private Element.Directory dir2;
  private Element.File file1;

  @BeforeEach
  public void setUp() {
    cli = new CLI("juani");
    dir1 =
        new Element.Directory(
            "downloads", "C:juani/downloads", new ArrayList<>(), cli.root, cli.root);
    dir2 = new Element.Directory("music", "C:juani/music", new ArrayList<>(), dir1, cli.root);
    file1 = new Element.File("hola.txt", "C:juani/hola.txt", cli.root);
  }

  @Test
  public void testLsEmpty() {
    assertTrue(cli.ls().isEmpty());
  }

  @Test
  public void testLsOrdAscAndDesc() {
    cli.mkdir(dir1);
    cli.touch(file1);
    List<Element> asc = cli.lsOrd(Filter.ASC);
    assertEquals(2, asc.size());
    assertEquals("downloads", asc.get(0).name());
    List<Element> desc = cli.lsOrd(Filter.DESC);
    assertEquals(2, desc.size());
    assertEquals("hola.txt", desc.get(0).name());
    assertNull(cli.lsOrd(null));
  }

  @Test
  public void testMkdir() {
    String res1 = cli.mkdir(dir1);
    assertEquals("downloads directory created", res1);
    String res2 = cli.mkdir(dir1);
    assertEquals("downloads directory not created", res2);
  }

  @Test
  public void testCdValidAndInvalid() {
    dir1 = new Element.Directory("hola", "a", new ArrayList<>(), null, null);
    cli.mkdir(dir1);
    assertEquals("no hay tal dir", cli.cd(dir1));

    dir1 =
        new Element.Directory(
            "downloads", "C:juani/downloads", new ArrayList<>(), cli.root, cli.root);
    cli.mkdir(dir1);
    cli.root.getSource().add(dir1);
    assertEquals("moved to directory downloads", cli.cd(dir1));
  }

  @Test
  public void testCdStringDot() {
    assertEquals("moved to directory juani", cli.cd("."));
  }

  @Test
  public void testCdStringDotDot() {
    assertEquals("no hay padre", cli.cd(".."));
    cli.mkdir(dir1);
    dir1.getSource().add(file1);
    cli.cd(dir1);
    Element.Directory child =
        new Element.Directory("juan", "C:juani/juan", new ArrayList<>(), cli.root, cli.root);
    cli.root.getSource().add(child);
    cli.cd(child);
    assertEquals("moved to directory juani", cli.cd(".."));
  }

  @Test
  public void testCdStringInvalid() {
    assertEquals("arg no valido", cli.cd("invalid"));
  }

  @Test
  public void testPwd() {
    assertEquals("C:juani", cli.pwd());
  }

  @Test
  public void testTouch() {
    String res1 = cli.touch(file1);
    assertEquals("hola.txt file created", res1);
    String res2 = cli.touch(file1);
    assertEquals("existe ya", res2);
  }

  @Test
  public void testRm() {
    cli.touch(file1);
    assertEquals("hola.txt removed", cli.rm(file1));
    assertEquals("no eliminado", cli.rm(file1));
  }

  @Test
  public void testRmRecursive() {
    cli.mkdir(dir1);
    dir1.getSource().add(file1);
    cli.rmRecursive(dir1);

    assertFalse(cli.ls().contains(dir1));
  }

  @Test
  public void testRmRecursiveDeep() {
    Element.Directory deep =
        new Element.Directory("hola", "C:juani/hola", new ArrayList<>(), dir2, cli.root);
    dir2.getSource().add(deep);
    dir1.getSource().add(dir2);
    cli.mkdir(dir1);
    cli.rmRecursive(dir1);

    assertFalse(cli.ls().contains(dir1));
  }

  @Test
  void testRmRecursiveWhenMarkIsDirectoryToRemove() {
    CLI cli1 = new CLI("root");
    Element.Directory downloads =
        new Element.Directory(
            "downloads", "/root/downloads", new ArrayList<>(), cli1.root, cli1.root);
    cli1.mkdir(downloads);
    cli1.mark = downloads;

    String result = cli1.rmRecursive(downloads);
    assertEquals("downloads removed", result);

    assertEquals(cli1.root, cli1.mark);
  }
}
