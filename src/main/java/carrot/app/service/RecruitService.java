package carrot.app.service;

import java.util.List;
import java.util.Map;

import carrot.app.dto.RecruitDTO;

public interface RecruitService {

	List<RecruitDTO> getRecruitList(Map<String, Object> recruitMap);

	RecruitDTO getRecruit(RecruitDTO recruitDto);

	int addRecruit(RecruitDTO recruitDto);

	int updateRecruit(RecruitDTO recruitDto);

	int deleteRecruit(RecruitDTO recruitDto);

	int countJoin(RecruitDTO recruitDto);

	int insertJoin(RecruitDTO recruitDto);
	
	int countRecruitJoin(RecruitDTO recruitDto);

	int countScrap(RecruitDTO recruitDto);

	int insertScrap(RecruitDTO recruitDto);
}
