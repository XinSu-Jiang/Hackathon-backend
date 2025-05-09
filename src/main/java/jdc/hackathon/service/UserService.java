// UserService.java
package jdc.hackathon.service;

import jdc.hackathon.domain.dto.user.UserDto;
import jdc.hackathon.domain.dto.user.UserRequestDTO;
import jdc.hackathon.domain.dto.user.UserResponseDTO;
import jdc.hackathon.domain.entity.User;
import jdc.hackathon.domain.repository.UserRepository;
import jdc.hackathon.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponseDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Long userId, UserRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        UserMapper.updateEntityFromDto(dto, user);
        return UserMapper.toResponseDto(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        userRepository.deleteById(userId);
    }

    /** 공개 프로필 조회 */
    @Transactional(readOnly = true)
    public UserDto getPublicProfile(Long userId, Long viewerId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return UserMapper.toUserDto(user);
    }
}
