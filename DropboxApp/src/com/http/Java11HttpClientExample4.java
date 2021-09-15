package com.http;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import helper.Constants;
import helper.Helper;

public class Java11HttpClientExample4 {
	private String responseBody;
	private String searchString;

	public String getResponseBody() {
		return responseBody;
	}

	private void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	private final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();

	public static HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
		var builder = new StringBuilder();
		for (Map.Entry<Object, Object> entry : data.entrySet()) {
			if (builder.length() > 0) {
				builder.append("&");
			}

			builder.append(entry.getValue().toString());
		}
		return HttpRequest.BodyPublishers.ofString(builder.toString());
	}

	public void sendPOST() throws IOException, InterruptedException {
		// form parameters
		Map<Object, Object> data = new HashMap<>();
		// data.put("product", product);
		data.put("searchString", searchString);

		HttpRequest request = HttpRequest.newBuilder().POST(ofFormData(data))
				.uri(URI.create(Constants.DROPBOX_SEARCHFILENAME_URI))
				.setHeader("Authorization", "Basic " + Helper.setAuthorizationBase64Encoded()).build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		// print status code
		System.out.println(response.statusCode());

		setResponseBody(response.body());
	}
}
