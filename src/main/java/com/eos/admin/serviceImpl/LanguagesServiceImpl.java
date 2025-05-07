package com.eos.admin.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eos.admin.dto.EmployeeDto;
import com.eos.admin.dto.LanguageDTO;
import com.eos.admin.entity.Employee;
import com.eos.admin.entity.Language;
import com.eos.admin.repository.EmployeeRepository;
import com.eos.admin.repository.LanguagesRepository;
import com.eos.admin.service.LanguageService;

@Service
public class LanguagesServiceImpl implements LanguageService {
	private final EmployeeRepository employeeRepository;
	private final LanguagesRepository languageRepository;

	@Autowired
	public LanguagesServiceImpl(EmployeeRepository employeeRepository, LanguagesRepository languageRepository) {
		this.employeeRepository = employeeRepository;
		this.languageRepository = languageRepository;
	}

	@Override
	public void saveEmplyeeLanguages(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub

		Employee employee = employeeRepository.findById(employeeDto.getId())
				.orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeDto.getId()));
		List<Language> languages = new ArrayList<>();
		for (LanguageDTO languageDTO : employeeDto.getLanguage()) {
			Language language = new Language();
			language.setLanguageName(languageDTO.getLanguageName());
			language.setCanRead(languageDTO.isCanRead());
			language.setCanWrite(languageDTO.isCanWrite());
			language.setEmployee(employee); // Link language to the employee
			languages.add(language);
		}

		employee.setLanguage(languages);
		employeeRepository.save(employee);
	}

}
