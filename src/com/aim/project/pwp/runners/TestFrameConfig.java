package com.aim.project.pwp.runners;

public class TestFrameConfig {
	public TestFrameConfig() {
		
	}

	public int getTotalRuns() {
		return 11;
	}
	
	public long getBenchmarkedTimeLimit() {
		return 53_000l;
	}
	
	public int getRWSDefault() {
		return 5;
	}
	
	public int getRWSMin() {
		return 1;
	}
	
	public int getRWSMax() {
		return 9;
	}
}
