package jdc.hackathon.service;

import jdc.hackathon.domain.enumType.ApplicationStatus;
import jdc.hackathon.domain.enumType.DonationStatus;

public interface NotificationService {
    // 지원 관련
    void sendApplicationReceived(Long toUserId, Long applicationId, Long postId, String applicantName);
    void sendApplicationResult(Long toUserId, Long applicationId, Long postId, ApplicationStatus result);

    // 후원 관련
    void sendDonationReceived(Long toUserId, Long donationId, Long postId, int amount, String donorName);
    void sendDonationResult(Long toUserId, Long donationId, Long postId, DonationStatus result);

    // 글 상태 변경 관련
    void sendPostFull(Long toUserId, Long postId);
    void sendPostFunded(Long toUserId, Long postId);
}