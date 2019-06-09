package de.cryxy.homeauto.surveillance.io;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.imageio.ImageIO;

import de.cryxy.homeauto.surveillance.entities.Snapshot;
import de.cryxy.homeauto.surveillance.enums.ImageSize;

public class IOHelper {

	public static ZonedDateTime creationTimeFromPath(Path path) throws java.io.IOException {
		BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
		Instant instant = fileAttributes.creationTime().toInstant();
		ZonedDateTime creationTime = instant.atZone(ZoneId.systemDefault());
		return creationTime;
	}

	public static InputStream resizeSnapshot(Snapshot snapshot, ImageSize quality) throws IOException, SQLException {
		// Einlesen

		try (InputStream imageStream = snapshot.getImage().getBinaryStream()) {
			BufferedImage originalImage = ImageIO.read(imageStream);
			if (originalImage == null)
				return null;
			int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
			Integer[] widthAndHeigth = quality.getWidthAndHeigth();
			// Resizing
			BufferedImage resizedImage = new BufferedImage(widthAndHeigth[0], widthAndHeigth[1], type);
			Graphics2D g = resizedImage.createGraphics();
			// Rendering hints
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.drawImage(originalImage, 0, 0, widthAndHeigth[0], widthAndHeigth[1], null);
			g.dispose();
			// Ausgabe
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, "jpg", os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());
			os.close();
			return is;
		} catch (Exception e) {
			throw new de.cryxy.homeauto.surveillance.exceptions.IOException("Error resizing image!", e);
		}

	}

}
