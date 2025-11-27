package com.autovideo.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.client.fluent.Request;

import com.autovideo.img.ImageVideoSelector;
import com.autovideo.img.ImgUtils;
import com.autovideo.img.VideoImage;
import com.autovideo.utils.ProcessRunner;
import com.autovideo.utils.Utils;
import com.autovideo.video.SectionOverlay;
import com.autovideo.video.VideoRenderer;
import com.autovideo.wikidata.datatypes.WikidataItem;
import com.autovideo.wikidata.downloaders.WikidataItemDownloader;
import com.autovideo.wiki.WikiSection;
import com.autovideo.wiki.WikiSectionAggregator;
import com.autovideo.wiki.WikiSectionSplitter;
import com.autovideo.wiki.WikiVideo;
import com.autovideo.wiki.WikiVideoAggregator;
import com.autovideo.wikipedia.datatypes.WikipediaItem;
import com.autovideo.wikipedia.downloaders.WikipediaItemDownloader;
import com.autovideo.utils.Language;

/**
 * Main service for generating Wikipedia videos.
 * This is the core logic extracted from WikiVideoCreator so both CLI and GUI can use it.
 */
public class WikipediaVideoService {
    
    private final WikidataItemDownloader wikidataDownloader;
    private final WikiSectionSplitter sectionSplitter;
    private final WikiSectionAggregator sectionAggregator;
    private final WikiVideoAggregator videoAggregator;
    
    public WikipediaVideoService() {
        this.wikidataDownloader = new WikidataItemDownloader();
        this.sectionSplitter = new WikiSectionSplitter();
        this.sectionAggregator = new WikiSectionAggregator();
        this.videoAggregator = new WikiVideoAggregator();
    }
    
