package ru.nao.bot.rest.controllers;


import lombok.extern.slf4j.Slf4j;
import ru.nao.bot.common.file.Base64FileAdapter;
import ru.nao.bot.common.nao.entities.ContextInfo;
import ru.nao.bot.common.nao.entities.FaceInfo;
import ru.nao.bot.common.nao.entities.QuestionInfo;
import ru.nao.bot.rest.adapters.MultipartFileAdapter;
import ru.nao.bot.rest.services.RequestsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
public class RequestController {


    private final RequestsService requestsService;

    @Autowired
    public RequestController(RequestsService requestsService) {
        this.requestsService = requestsService;
    }


    @GetMapping("/requests")
    public List<String> getRequests() {
        log.trace("Get request identifiers");
        return requestsService.getRequestsIds();
    }

    @GetMapping("/requests/{id}")
    public ContextInfo getContext(@PathVariable String id) {
        log.trace("Get request info by id {}", id);
        return requestsService.getRequestInfo(id);
    }

    @GetMapping(value = "/requests/{id}/download")
    public void downloadImage(@PathVariable String id, HttpServletResponse response) {
        log.trace("Get image by request info id {}", id);
        requestsService.outputImageStream(id, response);
    }

    @PostMapping("/requests/find/face/file")
    public FaceInfo faceFileUpload(@RequestParam(required = false) String id, @RequestParam("file") MultipartFile file) {
        log.trace("Save file content for id {}", id);
        return requestsService.findFace(id, new MultipartFileAdapter(file));
    }

    @PostMapping("/requests/find/face/base64")
    public FaceInfo faceBase64Find(@RequestParam(required = false) String id,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("fileContent") String fileContent) {
        log.trace("Save base64 file content of fileName {} for request info id {}", fileName, id);
        return requestsService.findFace(id, new Base64FileAdapter(fileName, fileContent));
    }

    @PostMapping("/requests/auth/face/base64")
    public FaceInfo faceBase64Auth(@RequestParam(required = false) String id,
                                   @RequestParam() String login,
                                   @RequestParam("fileName") String fileName,
                                   @RequestParam("fileContent") String fileContent) {
        log.trace("Save base64 file content of fileName {} for request info id {}", fileName, id);
        return requestsService.faceAuth(id, login, new Base64FileAdapter(fileName, fileContent));
    }

    @PostMapping("/requests/ask")
    public QuestionInfo answer2Question(@RequestParam(required = false) String id, @RequestParam("question") String question) {
        log.trace("For request {} try to anser to the question  {}", id, question);
        return requestsService.answer2Question(id, question);
    }
}
