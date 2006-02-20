package net.jsunit.action;

import net.jsunit.DistributedTestRunManager;
import net.jsunit.XmlRenderable;

public class FarmTestRunnerAction extends JsUnitFarmServerAction {

	private DistributedTestRunManager manager;
    private String overrideURL;

    public String execute() throws Exception {
        String message = "Received request to run farm tests";
        if (overrideURL != null)
            message += " with URL " + overrideURL;
        server.logStatus(message);
        manager = new DistributedTestRunManager(hitter, server.getConfiguration(), overrideURL);
        manager.runTests();
        server.logStatus("Done running farm tests");
        return SUCCESS;
    }

	public XmlRenderable getXmlRenderable() {
		return manager.getTestRunResult();
	}

	public DistributedTestRunManager getTestRunManager() {
		return manager;
	}

	public void setUrl(String overrideURL) {
        this.overrideURL = overrideURL;
    }
}
