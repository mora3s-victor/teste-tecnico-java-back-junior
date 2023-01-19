package com.dev4ever.testJava.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev4ever.testJava.entities.Endereco;
import com.dev4ever.testJava.entities.Pessoa;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco,Long>{
	
	List<Endereco> findByPessoa(Pessoa pessoa);
}
