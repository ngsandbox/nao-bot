package ru.nao.bot.common.nao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
public class FileInfo implements Serializable {

    private String reqId;
    private String fileName;
    private byte[] fileBody;

    @Override
    public String toString() {
        return "FileInfo: {" +
                " reqId: " + reqId +
                ", fileName: " + fileName +
                " }";
    }

}
