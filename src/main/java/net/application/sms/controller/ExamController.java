/**
 * 
 */
package net.application.sms.controller;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.application.sms.service.QuestionService;
import net.application.sms.beans.ExamQuestion;
import net.application.sms.entity.Answer;
import net.application.sms.entity.Question;
import net.application.sms.helper.FileUploadUtil;
/**
 * @author Hilohe Gigi
 *
 */
@Controller
public class ExamController {
	 
	private static List<ExamQuestion> currentExam;
	private static String finalMessage = "Din pacate respins..";
	private QuestionService questionService;
	private int MAX_WRONG_QUESTIONS = 4;
	private int NUMBERS_OF_QUESTION = 26;
	

	public ExamController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}
	
	@GetMapping("/")
	public String redirectIndex() {
		return "redirect:/questions";
	}
	@GetMapping("/exam")
	public String startNewExam(Model model) {
		extractQuestionForExam();
		String redirectPath = "/exam/"+firstQuestionFree().toString();
		return "redirect:"+redirectPath;
	}
	
	@GetMapping("/nextQuestion")
	public String nextQuestion(Model model) {
		String redirectPath = "/exam/"+firstQuestionFree().toString();
		return "redirect:"+redirectPath;
	}
	
	@GetMapping("/exam/{id}")
	public String showExamQuestion(@PathVariable Long id,Model model) {
		ExamQuestion question = findExamQuestionById(id);
		model.addAttribute("question",question);
		model.addAttribute("correctAnswers",provideCorrectsAnswers());
		model.addAttribute("wrongAnswers",provideWrongAnswers());
		return "exam_new";
	}
	
	@GetMapping("/exam/end")
	public String examEnd(Model model){
		model.addAttribute("correctAnswers",provideCorrectsAnswers());
		model.addAttribute("wrongAnswers",provideWrongAnswers());
		model.addAttribute("finalMessage",provideFinalText());
		model.addAttribute("examsQuestions",prepareExamsQuestionAnswered());
		return "exam_end";
	}
	
	@PostMapping("/checkAsnwer" )
	public String saveQuestion(@RequestParam("idCurrentQuestion") Long idQuestion,@RequestParam("jsonValueAnswers") String jsonAnswers) {
		Map<Long,Boolean> answersGived = new HashMap<Long,Boolean>();
		try {
			JSONObject jsonObject = new JSONObject(jsonAnswers);
			JSONArray answersInserted = jsonObject.getJSONArray("answers");
			if(answersInserted != null) {
				for(int i=0; i < answersInserted.length(); i++) {
					JSONObject answer = answersInserted.getJSONObject(i);
					Long id = Long.parseLong(answer.getString("id"));
					Boolean isChecked = false;
					if(answer.getString("checked").equals("true")) {
						isChecked = true;
					}
					if(answer.getString("checked").equals("false")) {
						isChecked = false;
					}
					answersGived.put(id,isChecked);
				}
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		setCurrentQuestionAnswered(findExamQuestionById(idQuestion).getEntityQuestion().getId());
		validateAnswers(answersGived,idQuestion);
		return checkIfNotEnd();
	}
	
	
	private String checkIfNotEnd() {
		String redirectPage = "redirect:/nextQuestion";
		if(allQuestionAnswered() || provideWrongAnswers() > MAX_WRONG_QUESTIONS) {
			redirectPage = "redirect:/exam/end";
		}
		return redirectPage;
	}
	
	private void extractQuestionForExam() {
		currentExam = new LinkedList<ExamQuestion>();
		List<Question> allQuestions = questionService.getAllQuestions();
		List<Long> idsQuestionsPicked = new LinkedList<Long>();
		
		int index = 1;
		while(currentExam.size() < 26) {
			Random randQuestion = new Random();
			Question pickedQuestion = allQuestions.get(randQuestion.nextInt(allQuestions.size()));
			if(!idsQuestionsPicked.contains(pickedQuestion.getId())) {
				idsQuestionsPicked.add(pickedQuestion.getId());
				ExamQuestion question = new ExamQuestion();
				question.setEntityQuestion(pickedQuestion);
				question.setAnswered(false);
				question.setIndex(index);
				index++;
				currentExam.add(question);
			}
		}
		
	}
	
	private Long firstQuestionFree() {
		Long indexFinded = -1L;
		for (ExamQuestion question: currentExam) {
			if(question.isAnswered() == false) {
				indexFinded = question.getEntityQuestion().getId();
				break;
			}
		}
		return indexFinded;
	}
	
	private ExamQuestion findExamQuestionById(Long id) {
		ExamQuestion question = null;
		for (ExamQuestion quest: currentExam) {
			if(quest.getEntityQuestion().getId() == id) {
				question = quest;
				break;
			}
		}
		return question;
	}
	
	private int provideCorrectsAnswers() {
		int correctAnswers = 0;
		for (ExamQuestion quest: currentExam) {
			if(quest.isAnswered() && quest.isCorrectAnswer()) {
				correctAnswers++;
			}
		}
		return correctAnswers;
	}
	private void setCurrentQuestionAnswered(Long id) {
		for (ExamQuestion quest: currentExam) {
			if(quest.getEntityQuestion().getId() == id) {
				quest.setAnswered(true);
				break;
			}
		}
	}
	private int provideWrongAnswers() {
		int correctAnswers = 0;
		for (ExamQuestion quest: currentExam) {
			if(quest.isAnswered() && !quest.isCorrectAnswer()) {
				correctAnswers++;
			}
		}
		return correctAnswers;
	}
	
	private boolean allQuestionAnswered() {
		boolean isAllAnswered = false;
		int correctAnswers = 0;
		for (ExamQuestion quest: currentExam) {
			if(quest.isAnswered()) {
				correctAnswers++;
			}
		}
		if(currentExam.size() == correctAnswers) {
			isAllAnswered = true;
		}
		return isAllAnswered;
	}
	
	private String provideFinalText() {
		String endText = "";
		if(provideWrongAnswers() > MAX_WRONG_QUESTIONS) {
			endText = "Din pacate Respins...";
		}
		else {
			endText = "Felicitari! Admis!";
		}
		return endText;
	}
	
	private void validateAnswers(Map<Long,Boolean> answers, Long questionId) {
		ExamQuestion question = findExamQuestionById(questionId);
		Map comparisonMap = new HashMap<Long,Boolean>();
		for (Answer answer : question.getEntityQuestion().getAnswers()) {
			Long id = answer.getId();
			Boolean isChecked = answer.getRightAnswer();
			comparisonMap.put(id, isChecked);
		}
		boolean isAnswerRight = comparisonMap.equals(answers);
		question.setCorrectAnswer(isAnswerRight);
	}
	
	private List<ExamQuestion> prepareExamsQuestionAnswered() {
		List<ExamQuestion> answeredQuestions = new LinkedList<ExamQuestion>();
		for (ExamQuestion quest: currentExam) {
			if(quest.isAnswered()) {
				answeredQuestions.add(quest);
			}
		}
		return answeredQuestions;
	}
	
	
}
