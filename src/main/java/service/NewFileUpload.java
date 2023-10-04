package service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewFileUpload {

    public static void main(String[] args) {
        JFrame frame = new JFrame("File Upload");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Enter text to upload:");
        JTextField textField = new JTextField();
        JButton browseButton = new JButton("Browse");
        JButton uploadButton = new JButton("Upload");
        JLabel linkLabel = new JLabel();

        panel.add(label);
        panel.add(textField);
        panel.add(browseButton);
        panel.add(uploadButton);
        panel.add(linkLabel);

        frame.add(panel);
        frame.setVisible(true);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    textField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // آپلود فایل را در یک رشته جدید اجرا می‌کنیم
                Thread uploadThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            StringBuilder response = new StringBuilder();
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
                                response = new StringBuilder();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();

                                // چاپ پاسخ
                                System.out.println(response);
                            } else {
                                System.out.println("خطا در ارسال درخواست. کد خطا: " + responseCode);
                            }
                            
                            // استخراج لینک از پاسخ Curl با استفاده از عبارت منظم
                            String link = extractLinkFromResponse(response.toString());
                            System.out.println(response);

                            if (link != null) {
                                linkLabel.setText("<html><a href='" + link + "'>Download Link</a></html>");
                                linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            } else {
                                linkLabel.setText("لینک پیدا نشد.");
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                uploadThread.start(); // شروع اجرای رشته آپلود
            }
        });
    }

    // تابع برای استخراج لینک از پاسخ Curl با استفاده از عبارت منظم
    private static String extractLinkFromResponse(String response) {
        Pattern pattern = Pattern.compile("\"link\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
    
