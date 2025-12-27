package backend;

import types.LogCategory;
import types.LogLevel;

public interface Logger {
	void log(LogLevel level, LogCategory category ,String message);

}
