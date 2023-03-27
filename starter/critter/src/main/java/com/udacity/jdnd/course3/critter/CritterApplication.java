package com.udacity.jdnd.course3.critter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Launches the Spring application. Unmodified from starter code.
 */
@SpringBootApplication
public class CritterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CritterApplication.class, args);
	}

    /**
     * Dummy controller class to verify installation success. Do not use for
     * your project work.
     */
    @RestController
    public static class CritterController {

        @GetMapping("/test")
        public String test(){
            return "Critter Starter installed successfully";
        }
    }
}
