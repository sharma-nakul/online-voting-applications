package ns.ws.smsvoting.api;

//import java.util.Date;
import java.util.HashMap;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ns.ws.smsvoting.Moderator;

@RestController
public class VotingController {

	private static int nextId=56788;
	private static HashMap<String, Moderator> moderatorMap = new HashMap<String, Moderator>();

	
/*-----------ADD MODERATOR TO THE SYSTEM--------------------------------------*/	
	@RequestMapping(value="/moderators", method=RequestMethod.POST )
	@ResponseBody
	private static Moderator save(Moderator mod) 
	{
		if (moderatorMap == null) {
			moderatorMap = new HashMap<String, Moderator>();
			nextId = 1;
		}
	//	Date dt=new Date();
	//	SimpleDateFormat dtFormat=new SimpleDateFormat("yyyy-mm-ddkk:mm:ss:SSS.z");
		mod.setId(nextId);;
		nextId = nextId + 1;
		String userId="n"+Integer.toString(nextId)+"s";
	//	String created_at=dtFormat.format(dt);
		Moderator addUser = new Moderator(nextId,mod.getName(),mod.getEmail(),mod.getPassword());
		moderatorMap.put(userId, addUser);
		return addUser;
	}
	
/*-----------------------------VIEW MODERATOR RESOURCE-----------------------------------*/	
	@RequestMapping(value="/moderators/{userId}", method=RequestMethod.GET)
	public Moderator moderatorDetails(@PathVariable("userID") String userId)
	{
	 System.out.println(userId);
	 return moderatorMap.get(userId);
	}
	
	/*static {
		Moderator mod1 = new Moderator();
		mod1.setName("Nakul");
		saveValues(mod1);
		
		Moderator mod2 = new Moderator();
		mod1.setName("Prateek");
		saveValues(mod2);
	}
*/
	}
