package com.autovideo.service;

/**
 * Interface for tracking video generation progress.
 * Implement this if you wanna get notified about what's happening during video creation.
 */
public interface ProgressListener {
    /**
     * Called when we move to a new stage of processing
     * @param stageName like "Downloading Wikipedia article" or "Generating speech"
     */
    void onStageChanged(String stageName);
    
    /**
     * Called to update progress within the current stage
     * @param fraction between 0.0 and 1.0 representing overall progress
     */
    void onProgress(double fraction);
    
    /**
     * Called for general info messages
     * @param message any status update worth showing the user
     */
    void onMessage(String message);
    
    /**
     * Called when something goes wrong
     * @param error the exception that happened
     */
    void onError(Throwable error);
}

