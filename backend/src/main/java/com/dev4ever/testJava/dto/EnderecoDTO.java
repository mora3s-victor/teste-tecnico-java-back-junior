package com.dev4ever.testJava.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.dev4ever.testJava.entities.Endereco;
import com.dev4ever.testJava.entities.enums.TipoEndereco;


public class EnderecoDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 5, max = 100, message = "Deve ter entre 4 e 100 caracteres")	
	private String logradouro;
	
	@Size(min = 8, max = 8, message = "CEP deve ter 8 caracteres")	
	private String cep;
	
	@Positive(message = "Número deve ser positivo")
	@NotNull(message = "Campo obrigatório")
	private Integer numero;
	
	@NotNull(message = "Não pode ser nulo")
	private TipoEndereco tipo;

	private Long idPessoa;

	public EnderecoDTO() {		
	}

	public EnderecoDTO(Long id, String logradouro, String cep, Integer numero,TipoEndereco tipo, Long idPessoa) {
		this.id = id;
		this.logradouro = logradouro;
		this.cep = cep;
		this.numero = numero;
		this.tipo = tipo;
		this.idPessoa = idPessoa;
	}
	
	public EnderecoDTO(Endereco endereco) {
		id = endereco.getId();
		logradouro = endereco.getLogradouro();
		cep = endereco.getCep();
		numero = endereco.getNumero();
		tipo = endereco.getTipo();
		idPessoa = endereco.getPessoa().getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	public TipoEndereco getTipo() {
		return tipo;
	}

	public void setTipo(TipoEndereco tipo) {
		this.tipo = tipo;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}	
}
