import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.layers.WMSLayer;
import gov.nasa.worldwind.ogc.wms.WMSCapabilities;
import gov.nasa.worldwind.render.WWIcon;
import gov.nasa.worldwind.render.UserFacingIcon;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.SwingUtilities;
import java.net.URL;

/**
 * JavaFX + NASA WorldWind 3D Globe Viewer Demo
 * 
 * Single-file, self-contained application showing an interactive 3D globe using NASA's WorldWind SDK
 * embedded in JavaFX via SwingNode. Ideal for GIS/Java portfolio projects.
 * 
 * Features:
 * - Interactive 3D globe with terrain, imagery, stars, and atmosphere
 * - Built-in placemark marker (no external files needed)
 * - Optional public WMS satellite layer (NASA GIBS)
 * - Basic zoom in/out buttons for user interaction
 * 
 * Setup Requirements (VS Code):
 * - JDK 17 or 21
 * - JavaFX SDK 21[](https://gluonhq.com/products/javafx/)
 * - WorldWind JARs[](https://github.com/NASAWorldWind/WorldWindJava/releases/tag/v2.2.1):
 *   worldwind.jar, worldwindx.jar, jogl-all.jar, gluegen-rt.jar
 * 
 * Add to .vscode/settings.json:
 * "java.project.referencedLibraries": ["lib/*.jar"]
 * 
 * Add to .vscode/launch.json vmArgs:
 * "--module-path /path/to/javafx-sdk-21/lib --add-modules javafx.controls,javafx.fxml,javafx.swing"
 */
public class WorldWindJavaFXDemo extends Application {

    private WorldWindow wwd;

    @Override
    public void start(Stage primaryStage) {
        SwingNode swingNode = new SwingNode();

        // Initialize WorldWind on Swing EDT
        SwingUtilities.invokeLater(() -> {
            try {
                WorldWindowGLCanvas wwdCanvas = new WorldWindowGLCanvas();
                wwd = wwdCanvas;

                // Set initial view (San Francisco example)
                Configuration.setValue(AVKey.INITIAL_LATITUDE, 37.7749);
                Configuration.setValue(AVKey.INITIAL_LONGITUDE, -122.4194);
                Configuration.setValue(AVKey.INITIAL_ALTITUDE, 1_000_000);

                // Create default model
                wwd.setModel(WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME));

                // Add visual layers
                wwd.getModel().getLayers().add(new gov.nasa.worldwind.layers.StarsLayer());
                wwd.getModel().getLayers().add(new gov.nasa.worldwind.layers.AtmosphereLayer());

                // Add placemark using built-in icon (no file needed)
                RenderableLayer markers = new RenderableLayer();
                markers.setName("Markers");
                WWIcon placemark = new UserFacingIcon("gov/nasa/worldwind/place.png",
                        Position.fromDegrees(37.7749, -122.4194, 0));
                placemark.setToolTipText("San Francisco – Example Placemark");
                markers.addRenderable(placemark);
                wwd.getModel().getLayers().add(markers);
                System.out.println("Default placemark added successfully.");

                // Optional WMS layer (NASA GIBS Blue Marble)
                try {
                    URL wmsUrl = new URL("https://gibs.earthdata.nasa.gov/wms.cgi?SERVICE=WMS&REQUEST=GetCapabilities");
                    WMSCapabilities caps = WMSCapabilities.retrieve(wmsUrl);
                    WMSLayer wmsLayer = new WMSLayer("BlueMarbleNG");
                    wwd.getModel().getLayers().add(wmsLayer);
                    System.out.println("WMS 'BlueMarbleNG' layer loaded.");
                } catch (Exception e) {
                    System.err.println("WMS layer failed (continuing without): " + e.getMessage());
                }

                // Success confirmation
                System.out.println("WorldWind globe initialized successfully!");

                swingNode.setContent(wwdCanvas);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("WorldWind initialization failed: " + e.getMessage());
            }
        });

        // JavaFX controls: simple zoom buttons
        Button zoomIn = new Button("Zoom In");
        zoomIn.setOnAction(e -> {
            if (wwd != null) {
                wwd.getView().setEyePosition(wwd.getView().getEyePosition().add(0, 0, -200000));
            }
        });

        Button zoomOut = new Button("Zoom Out");
        zoomOut.setOnAction(e -> {
            if (wwd != null) {
                wwd.getView().setEyePosition(wwd.getView().getEyePosition().add(0, 0, 200000));
            }
        });

        HBox controls = new HBox(10, zoomIn, zoomOut);
        controls.setStyle("-fx-padding: 10; -fx-alignment: center;");

        BorderPane root = new BorderPane();
        root.setCenter(swingNode);
        root.setBottom(controls);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("JavaFX + NASA WorldWind 3D Globe Viewer Demo");
        primaryStage.setScene(scene);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}