    /**
     * Generate a Wikipedia video based on the request.
     * This does all the heavy lifting - downloads content, generates audio, renders video.
     * 
     * @param request what video to make and how to make it
     * @param listener callback for progress updates (can be null if you don't care)
     * @throws Exception if anything goes wrong during generation
     */
    public void generateVideo(WikiVideoRequest request, ProgressListener listener) throws Exception {
        try {
            notifyStage(listener, "Initializing");
            notifyProgress(listener, 0.0);
            
            // Make sure output dirs exist
            Files.createDirectories(Paths.get("tmp"));
            Files.createDirectories(request.getOutputDirectory());
            
            // Step 1: Download Wikipedia article
            notifyStage(listener, "Downloading Wikipedia article: " + request.getArticleTitle());
            notifyProgress(listener, 0.1);
            
            WikipediaItemDownloader wikipediaDownloader = 
                    new WikipediaItemDownloader.Builder(request.getLanguage())
                        .setGetHtml(true)
                        .setAcceptIds(false)
                        .build();
            
            WikipediaItem wikipediaPage = wikipediaDownloader.request(request.getArticleTitle());
            
            if (wikipediaPage == null) {
                throw new IOException("Could not download Wikipedia page: " + request.getArticleTitle());
            }
            
            notifyMessage(listener, "Successfully downloaded article: " + wikipediaPage.getTitle());
            notifyProgress(listener, 0.2);
            
            // Step 2: Get Wikidata info (flag, coordinates, key facts)
            notifyStage(listener, "Fetching Wikidata information");
            String flagImage = null;
            float latitude = 0, longitude = 0;
            SectionOverlay sectionOverlay = null;
            
            if (wikipediaPage.getWikidataId() != null) {
                List<WikidataItem> wikidataItems = wikidataDownloader.download(
                    Collections.singleton(wikipediaPage.getWikidataId())
                );
                
                if (!wikidataItems.isEmpty()) {
                    WikidataItem item = wikidataItems.get(0);
                    
                    // Try to get flag image
                    try {
                        flagImage = item.getProperties().get("P41").get(0).getValue();
                    } catch (Exception e) {
                        // no flag, that's fine
                    }
                    
                    // Try to get coordinates
                    try {
                        String coordinates = item.getProperties().get("P625").get(0).getValue();
                        latitude = Float.valueOf(coordinates.split(",")[0]);
                        longitude = Float.valueOf(coordinates.split(",")[1]);
                    } catch (Exception e) {
                        // no coordinates, whatever
                    }
                    
                    sectionOverlay = createOverlayFromKeyFacts(item, wikipediaPage);
                }
            }
            
            notifyProgress(listener, 0.3);
            
            // Step 3: Normalize content (remove parentheses and stuff)
            notifyStage(listener, "Processing article content");
            normalizeContent(wikipediaPage);
            
            // Step 4: Split into sections
            List<WikiSection> sections = sectionSplitter.split(
                wikipediaPage.getContent(), 
                request.getArticleTitle()
            );
            notifyMessage(listener, "Split article into " + sections.size() + " sections");
            notifyProgress(listener, 0.4);
            
            // Step 5: Get images and videos for each section
            notifyStage(listener, "Retrieving images from Wikipedia and Pixabay");
            new ImageVideoSelector(request.getImageCategory()).retrieve(
                request.getArticleTitle(), 
                sections, 
                9, 
                wikipediaPage
            );
            notifyProgress(listener, 0.5);
            
            // Step 6: Create video objects
            notifyStage(listener, "Organizing content into videos");
            List<WikiVideo> videos = sectionAggregator.createVideos(sections, wikipediaPage);
            videos = videoAggregator.aggregateVideos(videos);
            notifyMessage(listener, "Created " + videos.size() + " video(s)");
            notifyProgress(listener, 0.6);
            
            // Step 7: Generate HTML preview
            String outputBaseName = request.getOutputDirectory().resolve(request.getArticleTitle()).toString();
            PrintWriter htmlOut = new PrintWriter(outputBaseName + ".html");
            htmlOut.println("<html><head><meta charset='UTF-8'></head><body>");
            for (WikiVideo video : videos) {
                htmlOut.println("<h1>Video: " + video.getTitle() + 
                    " (estimated length: " + video.getEstimatedLength() + " minutes)</h1>");
                for (WikiSection s : video.getSections()) {
                    htmlOut.println("<h2>" + s.getTitle() + "</h2>");
                    s.getImages().forEach(img -> {
                        htmlOut.println("<img width=\"300\" src=\"" + img.getPreviewURL() + "\">");
                        if (img.getCaption() != null) {
                            htmlOut.println("<span>" + img.getCaption() + "</span>");
                        }
                    });
                    s.getVideos().forEach(pVideo -> {
                        htmlOut.println("<img width=\"300\" src=\"" + pVideo.getPreviewURL() + "\"> (VIDEO)");
                    });
                    htmlOut.println("<br/>");
                    htmlOut.println(s.getContent());
                }
            }
            htmlOut.println("</body></html>");
            htmlOut.close();
            
            // Clean up temp files
            ProcessRunner.run("sh", "output/clean.sh");
            
            // Step 8: Add overlay and flag to intro section
            WikiSection introSection = videos.get(0).getSections().stream()
                .filter(s -> s.getTitle().equals("Intro"))
                .findFirst()
                .orElse(null);
            
            if (introSection != null) {
                if (sectionOverlay != null && sectionOverlay.getBulletPoints().size() > 0) {
                    introSection.setSectionOverlay(sectionOverlay);
                }
                
                if (flagImage != null) {
                    byte[] bytes = Request.Get(Utils.getUrlForWikipediaImage(flagImage))
                        .execute()
                        .returnContent()
                        .asBytes();
                    String[] parts = flagImage.split("\\.");
                    String ext = parts[parts.length - 1];
                    
                    Files.write(Paths.get("tmp/overlay." + ext), bytes);
                    
                    if (ext.equals("svg")) {
                        notifyMessage(listener, "Converting flag SVG to JPG");
                        ImgUtils.convert("tmp/overlay." + ext, "tmp/overlay.jpg");
                        ImgUtils.createCaptionedImage("tmp/overlay.jpg", 
                            "Flag of " + request.getArticleTitle(), true);
                        ext = "jpg";
                    }
                    introSection.setOverlay("tmp/overlay." + ext);
                }
            }
            
            notifyProgress(listener, 0.7);
            
            // Step 9: Render each video
            for (int i = 0; i < videos.size(); i++) {
                WikiVideo video = videos.get(i);
                
                notifyStage(listener, "Rendering video " + (i + 1) + "/" + videos.size() + 
                    ": " + video.getTitle());
                
                String outputFileName = video.getPageTitle() + "_" + i + "_-_" + 
                    video.getTitle().replace(" ", "_");
                
                VideoImage image = video.sampleRandomImage();
                if (image == null) {
                    notifyMessage(listener, "Skipping video " + video.getTitle() + 
                        " (no suitable image found)");
                    continue;
                }
                
                notifyMessage(listener, "Processing sections for " + video.getTitle());
                video.getSections().forEach(s -> {
                    notifyMessage(listener, "  - " + s.getTitle() + 
                        (s.getVideos().size() != 0 ? " (has video clips)" : ""));
                });
                
                // This is where the actual video rendering happens (audio + video)
                VideoRenderer renderer = new VideoRenderer(
                    request.isWithAudio(), 
                    request.getVoiceEngine(), 
                    request.getLanguage()
                );
                
                String outputPath = request.getOutputDirectory()
                    .resolve(outputFileName + ".mp4")
                    .toString();
                    
                renderer.render(video, outputPath.replace(".mp4", ""));
                
                // Save thumbnail
                byte[] thumbnailBytes = Request.Get(image.getLargeImageURL())
                    .execute()
                    .returnContent()
                    .asBytes();
                    
                Files.write(
                    request.getOutputDirectory().resolve(outputFileName + ".jpg"), 
                    thumbnailBytes
                );
                
                ProcessRunner.run("sh", "output/clean.sh");
                
                double progressFraction = 0.7 + (0.3 * (i + 1) / videos.size());
                notifyProgress(listener, progressFraction);
            }
            
            notifyStage(listener, "Complete!");
            notifyProgress(listener, 1.0);
            notifyMessage(listener, "Video generation finished successfully");
            
        } catch (Exception e) {
            if (listener != null) {
                listener.onError(e);
            }
            throw e;
        }
    }
    
