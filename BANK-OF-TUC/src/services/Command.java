package services;

public interface Command {
	void execute();
	void undo();
}
