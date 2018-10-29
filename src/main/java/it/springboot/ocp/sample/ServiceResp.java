package it.springboot.ocp.sample;

public class ServiceResp {

	private String serviceName;
	private String message;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ServiceResp [serviceName=" + serviceName + ", message=" + message + "]";
	}

}
