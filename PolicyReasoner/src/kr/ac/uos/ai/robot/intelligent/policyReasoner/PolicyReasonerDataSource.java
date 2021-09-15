package kr.ac.uos.ai.robot.intelligent.policyReasoner;

import kr.ac.uos.ai.arbi.ltm.DataSource;

public class PolicyReasonerDataSource extends DataSource{

	private PolicyReasoner polictyReasoner;
	
	public PolicyReasonerDataSource(PolicyReasoner polictyReasoner) {
		// TODO Auto-generated constructor stub
		this.polictyReasoner = polictyReasoner;
	}
	
	@Override
	public void onNotify(String content) {

		System.out.println("Notified!" + content);
		polictyReasoner.onNotify("LTM", content);
	
	}
}
