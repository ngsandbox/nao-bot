package ru.nao.bot.rest.services;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import ru.nao.bot.rest.exceptions.FileProcessError;
import ru.nao.bot.rest.exceptions.RequestNotFound;
import ru.nao.bot.common.face.BaseResponse;
import ru.nao.bot.common.face.FaceService;
import ru.nao.bot.common.face.ResponseWrapper;
import ru.nao.bot.common.file.FileAdapter;
import ru.nao.bot.common.nao.ContextDao;
import ru.nao.bot.common.nao.entities.*;
import ru.nao.bot.common.nlp.NlpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Service
public class RequestsService {

    private final ContextDao contextDao;
    private final NlpService nlpService;
    private final FaceService findFaceService;

    @Autowired
    public RequestsService(ContextDao contextDao,
                           NlpService nlpService,
                           FaceService findFaceService) {
        this.contextDao = contextDao;
        this.nlpService = nlpService;
        this.findFaceService = findFaceService;
    }

    public List<String> getRequestsIds() {
        return contextDao.getContextIds();
    }

    public ContextInfo getRequestInfo(String contId) {
        return contextDao.getContext(contId);
    }

    public FaceInfo findFace(String contId, @NotNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", contId, fileAdapter.getFilename());
        ContextInfo contextInfo = contextDao.handleContext(contId);
        FileInfo fileInfo = new FileInfo(contextInfo.getId(), fileAdapter.getFilename(), fileAdapter.getContent());
        contextDao.saveFile(fileInfo);
        ResponseWrapper<BaseResponse> findResp = findFaceService.find(fileAdapter);
        String msg = "";
        String login = findResp.getResponse() != null ? findResp.getResponse().getLogin() : null;
        if (findResp.getStatus().isSuccess()) {
            ResponseWrapper<BaseResponse> authResp = findFaceService.auth(login, fileAdapter);
            if (authResp.getStatus().isSuccess()) {
                contextInfo.setStatus(ContextStatus.FaceRecognized);
            } else {
                msg = authResp.getMsg();
                contextInfo.setStatus(ContextStatus.FaceAuthFailed);
            }
        } else {
            msg = findResp.getMsg();
            contextInfo.setStatus(ContextStatus.FaceNotFound);
        }

        FaceInfo faceInfo = new FaceInfo(contextInfo, login, msg);
        log.debug("Face recognition status {} ", faceInfo);
        contextDao.saveContext(contextInfo);
        return faceInfo;
    }

    public FaceInfo faceAuth(String contId, @NonNull String login, @NonNull FileAdapter fileAdapter) {
        log.debug("Saving file content {} for request id {}", contId, fileAdapter.getFilename());
        ContextInfo contextInfo = contextDao.handleContext(contId);
        FileInfo fileInfo = new FileInfo(contextInfo.getId(), fileAdapter.getFilename(), fileAdapter.getContent());
        contextDao.saveFile(fileInfo);
        ResponseWrapper<BaseResponse> wrapper = findFaceService.register(login, fileAdapter);
        contextInfo.setStatus(wrapper.getStatus().isSuccess() ?
                ContextStatus.FaceRegistered : ContextStatus.FaceRegError);

        String msg = wrapper.getMsg();
        FaceInfo faceInfo = new FaceInfo(contextInfo, login, msg);
        log.debug("Face recognition status {} ", faceInfo);
        contextDao.saveContext(contextInfo);
        return faceInfo;
    }


    private InputStream getFileStream(String reqId) {
        log.debug("Try to get file stream by id {}", reqId);
        FileInfo fileInfo = contextDao.getFile(reqId);
        if (fileInfo == null) {
            throw new RequestNotFound("File not found for request id : " + reqId);
        }

        return new ByteArrayInputStream(fileInfo.getFileBody());
    }

    public void outputImageStream(String id, HttpServletResponse response) {
        log.debug("Return image sream for request id {}", id);
        try (InputStream stream = getFileStream(id)) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            IOUtils.copy(stream, response.getOutputStream());
        } catch (IOException ex) {
            log.error("Error to process file stram for request id {}", id, ex);
            throw new FileProcessError("Error to output file stream by id " + id, ex);
        }
    }

    public QuestionInfo answer2Question(String id, @NonNull String question) {
        ContextInfo contextInfo = contextDao.handleContext(id);
        QuestionInfo questionInfo = QuestionInfo.builder()
                .contextInfo(contextInfo)
                .question(question)
                .part(nlpService.processSentence(question))
                .answer("Hello!").build();
        contextInfo.setStatus(ContextStatus.Answer2Question);
        contextDao.saveContext(contextInfo);

        log.trace("Generated answer to the question {}", questionInfo);
        return questionInfo;
    }
}
