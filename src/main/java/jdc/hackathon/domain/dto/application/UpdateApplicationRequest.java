package jdc.hackathon.domain.dto.application;

import jakarta.validation.constraints.NotNull;
import jdc.hackathon.domain.enumType.ApplicationStatus;
import lombok.Data;

@Data
public class UpdateApplicationRequest {
    @NotNull
    private ApplicationStatus status;  // ACCEPTED or REJECTED
}