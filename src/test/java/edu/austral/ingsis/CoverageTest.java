package edu.austral.ingsis;

import static org.junit.jupiter.api.Assertions.*;

import edu.austral.ingsis.clifford.*;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CoverageTest {

  private Element.Directory createRoot() {
    return new Element.Directory("root", "", List.of(), null, null);
  }

  @Test
  void touch_CreatesNewFile() {
    Element.Directory root = createRoot();
    Cursor cursor = new Cursor("root");

    Command touch = new Touch(cursor, "file.txt");
    Command.Result result = touch.execute();

    assertEquals("'file.txt' file created", result.output());
    assertTrue(result.cursor().getMark().findElement("file.txt").isPresent());
  }

  @Test
  void touch_FileAlreadyExists() {
    Element.Directory root = createRoot();
    Element.File existing = root.createFile("file.txt");
    root = root.addElement(existing);
    Cursor cursor = new Cursor("root").NewCli(root);

    Command touch = new Touch(cursor, "file.txt");
    Command.Result result = touch.execute();

    assertEquals("'file.txt' file already exists", result.output());
    assertSame(cursor.getMark(), result.cursor().getMark());
  }

  @Test
  void mkdir_CreatesDirectory() {
    Cursor cursor = new Cursor("root");

    Command mkdir = new Mkdir(cursor, "docs");
    Command.Result result = mkdir.execute();

    assertEquals("'docs' directory created", result.output());
    assertTrue(result.cursor().getMark().findDirectory("docs").isPresent());
  }

  @Test
  void mkdir_DirectoryAlreadyExists() {
    Element.Directory root = createRoot();
    Element.Directory docs = root.createDirectory("docs");
    root = root.addElement(docs);
    Cursor cursor = new Cursor("root").NewCli(root);

    Command mkdir = new Mkdir(cursor, "docs");
    Command.Result result = mkdir.execute();

    assertEquals("'docs' directory already exists", result.output());
    assertSame(cursor.getMark(), result.cursor().getMark());
  }

  @Test
  void rm_RemovesFileSuccessfully() {
    Element.Directory root = createRoot();
    Element.File file = root.createFile("file.txt");
    root = root.addElement(file);
    Cursor cursor = new Cursor("root").NewCli(root);

    Command rm = new Rm(cursor, "file.txt", false);
    Command.Result result = rm.execute();

    assertEquals("'file.txt' removed", result.output());
    assertTrue(result.cursor().getMark().findElement("file.txt").isEmpty());
  }

  @Test
  void rm_FileDoesNotExist() {
    Cursor cursor = new Cursor("root");

    Command rm = new Rm(cursor, "notfound.txt", false);
    Command.Result result = rm.execute();

    assertEquals("'notfound.txt' does not exist", result.output());
    assertSame(cursor.getMark(), result.cursor().getMark());
  }

  @Test
  void rm_TriesToRemoveDirectory_Fails() {
    Element.Directory root = createRoot();
    Element.Directory folder = root.createDirectory("folder");
    root = root.addElement(folder);
    Cursor cursor = new Cursor("root").NewCli(root);

    Command rm = new Rm(cursor, "folder", false);
    Command.Result result = rm.execute();

    assertEquals("cannot remove 'folder', is a directory", result.output());
    assertTrue(result.cursor().getMark().findDirectory("folder").isPresent());
  }

  @Test
  void rmRecursive_ElementDoesNotExist() {
    Cursor cursor = new Cursor("root");

    Command rmRecursive = new Rm(cursor, "Dir", true);
    Command.Result result = rmRecursive.execute();

    assertEquals("'Dir' does not exist", result.output());
    assertSame(cursor.getMark(), result.cursor().getMark());
  }

  @Test
  void rmRecursive_RemovesEmptyDirectory() {
    Element.Directory root = new Element.Directory("root", "", List.of(), null, null);
    Element.Directory emptyDir = root.createDirectory("toDelete");
    root = root.addElement(emptyDir);

    Cursor cursor = new Cursor("root").NewCli(root);

    Command rmRecursive = new Rm(cursor, "toDelete", true);
    Command.Result result = rmRecursive.execute();

    assertEquals("'toDelete' removed", result.output());
    assertTrue(result.cursor().getMark().findElement("toDelete").isEmpty());
  }

  @Test
  void rmRecursive_RemovesDirectoryWithChildren() {
    Element.Directory root = new Element.Directory("root", "", List.of(), null, null);
    Element.Directory childDir = root.createDirectory("child");
    Element.File file = root.createFile("file.txt");

    Element.Directory parentDir =
        root.createDirectory("parent").addElement(childDir).addElement(file);
    root = root.addElement(parentDir);
    Cursor cursor = new Cursor("root").NewCli(root);
    Command rmRecursive = new Rm(cursor, "parent", true);
    Command.Result result = rmRecursive.execute();
    assertEquals("'parent' removed", result.output());
    assertTrue(result.cursor().getMark().findElement("parent").isEmpty());
  }
}
