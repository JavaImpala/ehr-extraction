package util.matcher;

import java.util.function.Consumer;

public class InterceptMatcher implements Matcher{

	private final Matcher wrapped;
	private final Consumer<String> interceptor;
	
	private InterceptMatcher(Matcher wrapped, Consumer<String> interceptor) {
		this.wrapped = wrapped;
		this.interceptor = interceptor;
	}
	
	public static InterceptMatcher create(Matcher wrapped, Consumer<String> interceptor) {
		return new InterceptMatcher(wrapped, interceptor);
	}

	@Override
	public void readLine(String line) {
		wrapped.readLine(line);
		interceptor.accept(line);
	}

	@Override
	public MatchingState getState() {
		return wrapped.getState();
	}

}
