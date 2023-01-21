package com.dev4ever.testJava.services.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev4ever.testJava.dto.EnderecoInsertDTO;
import com.dev4ever.testJava.entities.Pessoa;
import com.dev4ever.testJava.repositories.PessoaRepository;
import com.dev4ever.testJava.resources.exceptions.FieldMessage;

public class EnderecoValidator implements ConstraintValidator<EnderecoValid, EnderecoInsertDTO> {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public void initialize(EnderecoValid ann) {
	}

	@Override
	public boolean isValid(EnderecoInsertDTO dto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		Optional<Pessoa> pessoa = null;

		if (dto.getIdPessoa() != null) {		
			pessoa = pessoaRepository.findById(dto.getIdPessoa());
			if (pessoa.isEmpty()) {
				list.add(new FieldMessage("idPessoa", "Id não existe"));
			}
			
		} else {
			list.add(new FieldMessage("idPessoa", "Id nulo"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
