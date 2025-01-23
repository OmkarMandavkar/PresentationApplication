package com.dev.PresentationApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dev.PresentationApp.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

	@Query("SELECT r FROM Rating r WHERE r.presentation.pid = ?1 AND r.user.id = ?2")
	Optional<Rating> findByPidAndId(Integer pid, Integer id);

	@Query("SELECT r FROM Rating r WHERE r.presentation.pid = ?1")
	List<Rating> findByPid(Integer pid);

	@Query("SELECT r FROM Rating r WHERE r.user.id = ?1")
	List<Rating> findByUserid(Integer id);

	@Query("SELECT r.presentation.pid, r.presentation.course, r.presentation.topic, r.totalScore FROM Rating r WHERE r.user.id = ?1")
	List<Object[]> findByUser(Integer id);

	@Query("SELECT r.user.id AS id, r.rid, r.totalScore FROM Rating r WHERE r.presentation.pid = ?1")
	List<Object[]> findAllRatingsByPresentationId(Integer pid);

	@Query("SELECT r FROM Rating r WHERE r.presentation.pid = ?1 AND r.user.id = ?2")
	Rating findRatingByPidAndUserId(Integer pid, Integer id);

}
