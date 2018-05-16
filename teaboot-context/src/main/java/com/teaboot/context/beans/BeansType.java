package com.teaboot.context.beans;

public enum BeansType {
	SERVICE(0), CONTROLLOR(1), COMPONENT(2), MAPPING(10);
	int value = 0;

	BeansType(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
