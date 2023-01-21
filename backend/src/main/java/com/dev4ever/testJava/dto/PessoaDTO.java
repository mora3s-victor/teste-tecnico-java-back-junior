package com.dev4ever.testJava.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import com.dev4ever.testJava.entities.Endereco;
import com.dev4ever.testJava.entities.Pessoa;

public class PessoaDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	@Size(min = 4, max = 60, message = "Deve ter entre 4 e 60 caracteres")	
	@NotBlank(message = "Campo obrigatório")
	private String nome;
	
	@Past(message = "Data de nascimento não pode ser presente ou futura")
	private Instant dataNascimento;
	
	private Set<EnderecoDTO> enderecos = new HashSet<>();
	
	public PessoaDTO() {		
	}

	public PessoaDTO(Long id, String nome, Instant dataNascimento) {
		this.id = id;
		this.nome = nome;
		this.dataNascimento = dataNascimento;
	}
	
	public PessoaDTO(Pessoa pessoa) {
		id = pessoa.getId();
		nome = pessoa.getNome();
		dataNascimento = pessoa.getDataNascimento();
	}
	
	public PessoaDTO(Pessoa pessoa, Set<Endereco> enderecos) {
		this(pessoa);
		enderecos.forEach(end -> this.enderecos.add(new EnderecoDTO(end	)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Instant getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Instant dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Set<EnderecoDTO> getEnderecos() {
		return enderecos;
	}
}
