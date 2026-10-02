package org.example.springmvcdemo.service;

import org.example.springmvcdemo.model.Question;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionsService {

    // Almacén en memoria de preguntas (id -> Question)
    private final Map<Integer, Question> questions = new HashMap<>();

    public QuestionsService() {
        addQuiz(new Question(1, "¿Capital de Francia?",
                new ArrayList<>(List.of("París", "Madrid", "Roma", "Berlín")), "París"));
        addQuiz(new Question(2, "¿2 + 2?",
                new ArrayList<>(List.of("3", "4", "5", "6")), "4"));
    }

    public List<Question> loadQuizzes() {
        return new ArrayList<>(questions.values());
    }

    public void addQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void editQuiz(Question question) {
        questions.put(question.getId(), question);
    }

    public void deleteQuiz(int id) {
        questions.remove(id);
    }
}
