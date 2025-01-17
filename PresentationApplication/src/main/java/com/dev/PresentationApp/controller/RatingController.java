package com.dev.PresentationApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.PresentationApp.entity.Rating;
import com.dev.PresentationApp.service.RatingService;

@RestController
@RequestMapping(value = "/rating")
public class RatingController {

	private RatingService ratingService;

	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@PostMapping(value = "/rate")
	public ResponseEntity<String> ratePresentation(@RequestParam Integer id, @RequestParam Integer pid,
			@RequestBody Rating rating) {
		String response = ratingService.ratePresentation(id, pid, rating);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping(value = "/getRatingByPid")
	public List<?> getRatingByPid(@RequestParam Integer pid) {

		return ratingService.getRatingByPresentation(pid);
//		return new ResponseEntity<>(rating, HttpStatus.OK);
//		return rating;
	}

//	ratePresentation(sid, pid, rating)
//	getRatingPresentation(pid)
//	getAllRatingsBySid(sid)

}
