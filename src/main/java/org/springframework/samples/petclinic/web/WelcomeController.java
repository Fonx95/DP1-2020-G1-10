package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	    
		  List<Person> persons = new ArrayList<Person>();
		  Person person = new Person();
		  person.setFirstName("Alfonso");
		  person.setLastName("Masa Calderon");
		  persons.add(person);
		  model.put("persons", persons);
		  model.put("title", "alfmascal project");
		  model.put("group", "no group");
		  return "welcome";
	  }
}
