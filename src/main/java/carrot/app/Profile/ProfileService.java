package carrot.app.Profile;

import carrot.app.User.UserVo;
import carrot.app.Profile.ProfileVo;
import carrot.app.mapper.ProfileMapper;
import carrot.app.mapper.UserMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.OutputStream;

@Service
//@RequiredArgsConstructor
public class ProfileService {
    private final ProfileMapper profileMapper;
    private final UserMapper userMapper;

    @Autowired
    public ProfileService(ProfileMapper profileMapper, UserMapper userMapper) {
        this.profileMapper = profileMapper;
        this.userMapper = userMapper;
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
                        if (newNickCount == 0){
                            userMapper.updateProfileNickname(newNick,nickname);
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
                    // 프로필이 없으면 새로 생성
                    ProfileVo profile = new ProfileVo();
                    profile.setUser_nick(userVo.getUnick());
                    profile.setProfile(Long.valueOf("1234"));
                    profile.setGoal(goal);

                    Integer userNum = userMapper.getUserNumByUserNick(nickname);
                    profile.setUser_num(userNum);

                    profileMapper.saveUserGoal(profile);
                    System.out.println("새로운 프로필이 생성되었습니다.");
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

}

//    @Transactional
//    public void saveUserGoal(String goal, String newNick) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserVo) {
//            UserVo userVo = (UserVo) authentication.getPrincipal();
//
//            String nickname = userVo.getUnick();
//            int nicknameCount = countUserNickname(nickname);
//            //peekaboo
//
//            if (nicknameCount == 1) {
//                ProfileVo existingProfile = profileMapper.getProfileByNickname(nickname);
//                // 알퍄퍄
//                if (existingProfile != null) {
//                    if (!nickname.equals(newNick)) {
//                        int newNickCount = countUserNickname(newNick);
//                        if (newNickCount == 0) {
//                            existingProfile.setUser_nick(newNick);
//                            existingProfile.setGoal(goal);
//                            profileMapper.updateProfileNickname(newNick, nickname); // 닉네임 변경
//                            profileMapper.updateProfileGoal(existingProfile); // 목표 업데이트
//                            userVo.setUnick(newNick);
//                            System.out.println("Goal이 업데이트되었습니다.");
//                        } else {
//                            System.out.println("이미 존재하는 닉네임입니다.");
//                            existingProfile.setGoal(goal);
//                            profileMapper.updateProfileGoal(existingProfile); // 목표 업데이트 (닉네임 변경은 안 함)
//                        }
//                    } else {
//                        existingProfile.setGoal(goal);
//                        profileMapper.updateProfileGoal(existingProfile); // 목표 업데이트 (닉네임 변경은 안 함)
//                    }
//                } else {
//                    System.out.println("닉네임에 해당하는 프로필이 없습니다.");
//                    // 프로필이 없으면 새로 생성하는 등의 로직을 추가해야할 수도 있습니다.
//                }
//            } else {
//                ProfileVo profile = new ProfileVo();
//                profile.setUser_nick(userVo.getUnick());
//                profile.setProfile(Long.valueOf("1234"));
//                profile.setGoal(goal);
//
//                Integer userNum = userMapper.getUserNumByUserNick(nickname);
//                profile.setUser_num(userNum);
//
//                profileMapper.saveUserGoal(profile);
//                System.out.println("새로운 프로필이 생성되었습니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("Authentication or UserVo is not available.");
//        }
//    }