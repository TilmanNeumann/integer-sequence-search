package de.tilman_neumann.math.app.oeis.transform;

import junit.framework.TestSuite;

import org.apache.log4j.Logger;

import de.tilman_neumann.test.junit.PackageTest;
import de.tilman_neumann.util.ReflectionUtil;

/**
 * All tests for the actual package.
 * @author Tilman Neumann
 * @since 2011-09-19
 */
public class PackageTests extends PackageTest {

	private static final Logger LOG = Logger.getLogger(PackageTests.class);
	private static final String TEST_PACKAGE = ReflectionUtil.getPackageName(PackageTests.class);

	/**
	 * @return All tests for the actual package
	 */
    public static TestSuite suite() {
        //LOG.info("Add test package " + TEST_PACKAGE);
        TestSuite suite = new TestSuite("Test package " + TEST_PACKAGE);
        //LOG.info("Add test class " + Rng.class.getName());
        suite.addTest(new TestSuite(EulerTransformationTest.class));
        // XXX: Add more tests here...

        return suite;
    }
}
