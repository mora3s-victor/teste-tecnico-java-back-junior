package com.dev4ever.testJava.utils;

import java.time.Instant;

import com.dev4ever.testJava.dto.PessoaDTO;
import com.dev4ever.testJava.entities.Pessoa;

public class Factory {
	
	public static Pessoa createPessoa() {
		return new Pessoa(1L,"Maria",Instant.parse("2021-11-20T03:00:00Z"));
	}
	
	public static PessoaDTO createPessoaDTO() {
		return new PessoaDTO(createPessoa());
	}
}
