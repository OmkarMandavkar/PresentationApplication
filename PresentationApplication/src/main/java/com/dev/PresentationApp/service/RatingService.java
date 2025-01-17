package com.dev.PresentationApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.entity.Rating;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.exception.PresentationNotFoundException;
import com.dev.PresentationApp.exception.UserNotFoundException;
import com.dev.PresentationApp.repository.PresentationRepository;
import com.dev.PresentationApp.repository.RatingRepository;
import com.dev.PresentationApp.repository.UserRepository;

@Service
public class RatingService {

	private UserRepository userRepository;

	private PresentationRepository presentationRepository;

	private RatingRepository ratingRepository;

	public RatingService(UserRepository userRepository, PresentationRepository presentationRepository,
			RatingRepository ratingRepository) {
		this.userRepository = userRepository;
		this.presentationRepository = presentationRepository;
		this.ratingRepository = ratingRepository;
	}

	public String ratePresentation(Integer id, Integer pid, Rating rating) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));

		Presentation presentation = presentationRepository.findById(pid).filter(p -> p.getUser().getId().equals(id))
				.orElseThrow(() -> new PresentationNotFoundException("Presentation not found or not assigned to user"));

		// REMINDER: check presentationStatus before calculating the score
		// check whether the status is completed or not

		if (presentation.getPresentationStatus() == PresentationStatus.COMPLETED) {
			return "Presentation already Completed";
		}

		else if (presentation.getPresentationStatus() == PresentationStatus.ASSIGNED
				|| presentation.getPresentationStatus() == PresentationStatus.ONGOING) {
			Double totalScore = calculateTotalScore(rating);

			rating.setUser(user);
			rating.setPresentation(presentation);
			rating.setTotalScore(totalScore);
			presentation.setPresentationStatus(PresentationStatus.COMPLETED);
			ratingRepository.save(rating);

			return "Rating successfully added with a total score of " + totalScore;
		}
		return "Enter valid Pid";
	}

	private Double calculateTotalScore(Rating rating) {

		int communication = 0;
		int confidence = 0;
		int content = 0;
		int interaction = 0;
		int liveliness = 0;
		int usageProps = 0;

		communication = rating.getCommunication();
		confidence = rating.getConfidence();
		content = rating.getContent();
		interaction = rating.getInteraction();
		liveliness = rating.getLiveliness();
		usageProps = rating.getUsageProps();

		double total = (communication + confidence + content + interaction + liveliness + usageProps) / 6.0;
		return Math.round(total * 10) / 10.0;
	}

	public List<Rating> getRatingByPresentation(Integer pid) {

//		Presentation presentation = presentationRepository.findByPid(pid)
//				.orElseThrow(() -> new PresentationNotFoundException("No Presentation Found"));

		List<Rating> present = ratingRepository.fetchRatingByPid(pid);

		if (present.isEmpty()) {
			throw new PresentationNotFoundException("Not Completed");
		} else {
			return ratingRepository.fetchRatingByPid(pid);
		}
		
//		if (presentation.getPresentationStatus() == PresentationStatus.ASSIGNED ||presentation.getPresentationStatus() == PresentationStatus.ONGOING) {
//			return PresentationNotFoundException("Not Completed");
//		}
//		else {
//			return ratingRepository.fetchRatingByPid(pid);
//		}
			
	}
}