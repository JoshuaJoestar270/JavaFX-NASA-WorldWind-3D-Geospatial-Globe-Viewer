# JavaFX + NASA WorldWind 3D Geospatial Globe Viewer

Interactive 3D globe viewer desktop application built with JavaFX and NASA WorldWind Java SDK.

### Overview
Developed an interactive 3D globe viewer using JavaFX for the UI and NASA WorldWind for geospatial rendering.  
- Embedded the WorldWind canvas in a JavaFX window via SwingNode for cross-platform compatibility  
- Configured dynamic globe display with terrain/imagery layers, stars, and atmosphere effects  
- Added placemark markers with tooltips (using built-in icons – no external files needed)  
- Integrated optional public WMS layer (NASA GIBS satellite imagery)  
- Included basic zoom in/out buttons for user interaction  

Demonstrates proficiency in Java, JavaFX UI development, geospatial SDK integration, multi-threaded rendering, and exploration of NASA WorldWind/OGC standards – directly relevant to GIS and mapping applications.

### Tech Stack
- Java  
- JavaFX  
- NASA WorldWind Java SDK  
- SwingNode  

### Setup Notes
- JDK 17 or 21  
- JavaFX SDK 21: https://gluonhq.com/products/javafx/  
- WorldWind JARs (v2.2.1): https://github.com/NASAWorldWind/WorldWindJava/releases/tag/v2.2.1  
  Required: worldwind.jar, worldwindx.jar, jogl-all.jar, gluegen-rt.jar  
- Add JARs to classpath and JavaFX VM args (same as previous projects)  

No external image files required – uses built-in WorldWind icon for placemark.
