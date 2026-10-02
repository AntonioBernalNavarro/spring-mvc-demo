package org.example.springmvcdemo.controller;

import org.example.springmvcdemo.model.Question;
import org.example.springmvcdemo.model.User;
import org.example.springmvcdemo.service.QuestionsService;
import org.example.springmvcdemo.service.QuizUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class QuizController {

    private final QuestionsService questionsService;
    private final QuizUserDetailsService userDetailsService;

    private final Map<String, Map<Integer, String>> userAnswers = new HashMap<>();

    public QuizController(QuestionsService questionsService,
                          QuizUserDetailsService userDetailsService) {
        this.questionsService = questionsService;
        this.userDetailsService = userDetailsService;
    }

    // ---------- LOGIN / REGISTRO ----------

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        String role = (user.getRole() != null && !user.getRole().isEmpty())
                ? user.getRole() : "USER";
        userDetailsService.registerUser(
                user.getUsername(), user.getPassword(), user.getEmail(), role);
        return "redirect:/login";
    }

    // ---------- ADMIN ----------

    @GetMapping("/quizList")
    public String quizList(Model model) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        return "quizList";
    }

    @GetMapping("/addQuiz")
    public String showAddQuizForm(Model model) {
        model.addAttribute("question", new Question());
        return "addQuiz";
    }

    @PostMapping("/addQuiz")
    public String addQuiz(@ModelAttribute Question question,
                          @RequestParam("optionsRaw") String optionsRaw) {
        List<String> opts = Arrays.asList(optionsRaw.split("\\r?\\n"));
        question.setOptions(new ArrayList<>(opts));
        questionsService.addQuiz(question);
        return "redirect:/quizList";
    }

    @GetMapping("/editQuiz/{id}")
    public String showEditQuizForm(@PathVariable int id, Model model) {
        Question selected = questionsService.loadQuizzes().stream()
                .filter(q -> q.getId() == id).findFirst().orElse(null);
        model.addAttribute("question", selected);
        return "editQuiz";
    }

    @PutMapping("/editQuiz")
    @ResponseBody
    public String editQuiz(@RequestBody Question question) {
        questionsService.editQuiz(question);
        return "Question updated";
    }

    @DeleteMapping("/deleteQuiz/{id}")
    @ResponseBody
    public String deleteQuiz(@PathVariable int id) {
        questionsService.deleteQuiz(id);
        return "Question deleted";
    }

    // ---------- USER ----------

    @GetMapping("/quiz")
    public String quiz(Model model, Authentication authentication) {
        model.addAttribute("questions", questionsService.loadQuizzes());
        model.addAttribute("username", authentication.getName());
        return "quiz";
    }

    @PostMapping("/submitAnswers")
    public String submitAnswers(@RequestParam Map<String, String> allParams,
                                Authentication authentication) {
        Map<Integer, String> answers = new HashMap<>();
        for (Map.Entry<String, String> e : allParams.entrySet()) {
            if (e.getKey().startsWith("answer_")) {
                int qid = Integer.parseInt(e.getKey().substring("answer_".length()));
                answers.put(qid, e.getValue());
            }
        }
        userAnswers.put(authentication.getName(), answers);
        return "redirect:/result";
    }

    @GetMapping("/result")
    public String result(Model model, Authentication authentication) {
        Map<Integer, String> answers = userAnswers.getOrDefault(
                authentication.getName(), new HashMap<>());
        List<Question> questions = questionsService.loadQuizzes();

        int score = 0;
        List<Map<String, Object>> results = new ArrayList<>();
        for (Question q : questions) {
            String ua = answers.get(q.getId());
            boolean ok = ua != null && ua.equals(q.getCorrectAnswer());
            if (ok) score++;
            Map<String, Object> item = new HashMap<>();
            item.put("questionText", q.getQuestionText());
            item.put("userAnswer", ua);
            item.put("correctAnswer", q.getCorrectAnswer());
            item.put("correct", ok);
            results.add(item);
        }
        model.addAttribute("score", score);
        model.addAttribute("total", questions.size());
        model.addAttribute("results", results);
        return "result";
    }
}
