package api;

import org.springframework.data.mongodb.repository.MongoRepository;
import user.Moderator;

public interface ModeratorRepo extends MongoRepository <Moderator, String> {

        public Moderator findById(int id);
        
}
