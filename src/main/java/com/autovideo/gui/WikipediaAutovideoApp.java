package com.autovideo.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;

import com.amazonaws.services.polly.model.Engine;
import com.autovideo.config.ConfigManager;
import com.autovideo.img.ImageCategory;
import com.autovideo.service.ProgressListener;
import com.autovideo.service.WikiVideoRequest;
import com.autovideo.service.WikipediaVideoService;
import com.autovideo.utils.Language;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Main GUI application for Wikipedia Autovideo.
 * This is what users see when they launch the desktop app.
 */
public class WikipediaAutovideoApp extends Application {
    
    private TextField articleField;
    private ComboBox<String> engineCombo;
    private ComboBox<String> languageCombo;
    private TextField outputDirField;
    private Button browseDirButton;
    private CheckBox advancedCheckbox;
    private VBox advancedPanel;
    private TextArea statusArea;
    private ProgressBar progressBar;
    private Button generateButton;
    private Button openFolderButton;
    
    // Advanced settings
    private TextField pixabayKeyField;
    private TextField awsAccessKeyField;
    private TextField awsSecretKeyField;
    private TextField awsRegionField;
    private ComboBox<String> imageCategoryCombo;
    
    private WikipediaVideoService service;
    private File selectedOutputDir;
    
    @Override
    public void start(Stage primaryStage) {
        service = new WikipediaVideoService();
        
        // Check if config exists, if not show setup wizard
        if (!ConfigManager.configExists()) {
            showSetupWizard(primaryStage);
        }
        
        primaryStage.setTitle("Wikipedia Autovideo Generator");
        
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        
        // Top section - basic controls
        root.getChildren().add(createBasicControlsSection());
        
        // Middle section - advanced options (collapsible)
        root.getChildren().add(createAdvancedSection());
        
        // Status section
        root.getChildren().add(createStatusSection());
        
        // Bottom section - buttons
        root.getChildren().add(createButtonSection());
        
        Scene scene = new Scene(root, 800, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createBasicControlsSection() {
        VBox section = new VBox(10);
        
        Label titleLabel = new Label("Basic Settings");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Article title field
        HBox articleBox = new HBox(10);
        articleBox.setAlignment(Pos.CENTER_LEFT);
        Label articleLabel = new Label("Wikipedia Article:");
        articleLabel.setPrefWidth(150);
        articleField = new TextField();
        articleField.setPromptText("e.g., New_York or https://en.wikipedia.org/wiki/New_York");
        articleField.setPrefWidth(400);
        articleBox.getChildren().addAll(articleLabel, articleField);
        
        // Voice engine dropdown
        HBox engineBox = new HBox(10);
        engineBox.setAlignment(Pos.CENTER_LEFT);
        Label engineLabel = new Label("Voice Engine:");
        engineLabel.setPrefWidth(150);
        engineCombo = new ComboBox<>();
        engineCombo.getItems().addAll("neural", "standard");
        engineCombo.setValue("neural");
        engineCombo.setPrefWidth(200);
        engineBox.getChildren().addAll(engineLabel, engineCombo);
        
        // Language dropdown
        HBox langBox = new HBox(10);
        langBox.setAlignment(Pos.CENTER_LEFT);
        Label langLabel = new Label("Language:");
        langLabel.setPrefWidth(150);
        languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("EN", "FR", "ES", "DE", "IT", "NL", "ZH");
        languageCombo.setValue("EN");
        languageCombo.setPrefWidth(200);
        langBox.getChildren().addAll(langLabel, languageCombo);
        
        // Output directory picker
        HBox outputBox = new HBox(10);
        outputBox.setAlignment(Pos.CENTER_LEFT);
        Label outputLabel = new Label("Output Directory:");
        outputLabel.setPrefWidth(150);
        outputDirField = new TextField();
        outputDirField.setPromptText("Choose where to save the video...");
        outputDirField.setPrefWidth(300);
        outputDirField.setEditable(false);
        browseDirButton = new Button("Browse...");
        browseDirButton.setOnAction(e -> browseForOutputDir());
        outputBox.getChildren().addAll(outputLabel, outputDirField, browseDirButton);
        
        // Set default output dir
        selectedOutputDir = new File("output/final/");
        outputDirField.setText(selectedOutputDir.getAbsolutePath());
        
        section.getChildren().addAll(
            titleLabel,
            new Separator(),
            articleBox,
            engineBox,
            langBox,
            outputBox
        );
        
        return section;
    }
    
    private VBox createAdvancedSection() {
        VBox section = new VBox(10);
        
        advancedCheckbox = new CheckBox("Show Advanced Options");
        advancedCheckbox.setOnAction(e -> toggleAdvancedPanel());
        
        advancedPanel = new VBox(10);
        advancedPanel.setVisible(false);
        advancedPanel.setManaged(false);
        
        Label advLabel = new Label("Advanced Settings");
        advLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Pixabay API key
        HBox pixabayBox = new HBox(10);
        pixabayBox.setAlignment(Pos.CENTER_LEFT);
        Label pixabayLabel = new Label("Pixabay API Key:");
        pixabayLabel.setPrefWidth(150);
        pixabayKeyField = new TextField();
        pixabayKeyField.setPromptText("Enter Pixabay API key");
        pixabayKeyField.setPrefWidth(300);
        pixabayBox.getChildren().addAll(pixabayLabel, pixabayKeyField);
        
        // AWS Access Key
        HBox awsAccessBox = new HBox(10);
        awsAccessBox.setAlignment(Pos.CENTER_LEFT);
        Label awsAccessLabel = new Label("AWS Access Key:");
        awsAccessLabel.setPrefWidth(150);
        awsAccessKeyField = new TextField();
        awsAccessKeyField.setPromptText("Enter AWS access key");
        awsAccessKeyField.setPrefWidth(300);
        awsAccessBox.getChildren().addAll(awsAccessLabel, awsAccessKeyField);
        
        // AWS Secret Key
        HBox awsSecretBox = new HBox(10);
        awsSecretBox.setAlignment(Pos.CENTER_LEFT);
        Label awsSecretLabel = new Label("AWS Secret Key:");
        awsSecretLabel.setPrefWidth(150);
        awsSecretKeyField = new PasswordField();
        awsSecretKeyField.setPromptText("Enter AWS secret key");
        awsSecretKeyField.setPrefWidth(300);
        awsSecretBox.getChildren().addAll(awsSecretLabel, awsSecretKeyField);
        
        // AWS Region
        HBox awsRegionBox = new HBox(10);
        awsRegionBox.setAlignment(Pos.CENTER_LEFT);
        Label awsRegionLabel = new Label("AWS Region:");
        awsRegionLabel.setPrefWidth(150);
        awsRegionField = new TextField();
        awsRegionField.setPromptText("us-east-1");
        awsRegionField.setPrefWidth(200);
        awsRegionBox.getChildren().addAll(awsRegionLabel, awsRegionField);
        
        // Image category
        HBox categoryBox = new HBox(10);
        categoryBox.setAlignment(Pos.CENTER_LEFT);
        Label categoryLabel = new Label("Image Category:");
        categoryLabel.setPrefWidth(150);
        imageCategoryCombo = new ComboBox<>();
        for (ImageCategory cat : ImageCategory.values()) {
            imageCategoryCombo.getItems().add(cat.name());
        }
        imageCategoryCombo.setValue(ImageCategory.BUILDINGS.name());
        imageCategoryCombo.setPrefWidth(200);
        categoryBox.getChildren().addAll(categoryLabel, imageCategoryCombo);
        
        Button saveConfigButton = new Button("Save Configuration");
        saveConfigButton.setOnAction(e -> saveConfiguration());
        
        advancedPanel.getChildren().addAll(
            advLabel,
            new Separator(),
            pixabayBox,
            awsAccessBox,
            awsSecretBox,
            awsRegionBox,
            categoryBox,
            saveConfigButton
        );
        
        // Load current config if it exists
        loadCurrentConfig();
        
        section.getChildren().addAll(advancedCheckbox, advancedPanel);
        
        return section;
    }
    
    private VBox createStatusSection() {
        VBox section = new VBox(10);
        
        Label statusLabel = new Label("Status & Progress");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setPrefRowCount(10);
        statusArea.setWrapText(true);
        statusArea.setText("Ready to generate videos. Enter a Wikipedia article title and click Generate.");
        
        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(Double.MAX_VALUE);
        
        section.getChildren().addAll(statusLabel, new Separator(), statusArea, progressBar);
        
        return section;
    }
    
    private HBox createButtonSection() {
        HBox section = new HBox(15);
        section.setAlignment(Pos.CENTER);
        
        generateButton = new Button("Generate Video");
        generateButton.setPrefWidth(150);
        generateButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        generateButton.setOnAction(e -> startVideoGeneration());
        
        openFolderButton = new Button("Open Output Folder");
        openFolderButton.setPrefWidth(150);
        openFolderButton.setOnAction(e -> openOutputFolder());
        
        Button settingsButton = new Button("Settings...");
        settingsButton.setPrefWidth(100);
        settingsButton.setOnAction(e -> showSettingsDialog());
        
        section.getChildren().addAll(generateButton, openFolderButton, settingsButton);
        
        return section;
    }
    
    private void toggleAdvancedPanel() {
        boolean show = advancedCheckbox.isSelected();
        advancedPanel.setVisible(show);
        advancedPanel.setManaged(show);
    }
    
    private void browseForOutputDir() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Output Directory");
        if (selectedOutputDir != null && selectedOutputDir.exists()) {
            chooser.setInitialDirectory(selectedOutputDir);
        }
        
        File selected = chooser.showDialog(generateButton.getScene().getWindow());
        if (selected != null) {
            selectedOutputDir = selected;
            outputDirField.setText(selected.getAbsolutePath());
        }
    }
    
    private void loadCurrentConfig() {
        try {
            if (ConfigManager.configExists()) {
                pixabayKeyField.setText(ConfigManager.getProperty("pixabay_api_key"));
                awsAccessKeyField.setText(ConfigManager.getProperty("aws_access_key"));
                awsSecretKeyField.setText(ConfigManager.getProperty("aws_secret_key"));
                String region = ConfigManager.getProperty("aws_region");
                awsRegionField.setText(region != null ? region : "us-east-1");
            }
        } catch (IOException e) {
            logStatus("Warning: Could not load existing config: " + e.getMessage());
        }
    }
    
    private void saveConfiguration() {
        try {
            ConfigManager.createConfig(
                awsAccessKeyField.getText().trim(),
                awsSecretKeyField.getText().trim(),
                awsRegionField.getText().trim(),
                pixabayKeyField.getText().trim()
            );
            
            showAlert(Alert.AlertType.INFORMATION, "Configuration Saved",
                "Your settings have been saved to autovideo.conf");
            
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Failed",
                "Could not save configuration: " + e.getMessage());
        }
    }
    
