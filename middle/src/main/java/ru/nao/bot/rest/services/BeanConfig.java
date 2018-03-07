package ru.nao.bot.rest.services;

import ru.nao.bot.common.face.FaceService;
import ru.nao.bot.common.nao.ContextDao;
import ru.nao.bot.common.nlp.NlpService;
import org.ngsandbox.face.RemoteFaceServiceImpl;
import ru.nao.bot.hazelcast.HazelContextDaoImpl;
import ru.nao.bot.hazelcast.HazelService;
import ru.nao.bot.nlp.NlpServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanConfig {

    private static final String HOST_NAME = "http://93.88.76.57:8090";

    @Bean
    public NlpService nlpServiceBean() {
        return new NlpServiceImpl();
    }

    @Bean
    public FaceService findPhotoBean() {
        return new RemoteFaceServiceImpl(HOST_NAME);
    }

    @Bean
    public ContextDao requestsDaoBean() {
        return new HazelContextDaoImpl(new HazelService());
    }
}
