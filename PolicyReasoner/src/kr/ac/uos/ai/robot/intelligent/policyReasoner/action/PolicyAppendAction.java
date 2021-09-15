package kr.ac.uos.ai.robot.intelligent.policyReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument.PolicyAppendArgument;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.policy.PolicyHandler;

public class PolicyAppendAction implements ActionBody {

	private PolicyHandler handler;
	


	public PolicyAppendAction(PolicyHandler handler) {

		this.handler = handler;
	}


	
	@Override
	public Object execute(Object o) {
		
		PolicyAppendArgument argument = (PolicyAppendArgument) o;
		System.out.println("generate policy");
		
		handler.generatePolicy(argument);
		// TODO Auto-generated method stub
		return null;
	}
}
