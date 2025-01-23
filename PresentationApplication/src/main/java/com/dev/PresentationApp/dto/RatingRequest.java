package com.dev.PresentationApp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingRequest {

	private Integer communication;
    private Integer confidence;
    private Integer content;
    private Integer interaction;
    private Integer liveliness;
    private Integer usageProps;
}
