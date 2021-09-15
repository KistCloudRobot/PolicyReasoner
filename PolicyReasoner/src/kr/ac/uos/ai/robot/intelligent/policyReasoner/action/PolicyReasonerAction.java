package kr.ac.uos.ai.robot.intelligent.policyReasoner.action;


import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.PolicyReasoner;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument.ContextArgument;
import uos.ai.jam.Interpreter;

public class PolicyReasonerAction {
	
	private AgentAction assertContextAction;
	private AgentAction policyUpdateAction;
	private AgentAction serviceAppendAction;
	private AgentAction policyAppendAction;
	private AgentAction retractContextAction;
	private AgentAction serviceFinishAction;
	private AgentAction GoalAppendAction;
	private AgentAction GoalPostAction;
	private AgentAction GoalUnpostAction;
	
	private PolicyReasoner policyReasoner;
	private Interpreter interpreter;
	private LoggerManager loggerManager;

	public PolicyReasonerAction(PolicyReasoner reasoner, Interpreter interpreter, LoggerManager loggerManager) {
		this.policyReasoner = reasoner;
		this.interpreter = interpreter;
		this.loggerManager = loggerManager;
		
		init();
	}
	
	public void init() {
		assertContextAction = new AgentAction("assertContext", new AssertContextAction(policyReasoner.getGlMessageManager()));
		retractContextAction = new AgentAction("retractContext", new RetractContextAction(policyReasoner.getGlMessageManager()));
		policyAppendAction = new AgentAction("policyAppend", new PolicyAppendAction(policyReasoner.getPolicyHandler()));
		policyUpdateAction = new AgentAction("policyUpdate", new PolicyUpdateAction(policyReasoner.getPolicyHandler()));

		loggerManager.registerAction(assertContextAction,LogTiming.Prior);
		loggerManager.registerAction(retractContextAction,LogTiming.Prior);
		loggerManager.registerAction(policyAppendAction,LogTiming.Prior);
		loggerManager.registerAction(policyUpdateAction,LogTiming.Prior);
		
		assertContextAction.changeAction(true);
		retractContextAction.changeAction(true);
		policyAppendAction.changeAction(true);
		policyUpdateAction.changeAction(true);
	}

	
	public void action(String str, Object o) {
		System.out.println(str);
		loggerManager.getAction(str).execute(o);
	}
	
	public ContextArgument generateContextArgument(String glString) {
		
		ContextArgument argument = new ContextArgument();
		
		GeneralizedList gl = null;
		GeneralizedList glContext = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
			glContext = gl.getExpression(0).asGeneralizedList();
			argument.setName(glContext.getName());
			argument.setGlContext(glContext);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return argument;
	}
}
