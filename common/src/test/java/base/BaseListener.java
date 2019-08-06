package base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BaseListener implements ITestListener {
    private final Logger LOGGER = LogManager.getLogger(this.getClass());

    private void printException(ITestResult results){
        if (results != null && results.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            results.getThrowable().printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            LOGGER.error(stacktrace);
        }
    }

    public void onTestStart(ITestResult result) {printException(result);}

    public void onTestSuccess(ITestResult result) {printException(result);}

    public void onTestFailure(ITestResult result) {printException(result);}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {printException(result);}

    public void onTestSkipped(ITestResult result) {printException(result);}

    public void onTestFailedWithTimeout(ITestResult result) {printException(result);}

}