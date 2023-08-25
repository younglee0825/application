package carrot.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carrot.app.dto.RecruitDTO;
import carrot.app.mapper.RecruitMapper;

@Service("recruitService")
public class RecruitServiceImpl implements RecruitService {

	@Autowired
	private RecruitMapper mapper;

	@Override
	public List<RecruitDTO> getRecruitList(Map<String, Object> recruitMap) {
		return mapper.recruitList(recruitMap);
	}

	@Override
	public RecruitDTO getRecruit(RecruitDTO recruitDto) {
		return mapper.selectRecruit(recruitDto);
	}

	@Override
	public int addRecruit(RecruitDTO recruitDto) {
		return mapper.addRecruit(recruitDto);
	}

	@Override
	public int updateRecruit(RecruitDTO recruitDto) {
		return mapper.updateRecruit(recruitDto);
	}

	@Override
	public int deleteRecruit(RecruitDTO recruitDto) {
		return mapper.deleteRecruit(recruitDto);
	}

	@Override
	public int countJoin(RecruitDTO recruitDto) {
		return mapper.countJoin(recruitDto);
	}

	@Override
	public int countRecruitJoin(RecruitDTO recruitDto) {
		return mapper.countRecruitJoin(recruitDto);
	}

	@Override
	public int insertJoin(RecruitDTO recruitDto) {
		return mapper.insertJoin(recruitDto);
	}

	@Override
	public int countScrap(RecruitDTO recruitDto) {
		return mapper.countScrap(recruitDto);
	}

	@Override
	public int insertScrap(RecruitDTO recruitDto) {
		return mapper.insertScrap(recruitDto);
	}
}
