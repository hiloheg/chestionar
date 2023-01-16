package net.application.sms.service;
import java.util.List;
import net.application.sms.entity.Question;

public interface QuestionService {
	List<Question> getAllQuestions();
	
	Question saveQuestion(Question question);
	
	void deleteQuestion(Long idQuestion);
}
