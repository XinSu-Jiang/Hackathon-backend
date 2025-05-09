package jdc.hackathon.service;

import jdc.hackathon.domain.dto.application.ApplicationResponse;
import jdc.hackathon.domain.dto.application.UpdateApplicationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicationService {
    ApplicationResponse apply(Long userId, Long postId);
    void cancel(Long userId, Long applicationId);
    ApplicationResponse respond(Long userId, Long applicationId, UpdateApplicationRequest req);
    Page<ApplicationResponse> getMyApplications(Long userId, Pageable pageable);
    Page<ApplicationResponse> getPostApplications(Long userId, Long postId, Pageable pageable);
}
