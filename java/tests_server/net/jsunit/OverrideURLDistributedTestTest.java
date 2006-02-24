package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.model.ResultType;

public class OverrideURLDistributedTestTest extends DistributedTest {
    private JsUnitServer server;

    public OverrideURLDistributedTestTest(String name) {
        super(name);
    }

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String remoteMachineURLs() {
                return "http://localhost:8080";
            }

        };
    }

    protected DistributedTestRunManager createTestRunManager() {
        return new DistributedTestRunManager(
            new NoOpStatusLogger(),
            new Configuration(configurationSource()),
                "http://localhost:8080/jsunit/testRunner.html?"
                + "testPage=http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true"
        );
    }

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new Configuration(new StubConfigurationSource() {
            public String browserFileNames() {
                return BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://www.example.com";
            }
        }));
        server.start();
    }

    public void testCollectResults() {
        super.testCollectResults();
        assertEquals(ResultType.SUCCESS, manager.getFarmTestRunResult().getResultType());
    }

    public void tearDown() throws Exception {
        server.dispose();
        super.tearDown();
    }

}