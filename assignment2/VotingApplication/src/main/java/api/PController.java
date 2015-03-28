package api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import user.Moderator;
import user.Poll;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class PController {
	@Autowired
	private ModeratorRepo modRepo;

	//private static final HashMap<String, Poll> pollList = new HashMap<String, Poll>();

	/* Create New Poll in Moderator List */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls", method = RequestMethod.POST)
	@JsonView(DisplayResult.withoutResults.class)
	public ResponseEntity<Poll> createPoll(
			@PathVariable("moderator_id") Integer modId, @RequestBody Poll p,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			Moderator m = modRepo.findById(modId);
			Poll newPoll = new Poll(p.getQuestion(), p.getStarted_at(),	p.getExpired_at(), p.getChoice());
			generatePollId(newPoll, modId);
			m.putPollInList(newPoll);
			modRepo.save(m);
			//pollList.putIfAbsent(newPoll.getId(), newPoll);
			return new ResponseEntity<Poll>(newPoll, HttpStatus.CREATED);
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}

	}

	/* View Poll without Results */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "api/v1/polls/{poll_id}", method = RequestMethod.GET)
	@JsonView(DisplayResult.withoutResults.class)
	public ResponseEntity viewPollWithoutResult(
			@PathVariable("poll_id") String pollId) {
		//Poll viewPoll = getPoll(pollId);
		if(getPollFromModerator(pollId).isEmpty()||getPollFromModerator(pollId)==null)
			return new ResponseEntity("Poll does not exist", HttpStatus.BAD_REQUEST);
		else
		return new ResponseEntity(getPollFromModerator(pollId), HttpStatus.OK);
	}


	/* View Poll with Result */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	@JsonView(DisplayResult.withResults.class)
	public ResponseEntity viewPollWithResult(
			@PathVariable("poll_id") String pollId,
			@PathVariable("moderator_id") Integer modId,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			//Moderator m = MController.getModerator(modId);
			Moderator mod = modRepo.findById(modId);
			if(mod.getModeratorPoll(pollId)==null)
			return new ResponseEntity("Poll does not exist", HttpStatus.BAD_REQUEST);
			else
			return new ResponseEntity(mod.getModeratorPoll(pollId), HttpStatus.OK);

		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}
	}
	
	/* List Polls */
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls", method = RequestMethod.GET)
	@JsonView(DisplayResult.withResults.class)
	public ResponseEntity<ArrayList<Poll>> listAllPollsGet(
			@PathVariable("moderator_id") Integer modId,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			//Moderator getMod = MController.getModerator(modId);
			Moderator getMod = modRepo.findById(modId);
			return new ResponseEntity<ArrayList<Poll>>(getMod.getPollList(),
					HttpStatus.OK);
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}

	}

	/* Delete Poll */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
	public ResponseEntity deletePoll(@PathVariable("poll_id") String pollId,
			@PathVariable("moderator_id") Integer modId,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
		Moderator m = modRepo.findById(modId);
		m.deleteModeratorPoll(pollId);
		modRepo.save(m);
		return new ResponseEntity("Poll Deleted", HttpStatus.NO_CONTENT );
	} else {

		return new ResponseEntity("This can be accesed by moderator only",
				HttpStatus.BAD_REQUEST);
	}}
	
	/* Vote Poll */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/polls/{poll_id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@JsonView(DisplayResult.withResults.class)
	public ResponseEntity votePoll(@PathVariable("poll_id") String pollId,
			@RequestParam(value = "choice") Integer choice) {
		//Poll votePol = getPoll(pollId);
		List<Moderator> modList = new ArrayList<Moderator>();
		modList=modRepo.findAll();
		for(Moderator m : modList)
		{
			if(m.getModeratorPoll(pollId)!=null)
			{Poll votePol = m.getModeratorPoll(pollId);
				if (votePol != null) {
					int voteCount = votePol.getResults().get(choice) + 1;
					votePol.getResults().set(choice, voteCount);
					modRepo.save(m);
				}
			}
		}
		
		return new ResponseEntity("Vote Submitted", HttpStatus.NO_CONTENT);
		
	}

	private void generatePollId(Poll p, Integer modId)
	{
		Date dt = new Date();
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
		String time=f.format(dt);
		Long temp=modId+Long.valueOf(time);
		String s=Long.toUnsignedString(temp, 36).toUpperCase();
		p.setId(s);
	}
	
	private List<Poll> getPollFromModerator(String pollId)
	{
		List<Moderator> modList = new ArrayList<Moderator>();
		List<Poll> pollList=new ArrayList<Poll>();
		modList=modRepo.findAll();
		for(Moderator m : modList)
		{
			if(m.getModeratorPoll(pollId)!=null)
				pollList.add(m.getModeratorPoll(pollId));
		}
		return pollList;
	}
	
	
}
