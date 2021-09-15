package kr.ac.uos.ai.robot.intelligent.policyReasoner.action;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument.ContextArgument;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.message.GLMessageManager;

public class AssertContextAction implements ActionBody{
	private GLMessageManager glMessageManager;
	
	public AssertContextAction(GLMessageManager glMessageManager) {
		this.glMessageManager = glMessageManager;
		// TODO Auto-generated constructor stub
	}
	@Override
	public Object execute(Object o) {
		ContextArgument argument = (ContextArgument) o;
		
		System.out.println("assert context");
		glMessageManager.assertContext(argument.getGlContext());
		
		return null;
	}
}
