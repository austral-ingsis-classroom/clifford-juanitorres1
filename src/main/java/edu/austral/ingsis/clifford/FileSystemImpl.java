package edu.austral.ingsis.clifford;

public class FileSystemImpl implements FileSystem {
  private Cursor cursor;

  public FileSystemImpl(String user) {
    this.cursor = new Cursor(user);
  }

  @Override
  public String execute(Command command) {
    Command.Result result = command.execute();
    this.cursor = result.cursor();
    return result.output();
  }

  public Cursor getCli() {
    return cursor;
  }
}
