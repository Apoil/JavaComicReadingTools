
package com;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

public class ZipImageReader extends JFrame {

    private JFrame frame;
    private JLabel imageLabel;
    private JButton prevButton;
    private JButton nextButton;
    private List<BufferedImage> images; // 改为存储 BufferedImage 对象以便更好地控制缩放
    private int currentImageIndex = 0;
    private String zpath;

    public ZipImageReader(String selectedFilePath) {
        this.zpath = selectedFilePath;
        images = new ArrayList<>();
        frame = new JFrame("ZIP 图片读取");
        imageLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (images != null && !images.isEmpty() && currentImageIndex >= 0 && currentImageIndex < images.size()) {
                    BufferedImage currentImage = images.get(currentImageIndex);
                    Image scaledImage = currentImage.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    g.drawImage(scaledImage, 0, 0, this);
                }
            }
        };
        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPreviousImage();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextImage();
            }
        });

        frame.setLayout(new BorderLayout());
        frame.add(imageLabel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        try {
            loadImagesFromZip(zpath);
            if (!images.isEmpty()) {
                // 显示第一张图片，触发 paintComponent 方法
                imageLabel.repaint();
            } else {
                JOptionPane.showMessageDialog(frame, "No images found in the ZIP file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "An error occurred while loading images from the ZIP file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadImagesFromZip(String zipFilePath) throws Exception {
        try (ZipFile zipFile = new ZipFile(zipFilePath)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName().toLowerCase();
                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif")) {
                    try (InputStream stream = zipFile.getInputStream(entry)) {
                        BufferedImage image = ImageIO.read(stream);
                        images.add(image);
                    }
                }
            }
        }
    }

    private void showPreviousImage() {
        int prevIndex = currentImageIndex - 1;
        if (prevIndex >= 0) {
            currentImageIndex = prevIndex;
            imageLabel.repaint(); // 触发 paintComponent 方法重新绘制图片
        }
    }

    private void showNextImage() {
        int nextIndex = currentImageIndex + 1;
        if (nextIndex < images.size()) {
            currentImageIndex = nextIndex;
            imageLabel.repaint(); // 触发 paintComponent 方法重新绘制图片
        }
    }


}