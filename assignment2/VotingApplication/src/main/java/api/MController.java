package api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user.Moderator;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Nakul Sharma
 * This class is used to call all the Moderator API.
 */

@RestController
public class MController {

    @Autowired
    private ModeratorRepo repo;

    private String errMsg;

    /* Add Moderator */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "/api/v1/moderators",
            method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Moderator> addModerator(@Valid @RequestBody Moderator mod) {

        if (mod.getName() == null || mod.getName().isEmpty()) {
            errMsg = "Name field cannot be empty. Please provide name";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
            errMsg = "Email field cannot be empty. Please provide email";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
            errMsg = "Password cannot be empty. Please provide password";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else {
            Moderator addMod = new Moderator(mod.getName(), mod.getEmail(),
                    mod.getPassword());
            generateModeratorId(addMod);
            repo.save(addMod);
            return new ResponseEntity<>(addMod, HttpStatus.CREATED);
        }
    }

    /* View Moderator */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/moderators/{moderator_id}",
            method = RequestMethod.GET)
    public ResponseEntity<Moderator> viewModerator(@PathVariable("moderator_id") Integer mod_id) {
        if (mod_id == null) {
            errMsg = "Moderator Id not provided";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else {
            if (repo.findById(mod_id) == null)
                return new ResponseEntity("Moderator does not exist", HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(repo.findById(mod_id), HttpStatus.OK);
        }
    }


    /* Update Moderator */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @RequestMapping(value = "api/v1/moderators/{moderator_id}",
            method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Moderator> updateModerator(
            @Valid @RequestBody Moderator mod,
            @PathVariable("moderator_id") Integer mod_id) {

        if (mod.getEmail() == null || mod.getEmail().isEmpty()) {
            errMsg = "Email field cannot be empty. Please provide email";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else if (mod.getPassword() == null || mod.getPassword().isEmpty()) {
            errMsg = "Password cannot be empty. Please provide password";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else if (repo.findById(mod_id) == null) {
            errMsg = "Moderator does not exist";
            return new ResponseEntity(errMsg, HttpStatus.BAD_REQUEST);
        } else {
            Moderator updateMod = repo.findById(mod_id);
            updateMod.setEmail(mod.getEmail());
            updateMod.setPassword(mod.getPassword());
            repo.save(updateMod);
            return new ResponseEntity<>(updateMod,
                    HttpStatus.CREATED);
        }
    }

    /* Generate Moderator ID for new Moderator */
    private void generateModeratorId(Moderator mod) {
        List<Moderator> checkEmpty = repo.findAll();
        if (checkEmpty.isEmpty())
            mod.setId(12346);
        else {
            int id = checkEmpty.get(checkEmpty.size() - 1).getId();
            mod.setId(id + 1);
        }
    }



}
