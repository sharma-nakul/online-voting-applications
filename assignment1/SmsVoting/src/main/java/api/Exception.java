package api;


public class Exception extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String exceptionMsg;
	 
	public Exception(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
	public Exception() {}
	
	public String getExceptionMsg(){
		this.getMessage();
		return this.exceptionMsg;
		
	}
	
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
		
	}
	
   
}
