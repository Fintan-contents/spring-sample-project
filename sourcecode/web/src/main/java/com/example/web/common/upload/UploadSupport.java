package com.example.web.common.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * ファイルアップロードの補助を行うクラス。
 * 
 * アップロードされたファイルを一時領域へ格納し、そこから指定のパスへ移動したり一時ファイルを削除することができる。
 * 一時ファイルにはIDを割り当て、一時ファイルの操作はそのIDを通じて行う。
 * 
 * @author sample
 *
 */
@Component
@SessionScope
@ConfigurationProperties(prefix = "common.upload")
public class UploadSupport implements Serializable, DisposableBean {

    /**
     * 一時ファイルIDとファイル名を保持するマップ
     */
    private final ConcurrentMap<String, String> tempFiles = new ConcurrentHashMap<>();

    /**
     * 一時ファイルを格納するディレクトリ
     */
    private String tempDir;

    /**
     * マルチパートファイルを一時的なファイルとして保存して、そのファイルに紐づくIDを返す。
     * 
     * @param file マルチパートファイル
     * @return 一時ファイルID
     */
    public String saveTemporary(InputStreamSource file) {
        String tempFileId = UUID.randomUUID().toString();
        String tempFileName = UUID.randomUUID().toString();
        Path dest = Path.of(tempDir).resolve(tempFileName);
        try (InputStream src = file.getInputStream()) {
            Files.copy(src, dest);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        tempFiles.put(tempFileId, Objects.toString(dest.getFileName()));
        return tempFileId;
    }

    /**
     * 一時ファイルを移動する。
     * 
     * @param tempFileId 一時ファイルID
     * @param dest 移動先のファイルパス
     */
    public void moveFromTemporaryFile(String tempFileId, Path dest) {
        processFile(tempFileId, file -> {
            Files.move(file, dest);
        });
    }

    /**
     * 一時ファイルを削除する。
     * 
     * @param tempFileId 一時ファイルID
     */
    public void removeTemporaryFile(String tempFileId) {
        processFile(tempFileId, Files::delete);
    }

    /**
     * 一時ファイルの存在確認を行う。
     * 
     * @param tempFileId 一時ファイルID
     * @return 一時ファイルが存在する場合はtrueを返す
     */
    public boolean existsTemporaryFile(String tempFileId) {
        String fileName = tempFiles.get(tempFileId);
        if (fileName == null) {
            return false;
        }
        Path file = Path.of(tempDir).resolve(fileName);
        return Files.exists(file);
    }

    /**
     * 一時ファイルを処理する。
     * 
     * @param tempFileId 一時ファイルID
     * @param fileConsumer 一時ファイルを使用した処理を表す関数インターフェース
     */
    private void processFile(String tempFileId, FileConsumer fileConsumer) {
        String fileName = tempFiles.get(tempFileId);
        Path file = Path.of(tempDir).resolve(fileName);
        try {
            fileConsumer.consume(file);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        tempFiles.remove(tempFileId);
    }

    /**
     * 一時ファイルを格納するディレクトリを設定する。
     * 
     * @param tempDir 一時ファイルを格納するディレクトリ
     */
    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public void destroy() throws Exception {
        Path tempDirPath = Path.of(tempDir);
        for (String tempFileName : tempFiles.values()) {
            Path tempFile = tempDirPath.resolve(tempFileName);
            Files.deleteIfExists(tempFile);
        }
    }

    /**
     * 一時ファイルを使用した処理を表す関数インターフェース。
     * 
     * @author sample
     *
     */
    private interface FileConsumer {

        /**
         * 一時ファイルを使用した処理を行う。
         * 
         * @param file 一時ファイル
         * @throws IOException ファイル操作時に発生し得る例外
         */
        void consume(Path file) throws IOException;
    }
}
