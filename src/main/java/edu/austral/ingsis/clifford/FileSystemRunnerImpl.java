package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public class FileSystemRunnerImpl implements FileSystemRunner {
  private Cursor cursor;
  private FileSystemImpl fileSystem;

  public FileSystemRunnerImpl() {
    this.cursor = new Cursor("/");
    this.fileSystem = new FileSystemImpl("/");
  }

  @Override
  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();

    for (String commandString : commands) {
      Command command = parseCommand(commandString);
      String result = fileSystem.execute(command);
      results.add(result);
      this.cursor = fileSystem.getCli();
    }

    return results;
  }

  private Command parseCommand(String commandString) {
    String[] parts = commandString.split(" ");
    String commandName = parts[0];

    switch (commandName) {
      case "ls":
        if (parts.length > 1 && parts[1].startsWith("--ord=")) {
          String orderParam = parts[1].substring(6);
          Filter filter = orderParam.equals("asc") ? Filter.ASC : Filter.DESC;
          return new Ls(cursor, filter);
        } else {
          return new Ls(cursor);
        }

      case "mkdir":
        if (parts.length > 1) {
          return new Mkdir(cursor, parts[1]);
        }
        break;

      case "cd":
        if (parts.length > 1) {
          return new Cd(cursor, parts[1]);
        }
        break;

      case "pwd":
        return new Pwd(cursor);

      case "touch":
        if (parts.length > 1) {
          return new Touch(cursor, parts[1]);
        }
        break;

      case "rm":
        if (parts.length > 2 && parts[1].equals("--recursive")) {
          return new Rm(cursor, parts[2], true);
        } else if (parts.length > 1) {
          return new Rm(cursor, parts[1], false);
        }
        break;
    }
    return () -> new Command.Result("Command not recognized", cursor);
  }
}
