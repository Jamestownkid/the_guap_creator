package com.autovideo.wiki;

import java.awt.FontFormatException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.amazonaws.services.polly.model.Engine;
import com.autovideo.img.ImageCategory;
import com.autovideo.service.ProgressListener;
import com.autovideo.service.WikiVideoRequest;
import com.autovideo.service.WikipediaVideoService;
import com.autovideo.utils.Language;

/**
 * CLI entry point for Wikipedia Autovideo.
 * Now uses the WikipediaVideoService so GUI and CLI share the same logic.
 */
public class WikiVideoCreator {
	
	private boolean withAudio = false;
	private Engine engine;
	private ImageCategory category;
	
	public WikiVideoCreator(boolean withAudio, Engine engine, ImageCategory category) {
		this.withAudio = withAudio;
		this.engine = engine;
		this.category = category;
	}

	/**
	 * Create a video using the new service layer.
	 * This keeps the CLI working but now uses the same logic as the GUI.
	 */
	public void createVideo(String pageTitle, Language language) throws Exception {
		WikiVideoRequest request = new WikiVideoRequest.Builder(pageTitle)
			.voiceEngine(engine)
			.language(language)
			.imageCategory(category)
			.outputDirectory(Paths.get("output/final/"))
			.withAudio(withAudio)
			.build();
		
		// Simple CLI progress listener that prints to console
		ProgressListener listener = new ProgressListener() {
			@Override
			public void onStageChanged(String stageName) {
				System.out.println("\n=== " + stageName + " ===");
			}
			
			@Override
			public void onProgress(double fraction) {
				// could print progress bar here if you want
			}
			
			@Override
			public void onMessage(String message) {
				System.out.println(message);
			}
			
			@Override
			public void onError(Throwable error) {
				System.err.println("ERROR: " + error.getMessage());
				error.printStackTrace();
			}
		};
		
		WikipediaVideoService service = new WikipediaVideoService();
		service.generateVideo(request, listener);
	}
	
	private static void usage() {
		System.out.println("Usage:\n");
		System.out.println("java -jar autovideo.jar <wikipedia_page_name> [engine]");
		System.out.println("\n* Use an English Wikipedia page name as a first argument, with underscores instead of spaces");
		System.out.println("* The optional engine parameter determines which engine to use for speech syntesis:");
		System.out.println("  the choices are 'standard' (default) and 'neural' (better but more expensive)");
	}

	public static void main(String[] args) throws Exception {
		// Turn off annoying logging from dependencies
		Logger[] logs = new Logger[]{ Logger.getLogger("org.jaudiotagger") };
	    for (Logger l : logs)
	        l.setLevel(Level.OFF);
	    
	    if (args.length == 0) {
	    	usage();
	    	System.exit(0);
	    }
	    
	    String page = args[0];
	    Engine engine = Engine.Standard;
	    if (args.length >= 2) {
	    	String engineArg = args[1];
	    	try {
	    		engine = Engine.valueOf(engineArg);
	    	} catch (IllegalArgumentException e) {
	    		engine = Engine.Standard;
	    	}
	    }
	    
	    System.out.println("Starting video creation for Wikipedia page " + page + " and audio engine " + engine);
	    
	    new WikiVideoCreator(true, engine, ImageCategory.BUILDINGS).createVideo(page, Language.EN);
	}
}
