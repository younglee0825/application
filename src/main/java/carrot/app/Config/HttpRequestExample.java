package carrot.app.Config;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
public class HttpRequestExample {
    public static void main(String[] args) throws Exception {
        // 요청 URL
        String url = "http://localhost:8080/custom_login_proc";

        // 요청 파라미터
        String requestBody = "uemail=user@example.com&upwd=password";

        // HttpURLConnection 객체 생성
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        // 요청 메서드 설정
        connection.setRequestMethod("POST");

        // 요청 헤더 설정
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 요청 본문 작성
        connection.setDoOutput(true);
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        // 응답 코드 확인
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
}}
