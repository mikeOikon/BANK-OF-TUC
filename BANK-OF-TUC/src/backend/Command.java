package backend;

public interface Command {
	void execute();
	void undo();
}
