package de.tilman_neumann.test.junit;

import org.apache.log4j.Logger;

import de.tilman_neumann.util.ConfigUtil;

import junit.framework.TestCase;
import junit.framework.TestResult;

public class ClassTest extends TestCase {
	
	static {
		// early initializer, works no matter if run as junit test or as java application
		ConfigUtil.initProject();
	}
	
	private static final Logger LOG = Logger.getLogger(ClassTest.class);
	
	// Constructor is called for each test method in the test class!
	public ClassTest() {
		//LOG.info("create test for " + this.getClass());
	}
	
	public void run(TestResult result) {
		LOG.info("Run " + this.getClass().getName() + "." + this.getName() + "()");
		super.run(result);
	}
}
