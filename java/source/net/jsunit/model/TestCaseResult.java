package net.jsunit.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.jdom.Element;
 
/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class TestCaseResult {
	
    public static final String TEST_PAGE_TEST_NAME_DELIMITER = ":";
	public static final String DELIMITER = "|";
	public static final String ERROR_INDICATOR = "E";
	public static final String  FAILURE_INDICATOR = "F";
	private String testPageName;
    private String name;
    private double time;
    private String failure, error;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public double getTime() {
        return time;
    }

    public void setTimeTaken(double time) {
        this.time = time;
    }

    public boolean hadError() {
        return error != null;
    }

    public boolean hadFailure() {
        return failure != null;
    }

    public boolean wasSuccessful() {
        return getResultType() == ResultType.SUCCESS;
    }
    
    public ResultType getResultType() {
    	if (hadError())
    		return ResultType.ERROR;
    	if (hadFailure())
    		return ResultType.FAILURE;
    	return ResultType.SUCCESS;
    }

    public static TestCaseResult fromString(String string) {
        TestCaseResult result = new TestCaseResult();
        StringTokenizer toker = new StringTokenizer(string, DELIMITER);
        try {
        	String fullyQualifiedName;
			try {
				fullyQualifiedName = URLDecoder.decode(toker.nextToken(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
        	int colonIndex = fullyQualifiedName.lastIndexOf(TEST_PAGE_TEST_NAME_DELIMITER);
        	result.setTestPageName(fullyQualifiedName.substring(0, colonIndex));
        	result.setName(fullyQualifiedName.substring(colonIndex + 1));
            result.setTimeTaken(Double.parseDouble(toker.nextToken()));
            String successString = toker.nextToken();
            if (successString.equals(ERROR_INDICATOR))
                result.setError(toker.nextToken());
            else if (successString.equals(FAILURE_INDICATOR))
                result.setFailure(toker.nextToken());
        } catch (NoSuchElementException ex) {
            result.setError("Malformed test result: '" + string + "'.");
        }
        return result;
    }

    public static TestCaseResult fromXmlElement(Element testCaseElement) {
        return new TestCaseResultBuilder().build(testCaseElement);
    }

    public String getXmlFragment() {
        return new TestCaseResultWriter(this).getXmlFragment();
    }

    public String getProblemSummary() {
        return new TestCaseResultWriter(this).getProblemSummary();
    }

    public void setTestPageName(String testPageName) {
    	this.testPageName = testPageName;
    }
    
	public String getFullyQualifiedName() {
		return testPageName + TEST_PAGE_TEST_NAME_DELIMITER + name;
	}

	public String getTestPageName() {
		return testPageName;
	}
	
}