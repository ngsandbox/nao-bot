package ru.nao.bot.common.nlp;

import ru.nao.bot.common.nlp.entities.Part;
import ru.nao.bot.common.nlp.entities.Tag;

public interface NlpService {

    Part processSentence(String sentence);

    Tag findTag(Part part, String tag);

    String tagDescription(String tag);
}
