package br.com.tiagods.factory;

import java.sql.*;

        
public class Conexao{
//vamos abrir a conexao
    
    private String driver = "com.mysql.jdbc.Driver";
    private String url="jdbc:mysql://127.0.0.1:3306/cadastro";
    private String user = "root";
    private String password = ""; 
    
    private static Conexao instance;
    public static Conexao getInstance(){
        if(instance==null)
            instance=new Conexao();
        return instance;
    }
    public Connection getConnetion() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException erro) {
            throw new RuntimeException(erro);
        }
    }
}