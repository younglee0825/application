package carrot.app.mapper;

import carrot.app.Profile.ProfileVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProfileMapper {
    // Goal 저장
    void saveUserGoal(ProfileVo profilevo);
    ProfileVo getProfileByUserNum(Integer userNum);
    ProfileVo getProfileByNickname(String nickname);
    void updateProfileGoal(ProfileVo profile);
    void updateProfileNickname(String new_nick, String past_nick);
}
