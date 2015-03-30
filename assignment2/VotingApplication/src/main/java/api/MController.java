package api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import user.Moderator;

@RestController
public class MController {

	@Autowired
	private ModeratorRepo repo;
	
	private String errMsg;
	
	
	/* Add Moderator */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators", 
					method = RequestMethod.POST,
					headers = "content-type=application/json")
	@ResponseBody
	public ResponseEntity<Moderator> addModerator(@Valid @RequestBody Moderator mod) {

		if (mod.getName() == null || mod.getName().isEmpty()) {
			errMsg = "Name field cannot be empty. Please provide name";
			return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
		}

		else if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
			errMsg = "Email field cannot be empty. Please provide email";
			return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
		}

		else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
			errMsg = "Password cannot be empty. Please provide password";
			return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
		}
		
		else {
			Moderator addMod = new Moderator(mod.getName(), mod.getEmail(),
					mod.getPassword());
			generateModeratorId(addMod);
			repo.save(addMod);
			return new ResponseEntity<Moderator>(addMod, HttpStatus.CREATED);
		}
	}

	/* View Moderator */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}", 
					method = RequestMethod.GET,
					headers = "content-type=application/json")
	public ResponseEntity<Moderator> viewModerator(@PathVariable("moderator_id") Integer mod_id) 
	{
			if (mod_id == null ) 
			{
				errMsg = "Moderator Id not provided";
				return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
			}

			else{
				if (repo.findById(mod_id)==null)
					return new ResponseEntity("Moderator does not exist", HttpStatus.BAD_REQUEST);
				else
				return new ResponseEntity<Moderator>(repo.findById(mod_id),	HttpStatus.OK);
			}
				
				
		} 

	

	/* Update Moderator */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}", 
					method = RequestMethod.PUT,
					headers = "content-type=application/json")
	@ResponseBody
	public ResponseEntity<Moderator> updateModerator(
			@Valid @RequestBody Moderator mod,
			@PathVariable("moderator_id") Integer mod_id) {
		
		if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
				errMsg = "Email field cannot be empty. Please provide email";
				return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
			}

			else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
				errMsg = "Password cannot be empty. Please provide password";
				return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
			}
			else if (repo.findById(mod_id)==null)
			{	errMsg ="Moderator does not exist";
				return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);	
			}
			else {
				Moderator updateMod = repo.findById(mod_id);
				updateMod.setEmail(mod.getEmail());
				updateMod.setPassword(mod.getPassword());
				repo.save(updateMod);
				return new ResponseEntity<Moderator>(updateMod,
						HttpStatus.CREATED);
			}
		} 
		
	
	
	
	private void generateModeratorId(Moderator mod)
	{
		List<Moderator> checkEmpty= new ArrayList<Moderator>();
		checkEmpty=repo.findAll();
		if(checkEmpty.isEmpty())	
		mod.setId(12346);
		else 
		{
			int id = checkEmpty.get(checkEmpty.size()-1).getId();
			mod.setId(id+1);	
		}
	}
	


}
