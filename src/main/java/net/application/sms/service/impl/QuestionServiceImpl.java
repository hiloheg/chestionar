package net.application.sms.service.impl;

import java.util.Base64;
import java.util.List;
import org.springframework.stereotype.Service;
import net.application.sms.entity.Question;
import net.application.sms.repository.QuestionRepository;
import net.application.sms.service.QuestionService;

@Service
public class QuestionServiceImpl implements QuestionService{

	private QuestionRepository questionRepository;
	
	public QuestionServiceImpl(QuestionRepository questionRepository) {
		super();
		this.questionRepository = questionRepository;
	}
	
	@Override
	public List<Question> getAllQuestions() {
		List<Question> questions = questionRepository.findAll();
		return questions;
	}

	@Override
	public Question saveQuestion(Question question) {
		return questionRepository.save(question);
	}

	@Override
	public void deleteQuestion(Long idQuestion) {
		questionRepository.deleteById(idQuestion);
	}

}
