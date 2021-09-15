package test;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;

public class CMTest extends ArbiAgent {
	private static final String brokerURI 				= "tcp://127.0.0.1:61616";
	private static final String	agentURIPrefix			= "agent://";
	private static final String	dsURIPrefix				= "ds://";
	private static final String PL_URI					= "agent://www.arbi.com/local/PolicyReasoner";
	private static final String TM_URI					= "agent://www.arbi.com/taskManager";
	private static final String CM_URI				 	= "www.arbi.com/contextManager";
	private static final String KM_URI 					= "agent://www.arbi.com/knowledgeManager";
	static DataSource dataSource = new DataSource() ;

	public CMTest() {
		
	}
	
	@Override
	public void onStart() {
		dataSource.connect(brokerURI, dsURIPrefix+CM_URI, 2);
	}
	
	@Override
	public void send(String receiver, String data) {
		// TODO Auto-generated method stub
		super.send(receiver, data);
	}

	public static void main(String[] args) throws InterruptedException {
		ArbiAgent cm = new CMTest();	
		ArbiAgentExecutor.execute(brokerURI, agentURIPrefix+CM_URI, cm, 2);
		

		System.out.println("CMTest start");
		cm.send(PL_URI, "(context (batteryNeed \"AMR_LIFT1\" \"move\" 12))");
		//dataSource.assertFact("(context (batteryNeed \"AMR_LIFT1\" \"move\" 12))");
		Thread.sleep(1000);
		System.out.println("1");
		cm.send(PL_URI, "(context (collidable \"AMR_LIFT1\" \"move\" 1.0)))");
		//dataSource.assertFact("(context (collidable \"AMR_LIFT1\" \"move\" 1.0)))");
		Thread.sleep(1000);
		System.out.println("2");
		cm.send(PL_URI, "(context (batteryRemained \"AMR_LIFT1\" 50)))");
		//dataSource.assertFact("(context (batteryRemained \"AMR_LIFT1\" 50)))");
		/*
		cm.send(TR_URI, "(CurrentSchedule \"study\")");
		cm.send(TR_URI, "(StudyMethod \"audio\")");
		Thread.sleep(1000);
		cm.send(TR_URI, "(StudyMethod \"video\")");
		Thread.sleep(1000);		
		cm.send(TR_URI, "(StudyTool \"tablet\")");
		Thread.sleep(1000);
		cm.send(TR_URI, "(retract (StudyMethod \"audio\"))");
		cm.send(TR_URI, "(StudentRespnseTime \"3\")");
		cm.send(TR_URI, "(StudentHeartbeat \"50\")");
		Thread.sleep(1000);

		cm.send(TR_URI, "(StudentEmotion \"happy\")");
		*/
	}
	
}
