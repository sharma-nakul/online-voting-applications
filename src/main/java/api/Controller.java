package api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import user.Moderator;
import user.Poll;

@SuppressWarnings("serial")
@RestController
public class Controller extends HttpServlet {

	/* MODERATOR variables */
	private static int nextId = 123456;
	private static Long generateId;
	private static int n = 1;
	private static String moderator_id;
	private static HashMap<String, Moderator> userMap = new HashMap<String, Moderator>();

	/* POLL variables */
	private static String pollId; // This is alphanumeric

	// HashMap to add New Poll
	private static HashMap<String, Poll> addPoll = new HashMap<String, Poll>();

	// <moderator_id,pollList>
	private static HashMap<String, ArrayList<HashMap<String, Poll>>> pollInfo = new HashMap<String, ArrayList<HashMap<String, Poll>>>();

	/*-------------------------ADD MODERATOR TO THE SYSTEM----------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity addModerator(@RequestBody Moderator mod,
			@RequestHeader(value = "Authorization") String str) {

		if (Authenticate.isValid(str)) {
			if (userMap == null) {
				userMap = new HashMap<String, Moderator>();
				nextId = 123456;
			}
			mod.setId(nextId);
			LocalDateTime localTime = LocalDateTime.now(); // "2014-09-16T13:28:06.419Z"
			String created_at = localTime.toString()+"Z";
			mod.setCreated_at(created_at);
			moderator_id = Integer.toString(nextId);
			Moderator addUser = new Moderator(mod.getId(), mod.getName(),
					mod.getEmail(), mod.getPassword(), mod.getCreated_at());
			userMap.put(moderator_id, addUser);
			nextId = nextId + 1;
			return new ResponseEntity<Moderator>(addUser, HttpStatus.CREATED);

		} else {
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);
		}
	}

	/*-------------------------VIEW MODERATOR RESOURCE--------------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}", method = RequestMethod.GET)
	public ResponseEntity viewModerator(
			@RequestBody @PathVariable("moderator_id") String moderator_id,
			@RequestHeader(value = "Authorization") String str) {
		if (Authenticate.isValid(str))
			return new ResponseEntity(userMap.get(moderator_id), HttpStatus.OK);

		else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

	/*-------------------------UPDATE MODERATOR RESOURCE------------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.CREATED)
	@ResponseBody
	public ResponseEntity updateModerator(HttpServletResponse response,
			@RequestBody Moderator mod,
			@PathVariable("moderator_id") String moderator_id,
			@RequestHeader(value = "Authorization") String str) {
		if (Authenticate.isValid(str)) {
			response.setContentType("application/json");

			System.out.println(moderator_id);
			Moderator userToUpdate = userMap.get(moderator_id); // Fetch stored
																// moderator
																// using
																// moderator_id

			LocalDateTime localTime = LocalDateTime.now(); // "2014-09-16T13:28:06.419Z"
			String created_at = localTime.toString();

			userToUpdate.setEmail(mod.getEmail()); // set new email address
			userToUpdate.setPassword(mod.getPassword()); // set new password
			userToUpdate.setCreated_at(created_at); // Updating time with
													// current
													// time to reflect latest
													// changes.

			userMap.put(moderator_id, userToUpdate);
			return new ResponseEntity(userMap.get(moderator_id),
					HttpStatus.CREATED);
		} else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

	/*-------------------------CREATE POLL for Moderator------------------------*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}/polls", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Poll> addModerator(@RequestBody Poll pol,
			@PathVariable("moderator_id") Long moderator_id,
			@RequestHeader(value = "Authorization") String str) {
		// int flag = 0;
		if (Authenticate.isValid(str)) {

			if (generateId == null) {
				n = 1;
				generateId = moderator_id + (2 * n);
				n++;
			} else
				generateId = moderator_id + (2 * n);

			pollId = Long.toUnsignedString(generateId, 36);
			pol.setId(pollId);

			// Fetching Date & Time from JSON request
			String started_at = pol.getStarted_at();
			String expired_at = pol.getExpired_at();

			// Set results as 0 for all choices.
			int size = pol.getChoice().size();
			ArrayList<Integer> resultSet = new ArrayList<Integer>();
			for (int i = 0; i < size; i++)
				resultSet.add(0);
			pol.setResults(resultSet);

			// Keeping Poll information from JSON request in newPoll object
			Poll newPoll = new Poll(pol.getId(), pol.getQuestion(), started_at,
					expired_at, pol.getChoice(), pol.getResults());

			ArrayList<HashMap<String, Poll>> addPollList = new ArrayList<HashMap<String, Poll>>();
			addPoll.put(pollId, newPoll);
			addPollList.add(addPoll); // add in common poll list
			pollInfo.put(moderator_id.toString(), addPollList); // add in
																// moderator
																// poll list
			Poll showPoll = new Poll(newPoll.getId(), newPoll.getQuestion(),
					newPoll.getStarted_at(), newPoll.getExpired_at(),
					newPoll.getChoice());
			return new ResponseEntity<Poll>(showPoll, HttpStatus.CREATED);
		} else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

	/*-------------------------VIEW POLLS Without Results-----------------------*/
	@RequestMapping(value = "/api/v1/polls/{poll_id}", method = RequestMethod.GET)
	public ResponseEntity<Poll> viewPollWithoutResults(
			@PathVariable("poll_id") String poll_id) {
		HashMap<String, Poll> showPoll = new HashMap<String, Poll>();
		Poll temp = new Poll(addPoll.get(poll_id).getId(), addPoll.get(poll_id)
				.getQuestion(), addPoll.get(poll_id).getStarted_at(), addPoll
				.get(poll_id).getExpired_at(), addPoll.get(poll_id).getChoice());
		showPoll.put(poll_id, temp);
		return new ResponseEntity<Poll>(showPoll.get(poll_id), HttpStatus.OK);
	}

