package net.application.sms.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name = "answer")
public class Answer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	


	@ManyToOne
	@JoinColumn(name="idQuestion",nullable=false)
	private Question question;
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	@Column(name="answerText", nullable = true)
	private String answerText;
	
	@Column(name="rightAnswer",nullable = true)
	private boolean rightAnswer;
	
	public boolean getRightAnswer() {
		return rightAnswer;
	}

	public void setRightAnswer(boolean rightAnswer) {
		this.rightAnswer = rightAnswer;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}

	public Answer() {
		
	}

}
