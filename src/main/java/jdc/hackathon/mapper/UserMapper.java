// UserMapper.java
package jdc.hackathon.mapper;

import jdc.hackathon.domain.dto.user.UserDto;
import jdc.hackathon.domain.dto.user.UserRequestDTO;
import jdc.hackathon.domain.dto.user.UserResponseDTO;
import jdc.hackathon.domain.entity.User;

public class UserMapper {

    public static UserResponseDTO toResponseDto(User user) {
        if (user == null) return null;
        return UserResponseDTO.builder()
                .id(user.getId())
                .provider(user.getProvider())
                .oauthId(user.getOauthId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .introduction(user.getIntroduction())
                .deokPoints(user.getDeokPoints())
                .seedMoneyBalance(user.getSeedMoneyBalance())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static void updateEntityFromDto(UserRequestDTO dto, User user) {
        user.updateProfile(
                dto.getNickname()     != null ? dto.getNickname()     : user.getNickname(),
                dto.getProfileImage() != null ? dto.getProfileImage() : user.getProfileImage(),
                dto.getIntroduction() != null ? dto.getIntroduction() : user.getIntroduction()
        );
    }

    /** 공개 프로필용 DTO 매핑 */
    public static UserDto toUserDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .introduction(user.getIntroduction())
                .build();
    }
}
