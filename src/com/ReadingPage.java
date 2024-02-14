package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

public class ReadingPage extends JFrame {
    private ImageIcon imageIcon;
    private Image image;

    private String selectedFilePath;//用于存储选择的文件路径
    private String selectedDirectoryPath;//用于存储选择的文件夹路径

    private JButton load=new JButton("加载目录");
    private JButton loaddoc=new JButton("加载文件");
    private JButton catalogue=new JButton("目录");

    public ReadingPage(){
        setTitle("漫画阅读器ReadingTools");
        setSize(600, 345);
        ((JPanel)getContentPane()).setOpaque(false);
        //再设置图片
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        imageIcon = new ImageIcon("./src/com/bg.jpg");//图标组件
        image = imageIcon.getImage();
        JLabel imgLabel = new JLabel(imageIcon);
        getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0,0,600,345); //背景图片的位置

        this.setIconImage(image);//设置窗口图像
        this.setLocation(600,300);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);


        load.setSize(100, 35);
        load.setLocation(145, 245);
        load.setBackground(Color.gray);

        loaddoc.setSize(100, 35);
        loaddoc.setLocation(245, 245);
        loaddoc.setBackground(Color.gray);

        catalogue.setSize(100, 35);
        catalogue.setLocation(345, 245);
        catalogue.setBackground(Color.gray);


        load.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置为只选择目录
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    selectedDirectoryPath = selectedDirectory.getAbsolutePath();
                    // 在这里你可以对selectedDirectoryPath进行进一步处理，比如显示在界面上或保存到某个变量中。
                    System.out.println("选择的文件夹路径: " + selectedDirectoryPath);

                    String[] file = getAllFilePaths(selectedDirectoryPath);

                    for (int i = 0; i < file.length; i++) {
                        System.out.println("路径 " + (i+1) + ": " + file[i]);
                    }

                    new Reading(file);

                }

            }
        });


        loaddoc.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //zip等压缩文件用
                JFileChooser fileChooser=new JFileChooser();
                int returnValue=fileChooser.showOpenDialog(null);
                if (returnValue==JFileChooser.APPROVE_OPTION){
                    File selectedFile=fileChooser.getSelectedFile();
                    selectedFilePath=selectedFile.getAbsolutePath();
                    JOptionPane.showMessageDialog(null,selectedFilePath,"路径显示",JOptionPane.WARNING_MESSAGE);	//消息对话框

                    new ZipImageReader(selectedFilePath);
                }
            }

        });

        catalogue.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // 设置为只选择目录
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedDirectory = fileChooser.getSelectedFile();
                    String selectedDirectoryPath = selectedDirectory.getAbsolutePath();
                    // 在这里你可以对selectedDirectoryPath进行进一步处理，比如显示在界面上或保存到某个变量中。
                    System.out.println("选择的文件夹路径: " + selectedDirectoryPath);

                    String[] directories = getAllDirectoryPaths(selectedDirectoryPath);

                    for (int i = 0; i < directories.length; i++) {
                        System.out.println("目录 " + (i + 1) + ": " + directories[i]);
                    }

                    new Catalogue(directories);
                }

            }
        });


        this.add(load);
        this.add(loaddoc);
        this.add(catalogue);

    }

    public static String[] getAllFilePaths(String directoryPath) {
        try {
            List<String> filePathList = new ArrayList<>();
            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .forEach(path -> filePathList.add(path.toString()));

            // 将ArrayList转换为数组
            return filePathList.toArray(new String[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new String[0]; // 如果发生错误，返回一个空数组
        }
    }

    // 获取指定目录下所有子目录的路径（不包括文件）
    public static String[] getAllDirectoryPaths(String directoryPath) {
        try {
            List<String> directoryPathList = new ArrayList<>();
            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isDirectory)
                    .forEach(path -> {
                        if (!path.toString().equals(directoryPath)) { // 排除根目录自身
                            directoryPathList.add(path.toString());
                        }
                    });
            // 将ArrayList转换为数组
            return directoryPathList.toArray(new String[0]);
        } catch (IOException ex) {
            ex.printStackTrace();
            return new String[0]; // 如果发生错误，返回一个空数组
        }
    }


}
