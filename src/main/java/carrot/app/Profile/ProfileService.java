package carrot.app.Profile;

import carrot.app.User.UserVo;
import carrot.app.mapper.ProfileMapper;
import carrot.app.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;




@Service
@RequiredArgsConstructor
public class ProfileService {

    @Autowired
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;
    private final ImageService imageService;

    private static final String PROFILE_IMAGE_PATH = "src/main/resources/static/images/profile/";


    @Transactional
    public void createNewProfile(Integer userNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = (UserVo) authentication.getPrincipal();
        ProfileVo profile = new ProfileVo();
        profile.setUser_nick(userVo.getUnick());
        profile.setUser_num(userNum);
        profile.setGoal(""); // 목표 초기값 설정 (이 부분은 필요에 따라 변경)

        profileMapper.saveUserGoal(profile);
        System.out.println("새로운 프로필이 생성되었습니다.");
    }

    @Transactional
    public void saveUserGoal(String goal, String newNick) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserVo) {
            UserVo userVo = (UserVo) authentication.getPrincipal();

            String nickname = userVo.getUnick();
            int nicknameCount = countUserNickname(nickname);
            //기존닉네임 (userVO)

            if (nicknameCount == 1) {
                ProfileVo existingProfile = profileMapper.getProfileByNickname(nickname);
                //profileVo에서 'nick'을 가져옴

                if (existingProfile != null) {
                    // 새로운 닉네임 적용
                    if (!nickname.equals(newNick)) {
                        int newNickCount = countUserNickname(newNick);
                        if (newNickCount == 0) {
                            userMapper.updateProfileNickname(newNick, nickname);
                            userVo.setUnick(newNick);
                            existingProfile.setGoal(goal);
                            //userMapper.updateProfileNickname(newNick, nickname); // 닉네임 변경
                        } else {
                            System.out.println("이미 존재하는 닉네임입니다.");
                            return; // 이미 존재하는 닉네임일 경우, 함수를 종료하고 아무 작업도 하지 않음
                        }
                    } else {
                        existingProfile.setGoal(goal);
                    }
                    profileMapper.updateProfileGoal(existingProfile); // 목표 업데이트 (닉네임 변경은 안 함)
                } else {

                }
            } else {
                // nicknameCount가 1이 아니면, 최초 로그인이 아니므로 아무 작업도 하지 않음
                return;
            }
        } else {
            throw new IllegalArgumentException("Authentication or UserVo is not available.");
        }
    }


    private int countUserNickname(String nickname) {
        return userMapper.countUserByNickname(nickname);
    }

    public ProfileVo getProfileByUserNum(Integer userNum) {
        ProfileVo profileVo = profileMapper.getProfileByUserNum(userNum);
        if (profileVo == null) {
            // 프로필 정보가 없는 경우 빈 ProfileVo 객체를 생성하여 반환
            return new ProfileVo();
        }
        return profileVo;
    }

    public void saveImage(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserVo) {
            UserVo userVo = (UserVo) authentication.getPrincipal();

            // 사용자 닉네임(userNick)을 기반으로 프로필 정보 가져오기
            ProfileVo profileVo = profileMapper.getProfileByUserNum(userVo.getUnum());

            if (profileVo != null) {
                // 프로필 정보를 사용하여 이미지를 저장하는 로직 추가
                // profileVo.setUserImage(imageData); // 예시 코드, 실제 이미지 데이터 저장하는 방식에 맞게 변경
                try {
                    byte[] imageData = imageService.processImage(file);
                    profileVo.setImage(imageData);
                    String imageExtension = getImageExtension(file.getOriginalFilename());

                    // 이미지를 파일로 저장
                    String imagePath = PROFILE_IMAGE_PATH + userVo.getUnum() + imageExtension;
                    saveImageToFile(imageData, imagePath);

                    profileMapper.updateProfileImage(profileVo, imagePath);
                } catch (IOException e) {
                    throw new RuntimeException("Error Processing the Image", e);
                }
            } else {
                // 프로필이 없는 경우 예외 처리
                throw new IllegalArgumentException("Profile not found");
            }
        } else {
            // 인증되지 않은 경우 예외 처리
            throw new IllegalStateException("Authentication or UserVo is not available.");
        }
    }

    private String getImageExtension(String originalFilename) {
        if (originalFilename != null) {
            int lastIndex = originalFilename.lastIndexOf(".");
            if (lastIndex >= 0) {
                return originalFilename.substring(lastIndex);
            }
        }
        // 확장자가 없거나 파일명이 없을 경우 기본으로 .jpg로 지정합니다.
        return ".jpg";
    }

    private void saveImageToFile(byte[] imageData, String imagePath) throws IOException {
        Path path = Paths.get(imagePath);
        Files.write(path, imageData);
    }

    public String getProfileImageUrl(Integer userNum) {
        ProfileVo profileVo = profileMapper.getProfileByUserNum(userNum);
        if (profileVo != null && profileVo.getImage() != null) {
            // 이미지가 존재하면 이미지 URL 생성
            // 이 URL은 프로필 이미지를 보여주는 역할을 합니다.
            return PROFILE_IMAGE_PATH + userNum + ".jpg";
        } else {
            // 프로필이 없거나 이미지가 없는 경우 기본 이미지 URL을 반환하거나 처리할 수 있습니다.
            return "/resources/static/images/default-profile.jpg";
        }
    }}
