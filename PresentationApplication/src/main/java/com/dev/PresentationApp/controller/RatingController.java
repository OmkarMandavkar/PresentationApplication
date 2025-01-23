package com.dev.PresentationApp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.PresentationApp.dto.RatingRequest;
import com.dev.PresentationApp.service.RatingService;

@RestController
@RequestMapping(value = "/rating")
public class RatingController {

	private RatingService ratingService;

	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	/*
	 * Used to assign rating to particular presentation associated with user id
	 * @Param: pid, @Param: id
	 */
	@PostMapping(value = "/assignRating")
	public ResponseEntity<String> ratePresentation(@RequestParam Integer pid, @RequestParam Integer id,
			@RequestBody RatingRequest ratingRequest) {
		String response = ratingService.ratePresentation(pid, id, ratingRequest);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}


	/*
	 * Used to fetch all rating of a user who have completed presentations
	 * @Param: id
	 */
	@GetMapping(value = "/getRatingByUserId")
	public ResponseEntity<?> getRatingByUserId(@RequestParam Integer id) {
		List<Map<String, Object>> ratings = ratingService.getRatingsForUser(id);
		if (ratings.isEmpty()) {
			return new ResponseEntity<String>("Enter Valid Student Id", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	/*
	 * Used to fetch all rating of presentation by passing pid
	 * @Param: pid
	 */
	@GetMapping(value = "/getRatingsByPresentationId")
	public ResponseEntity<?> getAllRatingsByPresentationId(@RequestParam Integer pid) {
		List<Map<String, Object>> ratings = ratingService.getAllRatingsByPresentationId(pid);
		if (ratings.isEmpty()) {
			return new ResponseEntity<String>("Presentation does not exits / Presentation not assigned to Student", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(ratings, HttpStatus.OK);
	}

	/*
	 * Used to send a email containing presentation totalScore to Student
	 * @Param: pid, @Param: id
	 */
	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestParam Integer pid, @RequestParam Integer id) {
	    boolean rate = ratingService.sendEmail(pid, id);
	    if (rate) {
	        return ResponseEntity.ok("Email sent successfully!");
	    } else {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error sending email");
	    }
	}

}