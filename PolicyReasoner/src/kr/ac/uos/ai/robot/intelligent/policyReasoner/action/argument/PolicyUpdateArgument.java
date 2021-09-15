package kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument;

import org.json.simple.JSONObject;

public class PolicyUpdateArgument {

	private String name;
	private Float value;
	private String robot;
	
	public PolicyUpdateArgument() {
		name = "";
		value = 0f;
		robot = "";
		// TODO Auto-generated constructor stub
	}
	public String getRobot() {
		return robot;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRobot(String robot) {
		this.robot = robot;
	}
	
	public Float getValue() {
		return value;
	}
	public void setValue(Float newValue) {
		this.value = newValue;
	}
	@Override
	public String toString() {
		JSONObject json = new JSONObject();
		
		json.put("Name", name);
		json.put("Robot", robot);
		json.put("Value", value);
		
		return json.toJSONString();
	}
}
