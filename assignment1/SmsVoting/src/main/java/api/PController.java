package api;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;

import user.Moderator;
import user.Poll;
import api.MController;

@RestController
public class PController {

	private static final HashMap<String, Poll> pollList = new HashMap<String, Poll>();

	/* Create New Poll in Moderator List */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls", method = RequestMethod.POST)
	@JsonView(DisplayResult.withoutResults.class)
	
	public ResponseEntity<Poll> createPoll(
			@PathVariable("moderator_id") Integer modId, @RequestBody Poll p,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			Moderator m = MController.getModerator(modId);
			Poll newPoll = new Poll(p.getQuestion(), p.getStarted_at(),
					p.getExpired_at(), p.getChoice());
			m.putPollInList(newPoll);
			pollList.putIfAbsent(newPoll.getId(), newPoll);
			return new ResponseEntity<Poll>(newPoll, HttpStatus.CREATED);
		} else {

			return new ResponseEntity("This can be accesed by moderator only",
					HttpStatus.BAD_REQUEST);
		}

	}

	/* View Poll without Results */

	@RequestMapping(value = "api/v1/polls/{poll_id}", method = RequestMethod.GET)
	@JsonView(DisplayResult.withoutResults.class)
	public ResponseEntity<Poll> viewPollWithoutResult(
			@PathVariable("poll_id") String pollId) {
		Poll viewPoll = getPoll(pollId);
		return new ResponseEntity<Poll>(viewPoll, HttpStatus.OK);
	}

	/* View Poll with Result */

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "api/v1/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	@JsonView(DisplayResult.withResults.class)
	public ResponseEntity<Poll> viewPollWithResult(
			@PathVariable("poll_id") String pollId,
			@PathVariable("moderator_id") Integer modId,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
			Moderator m = MController.getModerator(modId);
			Poll viewPoll = m.getModeratorPoll(pollId);
			return new ResponseEntity<Poll>(viewPoll, HttpStatus.OK);

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
			Moderator getMod = MController.getModerator(modId);
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
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity deletePoll(@PathVariable("poll_id") String pollId,
			@PathVariable("moderator_id") Integer modId,
			@RequestHeader(value = "Authorization") String auth) {
		if (Authenticate.isValid(auth)) {
		Moderator m = MController.getModerator(modId);
		m.deleteModeratorPoll(pollId);
		pollList.remove(pollId);
		return new ResponseEntity(HttpStatus.NO_CONTENT );
	} else {

		return new ResponseEntity("This can be accesed by moderator only",
				HttpStatus.BAD_REQUEST);
	}

	}

	/*------------------------------------Vote Poll ---------------------------------------*/
	@RequestMapping(value = "api/v1/polls/{poll_id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@JsonView(DisplayResult.withResults.class)
	public void votePoll(@PathVariable("poll_id") String pollId,
			@RequestParam(value = "choice") Integer choice) {
		Poll votePol = getPoll(pollId);
		if (votePol != null) {
			int voteCount = votePol.getResults().get(choice) + 1;
			votePol.getResults().set(choice, voteCount);
		}
	}

	public static Poll getPoll(String pollId) {
		return pollList.get(pollId);
	}
}
