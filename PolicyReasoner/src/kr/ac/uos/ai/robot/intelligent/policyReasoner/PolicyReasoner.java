package kr.ac.uos.ai.robot.intelligent.policyReasoner;


import java.util.ArrayList;
import java.io.*;
import java.lang.*;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.PolicyReasonerAction;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.action.argument.ContextArgument;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.message.GLMessageManager;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.message.JsonMessageManager;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.message.RecievedMessage;
import kr.ac.uos.ai.robot.intelligent.policyReasoner.policy.PolicyHandler;
import uos.ai.jam.Interpreter;
import uos.ai.jam.JAM;

public class PolicyReasoner extends ArbiAgent {

	public static String ENV_JMS_BROKER;
	public static String ENV_AGENT_NAME;
	public static String ENV_ROBOT_NAME;
	public static final String ARBI_PREFIX = "www.arbi.com/";
	
	private static String brokerURI						= "tcp://127.0.0.1:61616";
	private static String myURI							= "www.arbi.com/Local/PolicyReasoner";
	private static int brokerType 						= 2;
	private static String TM_URI						= "www.arbi.com/Local/TaskManager";

	
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String	POLICYREASONER			= "www.arbi.com/local/PolicyReasoner";
	
	public static final String LOCAL_CONTEXTMANAGER 	= "agent://www.arbi.com/Local/ContextManager";
	public static final String AMR_LIFT1_CONTEXTMANAGER = "agent://www.arbi.com/Lift1/ContextManager";
	public static final String AMR_LIFT2_CONTEXTMANAGER = "agent://www.arbi.com/Lift2/ContextManager";
	public static final String AMR_TOW1_CONTEXTMANAGER 	= "agent://www.arbi.com/Tow1/ContextManager";
	public static final String AMR_TOW2_CONTEXTMANAGER 	= "agent://www.arbi.com/Tow2/ContextManager";
	
	private Interpreter									interpreter;
	private GLMessageManager							glMessageManager;
	private BlockingQueue<RecievedMessage>				messageQueue;
	private DataSource									ds;
	private PlanLoader 									planLoader;
	private PolicyHandler								policyHandler;
	private LoggerManager 								loggerManager;
	private JsonMessageManager							jsonMessageManager;
	
	private PolicyReasonerAction						policyReasonerAction;
	

	public PolicyReasoner() {

		initAddress();
		config();
		interpreter = JAM.parse(new String[] {"plan/boot.jam"} );
		
		ds = new PolicyReasonerDataSource(this);
		
		messageQueue= new LinkedBlockingQueue<RecievedMessage>();
		glMessageManager = new GLMessageManager(interpreter);
		planLoader = new PlanLoader(interpreter);
		policyHandler = new PolicyHandler(this,interpreter);
		jsonMessageManager = new JsonMessageManager(policyHandler);

		
		ArbiAgentExecutor.execute(ENV_JMS_BROKER, agentURIPrefix+POLICYREASONER, this, brokerType);

		loggerManager = LoggerManager.getInstance();
		policyReasonerAction = new PolicyReasonerAction(this, interpreter, loggerManager);

		init();
		
		//test();
	}


	public void initAddress() {
		ENV_JMS_BROKER = System.getenv("JMS_BROKER");
	}
	
