package carrot.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import carrot.app.dto.CommentDTO;
import carrot.app.dto.PostDTO;

@Mapper
public interface CommentMapper {
	List<PostDTO> postList(int pageNo, int pageSize);

	PostDTO post(int pnum);

	int addPost(PostDTO postDto);

	int deletePost(int postnum);

	List<CommentDTO> commentList(int pnum);

	int addComment(CommentDTO commentDto);

	int deleteComment(CommentDTO commentDto);
	
	CommentDTO commentProfile(int cnum);
	
	String selectComment(CommentDTO commentDto);
	
	int updateComment(CommentDTO commentDto);
}
