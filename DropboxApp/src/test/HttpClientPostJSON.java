package test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import helper.Helper;

public class HttpClientPostJSON {

    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

        // json formatted data
        String json = new StringBuilder()

                .append("{")
                .append("\"test1\":\"mkyong\",")
                .append("\"test2\":\"hello\"")
                .append("}").toString();  

        // add json header
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create("https://e9cb36cetrial.it-cpitrial02-rt.cfapps.eu10-001.hana.ondemand.com/http/simpletest"))
           //     .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .setHeader("Authorization", "Basic " + Helper.setAuthorizationBase64Encoded())
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // print status code
        System.out.println(response.statusCode());

        // print response body
        System.out.println(response.body());

    }
}