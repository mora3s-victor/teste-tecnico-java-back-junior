package com.dev4ever.testJava.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev4ever.testJava.dto.EnderecoDTO;
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
	public EnderecoDTO insert(EnderecoDTO dto) {
		try {
			Endereco entity = new Endereco();
			entity = setAddressType(entity, dto);
			entity.setLogradouro(dto.getLogradouro());
			entity.setCep(dto.getCep());
			entity.setNumero(dto.getNumero());
			entity.setPessoa(pessoaRepository.getOne(dto.getIdPessoa()));
			entity = repository.save(entity);
			return new EnderecoDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + dto.getIdPessoa());
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Violação de integridade");
		}
	}

	@Transactional
	public EnderecoDTO update(Long id, EnderecoDTO dto) {
		try {
			Endereco entity = repository.getOne(id);
			entity = setAddressType(entity, dto);
			entity.setLogradouro(dto.getLogradouro());
			entity.setCep(dto.getCep());
			entity.setNumero(dto.getNumero());
			entity.setTipo(dto.getTipo());
			entity = repository.save(entity);
			return new EnderecoDTO(entity);

		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id não encontrado: " + id);
		}
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

	/*Se for o primeiro endereco do usuário, esse será o principal. Caso não seja o primeiro, e o usuário
	queira que o novo endereço seja o principal, então os outros endereços se tornarão secundários 
	e o novo será o principal*/	
		public Endereco setAddressType(Endereco entity, EnderecoDTO dto) {

		List<Endereco> list = repository.findByPessoa(pessoaRepository.getOne(dto.getIdPessoa()));

		TipoEndereco principal = TipoEndereco.PRINCIPAL;
		TipoEndereco secundario = TipoEndereco.SECUNDARIO;

		if (list.isEmpty()) {
			entity.setTipo(principal);
			return entity;
		}

		if (dto.getTipo() == principal) {
			for (Endereco end : list) {
				if (end.getTipo() == principal) {
					end.setTipo(secundario);
					update(end.getId(), new EnderecoDTO(end));
					break;
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
