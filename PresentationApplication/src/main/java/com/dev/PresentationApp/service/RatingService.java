package com.dev.PresentationApp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.dev.PresentationApp.dto.RatingRequest;
import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.entity.Rating;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.enums.Role;
import com.dev.PresentationApp.exception.PresentationNotFoundException;
import com.dev.PresentationApp.exception.RatingNotFoundException;
import com.dev.PresentationApp.exception.UserNotFoundException;
import com.dev.PresentationApp.repository.PresentationRepository;
import com.dev.PresentationApp.repository.RatingRepository;
import com.dev.PresentationApp.repository.UserRepository;

@Service
public class RatingService {

	private UserRepository userRepository;

	private PresentationRepository presentationRepository;

	private RatingRepository ratingRepository;

	private JavaMailSender mailSender;

	public RatingService(UserRepository userRepository, PresentationRepository presentationRepository,
			RatingRepository ratingRepository, JavaMailSender mailSender) {
		super();
		this.userRepository = userRepository;
		this.presentationRepository = presentationRepository;
		this.ratingRepository = ratingRepository;
		this.mailSender = mailSender;
	}

	public boolean sendEmail(Integer pid, Integer id) {

		try {
			User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Enter Valid User Id"));

			Presentation presentation = presentationRepository.findByPid(pid)
					.orElseThrow(() -> new PresentationNotFoundException("Presentation Not found"));

			Rating rating = ratingRepository.findByPidAndId(pid, id)
					.orElseThrow(() -> new RatingNotFoundException("No rating found"));

			String subjectData = "Dear Candidate," 
					+ "\nYou have succefully completed the Presentation Assessment"
					+ "\n\nCOURSE: " + presentation.getCourse() 
					+ "\nTOPIC: " + presentation.getTopic() + "\nRATING: "
					+ rating.getTotalScore();

			SimpleMailMessage msg = new SimpleMailMessage();
			msg.setFrom("omkarmandavkar000@gmail.com");
			msg.setTo(user.getEmail());
			msg.setSubject("Presentation Results");
			msg.setText(subjectData);

			mailSender.send(msg);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String ratePresentation(Integer pid, Integer id, RatingRequest ratingRequest) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Enter Valid User Id"));

		if (user.getRole() != Role.STUDENT) {
			return "Presentation cannot be assigned to Admin";
		}

		Rating ratingData = ratingRepository.findByPidAndId(pid, id)
				.orElseThrow(() -> new RatingNotFoundException("Presentation is not assigned"));

		if (ratingData.getTotalScore() != null) {
			return "Total score already exists";
		}

		ratingData.setCommunication(ratingRequest.getCommunication());
		ratingData.setConfidence(ratingRequest.getConfidence());
		ratingData.setContent(ratingRequest.getContent());
		ratingData.setInteraction(ratingRequest.getInteraction());
		ratingData.setLiveliness(ratingRequest.getLiveliness());
		ratingData.setUsageProps(ratingRequest.getUsageProps());

		Double totalScore = calculateTotalScore(ratingData);
		ratingData.setTotalScore(totalScore);

		ratingRepository.save(ratingData);

		updateAverageTotalScoreInPresentation(pid);
		updateAverageTotalScoreInUser(id);

		return "Total score calculated successfully: " + totalScore;
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

		double total = (double) (communication + confidence + content + interaction + liveliness + usageProps) / 6;

		return Math.round(total * 10) / 10.0;
	}

	public void updateAverageTotalScoreInPresentation(Integer pid) {

		List<Rating> ratings = ratingRepository.findByPid(pid);

		if (ratings.isEmpty()) {
			throw new RatingNotFoundException("No ratings found for the given Presentation ID");
		}

		double totalScoreSum = 0.0;
		int count = 0;

		for (Rating rating : ratings) {
			if (rating.getTotalScore() != null) {
				totalScoreSum = totalScoreSum + rating.getTotalScore();
				count++;
			}
		}

		if (count == 0) {
			throw new RatingNotFoundException("No valid scores found to calculate the average.");
		}

		double averageScore = Math.round((totalScoreSum / count) * 10.0) / 10.0;

		Presentation presentation = presentationRepository.findById(pid)
				.orElseThrow(() -> new PresentationNotFoundException("Presentation not found for ID: " + pid));

		presentation.setUserTotalScore(averageScore);
		presentation.setPresentationStatus(PresentationStatus.COMPLETED);
		presentationRepository.save(presentation);
	}

	public void updateAverageTotalScoreInUser(Integer id) {

		List<Rating> ratings = ratingRepository.findByUserid(id);

		if (ratings.isEmpty()) {
			throw new RatingNotFoundException("No ratings found for the given Presentation ID");
		}

		double totalScoreSum = 0.0;
		int count = 0;

		for (Rating rating : ratings) {
			if (rating.getTotalScore() != null) {
				totalScoreSum = totalScoreSum + rating.getTotalScore();
				count++;
			}
		}

		if (count == 0) {
			throw new RatingNotFoundException("No valid scores found to calculate the average.");
		}

		double averageScore = Math.round((totalScoreSum / count) * 10.0) / 10.0;

		User user = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("User not found for ID: " + id));

		user.setUserTotalScore(averageScore);
		userRepository.save(user);
	}

	public List<Map<String, Object>> getRatingsForUser(Integer id) {
		List<Object[]> rawData = ratingRepository.findByUser(id);
		List<Map<String, Object>> result = new ArrayList<>();

		for (Object[] record : rawData) {
			Map<String, Object> entry = new HashMap<>();
			entry.put("pid", record[0]);
			entry.put("course", record[1]);
			entry.put("topic", record[2]);
			entry.put("totalScore", record[3]);
			result.add(entry);
		}
		return result;
	}

	public List<Map<String, Object>> getAllRatingsByPresentationId(Integer pid) {
		List<Object[]> rawData = ratingRepository.findAllRatingsByPresentationId(pid);
		List<Map<String, Object>> result = new ArrayList<>();

		for (Object[] record : rawData) {
			Map<String, Object> entry = new HashMap<>();
			entry.put("User id", record[0]);
			entry.put("Rating id", record[1]);
			entry.put("Total Score", record[2]);
			result.add(entry);
		}
		return result;
	}

}