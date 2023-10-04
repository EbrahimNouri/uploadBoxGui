package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurlExample {

    public static void main(String[] args) {
        try {
            // آدرس سایت file.io برای ارسال درخواست POST
            String url = "https://file.io";

            // متنی که می‌خواهیم بفرستیم
            String postData = "text=this is a secret pw";

            // ایجاد اتصال به سرور
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

            // تنظیم متد ارسال درخواست به POST
            conn.setRequestMethod("POST");

            // ارسال داده‌ها
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(postData.getBytes());
            os.flush();
            os.close();

            // خواندن پاسخ از سرور
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // چاپ پاسخ
                System.out.println(response);
            } else {
                System.out.println("خطا در ارسال درخواست. کد خطا: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
