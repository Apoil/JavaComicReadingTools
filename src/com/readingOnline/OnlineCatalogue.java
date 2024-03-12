package com.readingOnline;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import com.Catalogue;
import com.Reading;

import static com.ReadingPage.getAllFilePaths;

public class OnlineCatalogue{



    public static void main(String[] args) {
        ImageIcon icon;
        BufferedImage image = null;
        try {
            URL url = new URL("http://demo.ioveyou.love:88/Comic/(C100)%20%5B%E6%AF%9B%E7%8E%89%E7%89%9B%E4%B9%B3%20(%E7%8E%89%E4%B9%8B%E3%81%91%E3%81%A0%E3%81%BE)%5D%20%E3%82%AF%E3%83%AD%E3%83%A0%E3%81%A1%E3%82%83%E3%82%93%E3%81%AE%E5%A4%8F%E4%BC%91%E3%81%BF%20%EF%BD%9E%E3%82%BB%E3%83%9F%E3%81%8A%E3%81%98%E3%81%95%E3%82%93%E7%B7%A8%EF%BD%9E%20(%E3%82%AA%E3%83%AA%E3%82%B8%E3%83%8A%E3%83%AB)%20%5BDL%E7%89%88%5D/3.png");
            image = ImageIO.read(url);

            icon=new ImageIcon(image);

            createNewWin(icon,"");


           // ImageIO.write(image, "jpg", new File("C:\\Users\\asus\\Desktop\\copy.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("下载成功~");
    }


    public static void createNewWin(ImageIcon icon, String path){

        // 创建新窗口
        JFrame showWindow = new JFrame("预览窗口");
        showWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 添加内容到新窗口（例如一个标签）
        JLabel newLabel = new JLabel(icon);
        showWindow.add(newLabel);

        // 设置一个透明的内容面板
        JPanel contentPane = (JPanel) showWindow.getContentPane();
        contentPane.setOpaque(false);

        JPanel buttonPanel = new JPanel();
        JButton read=new JButton("开始阅读");
        read.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String[]files=getAllFilePaths(path);
                new Reading(files);
            }
        });
        buttonPanel.add(read);
        // 将按钮面板添加到窗口的南部
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // 显示新窗口
        showWindow.pack(); // 使窗口大小适应内容
        showWindow.setLocationRelativeTo(null); // 居中显示窗口
        showWindow.setVisible(true);

    }

}
