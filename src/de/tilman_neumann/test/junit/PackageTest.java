package de.tilman_neumann.test.junit;

import de.tilman_neumann.util.ConfigUtil;

import junit.framework.TestSuite;

public abstract class PackageTest extends TestSuite {
	
	static {
		// early initializer, works no matter if run as junit test or as java application
		ConfigUtil.initProject();
	}
}
