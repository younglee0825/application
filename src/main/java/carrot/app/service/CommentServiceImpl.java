package carrot.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import carrot.app.dto.CommentDTO;
import carrot.app.mapper.CommentMapper;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentMapper mapper;

	@Override
	public List<CommentDTO> getCommentList(int pnum) {
		return mapper.commentList(pnum);
	}

	@Override
	public int addComment(CommentDTO commentDto) {
		return mapper.addComment(commentDto);
	}

	@Override
	public int deleteComment(CommentDTO commentDTO) {
		return mapper.deleteComment(commentDTO);
	}

	@Override
	public CommentDTO getComment(int cnum) {
		return mapper.commentProfile(cnum);
	}

	@Override
	public String selectComment(CommentDTO commentDto) {
		return mapper.selectComment(commentDto);
	}

	@Override
	public int updateComment(CommentDTO commentDto) {
		return mapper.updateComment(commentDto);
	}

}
