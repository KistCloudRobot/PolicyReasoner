package kr.ac.uos.ai.robot.intelligent.policyReasoner.action;


import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument.PolicyUpdateArgument;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.policy.PolicyHandler;

public class PolicyUpdateAction implements ActionBody{
	private PolicyHandler handler; 
	

	public PolicyUpdateAction(PolicyHandler handler) {
		// TODO Auto-generated constructor stub
		
		this.handler = handler;
	}
	@Override
	public Object execute(Object o) {
		PolicyUpdateArgument argument = (PolicyUpdateArgument) o;
		
		// TODO Auto-generated method stub
		return null;
	}
}
