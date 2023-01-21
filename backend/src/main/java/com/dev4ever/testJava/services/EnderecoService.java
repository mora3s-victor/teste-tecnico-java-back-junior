package com.dev4ever.testJava.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev4ever.testJava.dto.EnderecoDTO;
import com.dev4ever.testJava.dto.EnderecoInsertDTO;
import com.dev4ever.testJava.entities.Endereco;
import com.dev4ever.testJava.entities.enums.TipoEndereco;
import com.dev4ever.testJava.repositories.EnderecoRepository;
import com.dev4ever.testJava.repositories.PessoaRepository;
import com.dev4ever.testJava.services.exceptions.DatabaseException;
import com.dev4ever.testJava.services.exceptions.ResourceNotFoundException;

@Service
public class EnderecoService {

	@Autowired
	private EnderecoRepository repository;

	@Autowired
	private PessoaRepository pessoaRepository;

	@Transactional(readOnly = true)
	public Page<EnderecoDTO> findAllPaged(Pageable pageable) {
		Page<Endereco> list = repository.findAll(pageable);
		return list.map(x -> new EnderecoDTO(x));
	}

	@Transactional(readOnly = true)
	public EnderecoDTO findById(Long id) {
		Optional<Endereco> obj = repository.findById(id);
		Endereco entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id não encontrado"));
		return new EnderecoDTO(entity);
	}

	@Transactional
	public EnderecoDTO insert(EnderecoInsertDTO dto) {

		Endereco entity = new Endereco();
		entity.setLogradouro(dto.getLogradouro());
		entity.setCep(dto.getCep());
		entity.setNumero(dto.getNumero());
		entity.setPessoa(pessoaRepository.getOne(dto.getIdPessoa()));
		entity = setAddressType(entity, dto);
		entity = repository.save(entity);
		return new EnderecoDTO(entity);
	}

	@Transactional
	public EnderecoDTO update(Long id, EnderecoDTO dto) {

		Endereco entity = repository.getOne(id);
		entity.setLogradouro(dto.getLogradouro());
		entity.setCep(dto.getCep());
		entity.setNumero(dto.getNumero());
		entity = setAddressType(entity, dto);
		entity = repository.save(entity);
		return new EnderecoDTO(entity);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id não encontrado");
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}

	/*
	 * Se for o primeiro endereco do usuário, esse será o principal. Caso não seja o
	 * primeiro, e o usuário queira que o novo endereço seja o principal, então os
	 * outros endereços se tornarão secundários e o novo será o principal
	 */
	public Endereco setAddressType(Endereco entity, EnderecoDTO dto) {

		List<Endereco> list = new ArrayList<>();

		TipoEndereco principal = TipoEndereco.PRINCIPAL;
		TipoEndereco secundario = TipoEndereco.SECUNDARIO;
		
		if (entity.getPessoa() != null) {
			list = repository.findByPessoa(entity.getPessoa());	
			if (list.isEmpty()) {
				entity.setTipo(principal);
				return entity;
			}
		}

		if (dto.getTipo() == principal) {
			for (Endereco end : list) {
				if (end.getTipo() == principal) {
					end.setTipo(secundario);
					update(end.getId(), new EnderecoDTO(end));
				
				}
			}
			entity.setTipo(principal);
			return entity;
		} else {
			entity.setTipo(secundario);
			return entity;
		}
	}
}
