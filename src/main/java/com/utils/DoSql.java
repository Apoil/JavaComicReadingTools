package com.utils;
import com.Dao.Comic;
import java.sql.*;

public class DoSql {

    Connection con = null;
    Statement statement = null;
    ResultSet res = null;
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://demo.ioveyou.love:3306/Comic?useUnicode=true&useSSL=false&serverTimezone=UTC&characterEncoding=utf-8";;
    String name = "users";
    String passwd = "zzs123";
    public static void main(String[] args) throws SQLException {
        DoSql dq=new DoSql();
        String a=dq.serchN("(C90) [どっつ&らいん] HAPPINESS (アイドルマスター、ラブライブ!)")[0];
        System.out.println("1:"+a);

    }
    public DoSql(){
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
