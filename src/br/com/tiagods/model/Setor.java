package br.com.tiagods.model;

import java.io.Serializable;

/**
 * Created by Tiago on 19/07/2017.
 */
public class Setor implements Serializable {
    private int id;
    private String nome;

    public Setor(int id, String nome){
    this.id = id;    
    this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
