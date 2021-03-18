package reports;

import java.util.ArrayList;
import java.util.List;

public class CarePlan {
	private String planCategory="";
	private String planArea="";
	private String action="";
	private String startDate="";
	private String endDate="";
	
	private String author;
	
	private List<CarePlanDescription> descriptions=new ArrayList<>();

	public String getPlanCategory() {
		return planCategory;
	}

	public void setPlanCategory(String planCategory) {
		this.planCategory = planCategory;
	}

	public String getPlanArea() {
		return planArea;
	}

	public void setPlanArea(String planArea) {
		this.planArea = planArea;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<CarePlanDescription> getDescriptions() {
		return descriptions;
	}

	public void addDescription(CarePlanDescription description) {
		this.descriptions.add(description);
	}	 
}
