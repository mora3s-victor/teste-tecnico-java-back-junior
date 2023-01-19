package com.dev4ever.testJava.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.dev4ever.testJava.dto.PessoaDTO;
import com.dev4ever.testJava.services.PessoaService;
import com.dev4ever.testJava.services.exceptions.DatabaseException;
import com.dev4ever.testJava.services.exceptions.ResourceNotFoundException;
import com.dev4ever.testJava.utils.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PessoaResource.class)
public class PessoaResourceUnitTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private PessoaService service;	
	private PessoaDTO pessoaDTO;	
	private PageImpl<PessoaDTO> page;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	@BeforeEach
	public void setUp() {
		
		existingId = 1L;
		nonExistingId = 2L;
		dependentId = 3L;
		
		pessoaDTO = Factory.createPessoaDTO();
		page = new PageImpl<>(List.of(pessoaDTO));

		when(service.findAllPaged(any())).thenReturn(page);
		
		when(service.findById(existingId)).thenReturn(pessoaDTO);
		when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		when(service.update(eq(existingId), any())).thenReturn(pessoaDTO);
		when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
		
		when(service.insert(any())).thenReturn(pessoaDTO);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
		
	}
	
	@Test
	public void insertShouldReturnPessoaDTO() throws Exception {
		String jsonBody = objectMapper.writeValueAsString(pessoaDTO);
		ResultActions result = 
				mockMvc.perform(post("/pessoas")
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception{
		ResultActions result = 
				mockMvc.perform(delete("/pessoas/{id}",existingId)
						.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void deleteShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		ResultActions result = 
				mockMvc.perform(delete("/pessoas/{id}",nonExistingId)
						.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldReturnDatabaseExceptionWhenDependentId() throws Exception{
		ResultActions result = 
				mockMvc.perform(delete("/pessoas/{id}",dependentId)
						.accept(MediaType.APPLICATION_JSON));		
		result.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateShouldReturnPessoaDTOWhenIdExists() throws Exception{
		String jsonBody = objectMapper.writeValueAsString(pessoaDTO);
		ResultActions result = 
				mockMvc.perform(put("/pessoas/{id}",existingId)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.dataNascimento").exists());		
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
	public void findAllPagedShouldReturnPage() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/pessoas")
						.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
	}

	@Test
	public void findByIdShouldReturnPessoaWhenIdExists() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/pessoas/{id}",existingId)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.dataNascimento").exists());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/pessoas/{id}",nonExistingId)
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}

}