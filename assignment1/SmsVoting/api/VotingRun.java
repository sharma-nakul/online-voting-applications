package ns.ws.smsvoting.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class VotingRun {

	public static void main(String[] args) {
        SpringApplication.run(VotingRun.class, args);

	}

}
