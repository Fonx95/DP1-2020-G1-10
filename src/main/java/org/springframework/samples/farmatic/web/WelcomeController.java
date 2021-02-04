package org.springframework.samples.farmatic.web;

import java.util.Map;

import org.springframework.samples.farmatic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class WelcomeController {
	
	
	@GetMapping({"/","/welcome"})
	public String welcome(Map<String, Object> model) {	    
		List<Person> persons = new ArrayList<Person>();
		Person person = new Person();
		person.setFirstName("Alfonso");
		person.setLastName("Masa Calderon");
		persons.add(person);
		Person person1 = new Person();
		person1.setFirstName("Fernando");
		person1.setLastName("Madroñal Rodriguez");
		persons.add(person1);
		Person person2 = new Person();
		person2.setFirstName("Antonio");
		person2.setLastName("Rosado Barrera");
		persons.add(person2);
		Person person3 = new Person();
		person3.setFirstName("Gabriel");
		person3.setLastName("Gutierrez Prieto");
		persons.add(person3);
		Person person4 = new Person();
		person4.setFirstName("Francisco Jose");
		person4.setLastName("Vargas Castro");
		persons.add(person4);
		model.put("persons", persons);
		model.put("title", "Farmatic Proyect");
		model.put("group", "10");
		log.info("Se ha mostrado la pagina de inicio");
		return "welcome";
	}
}
