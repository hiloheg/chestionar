package net.application.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.application.sms.entity.Question;
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

}
