/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.tiagods.model;

import br.com.tiagods.factory.Conexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Prolink
 */
public class SetorDAO {
    private Connection con;
    private Connection getConnection(){
        this.con  = Conexao.getInstance().getConnetion();
        return this.con;
    }
    
    public List<Setor> listar(){
        try{
            List<Setor> setores = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("select * from setor order by nome");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Setor setor = new Setor(rs.getInt("id"),rs.getString("nome"));
                setores.add(setor);
            }
            return setores;
        }catch(SQLException e){
            return null;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Não foi possivel realizar a consulta: \n"+e);
            }
        }
    }
    public boolean salvar(Setor setor){
        try{
            String sqlCommand = "insert into setor "
                    + "(nome) "
                    + "values (?)";
            PreparedStatement ps = getConnection().prepareStatement(sqlCommand, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,setor.getNome());
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel salvar o registro: \n"+e);
            return false;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    
    public int verificarSetorExiste(String nome){
        try{
            List<Setor> setores = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("select * from setor where nome=?");
            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Setor setor = new Setor(rs.getInt("id"),rs.getString("nome"));
                setores.add(setor);
            }
            return setores.size();
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel realizar a consulta: \n"+e);
            return -1;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
            }
        }
    }
}
