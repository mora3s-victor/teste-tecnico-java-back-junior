package com.dev4ever.testJava.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev4ever.testJava.dto.PessoaDTO;
import com.dev4ever.testJava.entities.Pessoa;
import com.dev4ever.testJava.repositories.PessoaRepository;
import com.dev4ever.testJava.services.exceptions.DatabaseException;
import com.dev4ever.testJava.services.exceptions.ResourceNotFoundException;

@Service
public class PessoaService {

	@Autowired
	private PessoaRepository repository;

	@Transactional(readOnly = true)
	public Page<PessoaDTO> findAllPaged(Pageable pageable) {
		Page<Pessoa> list = repository.findAll(pageable);
		return list.map(x -> new PessoaDTO(x,x.getEnderecos()));
	}
	
	@Transactional(readOnly = true)
	public PessoaDTO findById(Long id) {
		Optional<Pessoa> obj = repository.findById(id);
		Pessoa entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
		return new PessoaDTO(entity,entity.getEnderecos());
	}

	@Transactional
	public PessoaDTO insert(PessoaDTO dto) {
		Pessoa entity = new Pessoa();
		entity.setNome(dto.getNome());
		entity.setDataNascimento(dto.getDataNascimento());
		entity = repository.save(entity);
		return new PessoaDTO(entity);
	}

	@Transactional
	public PessoaDTO update(Long id, PessoaDTO dto) {
		try {
			Pessoa entity = repository.getOne(id);
			entity.setNome(dto.getNome());
			entity.setDataNascimento(dto.getDataNascimento());
			entity = repository.save(entity);
			return new PessoaDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado");
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}
}
