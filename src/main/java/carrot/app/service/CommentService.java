package carrot.app.service;

import java.util.List;

import carrot.app.dto.CommentDTO;

public interface CommentService {

	public List<CommentDTO> getCommentList(int pnum);
	
	public int addComment(CommentDTO commentDto);
	
	public int deleteComment(CommentDTO commentDTO);
	
	public CommentDTO getComment(int cnum);
	
	public String selectComment(CommentDTO commentDto);
	
	public int updateComment(CommentDTO commentDto);
}