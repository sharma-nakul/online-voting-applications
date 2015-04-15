package api;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.Moderator;
import user.Poll;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Nakul Sharma
 * This class is used to call all the Poll API.
 */

@RestController
public class PController {
    private static final Logger logger = Logger.getLogger(PController.class);
    private static ArrayList<Poll> modPollList = null;
    // private static ArrayList<Poll> expiredPoll = new ArrayList<>();
    private static ArrayList<String> msg;
    @Autowired
    private ModeratorRepo modRepo;


    public PController() {
        msg = new ArrayList<>();
//        msg=null;
    }

    /* Create New Poll in Moderator List */

    @RequestMapping(value = "api/v1/moderators/{moderator_id}/polls",
            method = RequestMethod.POST)
    @JsonView(DisplayResult.withoutResults.class)
    public ResponseEntity<Poll> createPollByModeratorId(
            @PathVariable("moderator_id") Integer modId, @Valid @RequestBody Poll p) {

        Moderator m = modRepo.findById(modId);
        Poll newPoll = new Poll(p.getQuestion(), p.getStarted_at(), p.getExpired_at(), p.getChoice());
        generatePollId(newPoll, modId);
        m.putPollInList(newPoll);
        modRepo.save(m);
        //  pollRepo.save(newPoll);
        return new ResponseEntity<>(newPoll, HttpStatus.CREATED);
    }


