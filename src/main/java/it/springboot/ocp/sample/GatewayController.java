package it.springboot.ocp.sample;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class GatewayController {

	private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);

	private static final String urlTemplate = "http://%s/hello?message={message}";
	private static final String SERVICES_FILE = "hello.services.file";

	private Map<String, String> services;

	private Properties props;

	public GatewayController() throws IOException {
		props = new Properties();
		reloadServices();
	}

	private void reloadServices() throws IOException {
		String filename = System.getenv().get(SERVICES_FILE);
		FileReader fr = new FileReader(filename);
		props.load(fr);
		fr.close();
	}

	@RequestMapping(path = "/call", method = RequestMethod.POST)
	public ServiceResp call(@RequestBody ServiceReq req) throws IOException {

		reloadServices();

		String name = req.getName();
		String serviceName = props.getProperty(name.toLowerCase());
		if (StringUtils.isEmpty(serviceName)) {
			ServiceResp negResp = new ServiceResp();
			negResp.setServiceName("");
			negResp.setMessage("No service available with name '" + name + "'");
			return negResp;
		}
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("message", req.getMessage());
		ServiceResp resp = restTemplate.getForObject(String.format(urlTemplate, serviceName), ServiceResp.class, paramMap);
		logger.info(resp.toString());

		return resp;
	}
}
