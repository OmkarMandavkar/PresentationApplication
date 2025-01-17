package com.dev.PresentationApp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.enums.Status;
import com.dev.PresentationApp.service.PresentationService;

@RestController
@RequestMapping(value = "presentation")
public class PresentationController {

	private PresentationService presentationService;

	public PresentationController(PresentationService presentationService) {
		this.presentationService = presentationService;
	}

	@PostMapping("/assign")
	public ResponseEntity<String> addEnquiry(@RequestParam Integer id, @RequestBody Presentation presentation) {
		String added = presentationService.addPresentation(id, presentation);
		return new ResponseEntity<String>("Presentation have been assigned to User with userId: " + id,
				HttpStatus.CREATED);
	}

	@GetMapping("/fetchByPid")
	public ResponseEntity<?> fetchPresentationById(@RequestParam Integer pid) {
		Optional<Presentation> displayData = presentationService.fetchByPid(pid);

		if (displayData.isPresent()) {
			return new ResponseEntity<>(displayData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No Presentation found with pid: " + pid, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/fetchAllPresentations")
	public ResponseEntity<List<Presentation>> fetchAllPresentations() {
	    List<Presentation> fetchAllData = presentationService.fetchAllData();
	    if (!fetchAllData.isEmpty()) {
	        return new ResponseEntity<>(fetchAllData, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PatchMapping("/updatePresentationStatus")
	public ResponseEntity<String> updatePresentationStatus(@RequestParam Integer pid, @RequestParam PresentationStatus presentationStatus ) {
		String updateStatus = presentationService.updateStatusByPid(pid, presentationStatus);
		return new ResponseEntity<String>(updateStatus, HttpStatus.OK);
	}

}