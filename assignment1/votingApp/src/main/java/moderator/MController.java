package moderator;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MController {

	private static HashMap<Integer, Moderator> modList= new HashMap<Integer,Moderator>();

	
/* Add Moderator */
	//, consumes="application/json", produces="application/json"
	
	@RequestMapping(value = "/api/v1/moderators", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Moderator> addModerator(@RequestBody Moderator mod)
	{
			Moderator addMod=new Moderator(mod.getName(),mod.getEmail(),mod.getPassword());
			modList.put(addMod.getId(), addMod);
			return new ResponseEntity<Moderator>(addMod,HttpStatus.CREATED);			
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value ="/", method=RequestMethod.GET)
	public ResponseEntity getModerator ()
	{
		 return new ResponseEntity("test", HttpStatus.OK);
	}
	
}