	/* View Poll without Results */

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "api/v1/polls/{poll_id}",
            method = RequestMethod.GET)
    @JsonView(DisplayResult.withoutResults.class)
    public ResponseEntity viewPollWithoutResult(
            @PathVariable("poll_id") String pollId) {
        if (getPollFromModerator(pollId).isEmpty() || getPollFromModerator(pollId) == null)
            return new ResponseEntity("Poll does not exist", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity(getPollFromModerator(pollId), HttpStatus.OK);
    }


	/* View Poll with Result */

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/moderators/{moderator_id}/polls/{poll_id}",
            method = RequestMethod.GET)
    @JsonView(DisplayResult.withResults.class)
    public ResponseEntity viewPollWithResult(
            @PathVariable("poll_id") String pollId,
            @PathVariable("moderator_id") Integer modId) {

        Moderator mod = modRepo.findById(modId);
        if (mod.getModeratorPoll(pollId) == null)
            return new ResponseEntity("Poll does not exist", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity(mod.getModeratorPoll(pollId), HttpStatus.OK);

    }

	/* List Polls */

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/moderators/{moderator_id}/polls",
            method = RequestMethod.GET)
    @JsonView(DisplayResult.withResults.class)
    public ResponseEntity<ArrayList<Poll>> listAllPollsByModeratorId(
            @PathVariable("moderator_id") Integer modId) {

        Moderator getMod = modRepo.findById(modId);
        if (getMod == null)
            return new ResponseEntity("No poll created by this moderator", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<>(getMod.getPollList(),
                    HttpStatus.OK);
    }


	/* Delete Poll */

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/moderators/{moderator_id}/polls/{poll_id}",
            method = RequestMethod.DELETE)
    public ResponseEntity deletePoll(@PathVariable("poll_id") String pollId,
                                     @PathVariable("moderator_id") Integer modId) {

        Moderator m = modRepo.findById(modId);
        m.deleteModeratorPoll(pollId);
        modRepo.save(m);
        //  pollRepo.delete(pollId);
        return new ResponseEntity("Poll Deleted", HttpStatus.NO_CONTENT);
    }

	/* Vote Poll */

    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/polls/{poll_id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonView(DisplayResult.withResults.class)
    public ResponseEntity votePoll(@PathVariable("poll_id") String pollId,
                                   @RequestParam(value = "choice") Integer choice) {

        List<Moderator> modList = modRepo.findAll();
        for (Moderator m : modList) {
            if (m.getModeratorPoll(pollId) != null) {
                Poll votePol = m.getModeratorPoll(pollId);
                if (votePol != null) {
                    int voteCount = votePol.getResults().get(choice) + 1;
                    votePol.getResults().set(choice, voteCount);
                    modRepo.save(m);
                    //pollRepo.save(votePol);
                }
            }
        }

        return new ResponseEntity("Vote Submitted", HttpStatus.NO_CONTENT);

    }

    /* Generate Poll ID with a combination of moderator_id and timestamp */

    private void generatePollId(Poll p, Integer modId) {
        Date dt = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddhhmmss");
        String time = f.format(dt);
        Long temp = modId + Long.valueOf(time);
        String s = Long.toUnsignedString(temp, 36).toUpperCase();
        p.setId(s);
    }

    private List<Poll> getPollFromModerator(String pollId) {
        List<Poll> pollList = new ArrayList<>();
        List<Moderator> modList = modRepo.findAll();
        for (Moderator m : modList) {
            if (m.getModeratorPoll(pollId) != null)
                pollList.add(m.getModeratorPoll(pollId));
        }
        return pollList;
    }

// ---------------------------------Extra Code : Assignment 2------------------------------------------------

    @RequestMapping(value = "api/v1/moderators/expired_polls",
            method = RequestMethod.GET)
    @JsonView(DisplayResult.withResults.class)
    public ResponseEntity getPollList() {
        ConcurrentHashMap<String, ArrayList<String>> expiredPolls = getExpiredPoll();
            return new ResponseEntity<>(expiredPolls, HttpStatus.OK);
    }


    public ConcurrentHashMap<String, ArrayList<String>> getExpiredPoll() {
        List<Moderator> modRepoAll = modRepo.findAll();
        ConcurrentHashMap<String, ArrayList<String>> allPolls = new ConcurrentHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String myInfo = "010090730";

        try {
            if (modRepoAll == null)
                logger.info("Moderator returning null");
            else {
                for (Moderator m : modRepoAll) {
                    String key = m.getEmail();
                    ArrayList<String> tempMsg = new ArrayList<>();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
                    LocalDateTime ldt = LocalDateTime.now();
                    Date currentDate = sdf.parse(ldt.format(dtf));
                    if (m.getPollList() != null) {
                        for (Poll p : m.getPollList()) {
                            ArrayList<String> pollChoice = p.getChoice();
                            String time = (p.getExpired_at().substring(0, 10)) + " " + (p.getExpired_at().substring(11, 19));
                            ArrayList<Integer> pollResult = p.getResults();
                            Date expiredDate = sdf.parse(time);
                            if (expiredDate.before(currentDate) && p.getFlag() == 0) {
                                p.setFlag(1);
                                String temp = m.getEmail() + ":" + myInfo + ":Poll Result[" + pollChoice.get(0) + "=" + pollResult.get(0) + "," + pollChoice.get(1) + "=" + pollResult.get(1) + "]";
                                tempMsg.add(temp);
                                modRepo.save(m);
                            }
                        }
                        allPolls.put(key, tempMsg);
                    }
                }
            }
            for (HashMap.Entry<String, ArrayList<String>> entry : allPolls.entrySet()) {
                String key = entry.getKey();
                ArrayList<String> pollValue = entry.getValue();
                if (pollValue.isEmpty())
                    allPolls.remove(key, pollValue);
            }
        } catch (NullPointerException e) {
            logger.info(e.getMessage());
            logger.info("No Poll Found, NullPointException generated");
        } catch (IndexOutOfBoundsException e) {
            logger.info(e.getMessage());
            logger.info("IndexOutOfBoundsException");
        } catch (NoSuchElementException e) {
            logger.info(e.getMessage());
            logger.info("Error retrieving data, NoSuchElementException generated");
        } catch (ParseException e) {
            logger.info(e.getMessage());
            logger.info("Cannot parse date, ParseException generated");
        }
        return allPolls;
    }
}
