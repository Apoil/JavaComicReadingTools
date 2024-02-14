package com;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static com.ReadingPage.getAllFilePaths;

public class Catalogue extends JFrame {
    private static PreviewLabel previewLabel;

    public Catalogue(String[] directories) {
        //创建窗口
        createAndShowGUI(directories);

    }
    // 预览标签类
    private static class PreviewLabel extends JLabel {
        public PreviewLabel(ImageIcon icon) {
            super(icon);
            setOpaque(true);
            setBackground(Color.YELLOW); // 或者其他背景色
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    //创建主要窗口实现
    private static void createAndShowGUI(String[] dir) {
        JFrame frame = new JFrame("目 录");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭窗口不结束程序
        frame.setLayout(new FlowLayout()); // 使用FlowLayout布局管理器
        String[] dirimges=new String[dir.length];//存储文件夹下第一个文件的路径
        String[] dirs=new String[dir.length];//存储文件夹目录
        for (int i=0;i<dir.length;i++){
            Path rootDirectory = Paths.get(dir[i]); // 替换为你的根目录路径
            try {
                int finalI = i;
                Files.walkFileTree(rootDirectory, new SimpleFileVisitor<Path>() {
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        // 对于每个子目录，获取第一个文件的路径
                        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                            for (Path entry : stream) {
                                if (Files.isRegularFile(entry)) {
                                    //System.out.println("在目录 " + dir + " 中找到的第一个文件是: " + entry);
                                    dirimges[finalI]=entry.toString();//存储第一个文件的的路径
                                    dirs[finalI]=dir.toString();//存储子路径

                                    return FileVisitResult.TERMINATE; // 找到文件后停止遍历
                                }
                            }
                        }
                        return FileVisitResult.CONTINUE; // 如果没有找到文件，继续遍历
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        //列表组件动画+预览
        class HoveringLabel extends JLabel {
            private static final int HOVER_INCREMENT = 5; // 悬停时增加的大小
            private boolean isHovered = false;
            private boolean isAnimating = false;
            private Timer timer;
            private Dimension originalSize;

            //重写Lable类，实现动画效果
            public HoveringLabel(String text, ImageIcon icon,ImageIcon sicon,String paths, int horizontalAlignment) {

                super(text, icon, horizontalAlignment);

                originalSize = getPreferredSize();
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        createAndShowNewWindow(sicon,paths);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        startAnimation();

                        createPreviewLabel(e.getPoint(),icon);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        stopAnimation();
                        destroyPreviewLabel();
                    }
                });
            }

            //开始动画
            private void startAnimation() {
                isAnimating = true;
                timer = new Timer(50, e -> {
                    if (isHovered) {
                        // 微微突出动画：改变标签大小
                        int newWidth = originalSize.width + HOVER_INCREMENT;
                        int newHeight = originalSize.height + HOVER_INCREMENT;
                        setPreferredSize(new Dimension(newWidth, newHeight));

                        // 如果已经调整过大小，停止定时器
                        if (newWidth == getWidth() && newHeight == getHeight()) {
                            stopAnimation();
                        }
                    }
                });
                timer.setRepeats(false); // 设置定时器只执行一次
                timer.start();
            }

            //结束动画
            private void stopAnimation() {
                isAnimating = false;
                if (timer != null) {
                    timer.stop();
                }
                // 恢复原始大小
                setPreferredSize(originalSize);
            }

            //自适应尺寸
            public void setPreferredSize(Dimension preferredSize) {
                super.setPreferredSize(preferredSize);
                revalidate();
                repaint();
            }

            //创建预览（未实现
            private void createPreviewLabel(Point point,ImageIcon icon) {
                ImageIcon previewIcon = icon;
                if (previewIcon != null) {
                    // 创建预览标签
                    previewLabel = new PreviewLabel(previewIcon);
                    // 获取窗口的中心点
                    Dimension windowSize = frame.getSize();
                    int centerX = (windowSize.width - previewLabel.getWidth()) / 2;
                    int centerY = (windowSize.height - previewLabel.getHeight()) / 2;

                    previewLabel.setBounds(centerX + centerY, this.getY(), previewIcon.getIconWidth(), previewIcon.getIconHeight());
                    //frame.add(previewLabel);
                    frame.revalidate();
                    frame.repaint();
                }
            }

            //销毁预览（未实现
            private void destroyPreviewLabel() {
                if (previewLabel != null) {
                    frame.remove(previewLabel);
                    previewLabel = null;
                    frame.revalidate();
                    frame.repaint();
                }
            }

            //创建预览窗口，开始阅读跳转
            private void createAndShowNewWindow(ImageIcon icon,String path) {
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


        // 假设你有一组图标文件路径
        String[] iconPaths =dirimges;

        // 循环创建带有图标的JLabel组件，并添加到frame中
        for (int i=0;i<iconPaths.length;i++){
            // 创建图标
            ImageIcon icon = createImageIcon(iconPaths[i]);
            ImageIcon sicon = createShowIcon(iconPaths[i]);

            // 如果图标加载成功，则创建JLabel并设置图标
            if (icon != null) {

                HoveringLabel label=new HoveringLabel("",icon,sicon,dirs[i],JLabel.LEFT);
                label.setLayout(new BoxLayout(label, BoxLayout.Y_AXIS));
                frame.getContentPane().add(label);
/*
                JLabel label = new JLabel();
                label.setLayout(new BoxLayout(label, BoxLayout.Y_AXIS));
                label.setIcon(icon);
                frame.getContentPane().add(label);*/
            } else {
                System.err.println("Unable to load image: " + iconPaths[i]);
            }
        }


        // 设置窗口的大小并显示
        frame.pack(); // 自动调整大小以适应内容
        frame.setLocationRelativeTo(null); // 居中显示
        frame.setVisible(true);


    }

    // 辅助方法，用于创建缩放ImageIcon，返回ImageIcon类型（200x200
    private static ImageIcon createImageIcon(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                Image image = ImageIO.read(file);
                int width = image.getWidth(null);
                int height = image.getHeight(null);

                // 根据图像的宽高比来设置新的尺寸
                int newWidth = 200;
                int newHeight = 200;
                if (width > height) {
                    // 宽度大于高度，设置宽度为100，按比例计算高度
                    newHeight = (int) Math.round((double) newWidth * height / width);
                } else if (height > width) {
                    // 高度大于宽度，设置高度为100，按比例计算宽度
                    newWidth = (int) Math.round((double) newHeight * width / height);
                }

                // 创建缩放后的图像
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                // 使用缩放后的图像创建ImageIcon
                return new ImageIcon(scaledImage);
            } catch (IOException e) {
                System.err.println("读取图片错误: " + filePath);
                e.printStackTrace();
            }
        } else {
            System.err.println("文件不存在: " + filePath);
        }
        return null;
    }

    //创建预览用缩放Icon，返回ImageIcon类型（600x600
    private static ImageIcon createShowIcon(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                Image image = ImageIO.read(file);
                int width = image.getWidth(null);
                int height = image.getHeight(null);

                // 根据图像的宽高比来设置新的尺寸
                int newWidth = 600;
                int newHeight = 600;
                if (width > height) {
                    // 宽度大于高度，设置宽度为600，按比例计算高度
                    newHeight = (int) Math.round((double) newWidth * height / width);
                } else if (height > width) {
                    // 高度大于宽度，设置高度为600，按比例计算宽度
                    newWidth = (int) Math.round((double) newHeight * width / height);
                }

                // 创建缩放后的图像
                Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                // 使用缩放后的图像创建ImageIcon
                return new ImageIcon(scaledImage);
            } catch (IOException e) {
                System.err.println("读取图片错误: " + filePath);
                e.printStackTrace();
            }
        } else {
            System.err.println("文件不存在: " + filePath);
        }
        return null;
    }


/*    private static ImageIcon createImageIcon(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            try {
                return new ImageIcon(file.getPath());
            } catch (Exception e) {
                System.err.println("Error loading image: " + filePath);
                e.printStackTrace();
            }
        } else {
            System.err.println("File does not exist: " + filePath);
        }
        return null;
    }*/


}



