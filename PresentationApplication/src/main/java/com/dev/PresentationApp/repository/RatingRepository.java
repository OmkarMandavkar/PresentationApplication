package com.dev.PresentationApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import com.dev.PresentationApp.entity.Presentation;
import com.dev.PresentationApp.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

	@Query("SELECT r FROM Rating r WHERE r.presentation.pid=:rid")
	List<Rating> fetchRatingByPid(Integer rid);
}
