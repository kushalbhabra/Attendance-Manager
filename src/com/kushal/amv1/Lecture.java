package com.kushal.amv1;


public class Lecture {
	
	private String subject;
	private String time;
	private String atn;
	

	public Lecture(){

	}

	public Lecture(String subject,String time,String atn){
		this.subject = subject;
		this.time= time;
		this.atn=atn;
	}

	public String getSubject() {
		return subject;
	}


	public String getTime() {
		return time;
	}
	
	public String getAtn() {
		return atn;
	}

	public void setSubject(String subject) {
		this.subject =	subject;
	}

	public void setTime(String time) {
		this.time = time;
	}
	public void setAtn(String atn) {
		this.atn = atn;
	}

}