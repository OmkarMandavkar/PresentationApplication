package com.dev.PresentationApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.enums.Status;

public interface PresentationRepository extends JpaRepository<Presentation, Integer>{

	Optional<Presentation> findByPid(Integer pid);
		
	@Query("SELECT p FROM Presentation p")
    List<Presentation> fetchAllPresentation();
	
}