    private void startVideoGeneration() {
        // Validate inputs
        String article = articleField.getText().trim();
        if (article.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Input",
                "Please enter a Wikipedia article title or URL.");
            return;
        }
        
        if (!ConfigManager.configExists()) {
            showAlert(Alert.AlertType.WARNING, "Configuration Missing",
                "Please configure your API keys in the Advanced Options section and click Save Configuration.");
            advancedCheckbox.setSelected(true);
            toggleAdvancedPanel();
            return;
        }
        
        // Check if ffmpeg is available
        if (!checkFfmpeg()) {
            showAlert(Alert.AlertType.ERROR, "FFmpeg Not Found",
                "FFmpeg is required but not found in your system PATH.\n\n" +
                "Please install FFmpeg:\n" +
                "- On Ubuntu/Debian: sudo apt install ffmpeg\n" +
                "- On Fedora: sudo dnf install ffmpeg\n" +
                "- On Windows: Download from ffmpeg.org and add to PATH");
            return;
        }
        
        // Disable button during generation
        generateButton.setDisable(true);
        statusArea.clear();
        progressBar.setProgress(0);
        
        // Extract article name from URL if needed
        String articleName = article;
        if (article.contains("wikipedia.org/wiki/")) {
            articleName = article.substring(article.lastIndexOf("/") + 1);
        }
        
