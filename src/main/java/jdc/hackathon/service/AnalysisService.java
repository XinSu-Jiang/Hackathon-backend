package jdc.hackathon.service;

import jdc.hackathon.domain.dto.post.PostResponse;
import jdc.hackathon.domain.dto.review.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AnalysisService {

    private final DonationPostService postService;      // 기존 서비스
    private final ReviewService reviewService;          // 기존 서비스
    private final OpenAiService openAiService;

    public String analyzeUserActivity(Long userId) {
        // 1) 내가 쓴 글들
        List<PostResponse> posts = postService.findPostsByUser(userId);

        // 2) 내가 받은 칭찬(리뷰)
        List<ReviewResponseDTO> reviews = reviewService.findReceivedReviews(userId);

        // 3) Chat API 메시지 작성
        List<Map<String,String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role","system",
                "content","당신은 재능기부 플랫폼의 활동 분석 도우미입니다."
        ));
        messages.add(Map.of(
                "role","user",
                "content", buildPrompt(posts, reviews)
        ));

        // 4) OpenAI에 요청하고 결과 리턴
        return openAiService.summarizeAnalysis(messages);
    }

    private String buildPrompt(List<PostResponse> posts, List<ReviewResponseDTO> reviews) {
        StringBuilder sb = new StringBuilder();

        sb.append("아래는 사용자가 작성한 재능기부 글 목록입니다:\n");
        for (PostResponse p : posts) {
            sb.append("- [").append(p.getId()).append("] ")
                    .append(p.getTitle()).append(" (")
                    .append(p.getDonationDate()).append(")\n");
        }
        sb.append("\n아래는 사용자가 받은 칭찬(리뷰)입니다:\n");
        for (ReviewResponseDTO r : reviews) {
            sb.append("- [").append(r.getId()).append("] 리뷰어:")
                    .append(r.getReviewer().getNickname())
                    .append(" 코멘트: ").append(r.getComment()).append("\n");
        }

        sb.append("\n위 활동을 바탕으로 '이 사용자가 어떤 재능기부를 해 왔고, 어떤 장점(칭찬)을 많이 받았는지'  세 문장으로 요약·분석해 주세요.");
        return sb.toString();
    }
}