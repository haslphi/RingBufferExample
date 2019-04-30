package at.jku.swtesting.modeltesting;

import org.junit.Before;
import org.junit.Test;

import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.CoverageMetric;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;

public class RingBufferModelTest {
	private Tester tester;
	private CoverageMetric cov;
	
	@Before
	public void prepare() {
		tester = new RandomTester(new RingBufferModelWithAdapter());
		tester.buildGraph();
		cov = new StateCoverage();
		tester.addListener(cov);
		tester.addListener(new VerboseListener());
		tester.addListener(new StopOnFailureListener());
	}
	
	@Test
	public void modelTestSmall() {
		tester.generate(10);
		tester.getModel().printMessage(cov.getName() + ": " + cov.toString());
	}
	
	@Test
	public void modelTestMedium() {
		tester.generate(50);
		tester.getModel().printMessage(cov.getName() + ": " + cov.toString());
	}
	
	@Test
	public void modelTestLarge() {
		tester.generate(75);
		tester.getModel().printMessage(cov.getName() + ": " + cov.toString());
	}
	
	@Test
	public void modelTestExtraLarge() {
		tester.generate(100);
		tester.getModel().printMessage(cov.getName() + ": " + cov.toString());
	}

}
