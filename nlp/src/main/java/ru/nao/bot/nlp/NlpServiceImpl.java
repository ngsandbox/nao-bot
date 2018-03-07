package ru.nao.bot.nlp;

import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.trees.Tree;
import lombok.extern.slf4j.Slf4j;
import ru.nao.bot.common.nlp.TagHelper;
import ru.nao.bot.common.nlp.NlpService;
import ru.nao.bot.common.nlp.entities.Part;
import ru.nao.bot.common.nlp.entities.Tag;
import ru.nao.bot.common.nlp.entities.Word;

import java.util.List;

@Slf4j
public class NlpServiceImpl implements NlpService {

    public NlpServiceImpl() {
    }

    @Override
    public Part processSentence(String sentence) {
        log.debug("Start process sentence {}", sentence);
        // read some text in the text variable
        String text = sentence == null ? "Where can I find the closest Sberbank's ATM?" : sentence;// Add your text here!
        String textRu = "Где найти ближайшее отделение Сбербанка?";// Add your text here!

        Sentence sent = new Sentence(text);
        Part part = convert(sent.parse());
        log.debug("Result processed sentence {}", part);
        List<String> nerTags = sent.nerTags();  // [PERSON, O, O, O, O, O, O, O]
        log.debug("Tags: {}", nerTags);
        return part;
    }

    @Override
    public Tag findTag(Part part, String tag) {
        if (part != null && part instanceof Tag) {
            Tag tg = (Tag) part;
            if (tg.getTag().equals(tag)) {
                return tg;
            }

            for (Part kid : tg.getChildren()) {
                Tag kidTag = findTag(kid, tag);
                if (kidTag != null) {
                    return kidTag;
                }
            }
        }

        return null;
    }


    public static Part convert(Tree tree) {
        if (tree == null) {
            return null;
        }

        Part part = null;
        log.debug("Tree: {}", tree);
        if (tree.label() != null) {
            if (Double.isNaN(tree.score())) {
                part = new Word(tree.label().value());
            } else {
                String tag = tree.label().value();
                part = Tag.builder()
                        .tag(tag)
                        .label(TagHelper.TAGS.get(tag))
                        .build();
            }
        }

        if (!tree.isLeaf() && tree.children() != null && tree.children().length > 0) {
            Tag.TagBuilder builder = Tag.builder();
            if (part != null && part instanceof Tag) {
                builder.label(((Tag) part).getLabel());
                builder.tag(((Tag) part).getTag());
            } else {
                log.error("tag is null of word but has leafs {}", part);
            }

            for (Tree kid : tree.children()) {
                builder.child(convert(kid));
            }

            part = builder.build();
        }

        return part;
    }

    @Override
    public String tagDescription(String tag) {
        return TagHelper.TAGS.get(tag);
    }
}
