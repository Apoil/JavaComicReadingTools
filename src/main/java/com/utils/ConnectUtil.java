package com.utils;


import com.Dao.Comic;

import javax.swing.*;
import java.sql.*;

/**
 * @author Hina_z
 * @version 1.0.0
 * @ClassName Connect.java
 * @Description 数据库连接类
 * @Param
 * @createTime 2024年2月16日21点52分
 */

public class ConnectUtil {

    Connection con = null;
    Statement statement = null;
    ResultSet res = null;
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://demo.ioveyou.love:3306/users?useUnicode=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf-8";;
    String name = "users";
    String passwd = "zzs123";

    public ConnectUtil(){
        try {
            Class.forName(driver).newInstance();
            con = DriverManager.getConnection(url, name, passwd);
            statement = con.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println("对不起，找不到这个Driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public boolean compare(String username,String password){
        boolean m = false;
        String sql = "select password from user where username=\"" + username + "\"";
        try {
            res = statement.executeQuery(sql);
            if (res.next()) {
                String pa = res.getString(1);
                System.out.println(pa + " " + password);
                if (pa.equals(password)) {
                    m = true;
                } else {
                    JOptionPane.showMessageDialog(null, "密码错误！");
                }
            } else {
                JOptionPane.showMessageDialog(null, "用户名不存在！");
            }
            res.close();
            con.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return m;
    }


    public  String[] serchN(String name) throws SQLException {

        String[] allp=new String[1000];
        try {
            int i=0;
            String sql = "select url from Comic where name=\"" + name + "\"";
            res=statement.executeQuery(sql);
            while(res.next()) {
                i++;
                Comic comic=new Comic();
                comic.setUrl(res.getString(1));
                allp[i-1]=comic.getUrl();
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        //关闭连接
        res.close();
        con.close();
        statement.close();
        return allp;
    }



}
