package com.dev4ever.testJava.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.dev4ever.testJava.dto.PessoaDTO;
import com.dev4ever.testJava.services.exceptions.ResourceNotFoundException;
import com.dev4ever.testJava.utils.Factory;

@SpringBootTest	
@Transactional
public class PessoaServiceIntegrationTests {
	
	@Autowired
	private PessoaService service;

	private Long existingId;
	private Long nonExistingId;

	private PessoaDTO pessoaDTO;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		pessoaDTO = Factory.createPessoaDTO();

	}	

	@Test
	public void findByIdShouldReturnPessoaDTOWhenIdExists() {
		PessoaDTO dto = service.findById(existingId);
		Assertions.assertNotNull(dto);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void updateShouldReturnPessoaDTOWhenIdExists() {
		PessoaDTO updated = service.update(existingId, pessoaDTO);
		Assertions.assertNotNull(updated);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, pessoaDTO);
		});
	}

	@Test
	public void deleteShouldThrowEmptyResultDataAccesExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	@Test
	public void deleteShouldDoNothinWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}
	}