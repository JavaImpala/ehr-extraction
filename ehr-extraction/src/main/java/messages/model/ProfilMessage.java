package messages.model;

import java.util.ArrayList;
import java.util.List;

public class ProfilMessage {
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSent() {
		return sent;
	}

	public void setSent(String sent) {
		this.sent = sent;
	}

	public List<MessageSection> getSection() {
		return section;
	}

	private String from;
	private List<String> to=new ArrayList<>();
	
	private String sent;
	
	private final List<MessageSection> section=new ArrayList<>();
	
	
	
}
