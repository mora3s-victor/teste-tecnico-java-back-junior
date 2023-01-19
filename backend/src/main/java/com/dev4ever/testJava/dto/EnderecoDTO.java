package com.dev4ever.testJava.dto;

import com.dev4ever.testJava.entities.Endereco;
import com.dev4ever.testJava.entities.enums.TipoEndereco;

public class EnderecoDTO {
	
	private Long id;
	private String logradouro;
	private String cep;
	private Integer numero;
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
