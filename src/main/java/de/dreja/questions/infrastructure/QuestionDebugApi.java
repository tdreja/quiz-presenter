package de.dreja.questions.infrastructure;

import de.dreja.common.service.Storage;
import de.dreja.questions.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("debug")
public class QuestionDebugApi {

    private final Storage storage;

    @Autowired
    QuestionDebugApi(Storage storage) {
        this.storage = storage;
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable("questionId") String questionId) {
        return storage.getRoot().getQuestion(questionId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/question")
    public ResponseEntity<String> postQuestion(@RequestBody Question question) {
        storage.getRoot().getQuestions().put(question.getId(), question);
        storage.store(storage.getRoot().getQuestions());
        return ResponseEntity.ok(question.getId());
    }
}