    /**
     * Removes parentheses and braces from Wikipedia content.
     * Makes the text flow better for narration.
     */
    private void normalizeContent(WikipediaItem wikipediaPage) {
        StringBuilder buffer = new StringBuilder();
        String content = wikipediaPage.getContent();
        int parenthesisCounter = 0;

        for (char c : content.toCharArray()) {
            if (c == '(' || c == '{')
                parenthesisCounter++;
            if (c == ')' || c == '}')
                parenthesisCounter--;
            if (!(c == '(' || c == '{' || c == ')' || c == '}') && parenthesisCounter == 0)
                buffer.append(c);
        }
        
        wikipediaPage.setContent(buffer.toString().replaceAll("\\.([A-Z\"])", ". $1"));
    }
    
    /**
     * Creates a nice overlay with key facts about the subject.
     * Things like founding date, founders, elevation, etc.
     */
    private SectionOverlay createOverlayFromKeyFacts(WikidataItem item, WikipediaItem page) {
        SectionOverlay overlay = new SectionOverlay("KEY INFO ABOUT " + page.getTitle() + "              ");
        
        // Founded/inception date (P571)
        if (item.getProperties().get("P571") != null) {
            String inception = item.getProperties().get("P571").get(0).getValue();
            overlay.addBulletPoint("founded in " + inception);
        }
        
        // Founders (P112)
        if (item.getProperties().get("P112") != null) {
            List<String> foundersList = item.getProperties().get("P112").stream()
                .map(p -> {
                    try {
                        return new WikidataItemDownloader()
                            .download(Collections.singleton(p.getValue()))
                            .get(0)
                            .getLabels()
                            .get(Language.EN)
                            .getText();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(x -> x != null)
                .collect(Collectors.toList());

            String founders = String.join(
                foundersList.size() == 2 ? " and " : ", ", 
                foundersList
            );
            overlay.addBulletPoint("founded by " + founders);
        }
        
        // Elevation (P2044)
        if (item.getProperties().get("P2044") != null) {
            String elevation = item.getProperties().get("P2044").get(0).getValue();
            overlay.addBulletPoint("elevation\\: " + elevation.replace("+", "") + "m");
        }
        
        return overlay;
    }
    
    private void notifyStage(ProgressListener listener, String stage) {
        if (listener != null) {
            listener.onStageChanged(stage);
        }
    }
    
    private void notifyProgress(ProgressListener listener, double fraction) {
        if (listener != null) {
            listener.onProgress(fraction);
        }
    }
    
    private void notifyMessage(ProgressListener listener, String message) {
        if (listener != null) {
            listener.onMessage(message);
        }
    }
}

