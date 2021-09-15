package kr.ac.uos.ai.robot.intelligent.policyReasoner.message;


import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;
import kr.ac.uos.ai.arbi.model.Value;

import kr.ac.uos.ai.arbi.model.Expression;
import uos.ai.jam.Interpreter;

public class GLMessageManager {

	private Interpreter interpreter;
	
	public GLMessageManager(Interpreter interpreter) {
		this.interpreter = interpreter;
		init();
	}
	
	private void init() {
	}
	
	public void assertContext(GeneralizedList glContext) {
		String glName;
		
		glName = glContext.getName();
		if (glContext.getExpressionsSize() != 0) {
				Object[] objectList = new Object[glContext.getExpressionsSize()];	
				for (int i = 0; i < glContext.getExpressionsSize(); i ++) {
				Expression o = glContext.getExpression(i);
				if(o.isGeneralizedList() == true) {
					String str = o.toString();
					objectList[i] = str;
				} else if(o.asValue().getType() == Value.Type.STRING){
					String str =  removeQuotationMarks(o.toString());
					objectList[i] = str;
				} else if(o.asValue().getType() == Value.Type.FLOAT) {
					Float f = Float.parseFloat(o.toString());
					objectList[i] = f;
				} else if(o.asValue().getType() == Value.Type.INT) {
					int j = Integer.parseInt(o.toString());
					objectList[i] = j;
				}
			}
			interpreter.getWorldModel().assertFact(glName, objectList);
			System.out.println("Assert context : " + glName);
		}else {
			interpreter.getWorldModel().assertFact(glName);
			System.out.println("Assert context : " + glName);
		}
	}
	
	public void assertFact(String name, Object... args) {
		
		interpreter.getWorldModel().assertFact(name, args);
		//System.out.println("Assert relation : " +  name);

	}
	/*
	public void retractFact(String glString) {
		GeneralizedList gl = null;
		
		try {
			gl = GLFactory.newGLFromGLString(glString);
			String name = gl.getName();
			if (gl.getExpressionsSize() != 0 ) {
				List<uos.ai.jam.expression.Expression> expressionList = new ArrayList<uos.ai.jam.expression.Expression>();	
				
				for (int i = 0; i < gl.getExpressionsSize(); i ++) {
				Expression o = gl.getExpression(i);
				Value v = null;
					if(o.isGeneralizedList() == true) {
						String str = o.toString();
					} else if(o.asValue().getType() == Value.Type.STRING){
						String str =  removeQuotationMarks(o.toString());
					} else if(o.asValue().getType() == Value.Type.FLOAT) {
						Float f = Float.parseFloat(o.toString());
					} else if(o.asValue().getType() == Value.Type.INT) {
						int j = Integer.parseInt(o.toString());
					}
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	public String makeGLMessage(String type, String name, Object... args) {
		StringBuilder msgBuilder = new StringBuilder();
		if (type.equals("postGoal")) {
			msgBuilder.append("(PostGoal (" + name);

		} else if(type.equals("policy")) {
			msgBuilder.append("(Policy (" + name);
		} else if(type.equals("unpostGoal")) {
			msgBuilder.append("(UnpostGoal (" + name);

		}
		
		if(args!=null) {
			for (Object o : args) {
				String str = o.toString();
				msgBuilder.append(" \"" + str + "\"");
			}
		}
		msgBuilder.append("))");
		System.out.println(msgBuilder.toString());
		return msgBuilder.toString();
	}
	
	public String removeQuotationMarks(String input){
//		System.out.println("quotation removed : " + input);
		if(input.startsWith("\"")){
			input = input.substring(1,input.length()-1);
		}
//		System.out.println("quotation removed after: " + input);
		
		return input;
	}
	
	public String removeDoubleQuotationMarks(String input){
//		System.out.println("quotation removed : " + input);
	
		String output = input.replace("\"", "");
//		System.out.println("quotation removed after: " + output);
		
		return output;
	}
	
	public String getGLName(String glString) {
		GeneralizedList gl = null;
		System.out.println("parse : " + glString);
		try {
			gl = GLFactory.newGLFromGLString(glString);
			return gl.getName();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContextName(String glString) {
		GeneralizedList gl = null;
		try {
			gl = GLFactory.newGLFromGLString(glString);
			GeneralizedList contextGL = gl.getExpression(0).asGeneralizedList();
			String contextName = contextGL.getName();
			
			return contextName;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
}

