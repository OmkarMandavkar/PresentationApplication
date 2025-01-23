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

import com.dev.PresentationApp.dto.PresentationRequest;
import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.enums.PresentationStatus;
import com.dev.PresentationApp.service.PresentationService;

@RestController
@RequestMapping(value = "presentation")
public class PresentationController {

	private PresentationService presentationService;

	public PresentationController(PresentationService presentationService) {
		this.presentationService = presentationService;
	}

	/*
	 * Used to create Presentations (ADMIN ACCESS ONLY)
	 * @RequestBody presentationRequest (JSON)
	 */
	@PostMapping("/admin/create")
	public ResponseEntity<String> addPresentation(@RequestParam Integer id ,@RequestBody PresentationRequest presentationRequest) {

		boolean presentationAdded = presentationService.addPresentation(id, presentationRequest);

		if (presentationAdded) {
			return new ResponseEntity<String>("Presentation Created Succefully", HttpStatus.CREATED);
		}
		return ResponseEntity.badRequest().body("Already Created");
	}

	/*
	 * Used to assign Presentations to Student (ADMIN ACCESS ONLY)
	 * @Param: pid, @Param: id
	 */

	@PostMapping("/admin/assign")
	public ResponseEntity<String> assignPresentation(@RequestParam Integer pid, @RequestParam Integer id) {

		String result = presentationService.assignPresentation(pid, id);

		if (result.contains("successfully assigned")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/*
	 * Used to fetch records by passing presentation id
	 * @Param: pid
	 */
	@GetMapping("/fetchByPid")
	public ResponseEntity<?> fetchPresentationById(@RequestParam Integer pid) {
		Optional<Presentation> displayData = presentationService.fetchByPid(pid);

		if (displayData.isPresent()) {
			return new ResponseEntity<>(displayData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("No Presentation found with pid: " + pid, HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Used to fetch all records of presentation
	 */
	@GetMapping("/fetchAllPresentations")
	public ResponseEntity<List<Presentation>> fetchAllPresentations() {
		List<Presentation> fetchAllData = presentationService.fetchAllData();
		if (!fetchAllData.isEmpty()) {
			return new ResponseEntity<>(fetchAllData, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/*
	 * Used to update PresentationStatus of a presentation
	 * @Param: pid, @Param: presentationStatus
	 */
	@PatchMapping("/updatePresentationStatus")
	public ResponseEntity<String> updatePresentationStatus(@RequestParam Integer pid, @RequestParam PresentationStatus presentationStatus) {
		String updateStatus = presentationService.updateStatusByPid(pid, presentationStatus);
		return new ResponseEntity<String>(updateStatus, HttpStatus.OK);
	}

}