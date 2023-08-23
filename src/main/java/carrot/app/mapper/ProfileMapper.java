package carrot.app.mapper;

import carrot.app.Profile.ProfileVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {
    // Profile 저장
    void saveProfile(ProfileVo profileVo);
    //goal 저장
    void saveUserGoal(ProfileVo profilevo);
    ProfileVo getProfileByUserNum(Integer userNum);
    ProfileVo getProfileByNickname(String nickname);
    void updateProfileGoal(ProfileVo profile);

    void updateProfileImage(@Param("profile") ProfileVo profile, @Param("imagePath") String imagePath);
    //void updateProfileNickname(String new_nick, String past_nick);

}


