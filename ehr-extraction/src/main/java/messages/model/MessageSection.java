package messages.model;

public class MessageSection {

	private String header="";
	
	private final StringBuilder content=new StringBuilder();

	public MessageSection() {
		
	}
	
	public void addContent(String string) {
		this.content.append(string);
	}
	
	public String getContent() {
		return content.toString();
	}
	

	public void setHeader(String header) {
		this.header = header;
	}
	
	public String getHeader() {
		return header;
	}
}