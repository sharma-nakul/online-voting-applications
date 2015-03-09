package api;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import user.Moderator;

@RestController
public class MController {

	private static HashMap<Integer, Moderator> modList = new HashMap<Integer, Moderator>();
	private String errMsg;

	/* Add Moderator */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators", method = RequestMethod.POST)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Moderator> addModerator(@RequestBody Moderator mod,
			Exception e , @RequestHeader(value = "Authorization") String auth) {

		if (Authenticate.isValid(auth)) {
			if (mod.getName() == null || mod.getName().isEmpty()) {
				errMsg = "Name field cannot be empty. Please provide name";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());

			}

			else if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
				errMsg = "Email field cannot be empty. Please provide email";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());

			}

			else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
				errMsg = "Password cannot be empty. Please provide password";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());

			}

			else {
				Moderator addMod = new Moderator(mod.getName(), mod.getEmail(),
						mod.getPassword());
				modList.put(addMod.getId(), addMod);
				return new ResponseEntity<Moderator>(addMod, HttpStatus.CREATED);
			}
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}
	}

	/* View Moderator */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}", method = RequestMethod.GET)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Moderator> viewModerator(
			@PathVariable("moderator_id") Integer mod_id, Exception e,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			if (mod_id == null) {
				errMsg = "Moderator Id not provided";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());
			}

			else
				return new ResponseEntity<Moderator>(getModerator(mod_id),
						HttpStatus.OK);
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}

	}

	/* Update Moderator */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}", method = RequestMethod.PUT)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<Moderator> updateModerator(
			@RequestBody Moderator mod,
			@PathVariable("moderator_id") Integer mod_id, Exception e,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
				errMsg = "Email field cannot be empty. Please provide email";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());
			}

			else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
				errMsg = "Password cannot be empty. Please provide password";
				e.setExceptionMsg(errMsg);
				throw new Exception(e.getExceptionMsg());
			}

			else {
				Moderator updateMod = getModerator(mod_id);
				updateMod.setEmail(mod.getEmail());
				updateMod.setPassword(mod.getPassword());
				modList.put(mod_id, updateMod);
				return new ResponseEntity<Moderator>(updateMod,
						HttpStatus.CREATED);
			}
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}

	}

	/* Return Moderator object from moderator list */
	public static Moderator getModerator(Integer id) {
		return modList.get(id);
	}

}
