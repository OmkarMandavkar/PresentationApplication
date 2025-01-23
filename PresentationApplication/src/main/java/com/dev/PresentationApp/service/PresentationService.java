package com.dev.PresentationApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.dev.PresentationApp.dto.PresentationRequest;
import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.entity.Rating;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.enums.Role;
import com.dev.PresentationApp.exception.PresentationNotFoundException;
import com.dev.PresentationApp.exception.UserNotFoundException;
import com.dev.PresentationApp.repository.PresentationRepository;
import com.dev.PresentationApp.repository.RatingRepository;
import com.dev.PresentationApp.repository.UserRepository;

@Service
public class PresentationService {

	private UserRepository userRepository;
	private PresentationRepository presentationRepository;
	private RatingRepository ratingRepository;

	public PresentationService(UserRepository userRepository, PresentationRepository presentationRepository,
			RatingRepository ratingRepository) {
		this.userRepository = userRepository;
		this.presentationRepository = presentationRepository;
		this.ratingRepository = ratingRepository;
	}

	public boolean addPresentation(Integer id, PresentationRequest request) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Enter Valid User Id"));

		Optional<Presentation> data = presentationRepository.findByCourseAndTopic(request.getCourse(),
				request.getTopic());
		if(user.getRole() == Role.ADMIN) {
			if (data.isPresent()) {
				return false;
			} else {
				Presentation ppt = new Presentation();
				BeanUtils.copyProperties(request, ppt);
				presentationRepository.save(ppt);
				return true;
			}
		}
		else {
			return false;
		}
		
	}

	public String assignPresentation(Integer pid, Integer id) {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Enter Valid User Id"));

		Presentation presentation = presentationRepository.findByPid(pid)
				.orElseThrow(() -> new PresentationNotFoundException("Presentation Not found"));

		if (user.getRole() != Role.STUDENT) {
			return "Presentation cannot be assigned to Admin";
		}

		Rating ratingExists = ratingRepository.findRatingByPidAndUserId(pid, id);
		if (ratingExists != null) {
			return "Presentation " + pid + " is already assigned to user " + id;
		}

		Rating rating = new Rating();
		rating.setUser(user);
		rating.setPresentation(presentation);

		presentation.setPresentationStatus(PresentationStatus.ONGOING);
		presentationRepository.save(presentation);
		ratingRepository.save(rating);

		return "Presentation " + pid + " is successfully assigned to " + id;
	}

	public Optional<Presentation> fetchByPid(Integer pid) {
		return presentationRepository.findByPid(pid);
	}

	public List<Presentation> fetchAllData() {
		return presentationRepository.findAll();
	}

	public String updateStatusByPid(Integer pid, PresentationStatus presentationStatus) {

		Presentation presentation = presentationRepository.findByPid(pid)
				.orElseThrow(() -> new PresentationNotFoundException("No Presentation Found"));

		presentation.setPresentationStatus(presentationStatus);
		presentationRepository.save(presentation);
		return "Status is updated to " + presentationStatus;
	}

}