package com.autovideo.service;

import com.amazonaws.services.polly.model.Engine;
import com.autovideo.img.ImageCategory;
import com.autovideo.utils.Language;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * All the params needed to generate a Wikipedia video.
 * Basically wraps up everything the user can configure.
 */
public class WikiVideoRequest {
    private final String articleTitle;
    private final Engine voiceEngine;
    private final Language language;
    private final ImageCategory imageCategory;
    private final Path outputDirectory;
    private final boolean withAudio;
    
    private WikiVideoRequest(Builder builder) {
        this.articleTitle = builder.articleTitle;
        this.voiceEngine = builder.voiceEngine;
        this.language = builder.language;
        this.imageCategory = builder.imageCategory;
        this.outputDirectory = builder.outputDirectory;
        this.withAudio = builder.withAudio;
    }
    
    public String getArticleTitle() {
        return articleTitle;
    }
    
    public Engine getVoiceEngine() {
        return voiceEngine;
    }
    
    public Language getLanguage() {
        return language;
    }
    
    public ImageCategory getImageCategory() {
        return imageCategory;
    }
    
    public Path getOutputDirectory() {
        return outputDirectory;
    }
    
    public boolean isWithAudio() {
        return withAudio;
    }
    
    public static class Builder {
        private String articleTitle;
        private Engine voiceEngine = Engine.Standard;
        private Language language = Language.EN;
        private ImageCategory imageCategory = ImageCategory.BUILDINGS;
        private Path outputDirectory = Paths.get("output/final/");
        private boolean withAudio = true;
        
        public Builder(String articleTitle) {
            this.articleTitle = articleTitle;
        }
        
        public Builder voiceEngine(Engine engine) {
            this.voiceEngine = engine;
            return this;
        }
        
        public Builder language(Language lang) {
            this.language = lang;
            return this;
        }
        
        public Builder imageCategory(ImageCategory category) {
            this.imageCategory = category;
            return this;
        }
        
        public Builder outputDirectory(Path path) {
            this.outputDirectory = path;
            return this;
        }
        
        public Builder withAudio(boolean withAudio) {
            this.withAudio = withAudio;
            return this;
        }
        
        public WikiVideoRequest build() {
            if (articleTitle == null || articleTitle.trim().isEmpty()) {
                throw new IllegalArgumentException("Article title can't be empty");
            }
            return new WikiVideoRequest(this);
        }
    }
}

