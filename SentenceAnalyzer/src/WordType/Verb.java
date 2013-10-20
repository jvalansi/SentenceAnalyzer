package WordType;

public class Verb extends WordType {

	private String baseForm = "";
	
	public Verb() {
	}

	public void setBaseForm(String s) {
		baseForm = s;
	}
	public String getBaseForm() {
		return baseForm;
	}
}
