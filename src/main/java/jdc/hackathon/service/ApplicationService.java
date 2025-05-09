package jdc.hackathon.service;

import jdc.hackathon.domain.dto.application.ApplicationResponse;
import jdc.hackathon.domain.dto.application.UpdateApplicationRequest;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(Long userId, Long postId);
    void cancel(Long userId, Long applicationId);
    ApplicationResponse respond(Long userId, Long applicationId, UpdateApplicationRequest req);
    List<ApplicationResponse> getMyApplications(Long userId);
    List<ApplicationResponse> getPostApplications(Long userId, Long postId);
}
