package com.cboxgames.idonia.backend.test;

public class HttpTestRunnable implements Runnable{

	public static final String RESULT_SUCCESS = "SUCCESS";
	public static final String RESULT_EXCEPTION = "EXCEPTION";
	
	/* What was the result of the test? */
	public String result;
	
	private Class<? extends HttpTest> _test_type;
	
	@Override
	public void run() {}
	
	public void setTestType(Class<? extends HttpTest> class1) { _test_type = class1; }
	public Class<? extends HttpTest> getTestType() { return _test_type; }

}
