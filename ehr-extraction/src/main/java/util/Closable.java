package util;

public interface Closable {
	public boolean isClosed();
	public void settle();
}
