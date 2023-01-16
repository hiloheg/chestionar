package net.application.sms.controller;

import java.util.ArrayList;
import org.apache.tomcat.util.codec.binary.Base64;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import net.application.sms.entity.Answer;
import net.application.sms.entity.Question;
import net.application.sms.helper.FileUploadUtil;
import net.application.sms.service.QuestionService;

@Controller
public class QuestionController {
	
	private QuestionService questionService;

	public QuestionController(QuestionService questionService) {
		super();
		this.questionService = questionService;
	}
	
	@GetMapping("/questions")
	public String listQuestions(Model model) {
		model.addAttribute("questions",questionService.getAllQuestions());
		
		return "questions";
	}
	
	@GetMapping("/questions/new")
	public String newQuestionForm(Model model) {
		Question question = new Question();
		model.addAttribute("newQuestion",question);
		return "question_new";
	}
	
	@PostMapping("/questions" )
	public String saveQuestion(@ModelAttribute("newQuestion") Question question,@RequestParam("jsonValueAnswers") String jsonAnswers,@RequestParam("inputImage") MultipartFile multipartFile) {
		try {
			
			JSONObject jsonObject = new JSONObject(jsonAnswers);
			JSONArray answersInserted = jsonObject.getJSONArray("answers");
			List<Answer> answerList = new ArrayList<Answer>();
			if(answersInserted != null) {
				for(int i=0; i < answersInserted.length(); i++) {
					Answer answerEntity = new Answer();
					JSONObject answer = answersInserted.getJSONObject(i);
					answerEntity.setQuestion(question);
					answerEntity.setAnswerText(answer.getString("text"));
					if(answer.getString("answerCheck").equals("0")) {
						answerEntity.setRightAnswer(false);
					}
					if(answer.getString("answerCheck").equals("1")) {
						answerEntity.setRightAnswer(true);
					}
					answerList.add(answerEntity);
				}
			}
			question.setAnswers(answerList);
			if(multipartFile != null) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				question.setImageString(fileName);
				String uploadDir = "static";
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		questionService.saveQuestion(question);
		return "redirect:/questions";
	}
	@GetMapping("/questions/{id}")
	public String deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return "redirect:/questions";
	}
}
