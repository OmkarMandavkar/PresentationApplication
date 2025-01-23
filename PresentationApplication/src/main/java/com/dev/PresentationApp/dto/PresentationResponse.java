package com.dev.PresentationApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PresentationResponse {

	private String course;
	private String topic;
	private Double userTotalScore;
}
