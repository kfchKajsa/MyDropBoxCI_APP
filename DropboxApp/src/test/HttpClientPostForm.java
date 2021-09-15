package test;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import helper.Constants;
import helper.Helper;

public class HttpClientPostForm {

	private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10)).build();

	public static void main(String[] args) throws IOException, InterruptedException {

		// form parameters
		Map<Object, Object> data = new HashMap<>();
		data.put("username", "abc");
		data.put("password", "123");
		data.put("custom", "secret");

		HttpRequest request = HttpRequest.newBuilder().POST(ofFormData(data))
				.uri(URI.create("https://e9cb36cetrial.it-cpitrial02-rt.cfapps.eu10-001.hana.ondemand.com/http/simpletest"))
				.setHeader("Authorization", "Basic " + Helper.setAuthorizationBase64Encoded()).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print status code
		System.out.println(response.statusCode());

		// print response body
		System.out.println(response.body());

	}

	// Sample: 'password=123&custom=secret&username=abc&ts=1570704369823'
	public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
		var builder = new StringBuilder();
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}
			builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
		}

		System.out.println(builder.toString());
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

}