        // Build request
        Engine engine = engineCombo.getValue().equals("neural") ? 
            Engine.Neural : Engine.Standard;
        Language lang = Language.fromString(languageCombo.getValue());
        ImageCategory category = ImageCategory.valueOf(imageCategoryCombo.getValue());
        
        WikiVideoRequest request = new WikiVideoRequest.Builder(articleName)
            .voiceEngine(engine)
            .language(lang)
            .imageCategory(category)
            .outputDirectory(selectedOutputDir.toPath())
            .withAudio(true)
            .build();
        
        // Create progress listener
        ProgressListener listener = new ProgressListener() {
            @Override
            public void onStageChanged(String stageName) {
                Platform.runLater(() -> logStatus("\n=== " + stageName + " ==="));
            }
            
            @Override
            public void onProgress(double fraction) {
                Platform.runLater(() -> progressBar.setProgress(fraction));
            }
            
            @Override
            public void onMessage(String message) {
                Platform.runLater(() -> logStatus(message));
            }
            
            @Override
            public void onError(Throwable error) {
                Platform.runLater(() -> {
                    logStatus("\nERROR: " + error.getMessage());
                    error.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Generation Failed",
                        "An error occurred: " + error.getMessage());
                    generateButton.setDisable(false);
                });
            }
        };
        
        // Run generation in background thread
        Thread generationThread = new Thread(() -> {
            try {
                service.generateVideo(request, listener);
                
                Platform.runLater(() -> {
                    generateButton.setDisable(false);
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Video generation completed successfully!\n\n" +
                        "Output saved to: " + selectedOutputDir.getAbsolutePath());
                });
                
            } catch (Exception e) {
                listener.onError(e);
            }
        });
        
        generationThread.setDaemon(true);
        generationThread.start();
    }
    
    private void openOutputFolder() {
        if (selectedOutputDir == null || !selectedOutputDir.exists()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Directory",
                "Output directory does not exist yet.");
            return;
        }
        
        try {
            // Open file manager to the output directory
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Runtime.getRuntime().exec("explorer " + selectedOutputDir.getAbsolutePath());
            } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                Runtime.getRuntime().exec("open " + selectedOutputDir.getAbsolutePath());
            } else {
                // Linux
                Runtime.getRuntime().exec("xdg-open " + selectedOutputDir.getAbsolutePath());
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Could Not Open Folder",
                "Failed to open output folder: " + e.getMessage());
        }
    }
    
    private void showSettingsDialog() {
        advancedCheckbox.setSelected(true);
        toggleAdvancedPanel();
    }
    
    private void showSetupWizard(Stage owner) {
        Alert wizard = new Alert(Alert.AlertType.NONE);
        wizard.initOwner(owner);
        wizard.initModality(Modality.APPLICATION_MODAL);
        wizard.setTitle("Welcome to Wikipedia Autovideo");
        wizard.setHeaderText("First Time Setup");
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        Label infoLabel = new Label(
            "Welcome! Before you can generate videos, we need to configure your API credentials.\n\n" +
            "You'll need:\n" +
            "1. A Pixabay API key (for images) - Get it at pixabay.com/api/docs/\n" +
            "2. AWS credentials (for text-to-speech) - Get them from AWS Console\n\n" +
            "Don't worry, you can change these later in the Advanced Options."
        );
        infoLabel.setWrapText(true);
        
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        
        TextField pixabayField = new TextField();
        TextField awsAccessField = new TextField();
        PasswordField awsSecretField = new PasswordField();
        TextField awsRegionField = new TextField("us-east-1");
        
        form.add(new Label("Pixabay API Key:"), 0, 0);
        form.add(pixabayField, 1, 0);
        form.add(new Label("AWS Access Key:"), 0, 1);
        form.add(awsAccessField, 1, 1);
        form.add(new Label("AWS Secret Key:"), 0, 2);
        form.add(awsSecretField, 1, 2);
        form.add(new Label("AWS Region:"), 0, 3);
        form.add(awsRegionField, 1, 3);
        
        content.getChildren().addAll(infoLabel, form);
        
        wizard.getDialogPane().setContent(content);
        wizard.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = wizard.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                ConfigManager.createConfig(
                    awsAccessField.getText().trim(),
                    awsSecretField.getText().trim(),
                    awsRegionField.getText().trim(),
                    pixabayField.getText().trim()
                );
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Setup Failed",
                    "Could not save configuration: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Setup Incomplete",
                "You'll need to configure your API keys before generating videos.");
        }
    }
    
    private boolean checkFfmpeg() {
        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -version");
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    private void logStatus(String message) {
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
        statusArea.appendText("[" + timestamp + "] " + message + "\n");
        statusArea.setScrollTop(Double.MAX_VALUE);
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

