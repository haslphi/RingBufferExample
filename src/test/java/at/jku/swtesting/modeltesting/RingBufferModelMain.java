package at.jku.swtesting.modeltesting;

import nz.ac.waikato.modeljunit.RandomTester;
import nz.ac.waikato.modeljunit.StopOnFailureListener;
import nz.ac.waikato.modeljunit.Tester;
import nz.ac.waikato.modeljunit.VerboseListener;
import nz.ac.waikato.modeljunit.coverage.CoverageMetric;
import nz.ac.waikato.modeljunit.coverage.StateCoverage;

public class RingBufferModelMain {

	public static void main(String[] args) {
		Tester tester = new RandomTester(new RingBufferModel());
		tester.buildGraph();
		CoverageMetric cov = new StateCoverage();
		tester.addListener(cov);
		tester.addListener(new VerboseListener());
		tester.addListener(new StopOnFailureListener());
		tester.generate(50);
		tester.getModel().printMessage(cov.getName() + ": " + cov.toString());

	}

}
