package com;

import com.utils.ConnectUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Login extends JFrame{

    private Login self;
    private ImageIcon imageIcon;
    private Image image;
    private String userid;// 登陆用户名和密码
    private String password;
    private JLabel unLabel = new JLabel("账号：");// 登陆面板控件
    private JTextField unField = new JTextField();
    private JLabel pwLabel = new JLabel("密码：");
    private JPasswordField pwField = new JPasswordField();
    private JButton dl = new JButton("登录");
    private JButton d2 = new JButton("重置");
    public Login() {

        this.self = this;
        this.setSize(600, 345);// 设置登陆面板
        ////设置窗口背景图
        //先将contentPane设置成透明的
        ((JPanel)getContentPane()).setOpaque(false);
        //再设置图片
        imageIcon = new ImageIcon("src/main/resources/images/bg.jpg");//图标组件
        image = imageIcon.getImage();
        JLabel imgLabel = new JLabel(imageIcon);
        getLayeredPane().add(imgLabel, new Integer(Integer.MIN_VALUE));
        imgLabel.setBounds(0,0,600,345); //背景图片的位置
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(image);//设置窗口图像
        this.setLocation(600,300);
        this.setVisible(true);
        this.setResizable(false);
        this.setLayout(null);

        //设置窗口名称
        this.setTitle("漫画阅读器");
        unLabel.setSize(60, 25);
        unLabel.setLocation(40, 40);
        unLabel.setForeground(Color.black);
        unLabel.setFont(new Font("黑体",Font.BOLD,18));
        unField.setSize(150, 35);
        unField.setLocation(100, 30);

        pwLabel.setSize(60, 25);
        pwLabel.setLocation(40, 100);
        pwLabel.setForeground(Color.black);
        pwLabel.setFont(new Font("黑体",Font.BOLD,18));
        pwField.setSize(150, 35);
        pwField.setLocation(100, 95);

        dl.setSize(80, 35);
        dl.setLocation(65, 175);
        dl.setBackground(Color.gray);
        d2.setSize(80, 35);
        d2.setLocation(185, 175);
        d2.setBackground(Color.gray);

        dl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectUtil cu=new ConnectUtil();
                userid = unField.getText();
                password = pwField.getText();
                if (cu.compare(userid, password)) {
                    self.setVisible(false);
                    JOptionPane.showMessageDialog(null, "登录成功", "登录情况",JOptionPane.PLAIN_MESSAGE);
                    new ReadingPage();
                }

//无JDBC登录
 /*               if(userid.equals("admin")&&password.equals("admin")) {
                    self.setVisible(false);
                    // JOptionPane.showMessageDialog(null, "登录成功", "登录情况",JOptionPane.PLAIN_MESSAGE);
                    new ReadingPage();
                } else {
                    JOptionPane.showMessageDialog(null, "账号或密码错误！", "登录情况",JOptionPane.PLAIN_MESSAGE);
                }*/
            }
        });
        d2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unField.setText("");
                pwField.setText("");
            }
        });
        this.add(unLabel);
        this.add(unField);
        this.add(pwLabel);
        this.add(pwField);
        this.add(dl);
        this.add(d2);

    }
}
