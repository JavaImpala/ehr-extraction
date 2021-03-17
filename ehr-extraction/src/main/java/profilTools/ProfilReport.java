package profilTools;

import util.Closable;

public class ProfilReport implements Closable{
	private int reportNumber=-1;
	
	private String stringDate="01.01.1970";
	private String writtenStringDate="01.01.1970";
	private String daytimeReportType="Ukjent";
	
	private String statusChanged="Uendret";
	
	private boolean priority=false;
	
	private String author="Ukjent";
	
	private StringBuilder contentBuilder=new StringBuilder();
	private String content="";

	private boolean closed=false;
	
	public int getReportNumber() {
		return reportNumber;
	}

	public void setReportNumber(int reportNumber) {
		this.reportNumber = reportNumber;
	}

	public String getStringDate() {
		return stringDate;
	}

	public void setStringDate(String stringDate) {
		this.stringDate = stringDate;
	}

	public String getWrittenStringDate() {
		return writtenStringDate;
	}

	public void setWrittenStringDate(String writtenStringDate) {
		this.writtenStringDate = writtenStringDate;
	}

	public String getDaytimeReportType() {
		return daytimeReportType;
	}

	public void setDaytimeReportType(String daytimeReportType) {
		this.daytimeReportType = daytimeReportType;
	}

	public String getStatusChanged() {
		return statusChanged;
	}

	public void setStatusChanged(String statusChanged) {
		this.statusChanged = statusChanged;
	}

	public boolean isPriority() {
		return priority;
	}

	public void setPriority(boolean priority) {
		this.priority = priority;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void addContent(String string) {
		this.contentBuilder.append(string);
	}
	
	public boolean isClosed(){
		return closed;
	}
	public void finalize() {
		this.closed=true;
		this.content=contentBuilder.toString();
	}

	@Override
	public String toString() {
		return "ProfilReport [reportNumber=" + reportNumber + ", stringDate=" + stringDate + ", writtenStringDate="
				+ writtenStringDate + ", daytimeReportType=" + daytimeReportType + ", statusChanged=" + statusChanged
				+ ", priority=" + priority + ", author=" + author + ", contentBuilder=" + contentBuilder + ", content="
				+ content + "]";
	}
}