	/*-------------------------------VOTE for POLL------------------------------*/
	@RequestMapping(value = "/api/v1/polls/{poll_id}", method = RequestMethod.PUT)
	// /polls/{poll_id}?choice={choice_index
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public Poll voteForPoll(@PathVariable("poll_id") String poll_id,
			@RequestParam("choice") Integer choice) {
		Poll pollToVote = addPoll.get(poll_id);
		int size = pollToVote.getChoice().size();
		int[] tempArray = new int[size];
		for (int i = 0; i < size; i++) {
			int k = pollToVote.getResults().get(i);
			if (choice == 0) {
				if (i == 0) {
					k = k + 1;
					tempArray[i] = k;
				}
				if (i == 1) {
					tempArray[i] = k;
				}

			}

			if (choice == 1) {

				if (i == 1) {
					k = k + 1;
					tempArray[i] = k;
				}
				if (i == 0) {
					tempArray[i] = k;
				}

			} else
				continue;
		}

		ArrayList<Integer> setArray = new ArrayList<Integer>();
		int s = tempArray.length;
		for (int i = 0; i < s; i++)
			setArray.add(tempArray[i]);
		pollToVote.setResults(setArray);
		Poll newVote = new Poll(poll_id, pollToVote.getQuestion(),
				pollToVote.getStarted_at(), pollToVote.getExpired_at(),
				pollToVote.getChoice(), pollToVote.getResults());
		addPoll.put(poll_id, newVote);
		return addPoll.get(poll_id);

	}

	/*-------------------------VIEW POLL with Results---------------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.GET)
	
	public ResponseEntity viewPollWithResults(
			@PathVariable("poll_id") String poll_id,
			@PathVariable("moderator_id") String moderator_id,
			@RequestHeader(value = "Authorization") String str) {
		if (Authenticate.isValid(str)) {
			Iterator itr = pollInfo.entrySet().iterator();
			HashMap<String, Poll> returnPoll = new HashMap<String, Poll>();
			ArrayList<HashMap<String, Poll>> returnPollList = new ArrayList<HashMap<String, Poll>>();
			while (itr.hasNext()) {
				Map.Entry getValue = (Map.Entry) itr.next();
				if (getValue.getKey().equals(moderator_id)) {
					Poll setPol = new Poll(addPoll.get(poll_id).getId(),
							addPoll.get(poll_id).getQuestion(), addPoll.get(
									poll_id).getStarted_at(), addPoll.get(
									poll_id).getExpired_at(), addPoll.get(
									poll_id).getChoice(), addPoll.get(poll_id)
									.getResults());
					returnPoll.put(poll_id, setPol);
					returnPollList.add(returnPoll);
					break;
				}

			}

			pollInfo.put(moderator_id, returnPollList);
			return new ResponseEntity(pollInfo.get(moderator_id), HttpStatus.OK);
		} else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

	/*-------------------------LIST all POLLS with Results---------------------------*/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}/polls/", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<ArrayList<HashMap<String, Poll>>> ListAllPolls(
			@PathVariable("moderator_id") String moderator_id,
			@RequestHeader(value = "Authorization") String str) {
		if (Authenticate.isValid(str)) {

			return new ResponseEntity<ArrayList<HashMap<String, Poll>>>(
					pollInfo.get(moderator_id), HttpStatus.OK);
		} else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

	/*------------------------------------DELETE Poll--------------------------------*/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/api/v1/moderators/{moderator_id}/polls/{poll_id}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deletePoll(
			@PathVariable("moderator_id") String moderator_id,
			@PathVariable("poll_id") String poll_id,
			@RequestHeader(value = "Authorization") String str) {
		if (Authenticate.isValid(str)) {

			Iterator itPollInfo = pollInfo.entrySet().iterator();
			while (itPollInfo.hasNext()) {
				Map.Entry pollValue = (Map.Entry) itPollInfo.next();
				if (pollValue.getKey().equals(moderator_id.toString())) {
					ArrayList<HashMap<String, Poll>> listPoll = (ArrayList<HashMap<String, Poll>>) pollInfo
							.get(moderator_id);
					Iterator itPollList = listPoll.listIterator();
					while (itPollList.hasNext()) {
						if (listPoll.iterator().next().containsKey(poll_id))
							listPoll.remove(itPollList.next());
						break;

					}
					break;
				}
			}

			return new ResponseEntity(HttpStatus.NO_CONTENT);
		} else
			return new ResponseEntity(
					"This path is accesible by Moderator only",
					HttpStatus.BAD_REQUEST);

	}

}
