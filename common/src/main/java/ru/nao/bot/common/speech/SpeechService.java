package ru.nao.bot.common.speech;

import ru.nao.bot.common.file.FileAdapter;
import ru.nao.bot.common.speech.entities.ResponseWrapper;
import ru.nao.bot.common.speech.entities.BaseResponse;

public interface SpeechService {

    ResponseWrapper<BaseResponse> parseVoice(FileAdapter adapter);
}
