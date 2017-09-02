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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Prolink
 */
public class ClienteDAO {
    private Connection con;
    
    private Connection getConnection(){
        this.con  = Conexao.getInstance().getConnetion();
        return this.con;
    }
    public boolean excluir(Cliente cliente){
        try{
            PreparedStatement ps = getConnection().prepareStatement("delete from cliente where id = ?");
            ps.setInt(1, cliente.getId());
            return ps.executeUpdate()>0;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, e);
            return false;
        }
    }
    public List<Cliente> listar(String sql){
        try{
            List<Cliente> clientes = new ArrayList<>();
            PreparedStatement ps = getConnection().prepareStatement("select * from cliente as c "
                    + "inner join setor as s on c.setor = s.id where c.id is not null "+sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("c.id"));
                cliente.setNome(rs.getString("c.nome"));
                cliente.setEmail(rs.getString("c.email"));
                cliente.setEndereco(rs.getString("c.endereco"));
                cliente.setAtivo(rs.getInt("c.ativo"));
                cliente.setSetor(rs.getString("c.setor")==null?
                        null:new Setor(rs.getInt("s.id"),rs.getString("s.nome"))
                );
                clientes.add(cliente);
            }
            return clientes;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel realizar a consulta: \n"+e);
            return null;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    public Map<String, Integer> agrupar(){
        try{
            Map<String, Integer> map = new HashMap<>();
            PreparedStatement ps = getConnection().prepareStatement("select if(ativo=1,'Ativo', 'Inativo') as status " +
",count(ativo) situacao from cliente group by ativo");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                map.put(rs.getString(1), rs.getInt(2));
            }
            return map;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel realizar a consulta: \n"+e);
            return null;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    public int salvar(Cliente cliente){
        try{
            String sqlCommand = "insert into cliente "
                    + "(nome,email,setor,endereco,ativo) "
                    + "values (?,?,?,?,?)";
            PreparedStatement ps = getConnection().prepareStatement(sqlCommand, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,cliente.getNome());
            ps.setString(2,cliente.getEmail());
            ps.setString(3,cliente.getSetor()==null?null:cliente.getSetor().getId()+"");
            ps.setString(4,cliente.getEndereco());
            ps.setInt(5, cliente.getAtivo());
            if(ps.executeUpdate()>0){
                ResultSet rs = ps.getGeneratedKeys();
                int key =-1;
                while(rs.next()){
                    key = rs.getInt(1);
                }
                return key;
            }
            else
                return -1;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Não foi possivel salvar o registro: \n"+e);
            return -1;
        }finally{ 
            try{
                con.close();
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }
    public boolean atualizar(Cliente cliente){
        try{
            String sqlCommand = "update cliente set nome=?, email=?, setor=? ,endereco=? ,ativo=? where id=?";
            PreparedStatement ps = getConnection().prepareStatement(sqlCommand);
            ps.setString(1,cliente.getNome());
            ps.setString(2,cliente.getEmail());
            ps.setInt(3,cliente.getSetor()==null?null:cliente.getSetor().getId());
            ps.setString(4,cliente.getEndereco());
            ps.setInt(5, cliente.getAtivo());
            ps.setInt(6, cliente.getId());
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
}
