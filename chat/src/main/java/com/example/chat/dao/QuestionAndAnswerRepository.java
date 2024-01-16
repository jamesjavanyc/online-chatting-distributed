package com.example.chat.dao;

import com.example.chat.models.QuestionAndAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QuestionAndAnswerRepository extends JpaRepository<QuestionAndAnswer, String>, JpaSpecificationExecutor<QuestionAndAnswer> {
    @Query("select qa.answer from QuestionAndAnswer qa where qa.question = :question")
    Optional<String> findAnswer(@Param("question") String question);

    @Modifying
    @Query("update QuestionAndAnswer qa set qa.answer = :answer where qa.question = :question")
    void updateAnswer(@Param("question") String ask, @Param("answer") String ans);
}