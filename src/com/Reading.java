package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Reading extends JFrame {
    private int page;
    private String[] context;
    private JLabel imgLabel;
    private JFrame frame;
    public int Ihight;
    public int Iwidth;


    public Reading(String[] file) {
        this.context = file;//存储传过来的图片路径
        this.page = 0;

        frame = new JFrame("漫画阅读器ReadingTools");
        /*frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 设置一个透明的内容面板
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setOpaque(false);

        // 创建自适应图片的JLabel
        imgLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (context != null && page >= 0 && page < context.length) {
                    String str = new File(context[page]).toString();

                    Image scaledImage = Catalogue.createReadingImage(str).getImage();



                    g.drawImage(scaledImage, 0, 0, this);

//原
/*                    try {
                BufferedImage image = ImageIO.read(new File(context[page]));
                        Image scaledImage = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                        g.drawImage(scaledImage, 0, 0, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/

                }
            }
        };


        Image FirstImage = Catalogue.createReadingImage(new File(context[0]).toString()).getImage();
        Iwidth=FirstImage.getWidth(this);
        Ihight=FirstImage.getHeight(this);
        imgLabel.setPreferredSize(new Dimension(Iwidth, Ihight));
        contentPane.add(imgLabel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        //按钮事件
        JButton previousButton = new JButton("上一页");
        previousButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                previousPage();
            }
        });
        buttonPanel.add(previousButton);

        JButton nextButton = new JButton("下一页");
        //按钮事件
        nextButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nextPage();
            }
        });
        buttonPanel.add(nextButton);

        // 将按钮面板添加到窗口的南部
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // 设置窗口的其他属性
        frame.pack(); // 使窗口大小适应内容
        frame.setLocationRelativeTo(null); // 居中显示窗口
        frame.setVisible(true);

    }

    // 更新图片的方法
    public void updateImage() {
        if (context != null && page >= 0 && page < context.length) {
            ImageIcon images = new ImageIcon(context[page]);
            imgLabel.setIcon(images);
            frame.repaint(); // 重新绘制界面以更新图片
        }
    }

    // 改变当前显示的图片索引的方法
    public void setPage(int newPage) {
        if (newPage >= 0 && newPage < context.length) {
            page = newPage;
            updateImage(); // 更新图片
        }
    }

    //下一页
    public void nextPage() {
        if (page < context.length - 1) {
            page++;
            updateImage();
        }
    }

    //上一页
    public void previousPage() {
        if (page > 0) {
            page--;
            updateImage();
        }
    }



}
