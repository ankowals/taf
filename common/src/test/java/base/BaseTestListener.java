package base;

import lombok.extern.log4j.Log4j2;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.PrintWriter;
import java.io.StringWriter;

@Log4j2
public class BaseTestListener implements ITestListener {

    private void printException(ITestResult results){
        if (results != null && results.getThrowable() != null) {
            StringWriter sw = new StringWriter();
            results.getThrowable().printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            log.error(stacktrace);
        }
    }

    public void onTestStart(ITestResult result) {printException(result);}

    public void onTestSuccess(ITestResult result) {printException(result);}

    public void onTestFailure(ITestResult result) {printException(result);}

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {printException(result);}

    public void onTestSkipped(ITestResult result) {printException(result);}

    public void onTestFailedWithTimeout(ITestResult result) {printException(result);}

}