	public void test() { // for context manager bridge
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("policy reasoner boot complete!!!");	
		String message;
		String result;
		message = "(context (batteryRemained \"http://www.arbi.com/ontologies/arbi.owl#amr_lift01\" $B))";
		result = query(LOCAL_CONTEXTMANAGER, message);
		RecievedMessage msg = new RecievedMessage(LOCAL_CONTEXTMANAGER, result);

		try {
			messageQueue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		message = "(context (batteryNeed \"http://www.arbi.com/ontologies/arbi.owl#amr_lift01\" \"move\" $B))";
		result = query(LOCAL_CONTEXTMANAGER, message);

		msg = new RecievedMessage(LOCAL_CONTEXTMANAGER, result);

		try {
			messageQueue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		message = "(context (collidable \"http://www.arbi.com/ontologies/arbi.owl#amr_lift01\" \"move\" $C))";
		result = query(LOCAL_CONTEXTMANAGER, message);
		
		msg = new RecievedMessage(LOCAL_CONTEXTMANAGER, result);
		try {
			messageQueue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		


	}
	
	private void config() {
		/*
		try {
			File file = new File("configuration/TaskReasonerConfiguration.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(file);
			XPathFactory xPathFactory = XPathFactory.newInstance();
			XPath xPath = xPathFactory.newXPath();
			
			XPathExpression _brokerURI = xPath.compile("//ServerURL");
			Node n = (Node) _brokerURI.evaluate(doc, XPathConstants.NODE);
			brokerURI = n.getTextContent();
			
			XPathExpression _myURI = xPath.compile("//AgentName");
			n = (Node) _myURI.evaluate(doc, XPathConstants.NODE);
			myURI = n.getTextContent();
			
			XPathExpression _brokerType = xPath.compile("//BrokerType");
			n = (Node) _brokerType.evaluate(doc, XPathConstants.NODE);
			if (n.getTextContent().equals("ZeroMQ")) {
				brokerType = 2;
			} else if (n.getTextContent().equals("Apollo")) {
				brokerType = 1;
			}
			
			XPathExpression _TM_URI = xPath.compile("//TaskManagerName");
			n = (Node) _TM_URI.evaluate(doc, XPathConstants.NODE);
			TM_URI = n.getTextContent();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	private void init() {
		
		glMessageManager.assertFact("GLMessageManager", glMessageManager);
		glMessageManager.assertFact("PolicyHandler", policyHandler);
		glMessageManager.assertFact("PolicyReasoner", this);
		glMessageManager.assertFact("JsonMessageManager", jsonMessageManager);
		glMessageManager.assertFact("LoggerManager", policyReasonerAction);
		
		glMessageManager.assertFact("PolicyRelation", "Efficiency", "batteryNeed");
		glMessageManager.assertFact("PolicyRelation", "Efficiency", "predictTaskTime");
		glMessageManager.assertFact("PolicyRelation", "Safety", "batteryRemained");
		glMessageManager.assertFact("PolicyRelation", "Safety", "batteryNeed");
		glMessageManager.assertFact("PolicyRelation", "Safety", "collidable");
		
		
		Thread t = new Thread() {
			public void run() {
				interpreter.run();
			}
		};
		t.start();
	}
		
	@Override
	public void onStart() {
		ds.connect(ENV_JMS_BROKER, dsURIPrefix+myURI, 2);
		//goal and context is wrapped
				
		String subscriveContext = "(rule (fact (context $context)) --> (notify (context $context)))";
		ds.subscribe(subscriveContext);
		
		ds.assertFact("(PolicyValue \"Safety\" \"AMR_LIFT1\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Safety\" \"AMR_LIFT2\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Safety\" \"AMR_TOW1\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Safety\" \"AMR_TOW2\" \"null\" 0)");
		
		ds.assertFact("(PolicyValue \"Efficiency\" \"AMR_LIFT1\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Efficiency\" \"AMR_LIFT2\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Efficiency\" \"AMR_TOW1\" \"null\" 0)");
		ds.assertFact("(PolicyValue \"Efficiency\" \"AMR_TOW2\" \"null\" 0)");

		send(AMR_LIFT1_CONTEXTMANAGER, "(assert (PolicyValue \"Efficiency\" \"AMR_LIFT1\" $task 0))");
		send(AMR_LIFT2_CONTEXTMANAGER, "(assert (PolicyValue \"Efficiency\" \"AMR_LIFT2\" $task 0))");
		send(AMR_TOW1_CONTEXTMANAGER, "(assert (PolicyValue \"Efficiency\" \"AMR_TOW1\" $task 0))");
		send(AMR_TOW2_CONTEXTMANAGER, "(assert (PolicyValue \"Efficiency\" \"AMR_TOW2\" $task 0))");
		
		send(AMR_LIFT1_CONTEXTMANAGER, "(assert (PolicyValue \"Safety\" \"AMR_LIFT1\" $task 0))");
		send(AMR_LIFT2_CONTEXTMANAGER, "(assert (PolicyValue \"Safety\" \"AMR_LIFT2\" $task 0))");
		send(AMR_TOW1_CONTEXTMANAGER, "(assert (PolicyValue \"Safety\" \"AMR_TOW1\" $task 0))");
		send(AMR_TOW2_CONTEXTMANAGER, "(assert (PolicyValue \"Safety\" \"AMR_TOW2\" $task 0))");
	}
	
	@Override
	public void onNotify(String sender, String notification) {
		System.out.println("Notyfied from " + sender + ". Message is " + notification);
		RecievedMessage msg = new RecievedMessage(sender, notification);
		try {
			messageQueue.put(msg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	@Override
	public void onData(String sender, String data) {
		try {
			System.out.println("data  = " + data);
			RecievedMessage message = new RecievedMessage(sender, data);

			messageQueue.put(message);
		} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ArbiAgent agent = new PolicyReasoner();
	}
	
	public boolean dequeueMessage() {
		
		if (messageQueue.isEmpty()) {
			return false;
		} else {
			try {
				RecievedMessage message = messageQueue.take();
				
				System.out.println("queue");
				glMessageManager.assertFact("RecievedMessage", message.getSender(), message.getMessage());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return true;
		}
	}
		
	public void updatePolicy(String policyName, Object robot, Object task, Object value) {
		StringBuilder sb = new StringBuilder();
		sb.append("(update (PolicyValue ");
		sb.append("\"" + policyName + "\" ");
		
		
		if(robot.toString().equals("AMR_LIFT1")) {
			sb.append("\"AMR_LIFT1\" $t $v) (PolicyValue " + "\"" + policyName +  "\" \"AMR_LIFT1\" \""+ task.toString() + "\" " +Float.parseFloat(value.toString()) +"))");
			send(AMR_LIFT1_CONTEXTMANAGER, sb.toString());
			ds.updateFact(sb.toString());
			
		} else if(robot.toString().equals("AMR_LIFT2")) {
			sb.append("\"AMR_LIFT2\" $t $v) (PolicyValue " + "\"" + policyName +  "\" \"AMR_LIFT2\" \""+ task.toString() + "\" " +Float.parseFloat(value.toString()) +"))");
			send(AMR_LIFT2_CONTEXTMANAGER, sb.toString());
			ds.updateFact(sb.toString());
			
		} else if(robot.toString().equals("AMR_TOW1")) {
			sb.append("\"AMR_TOW1\" $t $v) (PolicyValue " + "\"" + policyName +  "\" \"AMR_TOW1\" \""+ task.toString() + "\" " +Float.parseFloat(value.toString()) +"))");
			send(AMR_TOW1_CONTEXTMANAGER, sb.toString());
			ds.updateFact(sb.toString());
			
		} else if(robot.toString().equals("AMR_TOW2")) {
			sb.append("\"AMR_TOW2\" $t $v) (PolicyValue " + "\"" + policyName +  "\" \"AMR_TOW2\" \""+ task.toString() + "\" " +Float.parseFloat(value.toString()) +"))");
			send(AMR_TOW2_CONTEXTMANAGER, sb.toString());
			ds.updateFact(sb.toString());
			
		}
		
	}
	public boolean sendToTM(String type, String name, Object... args) {
		//System.out.println("send to tm : " + type + ", " +name);
		this.send(agentURIPrefix + TM_URI, glMessageManager.makeGLMessage(type, name, args));
		
		return true;
	}
	
	public void parsePlan(String string) {
		planLoader.parsePlan(string);
	}
	
	public void loadPlan(String string ) {
		planLoader.loadPlan(string);
	}
	public void loadPlanPackage(String string) {
		planLoader.loadPlanPackage(string);
	}
	
	public void assertFact(String name, Object... args) {
		glMessageManager.assertFact(name, args);
	}

	public GLMessageManager getGlMessageManager() {
		return glMessageManager;
	}
	
	public PolicyHandler getPolicyHandler() {
		return policyHandler;
	}
	
	
	public void receivedPolicyMessage(String str) {
		jsonMessageManager.updateLMPolicyValue(str);
	}
	
	public void sendToLM(ContextArgument argument) {
		System.out.println("not implement");
	}
	
	public ArrayList<Float> calcRank(String policyName_, String task_){		
		String task= "";
		String policyName= "";
		task= task_.toString();
		policyName = policyName_.toString();
	    //System.out.println(policyName +", "+ task);
		ArrayList<Float> arr = new ArrayList<Float>(); 
		if (policyName.equals("Safety")) {
			if (task.equals("move")) {
				arr.add(0.5f);
				arr.add(0.5f);
			}
			else {
				arr.add(0.5f);
				arr.add(0.5f);
			}
		}
		else if (policyName.equals("Efficiency")) {
			if (task.equals("move")) {
				arr.add(0.5f);
				arr.add(0.5f);
			}
			else {
				arr.add(0.5f);
				arr.add(0.5f);
			}
		}
		return arr;
	}
	
	public String calcGrade(Object policyName_, Object value_) {
		String value = "";
		String policyName= "";
		policyName = policyName_.toString();
		String str = value_.toString();
		//System.out.println(policyName + value_);
		double d = Double.valueOf(str).doubleValue();
		if (policyName.equals("Safety")) {
			if (d >= 0.8)
				value= "5";
			else if (d >=0.6)
				value= "4";
			else if (d >=0.4)
				value= "3";
			else if (d >=0.2)
				value= "2";
			else if (d >=0.0)
				value= "1";
		}
		else if (policyName.equals("Efficiency")) {
			if (d >= 0.8)
				value= "5";
			else if (d >=0.6)
				value= "4";
			else if (d >=0.4)
				value= "3";
			else if (d >=0.2)
				value= "2";
			else if (d >=0.0)
				value= "1";
		}		
		return value;
	}	
}
