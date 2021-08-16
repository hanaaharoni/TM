package com.hanaah.iptiq.exception;

public class MaximumCapacityReachedException extends Exception {
	public MaximumCapacityReachedException(int capacity) {
		super(String.format("Maximum capacity of [%d] has been reached", capacity));
	}
}
