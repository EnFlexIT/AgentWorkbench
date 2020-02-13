package de.enflexit.awb.samples.example01;

import jade.core.Agent;

public class HelloWorldAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		System.out.println("Hello world!");
	}
	
}