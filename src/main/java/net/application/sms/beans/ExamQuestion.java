package net.application.sms.beans;

import net.application.sms.entity.Question;

public class ExamQuestion {
	
	private Question entityQuestion;
	private boolean isAnswered;
	private boolean correctAnswer;
	private int index;
	public ExamQuestion()
	{
		
	}

	public boolean isCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(boolean correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Question getEntityQuestion() {
		return entityQuestion;
	}

	public void setEntityQuestion(Question entityQuestion) {
		this.entityQuestion = entityQuestion;
	}

	public boolean isAnswered() {
		return isAnswered;
	}

	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
