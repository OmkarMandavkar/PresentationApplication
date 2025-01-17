package com.dev.PresentationApp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.entity.User;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.exception.PresentationNotFoundException;
import com.dev.PresentationApp.exception.UserNotFoundException;
import com.dev.PresentationApp.repository.PresentationRepository;
import com.dev.PresentationApp.repository.UserRepository;

@Service
public class PresentationService {

	private PresentationRepository presentationRepository;

	private UserRepository userRepository;

	public PresentationService(PresentationRepository presentationRepository, UserRepository userRepository) {
		this.presentationRepository = presentationRepository;
		this.userRepository = userRepository;
	}

	public String addPresentation(Integer id, Presentation presentation) {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User Not Found"));
		presentation.setUser(user);
		presentationRepository.save(presentation);
		return "added";
	}

	public Optional<Presentation> fetchByPid(Integer pid) {
		return presentationRepository.findByPid(pid);
	}

	public List<Presentation> fetchAllData() {
		return presentationRepository.findAll();
	}

	public String updateStatusByPid(Integer pid, PresentationStatus presentationStatus) {
		Presentation presentation = presentationRepository.findByPid(pid)
				.orElseThrow(() -> new PresentationNotFoundException("Counsellor Not found/Enter registered cid"));
		presentation.setPresentationStatus(presentationStatus);
		presentationRepository.save(presentation);
		return "Status is updated";
	}

}
