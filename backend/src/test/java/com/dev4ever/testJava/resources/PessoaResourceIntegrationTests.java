package com.dev4ever.testJava.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.dev4ever.testJava.dto.PessoaDTO;
import com.dev4ever.testJava.utils.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PessoaResourceIntegrationTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private PessoaDTO pessoaDTO;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalPessoas;
	
	@BeforeEach
	void setUp() {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalPessoas = 6L;
		pessoaDTO = Factory.createPessoaDTO();
	}
	
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(pessoaDTO);		
		
		ResultActions result = 
				mockMvc.perform(put("/pessoas/{id}",nonExistingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void updateShouldReturnPessoaDTOWhenIdExists() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(pessoaDTO);		
		String expectedName = pessoaDTO.getNome();
		
		ResultActions result = 
				mockMvc.perform(put("/pessoas/{id}",existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.nome").value(expectedName));
	}

	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception{
		ResultActions result = 
				mockMvc.perform(get("/pessoas?page=0&size=3&sort=nome,asc")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(countTotalPessoas));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].nome").value("Ana"));
		result.andExpect(jsonPath("$.content[1].nome").value("Igor"));
		result.andExpect(jsonPath("$.content[2].nome").value("Isaac"));
		
	}
		
}





